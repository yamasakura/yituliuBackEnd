package com.lhs.service;

import com.lhs.bean.DBPogo.Item;
import com.lhs.bean.DBPogo.ItemRevise;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface ItemService {

	void resetItemShopValue();

	List<Item> findAll();

	List<ItemRevise> findAllItemRevise();

	List<ItemRevise> itemResvise(HashMap<String, Double> desc);

	void exportItemData(HttpServletResponse response);
}
