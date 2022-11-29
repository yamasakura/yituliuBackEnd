package com.lhs.service;

import com.lhs.bean.DBPogo.QuantileTable;
import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.vo.StageOrundumVo;
import com.lhs.bean.vo.StageVo;

import java.util.HashMap;
import java.util.List;

public interface StageResultSetInfoService {

    //这个服务主要是用于设置各种二次计算的数值


    /**
     *
     * @param times  样本量
     * @param efficiency  关卡效率
     * @param stageState  关卡类型
     * @param version  效率版本（区别为经验书的价值系数）
     * @return
     */
    List<List<StageResultData>> setStageResultPercentageT3(Integer times, Double efficiency,Integer stageState,Double version,String dataType);

    /**
     * 添加T2类材料的理智转化率
     * @param times  样本量
     * @param expect  期望理智
     * @param stageState  关卡类型
     * @param version  效率版本（区别为经验书的价值系数）
     * @return
     */
    List<List<StageResultData>> setStageResultPercentageT2(Integer times, Double expect,Integer stageState,Double version,String dataType);

    /**
     * //添加已关闭活动副本的理智转化率
     * @param actNameList  活动名称集合
     * @param stageState  关卡类型
     * @param version  效率版本（区别为经验书的价值系数）
     * @return
     */
    List<List<StageResultData>> setClosedActivityStagePercentage(String[] actNameList,Integer stageState,Double version,String dataType);

    /**
     * //添加部分特殊关卡的效率（多索雷斯）
     * @param stageResultData  关卡对象
     * @param version  效率版本（区别为经验书的价值系数）
     * @return
     */
    List<StageResultData> setSpecialActivityStage(StageResultData stageResultData,Double version,String dataType);

    /**
     * 计算关卡搓玉的效率
     */
    List<StageOrundumVo> setOrundumEfficiency();

    /**
     *  拿到用于迭代的材料名称和常驻最优关卡效率
     * @param rawDataHashMap map为<材料名,1.25/材料的最优常驻关卡的效率>
     * @return
     */
    HashMap<String, Double> getIterationItemValue(HashMap<String, List<StageResultData>> rawDataHashMap);

    /**
     * //添加置信度
     * @param penguinDataTimes  样本量
     * @param stageVo   关卡对象
     * @param itemValue  物品价值
     * @param probability  掉率
     * @param quantileTableList  分位表
     * @return
     */
    Double getConfidenceInterval(Integer penguinDataTimes, StageVo stageVo , double itemValue, double probability, List<QuantileTable> quantileTableList);
}
