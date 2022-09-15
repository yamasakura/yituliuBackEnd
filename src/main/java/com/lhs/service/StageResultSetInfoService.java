package com.lhs.service;

import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.vo.StageVo;

import java.util.HashMap;
import java.util.List;

public interface StageResultSetInfoService {

    //这个服务主要是用于设置各种二次计算的数值


    //添加T3类材料的理智转化率
    List<List<StageResultData>> setStageResultPercentageT3(Integer times, Double efficiency,Integer stageState);
    //添加T2类材料的理智转化率
    List<List<StageResultData>> setStageResultPercentageT2(Integer times, Double expect,Integer stageState);
    //添加已关闭活动副本的理智转化率
    List<List<StageResultData>> setClosedActivityStagePercentage(String[] actNameList,Integer stageState);
    //拿到用于迭代的材料名称和常驻最优关卡效率
    HashMap<String, Double> getIterationItemValue(HashMap<String, List<StageResultData>> rawDataHashMap);
    //添加部分特殊关卡的效率（多索雷斯）
    List<StageResultData> setSpecialActivityStage(StageResultData stageResultData);
    //添加置信度
    Double getConfidenceInterval(Integer penguinDataTimes, StageVo stageVo , double itemValue, double probability, List<QuantileTable> quantileTableList);
}
