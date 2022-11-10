package com.lhs.bean.vo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stage_result_api_vo") //不用管,给别人的api
public class StageResultApiVo {

    @Id
    private Long id;

    private String itemId; //产物ID

    private String stageName;//关卡名称

    private Integer times; //样本次数

    private Double probability; //概率

    private Double expect;//期望理智

    private String main; //主产物

    private String type;//材料类型

    private String secondary; //副产物

    private Double efficiency;//效率

    private Integer color; //颜色

    private Double percentage; //相对效率百分比

    private String updateDate;//更新时间

    private String spm; //每分钟消耗理智

    private Double confidence; //置信度

    private  Integer isUseValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemName) {
        this.itemId = itemName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
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

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Integer getIsUseValue() {
        return isUseValue;
    }

    public void setIsUseValue(Integer isUseValue) {
        this.isUseValue = isUseValue;
    }


    @Override
    public String toString() {
        return "StageResultApiVo{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", stageName='" + stageName + '\'' +
                ", times=" + times +
                ", probability=" + probability +
                ", expect=" + expect +
                ", main='" + main + '\'' +
                ", type='" + type + '\'' +
                ", secondary='" + secondary + '\'' +
                ", efficiency=" + efficiency +
                ", color=" + color +
                ", percentage=" + percentage +
                ", updateDate='" + updateDate + '\'' +
                ", spm='" + spm + '\'' +
                ", confidence='" + confidence + '\'' +
                ", isUseValue=" + isUseValue +
                '}';
    }
}
