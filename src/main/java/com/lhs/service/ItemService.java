package com.lhs.service;

import com.lhs.bean.DBPogo.Item;
import com.lhs.bean.DBPogo.ItemRevise;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface ItemService {
	//重置临时价值为绿票商店的价值
	void resetItemShopValue();
	//查找临时的等效理智价值
	List<Item> findAll();
	//查找最终的等效理智价值
	List<ItemRevise> findAllItemRevise();

	/**
	 *  用于计算等效理智价值
	 * @param hashMap map为<材料名,1.25/材料的最优常驻关卡的效率>
	 * @return
	 */
	List<ItemRevise> itemRevise(HashMap<String, Double> hashMap);

   //导出等效理智/绿票价值
	void exportItemData(HttpServletResponse response);
}
