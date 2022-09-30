package com.lhs.bean.vo;

public class StageOrundumVo {

   private  String stageCode;
   private  Double orundumPerAp;
   private  Double stageEfficiency;
   private  Double orundumPerApEfficiency;
   private  Double LMDCost;

   public String getStageCode() {
      return stageCode;
   }

   public void setStageCode(String stageCode) {
      this.stageCode = stageCode;
   }

   public Double getOrundumPerAp() {
      return orundumPerAp;
   }

   public void setOrundumPerAp(Double orundumPerAp) {
      this.orundumPerAp = orundumPerAp;
   }

   public Double getStageEfficiency() {
      return stageEfficiency;
   }

   public void setStageEfficiency(Double stageEfficiency) {
      this.stageEfficiency = stageEfficiency;
   }

   public Double getOrundumPerApEfficiency() {
      return orundumPerApEfficiency;
   }

   public void setOrundumPerApEfficiency(Double orundumPerApEfficiency) {
      this.orundumPerApEfficiency = orundumPerApEfficiency;
   }

   public Double getLMDCost() {
      return LMDCost;
   }

   public void setLMDCost(Double LMDCost) {
      this.LMDCost = LMDCost;
   }
}
