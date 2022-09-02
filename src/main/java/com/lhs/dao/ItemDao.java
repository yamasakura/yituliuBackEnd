package com.lhs.dao;

import com.lhs.bean.DBPogo.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemDao extends JpaRepository<Item, Long>{

	@Transactional
	void deleteByItemId(String itemId);

	@Transactional
	List<Item> findAllByOrderByItemIdAsc();

}
