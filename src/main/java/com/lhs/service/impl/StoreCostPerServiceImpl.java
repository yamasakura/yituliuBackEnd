package com.lhs.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.DBPogo.ItemRevise;
import com.lhs.bean.vo.StoreJson;
import com.lhs.bean.DBPogo.StoreCostPer;
import com.lhs.bean.vo.StoreJsonVo;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.common.util.ResultCode;
import com.lhs.common.util.SaveFile;
import com.lhs.dao.StoreCostPerDao;
import com.lhs.service.ItemService;
import com.lhs.service.StoreCostPerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class StoreCostPerServiceImpl implements StoreCostPerService {
	
	@Autowired
	private  StoreCostPerDao storeCostPerDao ;

	@Autowired
	private ItemService itemService ;

	@Autowired
	private StoreCostPerService storeCostPerService ;



	@Value("${frontEnd.backups}")
	private String backupsPath;

	@Value("${frontEnd.path}")
	private  String frontEndFilePath;

	@Override
	public void save(List<StoreCostPer> storeCostPer) {
		if(storeCostPer!=null) {
			storeCostPerDao.saveAll(storeCostPer);
		}else {
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}

	}




	@Override
	public void updateByJsonPerm() {
		List<ItemRevise> allItem =itemService.findAllItemRevise("auto0.625");
		String str = ReadFileUtil.readFile(frontEndFilePath+"//permStoreData.json");
		List<StoreJson> storeJsons = JSONArray.parseArray(str, StoreJson.class);

		DecimalFormat dfbfb = new DecimalFormat("0.00");

		List<StoreCostPer> list = new ArrayList<>();

		Long count = 1L;


		for (int i = 0; i < Objects.requireNonNull(storeJsons).size(); i++) {
			for (int j = 0; j < allItem.size(); j++) {
				if (storeJsons.get(i).getItemName().equals(allItem.get(j).getItemName())) {
					Double costPer = allItem.get(j).getItemValue() / Double.parseDouble(storeJsons.get(i).getCost());
					StoreCostPer storeCostPer = new StoreCostPer();
					storeCostPer.setId(count);
					count++;
					storeCostPer.setItemId(allItem.get(j).getItemId());
					storeCostPer.setItemName(storeJsons.get(i).getItemName());
					storeCostPer.setStoreType(storeJsons.get(i).getType());
					storeCostPer.setCostPer(Double.valueOf(dfbfb.format(costPer)));
					storeCostPer.setCost(Double.valueOf(storeJsons.get(i).getCost()));
					storeCostPer.setRawCost(storeJsons.get(i).getRawCost());
					storeCostPer.setItemValue(allItem.get(j).getItemValue());
					list.add(storeCostPer);
				}
			}
		}

		storeCostPerService.save(list);

	}

	@Override
	public void updateByJsonAct(MultipartFile file) {
		List<ItemRevise> allItem =itemService.findAllItemRevise("auto0.625");
		HashMap<String, Double> itemValueMap = new HashMap<>();
		HashMap<String, String> itemIdMap = new HashMap<>();
		for(ItemRevise itemRevise:allItem){
			itemValueMap.put(itemRevise.getItemName(),itemRevise.getItemValue());
			itemIdMap.put(itemRevise.getItemName(),itemRevise.getItemId());
		}

		String fileStr  = ReadFileUtil.readFile(file);
		JSONArray jsonArray = JSONArray.parseArray(fileStr);
		List<Object>   jsonList  = new ArrayList<>();
		if(jsonArray!=null)
		for(Object object:jsonArray){
			Map storeMap = JSONObject.parseObject(object.toString());
			Object o = storeMap.get("actStore");
			List<StoreJsonVo> storeJsonVos = JSONArray.parseArray(o.toString(),StoreJsonVo.class);

			List<StoreJsonVo>  storeJsonVoResult = new ArrayList<>();

			Integer itemArea = storeJsonVos.get(storeJsonVos.size() - 1).getItemArea();
			for(int i=1;i<=itemArea;i++){
				List<StoreJsonVo> storeJsonVoCopy = new ArrayList<>();
				for(StoreJsonVo storeJsonVo:storeJsonVos){
					if(storeJsonVo.getItemArea()==i){
						storeJsonVo.setItemPPR(itemValueMap.get(storeJsonVo.getItemName())*storeJsonVo.getItemQuantity()/storeJsonVo.getItemPrice());
						storeJsonVo.setItemId(itemIdMap.get(storeJsonVo.getItemName()));
						storeJsonVoCopy.add(storeJsonVo);
					}
				}
				storeJsonVoCopy.sort(Comparator.comparing(StoreJsonVo::getItemPPR).reversed());
				storeJsonVoResult.addAll(storeJsonVoCopy);
			}
			storeMap.put("actStore",storeJsonVoResult);
			jsonList.add(storeMap);
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
		String saveTime = simpleDateFormat.format(new Date());


		Object fileVo = JSONObject.toJSON(jsonList);
		SaveFile.save(backupsPath,"storeAct"+saveTime+".json",fileVo.toString());
		SaveFile.save(frontEndFilePath,"storeAct.json",fileVo.toString());

	}

	@Override
	public void updateByJsonPack(MultipartFile file) {
		List<ItemRevise> allItem =itemService.findAllItemRevise("auto0.625");
		HashMap<String, Double> itemValueMap = new HashMap<>();
		HashMap<String, String> itemIdMap = new HashMap<>();
		for(ItemRevise itemRevise:allItem){
			itemValueMap.put(itemRevise.getItemName(),itemRevise.getItemValue()/1.25);
			itemIdMap.put(itemRevise.getItemName(),itemRevise.getItemId());
		}

		String fileStr  = ReadFileUtil.readFile(file);
		JSONArray jsonArray = JSONArray.parseArray(fileStr);

		double 	standard_gacha = 648/55.5;
		double  standard_ap = 3.5;

		List<Object>  packList = new ArrayList<>();
		if(jsonArray!=null) {
			for (Object object : jsonArray) {

				Map storePackMap = JSONObject.parseObject(object.toString());
				double apValueToOriginium = 0.0;  //材料理智折合源石

				double packOriginium = 0.0; // 礼包折合源石
				double packDraw = 0.0; // 共计多少抽
				double packRmbPerDraw = 0.0; // 每抽价格
				double packRmbPerOriginium = 0.0; // 每石价格
				double packPPRDraw = 0.0; // 每抽性价比，相比648
				double packPPROriginium = 0.0; // 每石性价比，相比648


				int gachaOrundum = Integer.parseInt(storePackMap.get("gachaOrundum").toString()); // 合成玉
				int gachaOriginium = Integer.parseInt(storePackMap.get("gachaOriginium").toString());
				; // 源石
				int gachaPermit = Integer.parseInt(storePackMap.get("gachaPermit").toString());
				; // 单抽
				int gachaPermit10 = Integer.parseInt(storePackMap.get("gachaPermit10").toString());
				; // 十连
				int packPrice = Integer.parseInt(storePackMap.get("packPrice").toString());
				; // 十连


				//计算该理智的材料总理智折合源石
				if (storePackMap.get("packContent") != null) {
					JSONArray jsonArrayItem = JSONArray.parseArray(storePackMap.get("packContent").toString());
					for (Object objectItem : jsonArrayItem) {
						Map itemMap = JSONObject.parseObject(objectItem.toString());
						Object itemName = itemMap.get("itemName");

						int itemQuantity = Integer.parseInt(itemMap.get("itemQuantity").toString());

						apValueToOriginium = apValueToOriginium + itemValueMap.get(itemName.toString()) * itemQuantity;
					}
				}
				if (apValueToOriginium > 0.0) {
					apValueToOriginium = apValueToOriginium / 135;
				}


				packDraw = gachaOrundum / 600.0 + gachaOriginium * 0.3 + gachaPermit + gachaPermit10;
				packRmbPerDraw = packPrice / packDraw;
				packOriginium = gachaOrundum / 180.0 + gachaOriginium + gachaPermit * 600 / 180.0 + gachaPermit10 * 600 / 180.0 + apValueToOriginium;
				packRmbPerOriginium = packPrice / packOriginium;
				packPPRDraw = standard_gacha / packRmbPerDraw;
				packPPROriginium = standard_ap / packRmbPerOriginium;

				storePackMap.put("packDraw", packDraw);
				storePackMap.put("packRmbPerDraw", packRmbPerDraw);
				storePackMap.put("packOriginium", packOriginium);
				storePackMap.put("packRmbPerOriginium", packRmbPerOriginium);
				storePackMap.put("packPPRDraw", packPPRDraw);
				storePackMap.put("packPPROriginium", packPPROriginium);
				packList.add(storePackMap);
			}
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
		String saveTime = simpleDateFormat.format(new Date());

		Object fileVo = JSONObject.toJSON(packList);
		SaveFile.save(backupsPath,"storePack"+saveTime+".json",fileVo.toString());
		SaveFile.save(frontEndFilePath,"storePack.json",fileVo.toString());

	}

	@Override
	public String readActStoreJson() {
		return ReadFileUtil.readFile(frontEndFilePath+"//storeAct.json");
	}

	@Override
	public String readPermStoreJson() {
		return ReadFileUtil.readFile(frontEndFilePath+"//storePerm.json");
	}

	@Override
	public String readPackStoreJson() {
		return ReadFileUtil.readFile(frontEndFilePath+"//storePack.json");
	}


	@Override
	public List<StoreCostPer> findAll(String type) {
		return storeCostPerDao.findByStoreTypeOrderByCostPerDesc(type);
	}







}
