package com.lhs.bean.vo;


import javax.persistence.*;

@Entity
@Table(name = "stage_result_vo")
public class StageResultVo {

    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String stageId;  // 关卡id
    private String chapterName; //章节名称
    private Integer code;  //章节顺序
    private Integer times;  // 样本次数
    private String itemId;   //产物ID
    private String itemName;    //产物名称
    private String stageName;   // 关卡名称
    private String stageEnName;
    private Double probability;   // 概率
    private Double request;   // 单项结果
    private Double expect; // 期望理智
    private String main; // 主产物
    private Integer mainLevel; // 主产物
    private String type;  //材料类型
    private String secondary; // 副产物
    private String secondaryId;
    private Double efficiency;  //绿票转化率
    private Integer isShow;   // 是否显示
    private Integer isUseValue;  //是否参与定价
    private Integer color;  // 关卡标注颜色
    private Double percentage;    //理智转化率
    private String updateDate;  //更新时间
    private String spm;  //每分钟消耗理智
    private String activityName; //活动名称
    private Double efficiencyEx;  // 绿票转化率（ 理智小样
    private String extraItem; //  理智小样
    private Double confidence;  // 样本置信度

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

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
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

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageEnName() {
        return stageEnName;
    }

    public void setStageEnName(String stageEnName) {
        this.stageEnName = stageEnName;
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

    public Integer getMainLevel() {
        return mainLevel;
    }

    public void setMainLevel(Integer mainLevel) {
        this.mainLevel = mainLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getIsUseValue() {
        return isUseValue;
    }

    public void setIsUseValue(Integer isUseValue) {
        this.isUseValue = isUseValue;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

    public String getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(String extraItem) {
        this.extraItem = extraItem;
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
                "id=" + id +
                ", stageId='" + stageId + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", code=" + code +
                ", itemId='" + itemId + '\'' +
                ", stageName='" + stageName + '\'' +
                ", stageEnName='" + stageEnName + '\'' +
                ", times=" + times +
                ", probability=" + probability +
                ", request=" + request +
                ", expect=" + expect +
                ", main='" + main + '\'' +
                ", type='" + type + '\'' +
                ", secondary='" + secondary + '\'' +
                ", secondaryId='" + secondaryId + '\'' +
                ", efficiency=" + efficiency +
                ", color=" + color +
                ", percentage=" + percentage +
                ", updateDate='" + updateDate + '\'' +
                ", spm='" + spm + '\'' +
                ", isUseValue=" + isUseValue +
                ", isShow=" + isShow +
                ", activityName='" + activityName + '\'' +
                ", extraItem='" + extraItem + '\'' +
                ", efficiencyEx=" + efficiencyEx +
                ", confidence='" + confidence + '\'' +
                '}';
    }
}
