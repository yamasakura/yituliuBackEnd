package com.lhs.bean.vo;




public class StageResultVo {//返回前端的结果

    private String updateDate;  // 更新时间
    private String itemType;  // 物品类型
    private String itemId;  //物品id
    private String stageId;  // 关卡id
    private Integer times;  // 样本次数
    private String stageName;   // 关卡名称
    private Double probability;   // 概率
    private Double request;   // 单项结果
    private Double expect; // 期望理智
    private String main; // 主产物
    private String secondary; // 副产物
    private String secondaryId; // 副产物id
    private Double efficiency;  //绿票转化率
    private Integer color;  // 关卡标注颜色
    private Double percentage;    //理智转化率
    private String spm;  //每分钟消耗理智
    private String activityName; //活动名称
    private Double efficiencyEx;  // 绿票转化率（ 理智小样
    private Double confidence;  // 样本置信度

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Double getRequest() {
        return request;
    }

    public void setRequest(Double request) {
        this.request = request;
    }

    public Double getExpect() {
        return expect;
    }

    public void setExpect(Double expect) {
        this.expect = expect;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getSpm() {
        return spm;
    }

    public void setSpm(String spm) {
        this.spm = spm;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Double getEfficiencyEx() {
        return efficiencyEx;
    }

    public void setEfficiencyEx(Double efficiencyEx) {
        this.efficiencyEx = efficiencyEx;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "StageResultVo{" +
                "updateData='" + updateDate + '\'' +
                ", itemType='" + itemType + '\'' +
                ", itemId='" + itemId + '\'' +
                ", stageId='" + stageId + '\'' +
                ", times=" + times +
                ", stageName='" + stageName + '\'' +
                ", probability=" + probability +
                ", request=" + request +
                ", expect=" + expect +
                ", main='" + main + '\'' +
                ", secondary='" + secondary + '\'' +
                ", secondaryId='" + secondaryId + '\'' +
                ", efficiency=" + efficiency +
                ", color=" + color +
                ", percentage=" + percentage +
                ", spm='" + spm + '\'' +
                ", activityName='" + activityName + '\'' +
                ", efficiencyEx=" + efficiencyEx +
                ", confidence=" + confidence +
                '}';
    }
}
