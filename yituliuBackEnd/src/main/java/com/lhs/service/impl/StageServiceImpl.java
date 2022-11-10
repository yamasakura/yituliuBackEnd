package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lhs.bean.DBPogo.Stage;
import com.lhs.bean.pojo.PenguinDataVo;
import com.lhs.bean.vo.StageVo;
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
    public List<StageVo> findAllVo() {
        List<Stage> all = stageDao.findAll();

        List<StageVo> allStage = new ArrayList<>();
        for (Stage stage : all) {
            StageVo stageVo = new StageVo();
            BeanUtils.copyProperties(stage,stageVo);
            stageVo.setApCostEx(stage.getApCost());
            allStage.add(stageVo);
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
                    System.out.print(stage.getZoneId()+"——————");
                    if(stage.getZoneId().startsWith("act")){
                        stage.setStageState(0);
                        stage.setIsValue(0);
                        stage.setIsShow(0);
                        if(stage.getZoneId().endsWith("perm")){
                            stage.setIsShow(1);
                            stage.setIsValue(1);
                            System.out.print("常驻活动关");
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
    public List<PenguinDataVo> penguinDataMerge(List<PenguinDataVo> penguinDataList) {

        List<PenguinDataVo> tough_10List = new ArrayList<>();
        List<PenguinDataVo> main_10List = new ArrayList<>();
        List<PenguinDataVo> tough_11List = new ArrayList<>();
        List<PenguinDataVo> main_11List = new ArrayList<>();
        List<PenguinDataVo> normalList = new ArrayList<>();

        for(PenguinDataVo penguinDataVo:penguinDataList){
            if(penguinDataVo.getStageId().startsWith("tough_10")) {
                tough_10List.add(penguinDataVo);
                continue;
            }
            if(penguinDataVo.getStageId().startsWith("main_10"))  {
                main_10List.add(penguinDataVo);
                continue;
            }


            if(penguinDataVo.getStageId().startsWith("tough_11")) {
                tough_11List.add(penguinDataVo);
                continue;
            }
            if(penguinDataVo.getStageId().startsWith("main_11")) {
                main_11List.add(penguinDataVo);
                continue;
            }
             normalList.add(penguinDataVo);
        }

        List<PenguinDataVo> penguinDataVo_10 = zoneMerge(tough_10List, main_10List);
        List<PenguinDataVo> penguinDataVo_11 = zoneMerge(tough_11List, main_11List);
        normalList.addAll(penguinDataVo_10);
        normalList.addAll(penguinDataVo_11);


        return normalList;
    }


    private static List<PenguinDataVo> zoneMerge(List<PenguinDataVo> toughList,List<PenguinDataVo> mainList){
         for(PenguinDataVo penguinDataVo:toughList){
             String  toughStageId  = penguinDataVo.getStageId();
             toughStageId  = toughStageId.substring(toughStageId.indexOf("_")+1);
             for (int i = 0; i < mainList.size(); i++) {
                 String  mainStageId  = mainList.get(i).getStageId();
                 mainStageId  = mainStageId.substring(mainStageId.indexOf("_")+1);

                 if((toughStageId.equals(mainStageId))
                         &&(penguinDataVo.getItemId().equals(mainList.get(i).getItemId()))){
                     mainList.get(i).setTimes(mainList.get(i).getTimes()+penguinDataVo.getTimes());
                     mainList.get(i).setQuantity(mainList.get(i).getQuantity()+penguinDataVo.getQuantity());
                 }
             }
         }

          return mainList;
    }

}
