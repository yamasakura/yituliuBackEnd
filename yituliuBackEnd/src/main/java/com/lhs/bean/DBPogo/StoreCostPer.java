package com.lhs.bean.DBPogo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Store_cost_per")  //性价比表，主要存常驻商店
public class StoreCostPer {


	@Id
	private Long id;

	private String itemId;  //物品id

	private String itemName; //物品名称

	private String itemEnName;

	private Double itemValue; //物品价值

	private Double cost;  //单位售价  （比如5000龙门币卖7代币  单位售价是7/5000)

	private  String rawCost;    //商店售价

	private Double costPer;     //性价比
	
	private String storeType;  //商店类型

	
	
	public StoreCostPer() {
		super();
	}




	public StoreCostPer(Long id, String itemId, String itemName, String itemEnName, Double cost, Double itemValue,
                        Double costPer, String storeType) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemEnName = itemEnName;
		this.cost = cost;
		this.itemValue = itemValue;
		this.costPer = costPer;
		this.storeType = storeType;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getItemId() {
		return itemId;
	}




	public void setItemId(String itemId) {
		this.itemId = itemId;
	}




	public String getItemName() {
		return itemName;
	}




	public void setItemName(String itemName) {
		this.itemName = itemName;
	}




	public String getItemEnName() {
		return itemEnName;
	}




	public void setItemEnName(String itemEnName) {
		this.itemEnName = itemEnName;
	}




	public Double getCost() {
		return cost;
	}




	public void setCost(Double cost) {
		this.cost = cost;
	}


	public String getRawCost() {
		return rawCost;
	}

	public void setRawCost(String rawCost) {
		this.rawCost = rawCost;
	}



	public Double getItemValue() {
		return itemValue;
	}




	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}




	public Double getCostPer() {
		return costPer;
	}




	public void setCostPer(Double costPer) {
		this.costPer = costPer;
	}




	public String getStoreType() {
		return storeType;
	}




	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}




	@Override
	public String toString() {
		return "StoreCostPer [id=" + id + ", itemId=" + itemId + ", itemName=" + itemName + ", itemEnName=" + itemEnName
				+ ", cost=" + cost + ", itemValue=" + itemValue + ", costPer=" + costPer + ", storeType=" + storeType
				+ "]";
	}
	
	
	
	
}
