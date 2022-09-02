package com.lhs.bean.pojo;

public class PenguinClass {


//    企鹅物流数据
    private String stageId;  //关卡id
    private String itemId; //物品id
    private String quantity;  //物品掉落次数
    private String times;  //关卡刷取次数
    private String start; //开始时间
    private String end;  //结束时间

    public String getStageId() {
        return stageId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTimes() {
        return times;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "PenguinApi{" +
                "stageId='" + stageId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", times='" + times + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }

    public PenguinClass(String stageId, String itemId, String quantity, String times, String start, String end) {
        this.stageId = stageId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.times = times;
        this.start = start;
        this.end = end;
    }

    public PenguinClass() {
    }
}
