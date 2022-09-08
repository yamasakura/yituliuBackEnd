package com.lhs.bean.jsonObject;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yucan
 * @date 2022/9/8 9:37
 */
@Data
@AllArgsConstructor
public class JsonEfficiency {
    private String secondaryId;
    private String updateDate;
    private Double code;
    private Double color;
    private String main;
    private String type;
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
}