package com.lhs;

import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.pojo.ItemCost;
import com.lhs.bean.vo.StageResultPlan;
import com.lhs.dao.StageResultDataDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)   //这两个注解是为了让测试类能拥有同等的spring boot上下文环境
@SpringBootTest
public class OrundumEfficiency {

    @Autowired
    private StageResultDataDao stageResultDataDao;



    @Test
    public void Orundum() {

        String[] stageCode  =new String[]{"main_01-07","act13side_06_rep","act13side_07_rep"};

        for(String stageId :stageCode){


        List<StageResultData> byStageCode = stageResultDataDao.findByStageIdAndIsShow(stageId,1);

        double item_30011 =  0.0;
        double item_30012 =  0.0;
        double item_30061 =  0.0;
        double item_30062 =  0.0;

        for(StageResultData stageResultData:byStageCode){
            if("30011".equals( stageResultData.getItemId())){
                item_30011 = stageResultData.getKnockRating()/stageResultData.getApExpect()/stageResultData.getKnockRating();
            }
            if("30012".equals( stageResultData.getItemId())){

                item_30012 = stageResultData.getKnockRating()/stageResultData.getApExpect()/stageResultData.getKnockRating();
            }
            if("30061".equals( stageResultData.getItemId())){
                item_30061 = stageResultData.getKnockRating()/stageResultData.getApExpect()/stageResultData.getKnockRating();
            }
            if("30062".equals( stageResultData.getItemId())){
                item_30062 = stageResultData.getKnockRating()/stageResultData.getApExpect()/stageResultData.getKnockRating();
            }
        }

        System.out.println((item_30012/2+item_30011/6+item_30061/3+item_30062)*10);

        }
    }

    @Test
    public void   newItem(){

    }

    @Test
    public void  plan(){
        
    }


    private static String planCal(ItemCost itemT4,
                                  ItemCost itemT3,
                                  ItemCost itemT3AndT4,
                                  HashMap<String, StageResultPlan> stageResultDataEfficient,
                                  HashMap<String, StageResultPlan> stageResultDataQuick,
                                  Boolean isOptimal,
                                  Boolean isOnlyT3){
        StageResultPlan stageResultEfficientT4 = stageResultDataEfficient.get(itemT4.getItemName());
        StageResultPlan stageResultEfficientT3 = stageResultDataEfficient.get(itemT3.getItemName());
        StageResultPlan stageResultQuickT4 = stageResultDataQuick.get(itemT4.getItemName());
        StageResultPlan stageResultQuickT3 = stageResultDataQuick.get(itemT3.getItemName());
        if(isOptimal||("1".equals(stageResultEfficientT3.getSecondary()))) return stageResultEfficientT3.getStageCode();

        if(isOnlyT3){
            if(stageResultEfficientT4==null||stageResultQuickT4!=null){
                return stageResultEfficientT3.getStageCode();
            }
        }else {

            double efficientApCostT4 = stageResultEfficientT4.getApExpect()*itemT4.getItemNeed();
            double efficientApCostT3SubtractT4 =itemT3.getItemNeed() -  efficientApCostT4/stageResultEfficientT3.getApExpect();
            double efficientApCostT3 = 0.0;
            if(efficientApCostT3SubtractT4>0){
                efficientApCostT3 =  stageResultEfficientT3.getApExpect()*efficientApCostT3SubtractT4;
            }
            double efficientApCostCostCount = efficientApCostT4+efficientApCostT3;

            double quickApCostCount =  stageResultQuickT3.getApExpect()*itemT3AndT4.getItemNeed();
            if(efficientApCostCostCount>quickApCostCount){
                return stageResultQuickT3.getStageCode();
            }else {
                return stageResultEfficientT3.getStageCode();
            }

        }

        return stageResultEfficientT3.getStageCode();
    }
}
