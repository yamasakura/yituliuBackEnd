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

    @Autowired
    private IpDataDao ipDataDao;

    @Value("${fontEnd.path}")
    private  String fontEndFilePath ;

    @Override
    public List<StageResultVo> findByMainNotNull() {
        return stageResultVoDao.findByTypeNotNullAndEfficiencyLessThanOrderByEfficiencyDesc(1.9);
    }


    @Override
    public String readStageFileT3() {
//        double start = System.currentTimeMillis();
        String jsonFile = ReadJsonUtil.readJson(fontEndFilePath+"stageT3.json");  //从保存文件读取
//        double end = System.currentTimeMillis();
//        log.info("查找用时1:---" + (end - start) + "ms");
        return jsonFile;
    }

    @Override
    public String readStageFileT2() {
//        double start = System.currentTimeMillis();
        String jsonFile = ReadJsonUtil.readJson(fontEndFilePath+"stageT2.json");  //从保存文件读取
//        System.out.println(jsonFile);
//        double end = System.currentTimeMillis();
//        log.info("查找用时1:---" + (end - start) + "ms");
        return jsonFile;
    }

    @Override
    public String readStageClosedFile() {

        String jsonFile = ReadJsonUtil.readJson(fontEndFilePath+"closedStage.json");  //从保存文件读取

        return jsonFile;
    }


    @Override
    public List<List<StageResultVo>> getDataByEffAndTimesOrderByEffDesc(Integer times, Double efficiency) {
//        double start = System.currentTimeMillis();
        if(times == null && efficiency == null) throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        Integer pageNum = 0;
        Integer pageSize = 6;

        String[] mainName = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液"};
        List<List<StageResultVo>> pageList = new ArrayList<>();

        for (String main : mainName) {
            Page<StageResultVo> stageResultVoPage = findDataByTypeAndTimesAndEffOrderByEffDesc(
                    main, times, efficiency, pageNum, pageSize);
            List<StageResultVo> stageResultVoList = stageResultVoPage.getContent();
            pageList.add(stageResultVoList);
        }

//        double end = System.currentTimeMillis();
//        log.info("查找用时1:---" + (end - start) + "ms");

        return pageList;
    }



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


    @Override
    public List<List<StageResultVo>> getDataByMainOrderByExpectAsc() {
//        double start = System.currentTimeMillis();
        int pageNum = 0;
        int pageSize = 6;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        String[] mainName = new String[]{"装置","聚酸酯","固源岩","异铁","糖","酮凝集","破损装置","酯原料","源岩","异铁碎片","代糖","双酮"};
        List<List<StageResultVo>> pageList = new ArrayList<>();
        for (String main : mainName) {
            Page<StageResultVo> pageCopy = findDataByMainOrderByExpectAsc(
                    main, 50.0, 100,pageNum,pageSize);
            pageList.add(pageCopy.getContent());
        }

//        double end = System.currentTimeMillis();
//        System.out.println("查找用时:---" + (end - start) + "ms");
        return pageList;
    }






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
            visits.setVisitsBot(todayVisitsData.getVisitsBot());
            if (!"arkyituliu".equals(domainName)) {
                visits.setVisitsOld(todayVisitsData.getVisitsOld() );
                visits.setVisitsNew(todayVisitsData.getVisitsNew() + 1);
            } else {
                visits.setVisitsNew(todayVisitsData.getVisitsNew());
                visits.setVisitsOld(todayVisitsData.getVisitsOld() + 1);
            }
        } else {
            visits.setVisits(1);
            visits.setVisitsOld(1);
            visits.setVisitsNew(1);
            visits.setVisitsBot(1);
        }

//        log.info("域名是"+domainName+"   总浏览量："+visits.getVisits());

        visitsDao.save(visits);

//        String ipAddress = null;
//        try {
//            ipAddress = IpUtil.getIpAddress(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        IpData ipData = new IpData();
//        IpData ipDataExist = ipDataDao.findIpDataByIpAndVisitDayAndDomainName(ipAddress, today, domainName);
//        ipData.setId(simpleDateFormat.format(today)  + "_" + ipAddress);
//        ipData.setIp(ipAddress);
//
//        if (ipDataExist == null) {
//            ipData.setVisitTimes(1);
//            ipData.setVisitDay(today);
//            ipData.setCreateTime(new Date());
//        } else {
//            ipData.setVisitDay(today);
//            ipData.setCreateTime(ipDataExist.getCreateTime());
//            ipData.setVisitTimes(ipDataExist.getVisitTimes() + 1);
//            ipData.setUpdateTime(new Date());
//        }
//        ipData.setDomainName(domainName);
//        ipDataDao.save(ipData);
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
            visits.setVisitsOld(todayVisitsData.getVisitsOld());
            visits.setVisitsNew(todayVisitsData.getVisitsNew());
            visits.setVisitsBot(todayVisitsData.getVisitsBot() + 1);
        } else {
            visits.setVisits(1);
            visits.setVisitsOld(1);
            visits.setVisitsNew(1);
            visits.setVisitsBot(1);
        }
        visitsDao.save(visits);
    }


    @Override
    public List<Object> selectVisits(Date start, Date end) {
        List<Visits> visitsList = visitsDao.findByVisitsDayGreaterThanEqualAndVisitsDayLessThanEqual(start,end);
        List<String> dayList = new ArrayList<>();
        List<Integer> visitList = new ArrayList<>();
        List<Integer> visitOldList = new ArrayList<>();
        List<Integer> visitNewList = new ArrayList<>();
        List<Integer> visitBotList = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        for (Visits visits : visitsList) {
            dayList.add(ft.format(visits.getVisitsDay()));
            visitList.add(visits.getVisits());
            visitOldList.add(visits.getVisitsOld());
            visitNewList.add(visits.getVisitsNew());
            visitBotList.add(visits.getVisitsBot());
        }

        List<Object> visitVo = new ArrayList<>();
        visitVo.add(dayList);
        visitVo.add(visitList);
        visitVo.add(visitOldList);
        visitVo.add(visitNewList);
        visitVo.add(visitBotList);
        return visitVo;
    }


    @Override
    public List<StageResultVo> getDataByChapterNameOrderByChapterCodeDesc(String stageId, Integer itemLevel) {
        return stageResultVoDao.findByChapterNameAndMainLevelGreaterThanAndMainIsNotNullOrderByCodeAsc(stageId,itemLevel);
    }





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
