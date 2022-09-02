package com.lhs.service;

import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.pojo.PenguinData;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.bean.vo.StageVo;

import java.util.HashMap;
import java.util.List;

public interface StageResultSetInfoService {

    //这个服务主要是用于设置各种二次计算的数值


    //添加T3类材料的理智转化率
    List<List<StageResultVo>> setStageResultPercentageT3(Integer times, Double efficiency);
    //添加T2类材料的理智转化率
    List<List<StageResultVo>> setStageResultPercentageT2(Integer times, Double expect);
    List<List<StageResultVo>> setClosedActivityStagePercentage(String[] actNameList);
    //拿到用于迭代的材料名称和常驻最优关卡效率
    HashMap<String, Double> getIterationItemValue(HashMap<String, List<PenguinData>> rawDataHashMap);
    //添加部分特殊关卡的效率（多索雷斯）
    List<PenguinData> setSpecialActivityStage(PenguinData penguinData);
    //设置历史关卡的理智转化率
    //添加置信度
    Double getConfidenceInterval(Integer penguinDataTimes, StageVo stageVo , HashMap<String, Double> itemValueMap, double probability, List<QuantileTable> quantileTableList);
}
