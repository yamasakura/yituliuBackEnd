package com.lhs;

import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.dao.StageResultDataDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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


        List<StageResultData> byStageCode = stageResultDataDao.findByStageId(stageId);

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
}
