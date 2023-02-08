package com.lhs.bean.vo;

public class StoreActJsonVo {

    private String itemName;
    private Integer itemQuantity;
    private Integer itemPrice;
    private Double itemPPR;
    private Integer itemStock;
    private Integer itemArea;
    private String itemId;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getItemPPR() {
        return itemPPR;
    }

    public void setItemPPR(Double itemPPR) {
        this.itemPPR = itemPPR;
    }

    public Integer getItemStock() {
        return itemStock;
    }

    public void setItemStock(Integer itemStock) {
        this.itemStock = itemStock;
    }

    public Integer getItemArea() {
        return itemArea;
    }

    public void setItemArea(Integer itemArea) {
        this.itemArea = itemArea;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "StoreJsonVo{" +
                "itemName='" + itemName + '\'' +
                ", itemQuantity=" + itemQuantity +
                ", itemPrice=" + itemPrice +
                ", itemPPR=" + itemPPR +
                ", itemStock=" + itemStock +
                ", itemArea=" + itemArea +
                '}';
    }
}
