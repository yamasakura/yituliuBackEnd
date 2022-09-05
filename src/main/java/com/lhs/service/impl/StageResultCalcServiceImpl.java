package com.lhs.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.pojo.PenguinData;
import com.lhs.bean.DBPogo.Item;
import com.lhs.bean.pojo.PenguinClass;
import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.bean.vo.StageVo;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.ReadJsonUtil;
import com.lhs.common.util.ResultCode;
import com.lhs.dao.QuantileTableDao;
import com.lhs.dao.StageResultVoDao;

import com.lhs.service.ItemService;
import com.lhs.service.StageResultSetInfoService;
import com.lhs.service.StageService;
import com.lhs.service.StageResultCalcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
@Slf4j
public class StageResultCalcServiceImpl implements StageResultCalcService {

    @Autowired
    private StageResultVoDao stageResultVoDao;

    @Autowired
    private StageService stageService;

    @Autowired
    private ItemService itemService;

    @Value("${penguin.path}")
    private String penguinFilePath;

    @Autowired
    private StageResultSetInfoService stageResultSetInfoService;

    @Autowired
    private QuantileTableDao quantileTableDao;


    @Override
    public void saveAll(List<StageResultVo> stageResultVo) {
        if (stageResultVo != null) {
            stageResultVoDao.saveAll(stageResultVo);
        } else {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
    }


    @Override
    public void deleteAllInBatch() {
        stageResultVoDao.deleteAllInBatch();
    }

    /**
     * 前几天临时改的，暂时用循环实现，回头再优化了
     * @param indexNum  计算次数
     * @param countNum  计算结束次数
     * @return
     */
    @Override
    public List<PenguinData> stageResult(Integer indexNum, Integer countNum) {
        SimpleDateFormat simpleDateFormat_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        DecimalFormat DecimalFormat_2 = new DecimalFormat("0.0");
        SimpleDateFormat simpleDateFormat_dd = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
//        DecimalFormat DecimalFormat_3 = new DecimalFormat("0.000");
        Random random = new Random();
        String updateTime = simpleDateFormat_ss.format(new Date());
        log.info("更新时间是————" + updateTime);

        String saveTime = simpleDateFormat_dd.format(new Date());

//读取企鹅物流数据
        JSONObject matrixJson = null;
        String jsonFile = ReadJsonUtil.readJson(penguinFilePath+"matrix"+saveTime+".json");  //从保存文件读取
        matrixJson =  JSONObject.parseObject(jsonFile); //json化


//        企鹅物流的api，但是链接不稳定，目前采用本地读取
//        String url = "https://penguin-stats.io/PenguinStats/api/v2/result/matrix?show_closed_zones=true";   //API读取
//        matrixJson =  JSONObject.parseObject(HttpUtil.GetBody(url));


            List<PenguinData> penguinDataList = new ArrayList<>();  //临时关卡效率结果，有部分字段仅在计算中使用
            List<StageVo> stageInfoList = stageService.findAllVo();  //所有关卡信息
            List<Item> itemList = itemService.findAll();    //临时材料价值
            HashMap<String, Double> itemValueMap = new HashMap<>();
            for(Item item :itemList){
                itemValueMap.put(item.getItemName(),item.getItemValue());
            }

            List<QuantileTable> quantileTableList = quantileTableDao.findAll(); //置信度分位表

            List<PenguinClass> penguinDatalist = JSONObject.parseArray(JSON.toJSONString(matrixJson.get("matrix")), PenguinClass.class);  //转为集合


            penguinDataList = new ArrayList<>();


            double sum = 0.0;  //关卡效率临时结果
            double sumEx = 0.0; //关卡效率临时结果（小样）
            int rawDataListIndex = 0;  //如果上一关与下一关不同，记录当前索引

            double confidenceInterval = 0.0;  //置信度
            String exItem = "1";   //额外产物（为了理智小样准备的）
            for (int i = 0; i < penguinDatalist.size(); i++) {
                int stageInfoListIndex = 0;  //匹配关卡id后记录关卡信息集合的索引
                int itemListIndex = 0;    //匹配材料id后记录临时材料价值集合的索引
                double reason = 0.0;   //消耗理智
                double probability = 0.0; //概率
                double request = 0.0;  //单个材料的期望掉落理智价值
                double expect = 0.0;  //单个材料的期望理智
                double efficiency = 0.0;  //关卡效率
                double reasonEx = 0.0;    //消耗理智(减去小样）
                double requestEx = 0.0;   //物资箱的期望掉落理智价值
                double efficiencyEx = 0.0;  //关卡效率（小样活动）

                boolean stageNoNull = true;
                boolean itemNoNull = true;

                if (i > 0) {
//                这个if是用来重置关卡效率结果和记录上一关与下一关是否相同
                    if (!penguinDatalist.get(i).getStageId().equals(penguinDatalist.get(i - 1).getStageId())) {
                        sum = 0.0;
                        sumEx = 0.0;
                        confidenceInterval = 0.0;
                        rawDataListIndex = penguinDataList.size();
                        exItem = "1";
                    }
                }


                int penguinDataTimes = Integer.parseInt(penguinDatalist.get(i).getTimes());  //企鹅物流数据的样本数
                int penguinDataQuantity = Integer.parseInt(penguinDatalist.get(i).getQuantity());  //企鹅物流数据的掉落材料数
                String penguinDataItemId = penguinDatalist.get(i).getItemId();  //企鹅物流数据的材料id
                String penguinDataStageId = penguinDatalist.get(i).getStageId();//企鹅物流数据的关卡id


              //计算材料概率
                probability = (double) penguinDataQuantity / penguinDataTimes;

                if (penguinDataQuantity < 1) { continue; }   // 材料掉落次数太少跳出

                if (penguinDataTimes < 50) { continue; }  // 材料样本量太少跳出

                // 拿到当前企鹅物流数据对应的关卡信息
                for (int j = 0; j < stageInfoList.size(); j++) {
                    if (penguinDataStageId.equals(stageInfoList.get(j).getStageId())) {
                        stageInfoListIndex = j; stageNoNull = false; break;
                    }
                }
                if (stageNoNull ) continue;  // 没匹配对应关卡到就跳出


            // 拿到当前企鹅物流数据对应的材料信息
                for (int k = 0; k < itemList.size(); k++) {
                    if (penguinDataItemId.equals(itemList.get(k).getItemId())) {
                        itemListIndex = k; itemNoNull = false;
                        break;
                    }
                }


                if (itemNoNull) continue; // 没匹配对应材料到就跳出


                //计算样本置信度
                confidenceInterval =stageResultSetInfoService.getConfidenceInterval
                        (penguinDataTimes, stageInfoList.get(stageInfoListIndex), itemValueMap, probability, quantileTableList);


                //理智小样单独计算
                if ("ap_supply_lt_010".equals(itemList.get(itemListIndex).getItemId())) {
                    //关卡消耗理智减去小样的理智
                    stageInfoList.get(stageInfoListIndex).setReasonEx(stageInfoList.get(stageInfoListIndex).getReason() - probability * 10);
                    exItem = "ap_supply_lt_010";
                }

                if ("randomMaterial_6".equals(itemList.get(itemListIndex).getItemId())) {
                    //物资箱的额外结果
                    requestEx = itemList.get(itemListIndex).getItemValue() * probability;//材料单项期望产出理智结果
                }

                //取得关卡消耗理智
                reason = stageInfoList.get(stageInfoListIndex).getReason() * 1;
//            log.info(stageInfoList.get(stageInfoListIndex).getStageName() + "的理智消耗是：" + reason);
                //取得关卡消耗理智（小样）
                reasonEx = stageInfoList.get(stageInfoListIndex).getReasonEx() * 1;
//            log.info(stageInfoList.get(stageInfoListIndex).getStageName() + "的理智Ex消耗是：" + reasonEx);


                request = itemList.get(itemListIndex).getItemValue() * probability;//材料单项期望产出理智结果


                expect = reason / probability;  //材料期望理智
                sum = sum + request - requestEx; //期望产出理智总和（扣除小样）
                efficiency = (sum + reason * 0.054) / reason;  //关卡效率
                sumEx = sumEx + request;//理智期望产出总和（小样）
                efficiencyEx = (sumEx + reasonEx * 0.054) / reasonEx; //关卡效率（小样）

//            log.info(stageInfoList.get(stageInfoListIndex).getStageName()+" ,sum：" + sum + ",reason：" + reason + ",efficiency：" + efficiency);
//            log.info(stageInfoList.get(stageInfoListIndex).getStageName()+" ,requestEx：" + requestEx +" ,sumEx：" + sumEx + ",reasonEx：" + reasonEx + ",efficiencyEx：" + efficiencyEx);

                //存储原始数据
                PenguinData penguinData = new PenguinData();
                penguinData.setId((long)i);
                penguinData.setStageId(stageInfoList.get(stageInfoListIndex).getStageId());
                penguinData.setStageName(stageInfoList.get(stageInfoListIndex).getStageName());
                penguinData.setCode(stageInfoList.get(stageInfoListIndex).getChapterCode());
                penguinData.setChapterName(stageInfoList.get(stageInfoListIndex).getChapterName());
                penguinData.setItemId(itemList.get(itemListIndex).getItemId());
                penguinData.setItemName(itemList.get(itemListIndex).getItemName());
                penguinData.setTimes(penguinDataTimes);
                penguinData.setProbability(probability);
                if ("1-7".equals((stageInfoList.get(stageInfoListIndex).getStageName())) && "30012".equals(itemList.get(itemListIndex).getItemId())) {
                    penguinData.setExpect(expect * 5);
                } else {
                    penguinData.setExpect(expect);
                }
                penguinData.setEfficiency(efficiency);
                if (itemList.get(itemListIndex).getItemName().equals(stageInfoList.get(stageInfoListIndex).getMain())) {
                    penguinData.setMain(stageInfoList.get(stageInfoListIndex).getMain());
                    penguinData.setType(stageInfoList.get(stageInfoListIndex).getItemType());
                }
                penguinData.setSecondary(stageInfoList.get(stageInfoListIndex).getSecondary());
                penguinData.setSecondaryId(stageInfoList.get(stageInfoListIndex).getSecondaryId());
//                System.out.println(stageInfoList.get(stageInfoListIndex).getSecondaryId());
                penguinData.setColor(2);
                penguinData.setUpdateDate(updateTime);
                penguinData.setSpm(String.valueOf(stageInfoList.get(stageInfoListIndex).getSpm()));
                penguinData.setIsShow(stageInfoList.get(stageInfoListIndex).getIsShow());
                penguinData.setIsUseValue(stageInfoList.get(stageInfoListIndex).getIsValue());
                penguinData.setIsSpecial(stageInfoList.get(stageInfoListIndex).getIsSpecial());
                penguinData.setIsOpen(stageInfoList.get(stageInfoListIndex).getIsOpen());
                penguinData.setActivityName(exItem);
                penguinData.setRequest(request);
                penguinData.setEfficiencyEx(efficiencyEx);
                penguinData.setMainLevel(stageInfoList.get(stageInfoListIndex).getMainLevel());
                penguinData.setActivityName(stageInfoList.get(stageInfoListIndex).getActivityName());
                penguinData.setConfidence(confidenceInterval);

                for (int k = rawDataListIndex; k < penguinDataList.size(); k++) {
                    penguinDataList.get(k).setEfficiency(efficiency);
                    penguinDataList.get(k).setEfficiencyEx(efficiencyEx);

                }


                penguinDataList.add(penguinData);
            }


            try {
                //这里是将多索雷斯的关卡转化为通用关卡存储格式
                for (int i = 0; i < penguinDataList.size(); i++) {
                    if ("双日城大乐透".equals(penguinDataList.get(i).getType()) && penguinDataList.get(i).getTimes() > 100) {
                        List<PenguinData> dataList = stageResultSetInfoService.setSpecialActivityStage(penguinDataList.get(i));
                        penguinDataList.addAll(dataList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//        for(PenguinData penguinData:penguinDataList){
//            System.out.println(penguinData);
//        }


        // 将计算完的结果按分类放入map中，方便后面为他们赋值颜色等级
        //存入的是<材料名，所有该种材料关卡效率结果的集合>
            HashMap<String, List<PenguinData>> rawDataHashMap = new HashMap<>();
                   //
                for (PenguinData rawData : penguinDataList) {
                    //筛选条件  1.主产物不为空，2.该关卡是要显示的关卡，3.该关卡的主产物等级是T3，4.样本量大于500
                    if (rawData.getMain() != null && rawData.getIsShow() == 1 &&rawData.getMainLevel()>2&&rawData.getTimes()>500) {
                        if (rawDataHashMap.get(rawData.getType()) == null) {
                            List<PenguinData> rawDataListValue = Collections.singletonList(rawData);
                            rawDataHashMap.put(rawData.getType(), rawDataListValue);
                        } else {
                            List<PenguinData> rawDataListValue = new ArrayList<>(rawDataHashMap.get(rawData.getType()));
                            rawDataListValue.add(rawData);
                            rawDataHashMap.put(rawData.getType(), rawDataListValue);
                        }
                    }
                }

               //会返回一个map，map值为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
            HashMap<String, Double> iterationItemValue = stageResultSetInfoService.getIterationItemValue(rawDataHashMap);

//          当循环次数=结束次数不再计算材料等效价值
            if (indexNum !=(countNum-1)) {
                //这里是进行材料等效价值的计算
                itemService.itemRevise(iterationItemValue);
            }


        return penguinDataList;
    }




}



