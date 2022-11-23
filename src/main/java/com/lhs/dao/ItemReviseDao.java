package com.lhs.dao;

import com.lhs.bean.DBPogo.ItemRevise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemReviseDao extends JpaRepository<ItemRevise, Long>{
	//查询物品价值按id升序
	List<ItemRevise> findByVersion(String version);

}
