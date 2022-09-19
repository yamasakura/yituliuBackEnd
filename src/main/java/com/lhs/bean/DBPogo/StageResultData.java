package com.lhs.bean.DBPogo;


import javax.persistence.*;

@Entity
@Table(name = "stage_result_data")
public class StageResultData {

    @Id
    private Long id;
    private String stageId;  // 关卡id
    private String zoneId; //章节名称
    private Integer code;  //章节顺序
    private Integer sampleSize;  // 样本次数
    private String itemId;   //产物ID
    private String itemName;    //产物名称
    private String stageCode;   // 关卡名称
    private Double knockRating;   // 概率
    private Double result;   // 单项结果
    private Double apExpect; // 期望理智
    private String main; // 主产物
    private Integer mainLevel; // 主产物
    private String itemType;  //材料类型
    private String secondary; // 副产物
    private String secondaryId;  // 副产物id
    private Double efficiency;  //绿票转化率
    private Integer isShow;   // 是否显示
    private Integer isValue;  //是否参与定价
    private Integer stageState;  //关卡类型 0-普通，1-ss，2—故事集，3-理智小样
    private Integer stageColor;  // 关卡标注颜色
    private Double stageEfficiency;    //理智转化率
    private String spm;  //每分钟消耗理智
    private String activityName; //活动名称
    private Double efficiencyEx;  // 绿票转化率（ 理智小样
    private Double sampleConfidence;  // 样本置信度
    private String updateTime;  //更新时间
    private Integer minClearTime;
    private Integer part;
    private Integer partNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String chapterName) {
        this.zoneId = chapterName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(Integer times) {
        this.sampleSize = times;
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

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageName) {
        this.stageCode = stageName;
    }


    public Double getKnockRating() {
        return knockRating;
    }

    public void setKnockRating(Double probability) {
        this.knockRating = probability;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Double getApExpect() {
        return apExpect;
    }

    public void setApExpect(Double expect) {
        this.apExpect = expect;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Integer getMainLevel() {
        return mainLevel;
    }

    public void setMainLevel(Integer mainLevel) {
        this.mainLevel = mainLevel;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsValue() {
        return isValue;
    }

    public void setIsValue(Integer isUseValue) {
        this.isValue = isUseValue;
    }

    public Integer getStageState() {
        return stageState;
    }

    public void setStageState(Integer isSpecial) {
        this.stageState = isSpecial;
    }

    public Integer getStageColor() {
        return stageColor;
    }

    public void setStageColor(Integer color) {
        this.stageColor = color;
    }

    public Double getStageEfficiency() {
        return stageEfficiency;
    }

    public void setStageEfficiency(Double percentage) {
        this.stageEfficiency = percentage;
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

    public Double getSampleConfidence() {
        return sampleConfidence;
    }

    public void setSampleConfidence(Double confidence) {
        this.sampleConfidence = confidence;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateDate) {
        this.updateTime = updateDate;
    }

    public Integer getMinClearTime() {
        return minClearTime;
    }

    public void setMinClearTime(Integer minClearTime) {
        this.minClearTime = minClearTime;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public Integer getPartNo() {
        return partNo;
    }

    public void setPartNo(Integer partNo) {
        this.partNo = partNo;
    }

    @Override
    public String toString() {
        return "StageResultData{" +
                "id=" + id +
                ", stageId='" + stageId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", code=" + code +
                ", sampleSize=" + sampleSize +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", stageCode='" + stageCode + '\'' +
                ", knockRating=" + knockRating +
                ", result=" + result +
                ", apExpect=" + apExpect +
                ", main='" + main + '\'' +
                ", mainLevel=" + mainLevel +
                ", itemType='" + itemType + '\'' +
                ", secondary='" + secondary + '\'' +
                ", secondaryId='" + secondaryId + '\'' +
                ", efficiency=" + efficiency +
                ", isShow=" + isShow +
                ", isValue=" + isValue +
                ", stageState=" + stageState +
                ", stageColor=" + stageColor +
                ", stageEfficiency=" + stageEfficiency +
                ", spm='" + spm + '\'' +
                ", activityName='" + activityName + '\'' +
                ", efficiencyEx=" + efficiencyEx +
                ", sampleConfidence=" + sampleConfidence +
                ", updateTime='" + updateTime + '\'' +
                ", minClearTime=" + minClearTime +
                ", part=" + part +
                ", partNo=" + partNo +
                '}';
    }
}
