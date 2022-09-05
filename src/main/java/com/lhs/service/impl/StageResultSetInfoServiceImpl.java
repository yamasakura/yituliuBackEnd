package com.lhs.service.impl;

import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.pojo.PenguinData;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.bean.vo.StageVo;
import com.lhs.dao.StageResultVoDao;
import com.lhs.service.StageResultSetInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
@Slf4j
public class StageResultSetInfoServiceImpl implements StageResultSetInfoService {

    @Autowired
    private StageResultVoDao stageResultVoDao;

    /**
     * 设置关卡的理智转化率
     *
     * @param times      样本数
     * @param efficiency 最低效率  理智转化率100%=绿票绝对效率1.25
     * @return
     */
    @Override
    public List<List<StageResultVo>> setStageResultPercentageT3(Integer times, Double efficiency) {
        Random random = new Random();
        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液"};

        //最终返回的结果集合
        List<List<StageResultVo>> stageResultListT3 = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        for (String main : mainName) {
//            根据材料类型查出关卡,是否可查出取决于isShow属性,1为可显示
            List<StageResultVo> stageResultByTypeList =
                    stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc
                            (main, 1, efficiency, times);
//            复制一个集合 ,对数据库查出的集合直接进行set操作会改变session缓存中的数据，数据库的值也会更新,虽然好像也没啥事,但是还是复制了一下
            List<StageResultVo> stageResultByTypeListCopy = new ArrayList<>(stageResultByTypeList);


            for (int k = 0; k < stageResultByTypeList.size(); k++) {
//                当该关卡未被用于材料定价,判断他是活动本
                if (stageResultByTypeListCopy.get(k).getIsUseValue() == 0 && (!"act_side12_".equals(stageResultByTypeListCopy.get(k).getChapterName()))
                        && (!"act_side12_rep_".equals(stageResultByTypeListCopy.get(k).getChapterName()))) {
//                    这里会加一个单独的计算入商店无限池的龙门币后的活动本效率
                    StageResultVo stageResultVo = new StageResultVo();
                    BeanUtils.copyProperties(stageResultByTypeListCopy.get(k), stageResultVo);
                    stageResultVo.setEfficiency(stageResultByTypeListCopy.get(k).getEfficiency() + 0.09);
                    stageResultVo.setSecondary("龙门币");
                    stageResultVo.setSecondaryId("4001");
                    stageResultVo.setColor(-1);  //设置颜色为-1,前端会判断为红色
                    stageResultByTypeListCopy.add(0, stageResultVo);  //塞入复制后的集合中
                }
            }


            double standard = 1.25;    //绿票绝对效率的上限  等同于理智转化率100%

//      实际效率可能会比这个浮动0.几,下面会找出来每个材料排在第一的关卡效率作为标准,但是需要他是作为定价本使用的,判断isUseValue是1
            for (StageResultVo stageResultVo : stageResultByTypeList) {
                if (stageResultVo.getIsUseValue() == 1) {
                    standard = stageResultVo.getEfficiency();
//                    log.info("当前标准是"+stageResultVo.getStageName()+"——"+standard);
                    break;
                }
            }

            //将绿票绝对效率转化为理智转化率100%
            for (StageResultVo stageResultVo : stageResultByTypeListCopy) {
//                log.info( stageResultVo.getStageName() +" = "+stageResultVo.getEfficiency() +" / "+ standard +" = "+
//                        Double.valueOf(dfbfb.format(percentage * 100)));
                stageResultVo.setPercentage(Double.valueOf(decimalFormat.format(stageResultVo.getEfficiency() / standard * 100)));
            }
            //组装成返回结果
            stageResultListT3.add(stageResultByTypeListCopy);
        }


        return stageResultListT3;

    }

    @Override
    public List<List<StageResultVo>> setStageResultPercentageT2(Integer times, Double expect) {
        //最终返回的结果集合
        List<List<StageResultVo>> stageResultListT2 = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        String[] mainName = new String[]{"装置", "聚酸酯", "固源岩", "异铁", "糖", "酮凝集", "破损装置", "酯原料", "源岩", "异铁碎片", "代糖", "双酮"};
        String[] typeName = new String[]{"全新装置", "聚酸酯组", "固源岩组", "聚酸酯组", "糖组", "酮凝集组", "全新装置", "聚酸酯组", "固源岩组", "聚酸酯组", "糖组", "酮凝集组"};


        for (int i = 0; i < mainName.length; i++) {
            //  根据材料名称查出关卡,是否可查出取决于isShow属性,1为可显示
            List<StageResultVo> stageResultByExpect = stageResultVoDao.findByItemNameAndIsShowAndExpectLessThanAndTimesGreaterThanOrderByExpectAsc(
                    mainName[i], 1, 50.0, 100);
            List<StageResultVo> page = new ArrayList<>(stageResultByExpect);
            // 根据材料类型(这里查的是t2材料的上位t3材料)查出关卡,是否可查出取决于isShow属性,1为可显示
            List<StageResultVo> stageResultByTypeList_t3 =
                    stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc
                            (typeName[i], 1, expect, times);

            double standard = 1.25; //绿票绝对效率的上限  等同于理智转化率100%
            //      实际效率可能会比这个浮动0.几,下面会找出来每个材料排在第一的关卡效率作为标准,但是需要他是作为定价本使用的,判断isUseValue是1
            for (StageResultVo resultVo : stageResultByTypeList_t3) {
                if (resultVo.getIsUseValue() == 1) {
                    standard = resultVo.getEfficiency();
                    break;
                }
            }


            //将绿票绝对效率转化为理智转化率100%
            for (StageResultVo stageResultVo : page) {
                stageResultVo.setPercentage(Double.valueOf(decimalFormat.format(stageResultVo.getEfficiency() / standard * 100)));
            }

            stageResultListT2.add(page);
        }

        //组装成返回结果
        return stageResultListT2;
    }


    @Override
    public List<List<StageResultVo>> setClosedActivityStagePercentage(String[] actNameList) {
        //最终返回的结果集合
        List<List<StageResultVo>> stageResultListClosed = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        int pageNum = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        for (String actName : actNameList) {
            //查出循环中当前活动名称的关卡
            List<StageResultVo> list = stageResultVoDao.findByChapterNameAndMainLevelGreaterThanAndMainIsNotNullOrderByCodeAsc(actName, 2);

            for (StageResultVo stageResultVo : list) {
                // 根据材料类型查出关卡,是否可查出取决于isShow属性,1为可显示
                Page<StageResultVo> permStageList = stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(
                        stageResultVo.getType(), 1, 1.0, 50, pageable);
                double standard = 1.25; //绿票绝对效率的上限  等同于理智转化率100%
                //      实际效率可能会比这个浮动0.几,下面会找出来每个材料排在第一的关卡效率作为标准,但是需要他是作为定价本使用的,判断isUseValue是1
                for (int k = 0; k < permStageList.getContent().size(); k++) {
                    if (permStageList.getContent().get(k).getIsUseValue() == 1) {
                        standard = permStageList.getContent().get(k).getEfficiency();
                        break;
                    }
                }
                //将绿票绝对效率转化为理智转化率100%
                stageResultVo.setPercentage(Double.valueOf(decimalFormat.format(stageResultVo.getEfficiency() / standard * 100)));
            }

            stageResultListClosed.add(list);
        }

        return stageResultListClosed;
    }

    /**
     * @param rawDataHashMap <材料名，所有该种材料关卡效率结果的集合>
     * @return HashMap<String, Double>  map为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
     */
    @Override
    public HashMap<String, Double> getIterationItemValue(HashMap<String, List<PenguinData>> rawDataHashMap) {

        //map为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
        HashMap<String, Double> mianValueNum = new HashMap<>();

        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液"};
        for (String itemName : mainName) {
            List<PenguinData> rawDataMapValue = rawDataHashMap.get(itemName);

            rawDataMapValue.sort(comparing(PenguinData::getEfficiency).reversed());  //按关卡效率倒序排序

            //用于保存材料定价本的关卡集合
            List<PenguinData> rawDataList = new ArrayList<>();

            int min = 0;  // 期望理智最低的关卡的索引
       //通过判断期望理智给关卡赋予一个值,前端判断后渲染颜色   橙色:4 ,紫色:3 ,蓝色:2 ,绿色:1   红色:-1(这里不会用到)

            for (int j = 0; j < rawDataMapValue.size(); j++) {
                if (rawDataMapValue.get(min).getExpect() > rawDataMapValue.get(j).getExpect()) {
           //如果最高和次高的期望理智差距不到1,则不进行索引的更改
                    if (rawDataMapValue.get(min).getExpect() - rawDataMapValue.get(j).getExpect() > 1) {
                        min = j;
                    }
                }
            }

           //  索引是0,该关卡效率第一 ,期望理智也是最底
            if (min == 0) {
                rawDataMapValue.get(0).setColor(4);
                // 索引非0,该关卡效率第一 ,期望理智也是最底

            } else {
                rawDataMapValue.get(0).setColor(3);
                rawDataMapValue.get(min).setColor(1);
            }


//   判断是否用于材料定价,是则加入集合中
            for (PenguinData penguinData : rawDataMapValue) {
                if (penguinData.getIsUseValue() == 1) {
                    rawDataList.add(penguinData);
                }
            }
            //map为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
            mianValueNum.put(itemName, 1.25 / rawDataList.get(0).getEfficiency());
//            log.info(rawDataList.get(0).getStageName() + "的临时效率是" + rawDataList.get(0).getEfficiency());
        }


        return mianValueNum;
    }

    @Override
    public List<PenguinData> setSpecialActivityStage(PenguinData penguinData) {
        double number = penguinData.getEfficiency() - 0.054;
        DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
        List<PenguinData> list = new ArrayList<>();
        Random random = new Random();
        String[] item = new String[]{"异铁组", "轻锰矿", "固源岩组", "固源岩", "糖", "聚酸酯", "异铁", "酮凝集", "装置"};
        String[] itemid = new String[]{"30043", "30083", "30013", "30012", "30022", "30032", "30042", "30052", "30062"};
        Double[] value = new Double[]{34.504, 34.755, 22.262, 4.58, 7.3593, 7.2534, 8.7857, 8.7911, 11.6551,};
        Double[] cost = new Double[]{50.0, 50.0, 35.0, 8.0, 13.0, 13.0, 16.0, 16.0, 22.0,};
        int[] itemLevel = new int[]{3, 3, 3, 2, 2, 2, 2, 2, 2,};
        double reason = 18.0;

        for (int i = 0; i < item.length; i++) {
            PenguinData stageData = new PenguinData();
            stageData.setId(penguinData.getId() + random.nextInt(100000));
            stageData.setStageName(penguinData.getStageName());
            stageData.setIsUseValue(0);
            stageData.setIsShow(penguinData.getIsShow());
            stageData.setSecondary("1");
            stageData.setChapterName(penguinData.getChapterName() + "_");
            stageData.setCode(penguinData.getCode());
            stageData.setTimes(penguinData.getTimes());
            stageData.setConfidence(penguinData.getConfidence());
            stageData.setColor(2);
            stageData.setIsShow(penguinData.getIsShow());
            stageData.setSpm(penguinData.getSpm());
            stageData.setMain(item[i]);
            stageData.setType(item[i]);
            stageData.setItemId(itemid[i]);
            stageData.setProbability((reason * number) / cost[i]);
            stageData.setActivityName(penguinData.getActivityName());
            stageData.setMainLevel(itemLevel[i]);
            stageData.setExpect(Double.valueOf(decimalFormat2.format(cost[i] / number)));
            stageData.setEfficiency(Double.valueOf(decimalFormat2.format(
                    (((reason * number) / cost[i]) * value[i] + reason * 0.06) / reason)));
//            System.out.println(stageData);
            list.add(stageData);
        }
        return list;
    }


    @Override
    public Double getConfidenceInterval(Integer penguinDataTimes, StageVo stageVo, HashMap<String, Double> itemValueMap, double probability, List<QuantileTable> quantileTableList) {
        double confidenceInterval = 0.0;
        double quantileValue = 0.0;
        int quantileTableListCode = 0;

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if (!"1".equals(stageVo.getSecondary())) {
            quantileValue = 0.03 * stageVo.getReason() * 1.25 / itemValueMap.get(stageVo.getSecondary()) / Math.sqrt(1 * probability * (1 - probability) / (penguinDataTimes - 1));
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


        } else if (!"0".equals(stageVo.getMain())) {

            quantileValue = 0.03 * stageVo.getReason() * 1.25 / itemValueMap.get(stageVo.getMain()) / Math.sqrt(1 * probability * (1 - probability) / (penguinDataTimes - 1));
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
        }


        return confidenceInterval;
    }
}
