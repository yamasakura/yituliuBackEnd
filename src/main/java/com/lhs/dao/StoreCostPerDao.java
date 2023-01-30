package com.lhs.dao;

import com.lhs.bean.DBPogo.StoreCostPer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StoreCostPerDao extends JpaRepository<StoreCostPer, Long>{
	//查询常驻商店性价比按性价比倒序
	List<StoreCostPer> findByStoreTypeOrderByCostPerDesc(String type);

}
