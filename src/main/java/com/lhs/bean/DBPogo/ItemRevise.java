package com.lhs.bean.DBPogo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item_revise")
public class ItemRevise {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String itemId;  //物品id

    private String itemName; //物品名称

    private String itemEnName;

    private Double itemValue; //物品价值

    private String type; //物品稀有度

    private String cardNum;  //前端排序的用索引

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

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    @Override
    public String toString() {
        return "ItemRevise{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemEnName='" + itemEnName + '\'' +
                ", itemValue=" + itemValue +
                ", type='" + type + '\'' +
                '}';
    }
}
