package com.lhs.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lhs.bean.DBPogo.ItemRevise;
import com.lhs.bean.pojo.ItemCustomValue;
import com.lhs.bean.vo.StorePermJsonVo;
import com.lhs.bean.DBPogo.StoreCostPer;
import com.lhs.bean.vo.StoreActJsonVo;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.CreateJsonFile;
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

import javax.servlet.http.HttpServletResponse;
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

	@Value("${frontEnd.buildingSchedule}")
	private String buildingSchedulePath;

	@Override
	public void save(List<StoreCostPer> storeCostPer) {
		if(storeCostPer!=null) {
			storeCostPerDao.saveAll(storeCostPer);
		}else {
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}

	}




	@Override
	public void updateStorePermByJson() {
		List<ItemRevise> allItem =itemService.findAllItemRevise("auto0.625");
		String str = ReadFileUtil.readFile(frontEndFilePath+"permStoreData.json");
		List<StorePermJsonVo> storePermJsonVos = JSONArray.parseArray(str, StorePermJsonVo.class);

		DecimalFormat decimalFormat_00 = new DecimalFormat("0.00");

		List<StoreCostPer> list = new ArrayList<>();

		Long count = 1L;


		for (int i = 0; i < Objects.requireNonNull(storePermJsonVos).size(); i++) {
			for (int j = 0; j < allItem.size(); j++) {
				if (storePermJsonVos.get(i).getItemName().equals(allItem.get(j).getItemName())) {
					Double costPer = allItem.get(j).getItemValue() / Double.parseDouble(storePermJsonVos.get(i).getCost());
					StoreCostPer storeCostPer = new StoreCostPer();
					storeCostPer.setId(count);
					count++;
					storeCostPer.setItemId(allItem.get(j).getItemId());
					storeCostPer.setItemName(storePermJsonVos.get(i).getItemName());
					storeCostPer.setStoreType(storePermJsonVos.get(i).getType());
					storeCostPer.setCostPer(Double.valueOf(decimalFormat_00.format(costPer)));
					storeCostPer.setCost(Double.valueOf(storePermJsonVos.get(i).getCost()));
					storeCostPer.setRawCost(storePermJsonVos.get(i).getRawCost());
					storeCostPer.setItemValue(allItem.get(j).getItemValue());
					list.add(storeCostPer);
				}
			}
		}

		storeCostPerService.save(list);

	}

	@Override
	public void updateStoreActByJson(MultipartFile file) {
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
			List<StoreActJsonVo> storeActJsonVos = JSONArray.parseArray(o.toString(), StoreActJsonVo.class);

			List<StoreActJsonVo> storeActJsonVoResult = new ArrayList<>();

			Integer itemArea = storeActJsonVos.get(storeActJsonVos.size() - 1).getItemArea();
			for(int i=1;i<=itemArea;i++){
				List<StoreActJsonVo> storeActJsonVoCopy = new ArrayList<>();
				for(StoreActJsonVo storeActJsonVo : storeActJsonVos){
					if(storeActJsonVo.getItemArea()==i){
						storeActJsonVo.setItemPPR(itemValueMap.get(storeActJsonVo.getItemName())* storeActJsonVo.getItemQuantity()/ storeActJsonVo.getItemPrice());
						storeActJsonVo.setItemId(itemIdMap.get(storeActJsonVo.getItemName()));
						storeActJsonVoCopy.add(storeActJsonVo);
					}
				}
				storeActJsonVoCopy.sort(Comparator.comparing(StoreActJsonVo::getItemPPR).reversed());
				storeActJsonVoResult.addAll(storeActJsonVoCopy);
			}
			storeMap.put("actStore", storeActJsonVoResult);
			jsonList.add(storeMap);
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
		String saveTime = simpleDateFormat.format(new Date());


		Object fileVo = JSONObject.toJSON(jsonList);
		SaveFile.save(backupsPath,"storeAct"+saveTime+".json",fileVo.toString());
		SaveFile.save(frontEndFilePath,"storeAct.json",fileVo.toString());

	}

	@Override
	public void updateStorePackByJson(String packStr) {

		List<ItemRevise> itemValueList =itemService.findAllItemRevise("auto0.625");
		String fileStr = ReadFileUtil.readFile(frontEndFilePath+"itemCustomValue.json");
		List<ItemCustomValue> itemCustomValues = JSONArray.parseArray(fileStr, ItemCustomValue.class);
		HashMap<String, Double> itemValueMap = new HashMap<>();

		for(ItemCustomValue itemCustomValue:itemCustomValues){
			itemValueMap.put(itemCustomValue.getItemName(),itemCustomValue.getItemValue());
		}

		for(ItemRevise itemRevise:itemValueList){
			itemValueMap.put(itemRevise.getItemName(),itemRevise.getItemValue()/1.25);
		}

		JSONArray jsonArray = JSONArray.parseArray(packStr);

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

				int gachaOrundum = 0;
				int gachaOriginium = 0;
				int gachaPermit = 0;
				int gachaPermit10 = 0;



			    if(storePackMap.get("gachaOrundum")!=null)	{
			    	 gachaOrundum = Integer.parseInt(storePackMap.get("gachaOrundum").toString()); // 合成玉
				}
				if(storePackMap.get("gachaOriginium")!=null)	{
					 gachaOriginium = Integer.parseInt(storePackMap.get("gachaOriginium").toString());// 源石
				}
				if(storePackMap.get("gachaPermit")!=null)	{
					 gachaPermit = Integer.parseInt(storePackMap.get("gachaPermit").toString());// 单抽
				}
				if(storePackMap.get("gachaPermit10")!=null)	{
					 gachaPermit10 = Integer.parseInt(storePackMap.get("gachaPermit10").toString());// 十连
				}



				int packPrice = Integer.parseInt(storePackMap.get("packPrice").toString());// 价格


				//计算该理智的材料总理智折合源石
				if (storePackMap.get("packContent") != null) {
					JSONArray jsonArrayItem = JSONArray.parseArray(storePackMap.get("packContent").toString());
					for (Object objectItem : jsonArrayItem) {
						Map itemMap = JSONObject.parseObject(objectItem.toString());
						Object itemName = itemMap.get("packContentItem");
						int itemQuantity = Integer.parseInt(itemMap.get("packContentQuantity").toString());
						apValueToOriginium = apValueToOriginium + itemValueMap.get(itemName.toString()) * itemQuantity;
					}
				}
				if (apValueToOriginium > 0.0) {
					apValueToOriginium = apValueToOriginium / 135;
				}


				packDraw = gachaOrundum / 600.0 + gachaOriginium * 0.3 + gachaPermit + gachaPermit10*10;
				packRmbPerDraw = packPrice / packDraw;
				packOriginium = gachaOrundum / 180.0 + gachaOriginium + gachaPermit * 600 / 180.0 + gachaPermit10 * 6000 / 180.0 + apValueToOriginium;
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
		return ReadFileUtil.readFile(frontEndFilePath+"storeAct.json");
	}

	@Override
	public String readPermStoreJson() {
		return ReadFileUtil.readFile(frontEndFilePath+"storePerm.json");
	}

	@Override
	public String readPackStoreJson() {
		return ReadFileUtil.readFile(frontEndFilePath+"storePack.json");
	}


	@Override
	public List<StoreCostPer> findStorePermByType(String type) {
		return storeCostPerDao.findByStoreTypeOrderByCostPerDesc(type);
	}







}
