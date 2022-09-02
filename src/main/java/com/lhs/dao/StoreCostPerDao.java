package com.lhs.dao;

import com.lhs.bean.DBPogo.StoreCostPer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StoreCostPerDao extends JpaRepository<StoreCostPer, Long>{

	List<StoreCostPer> findByStoreTypeOrderByCostPerDesc(String type);
	
	
}
