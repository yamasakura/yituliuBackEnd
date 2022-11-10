package com.lhs.bean.vo;


public class StageVo  {



	private String stageId;

	private String stageCode;

	private String zoneId;

	private Integer code;

	private Double apCost;

	private Double apCostEx;

	private String main;

	private Integer mainLevel;

	private String secondary;

	private String secondaryId;

	private Double spm;

	private String itemType;

	private Integer isOpen;

	private Integer stageState;

	private Integer isValue;

	private Integer isShow;

	private String stageType;

	private String activityName;

	private Integer part;

	private Integer partNo;

	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	public String getStageCode() {
		return stageCode;
	}

	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Double getApCost() {
		return apCost;
	}

	public void setApCost(Double apCost) {
		this.apCost = apCost;
	}

	public Double getApCostEx() {
		return apCostEx;
	}

	public void setApCostEx(Double apCostEx) {
		this.apCostEx = apCostEx;
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

	public Integer getStageState() {
		return stageState;
	}

	public void setStageState(Integer stageState) {
		this.stageState = stageState;
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
		return "StageVo{" +
				"stageId='" + stageId + '\'' +
				", stageCode='" + stageCode + '\'' +
				", zoneId='" + zoneId + '\'' +
				", code=" + code +
				", apCost=" + apCost +
				", apCostEx=" + apCostEx +
				", main='" + main + '\'' +
				", mainLevel=" + mainLevel +
				", secondary='" + secondary + '\'' +
				", secondaryId='" + secondaryId + '\'' +
				", apm=" + spm +
				", itemType='" + itemType + '\'' +
				", isOpen=" + isOpen +
				", stageState=" + stageState +
				", isValue=" + isValue +
				", isShow=" + isShow +
				", stageType='" + stageType + '\'' +
				", activityName='" + activityName + '\'' +
				", part=" + part +
				", partNo=" + partNo +
				'}';
	}
}
