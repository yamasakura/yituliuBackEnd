package com.lhs.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.DBPogo.Item;
import com.lhs.bean.pojo.PenguinClass;
import com.lhs.bean.DBPogo.QuantileTable;
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
    public void saveAll(List<StageResultData> stageResultData) {
        if (stageResultData != null) {
            stageResultVoDao.saveAll(stageResultData);
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
    public List<StageResultData> stageResult(Integer indexNum, Integer countNum) {
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


            List<StageResultData> stageResultDataList = new ArrayList<>();  //临时关卡效率结果，有部分字段仅在计算中使用


            List<StageVo> stageInfoList = stageService.findAllVo();  //所有关卡信息
            List<Item> itemList = itemService.findAll();    //临时材料价值
            HashMap<String, Double> itemValueMap = new HashMap<>();
            for(Item item :itemList){
                itemValueMap.put(item.getItemName(),item.getItemValue());
            }

            List<QuantileTable> quantileTableList = quantileTableDao.findAll(); //置信度分位表

            List<PenguinClass> penguinDatalist = JSONObject.parseArray(JSON.toJSONString(matrixJson.get("matrix")), PenguinClass.class);  //转为集合


            stageResultDataList = new ArrayList<>();


            double sum = 0.0;  //关卡效率临时结果
            double sumEx = 0.0; //关卡效率临时结果（小样）
            int differentStageIdIndex = 0;  //如果上一关与下一关不同，记录当前索引

            double confidenceInterval = 0.0;  //置信度

            for (int i = 0; i < penguinDatalist.size(); i++) {
                int stageInfoListIndex = 0;  //匹配关卡id后记录关卡信息集合的索引
                int itemListIndex = 0;    //匹配材料id后记录临时材料价值集合的索引
                double apCost = 0.0;   //消耗理智
                double probability = 0.0; //概率
                double result = 0.0;  //单个材料的期望掉落理智价值
                double apExpect = 0.0;  //单个材料的期望理智
                double efficiency = 0.0;  //关卡效率
                double apCostEx = 0.0;    //消耗理智(减去小样）
                double resultEx = 0.0;   //物资箱的期望掉落理智价值
                double efficiencyEx = 0.0;  //关卡效率（小样活动）

                boolean stageNoNull = true;
                boolean itemNoNull = true;

                if (i > 0) {
//                这个if是用来重置关卡效率结果和记录上一关与下一关是否相同
                    if (!penguinDatalist.get(i).getStageId().equals(penguinDatalist.get(i - 1).getStageId())) {
                        sum = 0.0;
                        sumEx = 0.0;
                        confidenceInterval = 0.0;
                        differentStageIdIndex = stageResultDataList.size();
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


                    if(!"1".equals(stageInfoList.get(stageInfoListIndex).getSecondary())&&itemList.get(itemListIndex).getItemName().equals(stageInfoList.get(stageInfoListIndex).getSecondary())){
                        confidenceInterval =stageResultSetInfoService.getConfidenceInterval
                                (penguinDataTimes, stageInfoList.get(stageInfoListIndex), itemValueMap.get(stageInfoList.get(stageInfoListIndex).getSecondary()), probability, quantileTableList);
                    }else{
                        if(!"0".equals(stageInfoList.get(stageInfoListIndex).getMain())&&itemList.get(itemListIndex).getItemName().equals(stageInfoList.get(stageInfoListIndex).getMain())){
                            confidenceInterval =stageResultSetInfoService.getConfidenceInterval
                                    (penguinDataTimes, stageInfoList.get(stageInfoListIndex), itemValueMap.get(stageInfoList.get(stageInfoListIndex).getMain()), probability, quantileTableList);
                        }
                    }


                //理智小样单独计算
                if ("ap_supply_lt_010".equals(itemList.get(itemListIndex).getItemId())) {
                    //关卡消耗理智减去小样的理智
                    stageInfoList.get(stageInfoListIndex).setApCostEx(stageInfoList.get(stageInfoListIndex).getApCost() - probability * 10);
                }

                if ("randomMaterial_6".equals(itemList.get(itemListIndex).getItemId())) {
                    //物资箱的额外结果
                    resultEx = itemList.get(itemListIndex).getItemValue() * probability;//材料单项期望产出理智结果
                }


                //取得关卡消耗理智
                apCost = stageInfoList.get(stageInfoListIndex).getApCost() * 1;
//            log.info(stageInfoList.get(stageInfoListIndex).getStageName() + "的理智消耗是：" + apCost);
                //取得关卡消耗理智（小样）
                apCostEx = stageInfoList.get(stageInfoListIndex).getApCostEx() * 1;
//            log.info(stageInfoList.get(stageInfoListIndex).getStageName() + "的理智Ex消耗是：" + apCostEx);


                result = itemList.get(itemListIndex).getItemValue() * probability;//材料单项期望产出理智结果


                apExpect = apCost / probability;  //材料期望理智
                sum = sum + result - resultEx; //期望产出理智总和（扣除小样）
                efficiency = (sum + apCost * 0.054) / apCost;  //关卡效率
                sumEx = sumEx + result;//理智期望产出总和（小样）
                efficiencyEx = (sumEx + apCostEx * 0.054) / apCostEx; //关卡效率（小样）

//            log.info(stageInfoList.get(stageInfoListIndex).getStageName()+" ,sum：" + sum + ",apCost：" + apCost + ",efficiency：" + efficiency);
//            log.info(stageInfoList.get(stageInfoListIndex).getStageName()+" ,resultEx：" + resultEx +" ,sumEx：" + sumEx + ",apCostEx：" + apCostEx + ",efficiencyEx：" + efficiencyEx);

                //存储原始数据
                StageResultData stageResultData = new StageResultData();
                stageResultData.setId((long)i);
                stageResultData.setStageId(stageInfoList.get(stageInfoListIndex).getStageId());
                stageResultData.setStageCode(stageInfoList.get(stageInfoListIndex).getStageCode());
                stageResultData.setCode(stageInfoList.get(stageInfoListIndex).getCode());
                stageResultData.setZoneId(stageInfoList.get(stageInfoListIndex).getZoneId());
                stageResultData.setItemId(itemList.get(itemListIndex).getItemId());
                stageResultData.setItemName(itemList.get(itemListIndex).getItemName());
                stageResultData.setSampleSize(penguinDataTimes);
                stageResultData.setKnockRating(probability);
                if ("1-7".equals((stageInfoList.get(stageInfoListIndex).getStageCode())) && "30012".equals(itemList.get(itemListIndex).getItemId())) {
                    stageResultData.setApExpect(apExpect * 5);
                } else {
                    stageResultData.setApExpect(apExpect);
                }
                stageResultData.setEfficiency(efficiency);
                if (itemList.get(itemListIndex).getItemName().equals(stageInfoList.get(stageInfoListIndex).getMain())) {
                    stageResultData.setMain(stageInfoList.get(stageInfoListIndex).getMain());
                    stageResultData.setItemType(stageInfoList.get(stageInfoListIndex).getItemType());
                }
                stageResultData.setSecondary(stageInfoList.get(stageInfoListIndex).getSecondary());
                stageResultData.setSecondaryId(stageInfoList.get(stageInfoListIndex).getSecondaryId());
//                System.out.println(stageInfoList.get(stageInfoListIndex).getSecondaryId());
                stageResultData.setStageColor(2);
                stageResultData.setUpdateTime(updateTime);
                stageResultData.setSpm(String.valueOf(stageInfoList.get(stageInfoListIndex).getSpm()));
                stageResultData.setIsShow(stageInfoList.get(stageInfoListIndex).getIsShow());
                stageResultData.setIsValue(stageInfoList.get(stageInfoListIndex).getIsValue());
                stageResultData.setStageState(stageInfoList.get(stageInfoListIndex).getStageState());
                stageResultData.setResult(result);
                stageResultData.setEfficiencyEx(efficiencyEx);
                stageResultData.setMainLevel(stageInfoList.get(stageInfoListIndex).getMainLevel());
                stageResultData.setActivityName(stageInfoList.get(stageInfoListIndex).getActivityName());
                stageResultData.setPart(stageInfoList.get(stageInfoListIndex).getPart());
                stageResultData.setPartNo(stageInfoList.get(stageInfoListIndex).getPartNo());
                stageResultDataList.add(stageResultData);


                for (int k = differentStageIdIndex; k < stageResultDataList.size(); k++) {
                    stageResultDataList.get(k).setEfficiency(efficiency);
                    stageResultDataList.get(k).setEfficiencyEx(efficiencyEx);
                    stageResultDataList.get(k).setSampleConfidence(confidenceInterval);
                }



//                System.out.println(stageResultData);
            }


            try {
                //这里是将多索雷斯的关卡转化为通用关卡存储格式
                for (int i = 0; i < stageResultDataList.size(); i++) {
                    if ("双日城大乐透".equals(stageResultDataList.get(i).getItemType()) && stageResultDataList.get(i).getSampleSize() > 100) {
                        List<StageResultData> dataList = stageResultSetInfoService.setSpecialActivityStage(stageResultDataList.get(i));
                        stageResultDataList.addAll(dataList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//        for(StageResultData penguinData:stageResultDataList){
//            System.out.println(penguinData);
//        }


        // 将计算完的结果按分类放入map中，方便后面为他们赋值颜色等级
        //存入的是<材料名，所有该种材料关卡效率结果的集合>
            HashMap<String, List<StageResultData>> allStageResultHashMap = new HashMap<>();
                   //
                for (StageResultData rawData : stageResultDataList) {
                    //筛选条件  1.主产物不为空，2.该关卡是要显示的关卡，3.该关卡的主产物等级是T3，4.样本量大于500
                    if (rawData.getMain() != null && rawData.getIsShow() == 1 &&rawData.getMainLevel()>2&&rawData.getSampleSize()>500) {
                        if (allStageResultHashMap.get(rawData.getItemType()) == null) {
                            List<StageResultData> rawDataListValue = Collections.singletonList(rawData);
                            allStageResultHashMap.put(rawData.getItemType(), rawDataListValue);
                        } else {
                            List<StageResultData> rawDataListValue = new ArrayList<>(allStageResultHashMap.get(rawData.getItemType()));
                            rawDataListValue.add(rawData);
                            allStageResultHashMap.put(rawData.getItemType(), rawDataListValue);
                        }
                    }
                }

               //会返回一个map，map值为<材料名,1.25/材料的最优常驻关卡的效率>  1.25为绿票与理智转化比,也是绿票绝对效率的上限
            HashMap<String, Double> iterationItemValue = stageResultSetInfoService.getIterationItemValue(allStageResultHashMap);

//          当循环次数=结束次数不再计算材料等效价值
            if (indexNum !=(countNum-1)) {
                //这里是进行材料等效价值的计算
                itemService.itemRevise(iterationItemValue);
            }


        return stageResultDataList;
    }




}



