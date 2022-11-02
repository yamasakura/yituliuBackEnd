package com.lhs.service;

import com.lhs.bean.DBPogo.StageResultData;

import java.util.List;

public interface StageResultCalcService {

	//保存关卡效率信息
	void saveAll(List<StageResultData> stageResultData);

	//删除所有数据
	void deleteAllInBatch();

	//计算关卡效率（循环次数，结束次数）
	List<StageResultData> stageResult(Integer i, Integer countNum,Integer times,Double version);



}
