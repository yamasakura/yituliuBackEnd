package com.lhs.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;

//用于读取常驻商店的json
public class StorePermJsonVo {

    @ExcelProperty("素材名称")
    private  String itemName;

    @ExcelProperty("价格")
    private  String cost;

    @ExcelProperty("价格")
    private  String rawCost;

    @ExcelProperty("类型")
    private String type;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRawCost() {
        return rawCost;
    }

    public void setRawCost(String rawCost) {
        this.rawCost = rawCost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StorePermJsonVo() {
    }

    @Override
    public String toString() {
        return "StoreJson{" +
                "itemName='" + itemName + '\'' +
                ", cost='" + cost + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
