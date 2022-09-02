package com.lhs.dao;

import com.lhs.bean.DBPogo.ItemRevise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemReviseDao extends JpaRepository<ItemRevise, Long>{



	@Transactional
	List<ItemRevise> findAllByOrderByItemIdAsc();

	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE item_revise",nativeQuery = true)
	void deleteQuery();

}
