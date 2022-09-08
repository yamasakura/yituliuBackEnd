package com.lhs.bean.vo;

import com.lhs.bean.jsonObject.JsonEfficiency;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yucan
 * @date 2022/9/8 9:25
 */
@Data
@AllArgsConstructor
public class MaterialInfoVo {
    private String secondaryId;
    private Double code;
    private Double color;
    private String main;
    private String itemName;
    private Integer times;
    private String stageName;
    private Integer percentage;
    private Integer id;
    private Double efficiencyEx;
    private String stageId;
    private Double efficiency;
    private String extraItem;
    private Integer isUseValue;
    private Double probability;
    private Double confidence;
    private String activityName;
    private String chapterName;
    private Integer isShow;
    private String secondary;
    private Double expect;
    private String itemId;
    private Double spm;
    private Integer mainLevel;

    public MaterialInfoVo (JsonEfficiency jsonEfficiency){
        this.secondaryId = jsonEfficiency.getSecondaryId();
        this.code = jsonEfficiency.getCode();
        this.color = jsonEfficiency.getColor();
        this.main = jsonEfficiency.getMain();
        this.itemName = jsonEfficiency.getItemName();
        this.times = jsonEfficiency.getTimes();
        this.stageName = jsonEfficiency.getStageName();
        this.percentage = jsonEfficiency.getPercentage();
        this.id = jsonEfficiency.getId();
        this.efficiencyEx = jsonEfficiency.getEfficiencyEx();
        this.stageId = jsonEfficiency.getStageId();
        this.efficiency = jsonEfficiency.getEfficiency();
        this.extraItem = jsonEfficiency.getExtraItem();
        this.isUseValue = jsonEfficiency.getIsUseValue();
        this.probability = jsonEfficiency.getProbability();
        this.confidence = jsonEfficiency.getConfidence();
        this.activityName = jsonEfficiency.getActivityName();
        this.chapterName = jsonEfficiency.getChapterName();
        this.isShow = jsonEfficiency.getIsShow();
        this.secondary = jsonEfficiency.getSecondary();
        this.expect = jsonEfficiency.getExpect();
        this.itemId = jsonEfficiency.getItemId();
        this.spm = jsonEfficiency.getSpm();
        this.mainLevel = jsonEfficiency.getMainLevel();
    }
}
