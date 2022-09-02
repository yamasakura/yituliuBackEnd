package com.lhs.bean.DBPogo;

import com.alibaba.excel.annotation.ExcelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "stage")

public class Stage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ExcelProperty("关卡Id")
	private String stageId;
	@ExcelProperty("关卡名称cn")
	private String stageName;
	@ExcelProperty("关卡名称en")
	private String stageEnName;
	@ExcelProperty("关卡系列")
	private String chapterName;
	@ExcelProperty("系列编号")
	private Integer chapterCode;
	@ExcelProperty("消耗理智")
	private Double reason;
	@ExcelProperty("主产物")
	private String main;
	@ExcelProperty("主产物等级")
	private Integer mainLevel;
	@ExcelProperty("副产物")
	private String secondary;
	@ExcelProperty("副产物Id")
	private String secondaryId;
	@ExcelProperty("spm")
	private Double spm;
	@ExcelProperty("掉落大类")
	private String itemType;
	@ExcelProperty("是否开放")
	private Integer isOpen;
	@ExcelProperty("是否特殊掉落")
	private Integer isSpecial;
	@ExcelProperty("是否用于定价")
	private Integer isValue;
	@ExcelProperty("是否显示")
	private Integer isShow;
	@ExcelProperty("关卡类型")
	private String stageType;
	@ExcelProperty("活动名称")
	private String activityName;

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public Integer getChapterCode() {
		return chapterCode;
	}

	public void setChapterCode(Integer chapterCode) {
		this.chapterCode = chapterCode;
	}

	public Double getReason() {
		return reason;
	}

	public void setReason(Double reason) {
		this.reason = reason;
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

	public Double getSpm() {
		return spm;
	}

	public void setSpm(Double spm) {
		this.spm = spm;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String type) {
		this.itemType = type;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(Integer isSpecial) {
		this.isSpecial = isSpecial;
	}

	public Integer getIsValue() {
		return isValue;
	}

	public void setIsValue(Integer isValue) {
		this.isValue = isValue;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public String toString() {
		return "Stage{" +
				"stageName='" + stageName + '\'' +
				", stageEnName='" + stageEnName + '\'' +
				", stageId='" + stageId + '\'' +
				", chapterName='" + chapterName + '\'' +
				", chapterCode=" + chapterCode +
				", reason=" + reason +
				", main='" + main + '\'' +
				", secondary='" + secondary + '\'' +
				", secondaryId='" + secondaryId + '\'' +
				", spm=" + spm +
				", type='" + itemType + '\'' +
				", isOpen=" + isOpen +
				", isSpecial=" + isSpecial +
				", isValue=" + isValue +
				", isShow=" + isShow +
				", stageType='" + stageType + '\'' +
				", activityName='" + activityName + '\'' +
				'}';
	}
}
