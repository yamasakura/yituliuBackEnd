package com.lhs.service;

import com.lhs.bean.DBPogo.StoreCostPer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface StoreCostPerService {

	void save(List<StoreCostPer> storeCostPer);

	//查找商店性价比信息
	List<StoreCostPer> findAll(String type);
    //通过json更新常驻商店
	void updateStorePermByJson();
	//通过json更新活动商店
	void updateStoreActByJson(MultipartFile file);
	//通过json更新活动礼包
	void updateStorePackByJson(String packStr);
    //读取活动商店json
	String readActStoreJson();
	//读取常驻商店json
	String readPermStoreJson();

    String readPackStoreJson();

    void exportPackJson(HttpServletResponse response, Long uid);
}
