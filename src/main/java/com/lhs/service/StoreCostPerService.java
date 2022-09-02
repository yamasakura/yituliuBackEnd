package com.lhs.service;

import com.lhs.bean.DBPogo.StoreCostPer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface StoreCostPerService {

	void save(List<StoreCostPer> storeCostPer);

	//查找商店性价比信息
	List<StoreCostPer> findAll(String type);
    //通过json更新常驻商店
	void updateByJsonPerm();
	//通过json更新活动商店
	void updateByJsonAct(MultipartFile file);
    //读取活动商店json
	String readActStoreJson();
	//读取常驻商店json
	String readPermStoreJson();

}
