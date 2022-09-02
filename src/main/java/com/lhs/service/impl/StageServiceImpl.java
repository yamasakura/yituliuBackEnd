package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lhs.bean.DBPogo.Stage;
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
            stageVo.setReasonEx(stage.getReason());
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
                    if("1-7".equals(stage.getStageName()))  stage.setMainLevel(3);
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

}
