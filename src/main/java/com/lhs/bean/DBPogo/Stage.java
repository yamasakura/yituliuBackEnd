package com.lhs.bean.DBPogo;

import com.alibaba.excel.annotation.ExcelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stage")  //关卡的各类信息

public class Stage {
	@Id
	@ExcelProperty("关卡Id")
	private String stageId;
	@ExcelProperty("关卡名称cn")
	private String stageCode;
	@ExcelProperty("关卡系列")
	private String zoneId;
	@ExcelProperty("系列编号")
	private Integer code;
	@ExcelProperty("消耗理智")
	private Double apCost;
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
	@ExcelProperty("是否特殊掉落")
	private Integer stageState;
	@ExcelProperty("是否用于定价")
	private Integer isValue;
	@ExcelProperty("是否显示")
	private Integer isShow;
	@ExcelProperty("关卡类型")
	private String stageType;
	@ExcelProperty("活动名称")
	private String activityName;
	@ExcelProperty("第X部")
	private Integer part;
	@ExcelProperty("第X部顺序")
	private Integer partNo;
	@ExcelProperty("理论通关时间")
	private Integer minClearTime;

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

	public void setSpm(Double apm) {
		this.spm = apm;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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

	public Integer getMinClearTime() {
		return minClearTime;
	}

	public void setMinClearTime(Integer minClearTime) {
		this.minClearTime = minClearTime;
	}

	@Override
	public String toString() {
		return "Stage{" +
				"stageId='" + stageId + '\'' +
				", stageCode='" + stageCode + '\'' +
				", zoneId='" + zoneId + '\'' +
				", code=" + code +
				", apCost=" + apCost +
				", main='" + main + '\'' +
				", mainLevel=" + mainLevel +
				", secondary='" + secondary + '\'' +
				", secondaryId='" + secondaryId + '\'' +
				", spm=" + spm +
				", itemType='" + itemType + '\'' +
				", stageState=" + stageState +
				", isValue=" + isValue +
				", isShow=" + isShow +
				", stageType='" + stageType + '\'' +
				", activityName='" + activityName + '\'' +
				", part=" + part +
				", partNo=" + partNo +
				", minClearTime=" + minClearTime +
				'}';
	}
}
