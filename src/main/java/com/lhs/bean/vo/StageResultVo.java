package com.lhs.bean.vo;




public class StageResultVo {//返回前端的结果

    private String stageCode;   // 关卡名称
    private String itemId;  //物品id
    private String itemType;  // 物品类型
    private Double apExpect; // 期望理智
    private String secondaryId; // 副产物id
    private String secondary; // 副产物
    private Double knockRating;   // 概率
    private Double stageEfficiency;    //理智转化率
    private Integer sampleSize;  // 样本次数
    private Integer stageColor;  // 关卡标注颜色
    private String spm;  //每分钟消耗理智
    private String activityName; //活动名称
    private Double sampleConfidence;  // 样本置信度
    private String updateTime;  // 更新时间
    private Integer stageState;
    private Integer minClearTime;


    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

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

    public Double getApExpect() {
        return apExpect;
    }

    public void setApExpect(Double apExpect) {
        this.apExpect = apExpect;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public Double getKnockRating() {
        return knockRating;
    }

    public void setKnockRating(Double knockRating) {
        this.knockRating = knockRating;
    }

    public Double getStageEfficiency() {
        return stageEfficiency;
    }

    public void setStageEfficiency(Double stageEfficiency) {
        this.stageEfficiency = stageEfficiency;
    }

    public Integer getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(Integer sampleSize) {
        this.sampleSize = sampleSize;
    }

    public Integer getStageColor() {
        return stageColor;
    }

    public void setStageColor(Integer stageColor) {
        this.stageColor = stageColor;
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

    public Double getSampleConfidence() {
        return sampleConfidence;
    }

    public void setSampleConfidence(Double sampleConfidence) {
        this.sampleConfidence = sampleConfidence;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStageState() {
        return stageState;
    }

    public void setStageState(Integer stageState) {
        this.stageState = stageState;
    }

    public Integer getMinClearTime() {
        return minClearTime;
    }

    public void setMinClearTime(Integer minClearTime) {
        this.minClearTime = minClearTime;
    }

    @Override
    public String toString() {
        return "StageResultVo{" +
                "stageCode='" + stageCode + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemType='" + itemType + '\'' +
                ", apExpect=" + apExpect +
                ", secondaryId='" + secondaryId + '\'' +
                ", secondary='" + secondary + '\'' +
                ", knockRating=" + knockRating +
                ", stageEfficiency=" + stageEfficiency +
                ", sampleSize=" + sampleSize +
                ", stageColor=" + stageColor +
                ", apm='" + spm + '\'' +
                ", activityName='" + activityName + '\'' +
                ", sampleConfidence=" + sampleConfidence +
                ", updateTime='" + updateTime + '\'' +
                ", stageState=" + stageState +
                ", minClearTime=" + minClearTime +
                '}';
    }
}
