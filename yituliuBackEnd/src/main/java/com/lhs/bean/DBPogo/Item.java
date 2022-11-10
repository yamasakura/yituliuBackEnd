package com.lhs.bean.DBPogo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="item")  //临时物品价值表，用于保存最终价值之前的临时等效理智/绿票价值
public class Item {

	@Id
	private String itemId;  //物品id
	
	private String itemName; //物品名称

	private Double itemValue; //物品价值
	
	private String type; //物品稀有度

	public Item() {
		super();
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

	public Double getItemValue() {
		return itemValue;
	}

	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Item{" +
				"itemId='" + itemId + '\'' +
				", itemName='" + itemName + '\'' +
				", itemValue=" + itemValue +
				", type='" + type + '\'' +
				'}';
	}
}
