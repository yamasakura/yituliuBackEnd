package com.lhs.service.impl;

import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.DBPogo.StageResultData;

import com.lhs.bean.vo.StageOrundumVo;
import com.lhs.bean.pojo.StageInfoVo;
import com.lhs.dao.StageResultDataDao;
import com.lhs.service.StageResultSetInfoService;
import com.lhs.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
@Slf4j
public class StageResultSetInfoServiceImpl implements StageResultSetInfoService {

    @Autowired
    private StageResultDataDao stageResultDataDao;

    @Autowired
    private StageService stageService;

    /**
     * 设置关卡的理智转化率
     *
     * @param times      样本数
     * @param efficiency 最低效率  理智转化率100%=绿票绝对效率1.25
     * @param expCoefficient 经验书价值系数
     * @return
     */
    @Override
    public List<List<StageResultData>> setStageResultPercentageT3(Integer times, Double efficiency, Integer stageState, Double expCoefficient) {
        Random random = new Random();
        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液", "转质盐组"};

        //最终返回的结果集合
        List<List<StageResultData>> stageResultListT3 = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        for (String main : mainName) {
//            根据材料类型查出关卡,是否可查出取决于isShow属性,1为可显示
            List<StageResultData> stageResultByTypeList = new ArrayList<>();
            stageResultByTypeList = stageResultDataDao.findByItemTypeAndIsShowAndExpCoefficientAndEfficiencyGreaterThanAndSampleSizeGreaterThanOrderByEfficiencyDesc
                    (main, 1,expCoefficient, efficiency, times);

//            复制一个集合 ,对数据库查出的集合直接进行set操作会改变session缓存中的数据，数据库的值也会更新,虽然好像也没啥事,但是还是复制了一下
            List<StageResultData> stageResultByTypeListCopy = new ArrayList<>();
            for (int i = 0; i < stageResultByTypeList.size(); i++) {
                stageResultByTypeListCopy.add(stageResultByTypeList.get(i));
                if (i > 5) {
                    break;
                }
            }

            double standard = 1.25;    //绿票绝对效率的上限  等同于理智转化率100%
//      实际效率可能会比这个浮动0.几,下面会找出来每个材料排在第一的关卡效率作为标准,但是需要他是作为定价本使用的,判断isUseValue是1
//            for (StageResultData stageResultData : stageResultByTypeList) {
//                if (stageResultData.getIsValue() == 1) {
//                    standard = stageResultData.getEfficiency();
////             log.info("当前标准是"+stageResultData.getStageCode()+"——"+standard);
//                    break;
//                }
//            }

//            if (stageState > 0) {
//                standard = 1.25;
//            }

            int stageColorIndex = 0;
            int indexStart = 0;

            for (int k = 0; k < stageResultByTypeListCopy.size(); k++) {
//                当该关卡未被用于材料定价,判断他是活动本
                if (stageResultByTypeListCopy.get(k).getIsValue() == 0 && (!"act_side12_".equals(stageResultByTypeListCopy.get(k).getZoneId()))
                        && (!"act_side12_rep_".equals(stageResultByTypeListCopy.get(k).getZoneId()))) {
//                    这里会加一个单独的计算入商店无限池的龙门币后的活动本效率
                    StageResultData stageResultData = new StageResultData();
                    BeanUtils.copyProperties(stageResultByTypeListCopy.get(k), stageResultData);
                    stageResultData.setEfficiency(stageResultByTypeListCopy.get(k).getEfficiency() + 0.09);
                    stageResultData.setSecondary("龙门币");
                    stageResultData.setSecondaryId("4001");
                    stageResultData.setStageColor(-1); //设置颜色为-1,前端会判断为红色
                    if (stageResultData.getEfficiency() / 1.25 < 1.0) {
                        stageResultByTypeListCopy.add(1, stageResultData);  //塞入复制后的集合中
                    } else {
                        stageResultByTypeListCopy.add(0, stageResultData);  //塞入复制后的集合中
                        stageColorIndex++;
                        indexStart++;
                    }
                    k++;
                }
            }

//            log.info("当前标准是："+standard);
            //将绿票绝对效率转化为理智转化率100%
            for (StageResultData stageResultData : stageResultByTypeListCopy) {
//                log.info( stageResultData.getStageCode() +" = "+stageResultData.getEfficiency() +" / "+ standard +" = "+
//                        Double.valueOf(decimalFormat.format(stageResultData.getEfficiency() / standard * 100)));
                stageResultData.setStageEfficiency(Double.valueOf(decimalFormat.format(stageResultData.getEfficiency() / standard * 100)));
                stageResultData.setStageEfficiencyEx(Double.valueOf(decimalFormat.format(stageResultData.getEfficiencyEx() / standard * 100)));
            }

            for (int i = stageColorIndex; i < stageResultByTypeListCopy.size(); i++) {
                if(-1==stageResultByTypeListCopy.get(i).getStageColor()) continue;
                if (stageResultByTypeListCopy.get(stageColorIndex).getApExpect() > stageResultByTypeListCopy.get(i).getApExpect()) {
                    stageColorIndex = i;
                }
            }



            for (int i = indexStart; i < stageResultByTypeListCopy.size(); i++) {
              int color =  stageResultByTypeListCopy.get(i).getStageColor();
                 if(i==stageColorIndex){
                     if(i==indexStart){
                        color++;
                     }else {
                         color--;
                     }
                 }
                if(i==indexStart){
                    color++;
                }
                stageResultByTypeListCopy.get(i).setStageColor(color);
            }



            //组装成返回结果
            stageResultListT3.add(stageResultByTypeListCopy);
        }





        return stageResultListT3;

    }


    /**
     * @param times      样本量
     * @param expect     期望理智
     * @param stageState 关卡类型
     * @param expCoefficient    经验书的价值系数
     * @return
     */
    @Override
    public List<List<StageResultData>> setStageResultPercentageT2(Integer times, Double expect, Integer stageState, Double expCoefficient) {
        //最终返回的结果集合
        List<List<StageResultData>> stageResultListT2 = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        String[] mainName = new String[]{"装置", "聚酸酯", "固源岩", "异铁", "糖", "酮凝集", "破损装置", "酯原料", "源岩", "异铁碎片", "代糖", "双酮"};
        String[] typeName = new String[]{"全新装置", "聚酸酯组", "固源岩组", "聚酸酯组", "糖组", "酮凝集组", "全新装置", "聚酸酯组", "固源岩组", "聚酸酯组", "糖组", "酮凝集组"};


        for (int i = 0; i < mainName.length; i++) {
            //  根据材料名称查出关卡,是否可查出取决于isShow属性,1为可显示
            List<StageResultData> stageResultByExpect = stageResultDataDao.findByItemNameAndIsShowAndExpCoefficientAndApExpectLessThanAndSampleSizeGreaterThanOrderByApExpectAsc(
                    mainName[i], 1, expCoefficient, 50.0, 100);
            List<StageResultData> page = new ArrayList<>(stageResultByExpect);
            // 根据材料类型(这里查的是t2材料的上位t3材料)查出关卡,是否可查出取决于isShow属性,1为可显示
            double standard = 1.25; //绿票绝对效率的上限  等同于理智转化率100%


            //将绿票绝对效率转化为理智转化率100%
            for (StageResultData stageResultData : page) {
//                log.info( stageResultData.getStageName() +" = "+stageResultData.getEfficiency() +" / "+ standard +" = "+
//                        Double.valueOf(decimalFormat.format(stageResultData.getEfficiency() / standard * 100)));
                stageResultData.setStageEfficiency(Double.valueOf(decimalFormat.format(stageResultData.getEfficiency() / standard * 100)));
            }

            stageResultListT2.add(page);
        }

        //组装成返回结果
        return stageResultListT2;
    }

    /**
     * @param actNameList 活动名称集合
     * @param stageState  关卡类型
     * @param expCoefficient    经验书的价值系数
     * @return
     */
    @Override
    public List<List<StageResultData>> setClosedActivityStagePercentage(String[] actNameList, Integer stageState, Double expCoefficient) {
        //最终返回的结果集合
        List<List<StageResultData>> stageResultListClosed = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        for (String actName : actNameList) {
            //查出循环中当前活动名称的关卡
            List<StageResultData> list = stageResultDataDao.findByZoneIdAndExpCoefficientAndMainLevelGreaterThanAndMainIsNotNullOrderByCodeAsc(actName, expCoefficient, 2);
            if (list.size() < 1) continue;
            for (StageResultData stageResultData : list) {
                double standard = 1.25; //绿票绝对效率的上限  等同于理智转化率100%
                //将绿票绝对效率转化为理智转化率100%
                stageResultData.setStageEfficiency(Double.valueOf(decimalFormat.format((stageResultData.getEfficiency() + 0.09) / standard * 100)));

            }

            stageResultListClosed.add(list);
        }

        return stageResultListClosed;
    }


    @Override
    public List<StageOrundumVo> setOrundumEfficiency() {
        DecimalFormat decimalFormat_2 = new DecimalFormat("0.00");
        DecimalFormat decimalFormat_0 = new DecimalFormat("0");
        List<StageOrundumVo> list = new ArrayList<>();

        List<StageInfoVo> allVo = stageService.findAllVo();
        List<StageResultData> stageResultDataStandard = stageResultDataDao.findByStageIdAndIsShow("main_01-07", 1);
        HashMap<String, Double> standardHashMap = orundumPerApCal(stageResultDataStandard);
        double standard = standardHashMap.get("orundumPerAp");

        StageOrundumVo stageOrundumVoStandard = new StageOrundumVo();
        stageOrundumVoStandard.setStageCode(stageResultDataStandard.get(0).getStageCode());

        stageOrundumVoStandard.setOrundumPerAp(Double.valueOf(decimalFormat_2.format(standard)));
        stageOrundumVoStandard.setStageEfficiency(100.00);
        stageOrundumVoStandard.setOrundumPerApEfficiency(100.00);
        stageOrundumVoStandard.setLMDCost(Double.valueOf(decimalFormat_2.format(standardHashMap.get("LMDCost"))));

        list.add(stageOrundumVoStandard);

        for (StageInfoVo stageInfoVo : allVo) {
            if (stageInfoVo.getIsShow() == 0 || "main_01-07".equals(stageInfoVo.getStageId())) continue;
            List<StageResultData> byStageCode = stageResultDataDao.findByStageIdAndIsShow(stageInfoVo.getStageId(), 1);
            if (byStageCode.size() < 1) continue;
            HashMap<String, Double> hashMap = orundumPerApCal(byStageCode);
            Double orundumPerAp = hashMap.get("orundumPerAp");


            if (orundumPerAp < 0.3) continue;

            StageOrundumVo stageOrundumVo = new StageOrundumVo();
            stageOrundumVo.setStageCode(byStageCode.get(0).getStageCode());
            stageOrundumVo.setOrundumPerAp(Double.valueOf(decimalFormat_2.format(orundumPerAp)));
            double stageEfficiency = byStageCode.get(0).getEfficiency() / stageResultDataStandard.get(0).getEfficiency();
            stageOrundumVo.setStageEfficiency(Double.valueOf(decimalFormat_2.format(stageEfficiency * 100)));
            stageOrundumVo.setOrundumPerApEfficiency(Double.valueOf(decimalFormat_2.format((orundumPerAp / standard) * 100)));
            stageOrundumVo.setLMDCost(Double.valueOf(decimalFormat_2.format(hashMap.get("LMDCost"))));

            list.add(stageOrundumVo);

        }

        list.sort(Comparator.comparing(StageOrundumVo::getOrundumPerAp).reversed());
        return list;
    }

    /**
     * @param allStageResultHashMap <材料名，所有该种材料关卡效率结果的集合>
     * @return HashMap<String, Double>  map为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
     */
    @Override
    public HashMap<String, Double> getIterationItemValue(HashMap<String, List<StageResultData>> allStageResultHashMap) {

        //map为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
        HashMap<String, Double> mianValueNum = new HashMap<>();

        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液", "转质盐组"};
        for (String itemName : mainName) {


            List<StageResultData> stageResultList = allStageResultHashMap.get(itemName);

            stageResultList.sort(comparing(StageResultData::getEfficiency).reversed());  //按关卡效率倒序排序

//   判断是否用于材料定价，取当前材料最高效率
            double maxEfficiency = 1.25;
            for (StageResultData stageResultData : stageResultList) {
                if (stageResultData.getIsValue() == 1) {
                    maxEfficiency = stageResultData.getEfficiency();
                    break;
                }
            }
            //map为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
            mianValueNum.put(itemName, 1.25 / maxEfficiency);
//            log.info(rawDataList.get(0).getStageName() + "的临时效率是" + rawDataList.get(0).getEfficiency());
        }


        return mianValueNum;
    }

    /**
     * @param stageResultData 关卡对象
     * @param expCoefficient         效率版本（区别为经验书的价值系数）
     * @return
     */
    @Override
    public List<StageResultData> setSpecialActivityStage(StageResultData stageResultData, Double expCoefficient) {
        double number = stageResultData.getEfficiency() - 0.054;
        DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
        List<StageResultData> list = new ArrayList<>();

        String[] item = new String[]{"异铁组", "轻锰矿", "固源岩组", "固源岩", "糖", "聚酸酯", "异铁", "酮凝集", "装置"};
        String[] itemId = new String[]{"30043", "30083", "30013", "30012", "30022", "30032", "30042", "30052", "30062"};
        Double[] value = new Double[]{34.504, 34.755, 22.262, 4.58, 7.3593, 7.2534, 8.7857, 8.7911, 11.6551,};
        Double[] cost = new Double[]{50.0, 50.0, 35.0, 8.0, 13.0, 13.0, 16.0, 16.0, 22.0,};
        int[] itemLevel = new int[]{3, 3, 3, 2, 2, 2, 2, 2, 2,};
        double reason = 18.0;

        long id = 0L;
        if (expCoefficient == 0.76) id = 200L;
        if (expCoefficient == 1.0) id = 300L;
        if (expCoefficient == 0.625) id = 400L;

//        if ("auto".equals(dataType)) id = id * 10;

        for (int i = 0; i < item.length; i++) {
            StageResultData stageData = new StageResultData();
            stageData.setId(id + stageResultData.getId() + i);
            stageData.setStageCode(stageResultData.getStageCode());
            stageData.setIsValue(0);
            stageData.setIsShow(stageResultData.getIsShow());
            stageData.setSecondary("1");
            stageData.setZoneId(stageResultData.getZoneId() + "_");
            stageData.setCode(stageResultData.getCode());
            stageData.setSampleSize(stageResultData.getSampleSize());
            stageData.setSampleConfidence(stageResultData.getSampleConfidence());
            stageData.setStageColor(2);
            stageData.setIsShow(stageResultData.getIsShow());
            stageData.setSpm(stageResultData.getSpm());
            stageData.setMain(item[i]);
            stageData.setStageState(stageResultData.getStageState());
            stageData.setItemType(item[i]);
            stageData.setItemId(itemId[i]);
            stageData.setKnockRating((reason * number) / cost[i]);
            stageData.setActivityName(stageResultData.getActivityName());
            stageData.setMainLevel(itemLevel[i]);
            stageData.setApExpect(Double.valueOf(decimalFormat2.format(cost[i] / number)));
            stageData.setEfficiency(Double.valueOf(decimalFormat2.format(
                    (((reason * number) / cost[i]) * value[i] + reason * 0.06) / reason)));
            stageData.setExpCoefficient(expCoefficient);
//            System.out.println(stageData);
            list.add(stageData);
        }
        return list;
    }


    @Override
    public Double getConfidenceInterval(Integer penguinDataTimes, StageInfoVo stageInfoVo, double itemValue, double probability, List<QuantileTable> quantileTableList) {
        double confidenceInterval = 0.0;
        double quantileValue = 0.0;
        int quantileTableListCode = 0;

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        quantileValue = 0.03 * stageInfoVo.getApCost() * 1.25 / itemValue / Math.sqrt(1 * probability * (1 - probability) / (penguinDataTimes - 1));
        for (int j = 1; j < quantileTableList.size(); j++) {
            if (quantileValue < quantileTableList.get(j).getValue()) {
                quantileTableListCode = j;
                break;
            }
        }
        if (quantileValue < 3.09023) {
            confidenceInterval = (quantileTableList.get(quantileTableListCode - 1).getSection() * 2 - 1) * 100;
        } else {
            confidenceInterval = 99.9;
        }

        return confidenceInterval;
    }


    private HashMap<String, Double> orundumPerApCal(List<StageResultData> stageResultDataList) {
        double item_30011 = 0.0;
        double item_30012 = 0.0;
        double item_30061 = 0.0;
        double item_30062 = 0.0;

        for (StageResultData stageResultData : stageResultDataList) {
            if ("30011".equals(stageResultData.getItemId())) {
                item_30011 = stageResultData.getKnockRating() / stageResultData.getApCost();
            }
            if ("30012".equals(stageResultData.getItemId())) {
                item_30012 = stageResultData.getKnockRating() / stageResultData.getApCost();
            }
            if ("30061".equals(stageResultData.getItemId())) {
                item_30061 = stageResultData.getKnockRating() / stageResultData.getApCost();
            }
            if ("30062".equals(stageResultData.getItemId())) {
                item_30062 = stageResultData.getKnockRating() / stageResultData.getApCost();
            }
        }


        double orundumPerAp = (item_30012 / 2 + item_30011 / 6 + item_30061 / 3 + item_30062) * 10;
        double LMDCost = ((item_30012 + item_30011 / 6) / 2 * 1600 + (item_30061 / 3 + item_30062) * 1000) * (600 / orundumPerAp) / 10000;

        HashMap<String, Double> hashMap = new HashMap<>();
        hashMap.put("orundumPerAp", orundumPerAp);
        hashMap.put("LMDCost", LMDCost);
        return hashMap;
    }
}
