package com.lhs.service.impl;

import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.DBPogo.Visits;
import com.lhs.bean.pojo.ItemCost;
import com.lhs.bean.vo.StageResultApiVo;
import com.lhs.bean.vo.StageResultPlan;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.ReadFileUtil;
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
import java.util.*;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    @Autowired
    private VisitsDao visitsDao;

    @Autowired
    private StageResultDataDao stageResultDataDao;

    @Autowired
    private StageResultVoApiDao stageResultVoApiDao;


    @Value("${frontEnd.path}")
    private  String frontEndFilePath ;


    /**
     * 查找所有主产物不为空的关卡
     * @return
     */
    @Override
    public List<StageResultData> findByMainNotNull() {
        List<StageResultData> list = stageResultDataDao.findByItemTypeNotNullAndEfficiencyLessThanOrderByEfficiencyDesc(1.9);
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
    public String readStageFileT3(String version) {
        String jsonFile = ReadFileUtil.readFile(frontEndFilePath+"stageT3"+version+".json");  //从保存文件读取
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
    public String readStageFileT2(String version) {

        String jsonFile = ReadFileUtil.readFile(frontEndFilePath+"stageT2"+version+".json");  //从保存文件读取
              if(jsonFile==null){
                  throw new ServiceException(ResultCode.DATA_NONE);
              }
        return jsonFile;
    }

    /**
     * 读取搓玉最优关卡文件
     * @return
     */
    @Override
    public String readStageFileOrundum(String version) {
        String jsonFile = ReadFileUtil.readFile(frontEndFilePath+"stageOrundum"+version+".json");  //从保存文件读取
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
    public String readStageClosedFile(String version) {

        String jsonFile = ReadFileUtil.readFile(frontEndFilePath+"closedStage"+version+".json");  //从保存文件读取
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
    public Page<StageResultData> findDataByTypeAndTimesAndEffOrderByEffDesc(String main, Integer times, Double efficiency, Integer pageNum, Integer pageSize) {
        if (main != null && pageNum != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<StageResultData> page = stageResultDataDao.findByItemTypeAndIsShowAndEfficiencyGreaterThanAndSampleSizeGreaterThanOrderByEfficiencyDesc(
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
    public Page<StageResultData> findDataByMainOrderByExpectAsc(String main, Double expect, Integer times, Integer pageNum, Integer pageSize) {

        if (main != null && pageNum != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);

            Page<StageResultData> page = stageResultDataDao.findByItemNameAndIsShowAndApExpectLessThanAndSampleSizeGreaterThanOrderByApExpectAsc(
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
            visits.setVisitsIndex(todayVisitsData.getVisitsIndex());
            visits.setVisitsGacha(todayVisitsData.getVisitsGacha());
            visits.setVisitsBuilding(todayVisitsData.getVisitsBuilding());
            visits.setVisitsBot(todayVisitsData.getVisitsBot() );
            if("index".equals(domainName)) visits.setVisitsIndex(todayVisitsData.getVisitsIndex() + 1);
            if("gacha".equals(domainName)) visits.setVisitsGacha(todayVisitsData.getVisitsGacha() + 1);
            if("building".equals(domainName)) visits.setVisitsBuilding(todayVisitsData.getVisitsBuilding() + 1);

        } else {
            visits.setVisits(1);
            visits.setVisitsBot(1);
            visits.setVisitsIndex(1);
            visits.setVisitsBuilding(1);
            visits.setVisitsGacha(1);
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
            visits.setVisitsIndex(todayVisitsData.getVisitsIndex());
            visits.setVisitsGacha(todayVisitsData.getVisitsGacha());
            visits.setVisitsBuilding(todayVisitsData.getVisitsBuilding());
            visits.setVisitsBot(todayVisitsData.getVisitsBot());
            if("bot".equals(domainName)) visits.setVisitsBot(todayVisitsData.getVisitsBot() + 1);
        } else {
            visits.setVisits(1);
            visits.setVisitsBot(1);
            visits.setVisitsIndex(1);
            visits.setVisitsBuilding(1);
            visits.setVisitsGacha(1);
        }
        visitsDao.save(visits);
    }


    @Override
    public List<Object> selectVisits(Date start, Date end) {
        List<Visits> visitsList = visitsDao.findByVisitsDayGreaterThanEqualAndVisitsDayLessThanEqual(start,end);
        List<String> dayList = new ArrayList<>();
        List<Integer> visitList = new ArrayList<>();
        List<Integer> gachaList = new ArrayList<>();
        List<Integer> buildingList = new ArrayList<>();
        List<Integer> botList = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();


        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        for (Visits visits : visitsList) {
            dayList.add(ft.format(visits.getVisitsDay()));
            visitList.add(visits.getVisits());
            gachaList.add(visits.getVisitsGacha());
            buildingList.add(visits.getVisitsBuilding());
            botList.add(visits.getVisitsBot());
            indexList.add(visits.getVisitsIndex());
        }

        List<Object> visitVo = new ArrayList<>();
        visitVo.add(dayList);
        visitVo.add(visitList);
        visitVo.add(gachaList);
        visitVo.add(buildingList);
        visitVo.add(botList);
        visitVo.add(indexList);
        return visitVo;
    }

    @Override
    public void itemCostPlan(HashMap<String, Integer> itemCost) {
        itemCost.put("提纯源岩", itemCost.get("提纯源岩") + itemCost.get("聚合剂"));
        itemCost.put("异铁块", itemCost.get("异铁块") + itemCost.get("聚合剂"));
        itemCost.put("酮阵列", itemCost.get("酮阵列") + itemCost.get("聚合剂"));
        itemCost.put("改量装置", itemCost.get("改量装置") + itemCost.get("双极纳米片"));
        itemCost.put("白马醇", itemCost.get("白马醇") + itemCost.get("双极纳米片")*2);
        itemCost.put("三水锰矿", itemCost.get("三水锰矿") + itemCost.get("D32钢"));
        itemCost.put("五水研磨石", itemCost.get("五水研磨石") + itemCost.get("D32钢"));
        itemCost.put("RMA70-24", itemCost.get("RMA70-24") + itemCost.get("D32钢"));
        itemCost.put("聚合凝胶", itemCost.get("聚合凝胶") + itemCost.get("晶体电子单元")*2);
        itemCost.put("炽合金块", itemCost.get("炽合金块") + itemCost.get("晶体电子单元"));
        itemCost.put("晶体电路", itemCost.get("晶体电路") + itemCost.get("晶体电子单元"));
         if(itemCost.get("固源岩组")!=null){
             Integer count = itemCost.get("固源岩组");

         }

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
