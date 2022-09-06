package com.lhs.service;

import com.lhs.bean.pojo.PenguinData;
import com.lhs.bean.vo.StageResultVo;

import java.util.List;

public interface StageResultCalcService {

	//保存关卡效率信息
	void saveAll(List<StageResultVo> stageResultVos);

	//删除所有数据
	void deleteAllInBatch();

	//计算关卡效率（循环次数，结束次数）
	List<PenguinData> stageResult(Integer i,Integer countNum);
}