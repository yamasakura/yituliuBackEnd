package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lhs.bean.DBPogo.Stage;
import com.lhs.bean.vo.PenguinDataRequestVo;
import com.lhs.bean.pojo.StageInfoVo;
import com.lhs.dao.StageDao;
import com.lhs.service.StageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class StageServiceImpl implements StageService {

    @Autowired
    private StageDao stageDao;

    @Override
    public List<Stage> findAll() {

        return stageDao.findAll();
    }

    @Override
    public List<StageInfoVo> findAllVo() {
        List<Stage> all = stageDao.findAll();

        List<StageInfoVo> allStage = new ArrayList<>();
        for (Stage stage : all) {
            StageInfoVo stageInfoVo = new StageInfoVo();
            BeanUtils.copyProperties(stage, stageInfoVo);
            stageInfoVo.setApCostEx(stage.getApCost());
            allStage.add(stageInfoVo);
        }
        return allStage;
    }

    @Override
    public void importStageData(MultipartFile file) {

        List<Stage> list = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), Stage.class, new AnalysisEventListener<Stage>() {
                @Override
                public void invoke(Stage stage, AnalysisContext analysisContext) {
                    if("1-7".equals(stage.getStageCode()))  stage.setMainLevel(3);
                    if(stage.getMinClearTime()>1000) stage.setSpm(stage.getApCost()/stage.getMinClearTime()*60000);

                    if(stage.getZoneId().startsWith("act")){
                        stage.setStageState(0);
                        stage.setIsValue(0);
                        stage.setIsShow(0);
                        if(stage.getZoneId().endsWith("perm")){
                            stage.setIsShow(1);
                            stage.setIsValue(1);
                        }
                    }else {
                        stage.setIsValue(1);
                        stage.setIsShow(1);
                        stage.setStageState(1);
                    }
//                    System.out.println(stage.getZoneId()+"关卡类型"+stage.getStageState()+"是否定价"+stage.getIsValue()+"是否显示"+stage.getIsShow());
                    list.add(stage);
                }
                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                }
            }).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stageDao.saveAll(list);

    }

    @Override
    public void exportStageData(HttpServletResponse response) {

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("stageData", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName+".xlsx" );

            List<Stage> list = stageDao.findAll();

            EasyExcel.write(response.getOutputStream(), Stage.class).sheet("Sheet1").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateStageInfo(Integer isShow, String stageId) {
        stageDao.updateStageInfo(isShow, stageId);

    }

    @Override
    public List<PenguinDataRequestVo> penguinDataMerge(List<PenguinDataRequestVo> penguinDataList) {

        List<PenguinDataRequestVo> tough_10List = new ArrayList<>();
        List<PenguinDataRequestVo> main_10List = new ArrayList<>();
        List<PenguinDataRequestVo> tough_11List = new ArrayList<>();
        List<PenguinDataRequestVo> main_11List = new ArrayList<>();
        List<PenguinDataRequestVo> normalList = new ArrayList<>();

        
        for(PenguinDataRequestVo penguinDataRequestVo :penguinDataList){
            if(penguinDataRequestVo.getStageId().startsWith("tough_10")) {
                tough_10List.add(penguinDataRequestVo);
                continue;
            }
            if(penguinDataRequestVo.getStageId().startsWith("main_10"))  {
                main_10List.add(penguinDataRequestVo);
                continue;
            }

            if(penguinDataRequestVo.getStageId().startsWith("tough_11")) {
                tough_11List.add(penguinDataRequestVo);
                continue;
            }
            if(penguinDataRequestVo.getStageId().startsWith("main_11")) {
                main_11List.add(penguinDataRequestVo);
                continue;
            }
             normalList.add(penguinDataRequestVo);
        }

        List<PenguinDataRequestVo> penguinDataRequestVo_10 = zoneMerge(tough_10List, main_10List);
        List<PenguinDataRequestVo> penguinDataRequestVo_11 = zoneMerge(tough_11List, main_11List);
        normalList.addAll(penguinDataRequestVo_10);
        normalList.addAll(penguinDataRequestVo_11);


        return normalList;
    }


    private static List<PenguinDataRequestVo> zoneMerge(List<PenguinDataRequestVo> toughList, List<PenguinDataRequestVo> mainList){
         for(PenguinDataRequestVo penguinDataRequestVo :toughList){
             String  toughStageId  = penguinDataRequestVo.getStageId();
             toughStageId  = toughStageId.substring(toughStageId.indexOf("_")+1);
             for (PenguinDataRequestVo dataRequestVo : mainList) {
                 String mainStageId = dataRequestVo.getStageId();
                 mainStageId = mainStageId.substring(mainStageId.indexOf("_") + 1);

                 if ((toughStageId.equals(mainStageId)) && (penguinDataRequestVo.getItemId().equals(dataRequestVo.getItemId()))) {
                     dataRequestVo.setTimes(dataRequestVo.getTimes() + penguinDataRequestVo.getTimes());
                     dataRequestVo.setQuantity(dataRequestVo.getQuantity() + penguinDataRequestVo.getQuantity());
                 }
             }
         }

          return mainList;
    }

}
