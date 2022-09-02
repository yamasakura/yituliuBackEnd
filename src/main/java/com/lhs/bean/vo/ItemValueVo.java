package com.lhs.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;

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



    @Override
    public String toString() {
        return "ItemValueVo{" +
                "itemName='" + itemName + '\'' +
                ", itemValueGreen=" + itemValueGreen +
                ", itemValueReason=" + itemValueReason +
                '}';
    }
}
