package com.lhs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.lhs.bean.DBPogo.IpData;
import com.lhs.bean.DBPogo.Visits;
import com.lhs.bean.vo.StageResultApiVo;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.IpUtil;
import com.lhs.common.util.ReadJsonUtil;
import com.lhs.common.util.ResultCode;
import com.lhs.dao.*;
import com.lhs.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    @Autowired
    private VisitsDao visitsDao;

    @Autowired
    private StageResultVoDao stageResultVoDao;

    @Autowired
    private StageResultVoApiDao stageResultVoApiDao;


    @Value("${frontEnd.path}")
    private  String frontEndFilePath ;


    /**
     * 查找所有主产物不为空的关卡
     * @return
     */
    @Override
    public List<StageResultVo> findByMainNotNull() {
        List<StageResultVo> list = stageResultVoDao.findByTypeNotNullAndEfficiencyLessThanOrderByEfficiencyDesc(1.9);
          if(list==null){
              throw  new ServiceException(ResultCode.DATA_NONE);
          }
         return list ;
    }

    /**
     * 读取T3材料关卡效率文件
     * @return
     */
    @Override
    public String readStageFileT3() {
        String jsonFile = ReadJsonUtil.readJson(frontEndFilePath+"stageT3.json");  //从保存文件读取
        if(jsonFile==null){
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        return jsonFile;
    }

    /**
     * 读取T2材料关卡效率文件
     * @return
     */
    @Override
    public String readStageFileT2() {

        String jsonFile = ReadJsonUtil.readJson(frontEndFilePath+"stageT2.json");  //从保存文件读取
              if(jsonFile==null){
                  throw new ServiceException(ResultCode.DATA_NONE);
              }
        return jsonFile;
    }

    /**
     * 读取已关闭活动的json
     * @return
     */
    @Override
    public String readStageClosedFile() {

        String jsonFile = ReadJsonUtil.readJson(frontEndFilePath+"closedStage.json");  //从保存文件读取
        if(jsonFile==null){
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        return jsonFile;
    }


    /**
     *
     * @param main  主产物
     * @param efficiency  期望理智
     * @param times  样本量
     * @param pageNum  页数
     * @param pageSize 每页个数
     * @return
     */
    @Override
    public Page<StageResultVo> findDataByTypeAndTimesAndEffOrderByEffDesc(String main, Integer times, Double efficiency, Integer pageNum, Integer pageSize) {
        if (main != null && pageNum != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<StageResultVo> page = stageResultVoDao.findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(
                    main, 1, efficiency,times, pageable);
            if (page != null) {
                return page;
            } else {
                throw new ServiceException(ResultCode.DATA_NONE);
            }
        } else {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
    }


    /**
     *
     * @param main  主产物
     * @param expect  期望理智
     * @param times  样本量
     * @param pageNum  页数
     * @param pageSize 每页个数
     * @return
     */
    @Override
    public Page<StageResultVo> findDataByMainOrderByExpectAsc(String main, Double expect, Integer times, Integer pageNum, Integer pageSize) {

        if (main != null && pageNum != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);

            Page<StageResultVo> page = stageResultVoDao.findByItemNameAndIsShowAndExpectLessThanAndTimesGreaterThanOrderByExpectAsc(
                    main, 1,50.0, 100, pageable);
            if (page != null) {
                return page;
            } else {
                throw new ServiceException(ResultCode.DATA_NONE);
            }
        } else {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }


    }




    @Override
    public void addVisitsAndIp(HttpServletRequest request, String domainName) {
        Visits visits = new Visits();

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
             today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Visits todayVisitsData = visitsDao.findVisitsByVisitsDay(today);
        visits.setVisitsDay(today);
        if (todayVisitsData != null) {
            visits.setVisits(todayVisitsData.getVisits() + 1);
        } else {
            visits.setVisits(1);
        }

//        log.info("域名是"+domainName+"   总浏览量："+visits.getVisits());
        visitsDao.save(visits);


    }




    @Override
    public void addVisits(String domainName) {
        Visits visits = new Visits();
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Visits todayVisitsData = visitsDao.findVisitsByVisitsDay(today);
        visits.setVisitsDay(today);
        if (todayVisitsData != null) {
            visits.setVisits(todayVisitsData.getVisits() + 1);
        } else {
            visits.setVisits(1);
        }
        visitsDao.save(visits);
    }


    @Override
    public List<Object> selectVisits(Date start, Date end) {
        List<Visits> visitsList = visitsDao.findByVisitsDayGreaterThanEqualAndVisitsDayLessThanEqual(start,end);
        List<String> dayList = new ArrayList<>();
        List<Integer> visitList = new ArrayList<>();

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        for (Visits visits : visitsList) {
            dayList.add(ft.format(visits.getVisitsDay()));
            visitList.add(visits.getVisits());
        }

        List<Object> visitVo = new ArrayList<>();
        visitVo.add(dayList);
        visitVo.add(visitList);
        return visitVo;
    }

    /**
     *  这个是给别人的api可以不用管
     */
    @Override
    public List<List<StageResultApiVo>> getDataByEffAndTimesOrderByEffDescAppApi(Integer times, Double efficiency) {
        double start = System.currentTimeMillis();


        if(times == null && efficiency == null) throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        Integer pageNum = 0;
        Integer pageSize = 6;
        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液"};
        List<List<StageResultApiVo>> pageList = new ArrayList<>();

        for (String main : mainName) {
            Page<StageResultApiVo> stageResultVoPage = findDataByTypeAndTimesAndEffOrderByEffDescAppApi(
                    main, times, efficiency, pageNum, pageSize);
            List<StageResultApiVo> stageResultVoList = stageResultVoPage.getContent();
            pageList.add(stageResultVoList);
        }

        double end = System.currentTimeMillis();
        System.out.println("查找用时:---" + (end - start) + "ms");
        return pageList;

    }


    /**
     *  这个是给别人的api可以不用管
     */
    @Override
    public Page<StageResultApiVo> findDataByTypeAndTimesAndEffOrderByEffDescAppApi(String main, Integer times, Double efficiency, Integer pageNum, Integer pageSize) {
        if (main != null && pageNum != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);

            Page<StageResultApiVo> page = stageResultVoApiDao.findByTypeAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(
                    main,  efficiency,times, pageable);
            if (page != null) {
                return page;
            } else {
                throw new ServiceException(ResultCode.DATA_NONE);
            }
        } else {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }

    }




}
