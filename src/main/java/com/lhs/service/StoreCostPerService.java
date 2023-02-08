package com.lhs.service;

import com.lhs.bean.DBPogo.StoreCostPer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface StoreCostPerService {

	void save(List<StoreCostPer> storeCostPer);

	//查找商店性价比信息
	List<StoreCostPer> findStorePermByType(String type);
    //通过json更新常驻商店
	void updateStorePermByJson();
	//通过json更新活动商店
	void updateStoreActByJson(MultipartFile file);
	//通过json更新活动礼包
	void updateStorePackByJson(String packStr);
    //读取活动商店json返回前端
	String readActStoreJson();
	//读取常驻商店json返回前端
	String readPermStoreJson();
	//读取礼包商店json返回前端
	String readPackStoreJson();

}
