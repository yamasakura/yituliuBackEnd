package com.lhs.bean.vo;

import java.io.Serializable;


public class StageVo  {

	private String stageName;

	private String stageEnName;

	private String stageId;

	private String chapterName;

	private Integer chapterCode;

	private Double reason;

	private Double reasonEx;

	private String main;

	private Integer mainLevel;

	private String secondary;

	private String secondaryId;

	private Double spm;

	private String itemType;

	private Integer isOpen;

	private Integer isSpecial;

	private Integer isValue;

	private Integer isShow;

	private String stageType;

	private String activityName;



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

	public void setItemType(String itemType) {
		this.itemType = itemType;
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

	public Double getReasonEx() {
		return reasonEx;
	}

	public void setReasonEx(Double reasonEx) {
		this.reasonEx = reasonEx;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public String toString() {
		return "StageVo{" +
				"stageName='" + stageName + '\'' +
				", stageEnName='" + stageEnName + '\'' +
				", stageId='" + stageId + '\'' +
				", chapterName='" + chapterName + '\'' +
				", chapterCode=" + chapterCode +
				", reason=" + reason +
				", reasonEx=" + reasonEx +
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
				'}';
	}
}
