package com.lhs.service;

import com.lhs.bean.DBPogo.Item;
import com.lhs.bean.DBPogo.ItemRevise;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface ItemService {

	//查找临时的等效理智价值
	List<Item> findAll();
	//查找最终的等效理智价值
	List<ItemRevise> findAllItemRevise(Double version);

	/**
	 *重置临时价值为绿票商店的价值
	 * @param version 效率版本（区别为经验书的价值系数）
	 */
	void resetItemShopValue(Double version);

	void resetItemReviseTable();

	/**
	 *价值迭代
	 * @param hashMap map为<材料名,1.25/材料的最优常驻关卡的效率>
	 * @param version  效率版本（区别为经验书的价值系数）
	 * @return  材料价值集合
	 */
	List<ItemRevise> itemRevise(HashMap<String, Double> hashMap,Double version,Integer index);

   //导出等效理智/绿票价值 Excel格式
	void exportItemDataToExcel(HttpServletResponse response);
	//导出等效理智/绿票价值 Json格式
	void exportItemDataToJson(HttpServletResponse response);
	//导入自定义价值计算礼包商店性价比 Excel格式
	String importItemCustomValue(MultipartFile file);
}
