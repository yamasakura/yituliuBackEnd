package com.lhs.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
//前端导出物品价值表用的实体类
public class ItemValueVo {

    @ExcelProperty("物品ID")
    private String itemId;
    @ExcelProperty("物品名称")
    private String itemName;
    @ExcelProperty("等效理智价值")
    private Double itemValueReason;
    @ExcelProperty("等效绿票价值")
    private Double itemValueGreen;
    @ExcelProperty("物品稀有度")
    private String itemType;
    @ExcelProperty("经验书价值比例")
    private Double expCoefficient;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemValueGreen() {
        return itemValueGreen;
    }

    public void setItemValueGreen(Double itemValueGreen) {
        this.itemValueGreen = itemValueGreen;
    }

    public Double getItemValueReason() {
        return itemValueReason;
    }

    public void setItemValueReason(Double itemValueReason) {
        this.itemValueReason = itemValueReason;
    }

    public Double getExpCoefficient() {
        return expCoefficient;
    }

    public void setExpCoefficient(Double expCoefficient) {
        this.expCoefficient = expCoefficient;
    }

    @Override
    public String toString() {
        return "ItemValueVo{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemValueReason=" + itemValueReason +
                ", itemValueGreen=" + itemValueGreen +
                ", itemType='" + itemType + '\'' +
                ", version='" + expCoefficient + '\'' +
                '}';
    }
}
