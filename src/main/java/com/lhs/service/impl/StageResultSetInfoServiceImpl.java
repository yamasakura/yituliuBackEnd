package com.lhs.service.impl;

import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.pojo.PenguinData;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.bean.vo.StageVo;
import com.lhs.dao.StageResultVoApiDao;
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


    @Override
    public  List<List<StageResultVo>>  setStageResultPercentageT3(Integer times, Double efficiency) {


        Random random = new Random();
        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液"};

        List<List<StageResultVo>>  stageResultListT3= new ArrayList<>();

        for (String main : mainName) {
            List<StageResultVo> stageResultByTypeList =
                    stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc
                            (main, 1, efficiency, times);
            List<StageResultVo> stageResultByTypeListCopy = new ArrayList<>(stageResultByTypeList);

            for (int k = 0; k < stageResultByTypeList.size(); k++) {
                if (stageResultByTypeListCopy.get(k).getIsUseValue() == 0 && (!"act_side12_".equals(stageResultByTypeListCopy.get(k).getChapterName()))
                        && (!"act_side12_rep_".equals(stageResultByTypeListCopy.get(k).getChapterName()))) {
                    StageResultVo stageResultVo = new StageResultVo();
                    BeanUtils.copyProperties(stageResultByTypeListCopy.get(k),stageResultVo);
                    stageResultVo.setEfficiency(stageResultByTypeListCopy.get(k).getEfficiency() + 0.09);
                    stageResultVo.setSecondary("龙门币");
                    stageResultVo.setSecondaryId("4001");
                    stageResultVo.setColor(-1);
                    stageResultByTypeListCopy.add(0, stageResultVo);
                    k++;
                }
            }


            double standard = 1.25;

            for (StageResultVo stageResultVo : stageResultByTypeList) {
                if (stageResultVo.getIsUseValue() == 1) {
                    standard = stageResultVo.getEfficiency();
//                    log.info("当前标准是"+stageResultVo.getStageName()+"——"+standard);
                    break;
                }
            }

            for (StageResultVo stageResultVo : stageResultByTypeListCopy) {
                double percentage = stageResultVo.getEfficiency() / standard;
                DecimalFormat dfbfb = new DecimalFormat("0.0");
//                log.info( stageResultVo.getStageName() +" = "+stageResultVo.getEfficiency() +" / "+ standard +" = "+
//                        Double.valueOf(dfbfb.format(percentage * 100)));
                stageResultVo.setPercentage(Double.valueOf(dfbfb.format(percentage * 100)));
            }

            stageResultListT3.add(stageResultByTypeListCopy);
        }


       return stageResultListT3;

    }

    @Override
    public  List<List<StageResultVo>>  setStageResultPercentageT2(Integer times, Double expect) {

        List<List<StageResultVo>>  stageResultListT2= new ArrayList<>();

        String[] mainName = new String[]{"装置", "聚酸酯", "固源岩", "异铁", "糖", "酮凝集", "破损装置", "酯原料", "源岩", "异铁碎片", "代糖", "双酮"};
        String[] typeName = new String[]{"全新装置", "聚酸酯组", "固源岩组", "聚酸酯组", "糖组", "酮凝集组", "全新装置", "聚酸酯组", "固源岩组", "聚酸酯组", "糖组", "酮凝集组"};

        List<Object> pageList = new ArrayList<>();
        for (int i = 0; i < mainName.length; i++) {
            List<StageResultVo> stageResultByExpect = stageResultVoDao.findByItemNameAndIsShowAndExpectLessThanAndTimesGreaterThanOrderByExpectAsc(
                    mainName[i], 1, 50.0, 100);
            List<StageResultVo> page = new ArrayList<>(stageResultByExpect);
            double standard = 1.25;
            List<StageResultVo> stageResultByTypeList_t3 =
                    stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc
                            (typeName[i], 1, expect, times);

            for (StageResultVo resultVo : stageResultByTypeList_t3) {
                if (resultVo.getIsUseValue() == 1) {
                    standard = resultVo.getEfficiency();
                    break;
                }
            }

            for (StageResultVo stageResultVo : page) {
                double percentage = stageResultVo.getEfficiency() / standard;
                DecimalFormat dfbfb = new DecimalFormat("0.0");
                stageResultVo.setPercentage(Double.valueOf(dfbfb.format(percentage * 100)));
            }

            stageResultListT2.add(page);
        }


        return stageResultListT2;
    }


    @Override
    public   List<List<StageResultVo>>  setClosedActivityStagePercentage(String[] actNameList ) {

        List<List<StageResultVo>>  stageResultListClosed= new ArrayList<>();
        int pageNum = 0;
        int pageSize = 6;
        Pageable pageable = PageRequest.of(pageNum, pageSize);


        for (String actName : actNameList) {
            //查出活动关卡

            List<StageResultVo> list = stageResultVoDao.findByChapterNameAndMainLevelGreaterThanAndMainIsNotNullOrderByCodeAsc(actName,2);

            for (StageResultVo stageResultVo : list) {
                //查出主线关卡算相对效率
                Page<StageResultVo> permStageList = stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(
                        stageResultVo.getType(), 1, 1.0, 0, pageable);

                DecimalFormat dfbfb = new DecimalFormat("0.0");
                double percentage = 0.0;
                double standard = 1.25;
                for (int k = 0; k < permStageList.getContent().size(); k++) {
                    if (permStageList.getContent().get(k).getIsUseValue() == 1) {
                        standard = permStageList.getContent().get(k).getEfficiency();
                        break;
                    }
                }
                percentage = stageResultVo.getEfficiency() / standard;
                stageResultVo.setPercentage(Double.valueOf(dfbfb.format(percentage * 100)));
            }

            stageResultListClosed.add(list);
        }

        return  stageResultListClosed;
    }


    @Override
    public HashMap<String, Double> getIterationItemValue(HashMap<String, List<PenguinData>> rawDataHashMap) {
        HashMap<String, Double> mianValueNum = new HashMap<>();
        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液"};
        for (String itemName : mainName) {
            List<PenguinData> rawDataMapValue = rawDataHashMap.get(itemName);
            rawDataMapValue.sort(comparing(PenguinData::getEfficiency).reversed());
            List<PenguinData> rawDataList = new ArrayList<>();
            int min = 0;
            for (int j = 0; j < rawDataMapValue.size(); j++) {
                if (rawDataMapValue.get(min).getExpect() > rawDataMapValue.get(j).getExpect()) {
                    if (rawDataMapValue.get(min).getExpect() - rawDataMapValue.get(j).getExpect() > 1) {
                        min = j;
                    }
                }
            }
            if (min == 0) {
                rawDataMapValue.get(0).setColor(4);
            } else {
                rawDataMapValue.get(0).setColor(3);
                rawDataMapValue.get(min).setColor(1);
            }

            for (PenguinData penguinData : rawDataMapValue) {
                if (penguinData.getIsValue() == 1) {
                    rawDataList.add(penguinData);
                }
            }

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
        int[] itemLevel = new int[]{3,3, 3,2, 2, 2, 2, 2, 2,};
        double reason = 18.0;

        for (int i = 0; i < item.length; i++) {
            PenguinData stageData = new PenguinData();
            stageData.setId(penguinData.getId()+random.nextInt(100000));
            stageData.setStageName(penguinData.getStageName());
            stageData.setIsValue(0);
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
