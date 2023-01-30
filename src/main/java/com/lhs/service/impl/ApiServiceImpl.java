package com.lhs.service.impl;

import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.DBPogo.Visits;
import com.lhs.bean.pojo.ItemCost;
import com.lhs.bean.vo.StageResultPlan;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.*;
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
            visits.setVisitsPack(todayVisitsData.getVisitsPack() );
            visits.setVisitsBot(todayVisitsData.getVisitsBot() );
            if("index".equals(domainName)) visits.setVisitsIndex(todayVisitsData.getVisitsIndex() + 1);
            if("gacha".equals(domainName)) visits.setVisitsGacha(todayVisitsData.getVisitsGacha() + 1);
            if("building".equals(domainName)) visits.setVisitsBuilding(todayVisitsData.getVisitsBuilding() + 1);
            if("pack".equals(domainName)) visits.setVisitsPack(todayVisitsData.getVisitsPack() + 1);

        } else {
            visits.setVisits(1);
            visits.setVisitsBot(1);
            visits.setVisitsIndex(1);
            visits.setVisitsBuilding(1);
            visits.setVisitsGacha(1);
            visits.setVisitsPack(1);
        }

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
            visits.setVisitsPack(todayVisitsData.getVisitsPack());
            if("bot".equals(domainName)) visits.setVisitsBot(todayVisitsData.getVisitsBot() + 1);
        } else {
            visits.setVisits(1);
            visits.setVisitsBot(1);
            visits.setVisitsIndex(1);
            visits.setVisitsBuilding(1);
            visits.setVisitsGacha(1);
            visits.setVisitsPack(1);
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
        List<Integer> packList = new ArrayList<>();


        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        for (Visits visits : visitsList) {
            dayList.add(ft.format(visits.getVisitsDay()));
            visitList.add(visits.getVisits());
            gachaList.add(visits.getVisitsGacha());
            buildingList.add(visits.getVisitsBuilding());
            botList.add(visits.getVisitsBot());
            indexList.add(visits.getVisitsIndex());
            packList.add(visits.getVisitsPack());
        }

        List<Object> visitVo = new ArrayList<>();
        visitVo.add(dayList);
        visitVo.add(visitList);
        visitVo.add(gachaList);
        visitVo.add(buildingList);
        visitVo.add(botList);
        visitVo.add(indexList);
        visitVo.add(packList);
        return visitVo;
    }

    @Override
    public void itemCostPlan(HashMap<String, Integer> itemCost) {
        HashMap<String, Integer> itemCostCopy = new HashMap<>();
        Set<String> set = itemCost.keySet();
        for (String o : set) {
            itemCostCopy.put(o,itemCost.get(o));
        }

        String[][] itemTable = getItemTable("table");


        for (int i = 0; i < itemTable.length; i++) {
            if(itemCostCopy.get(itemTable[i][0])!=null){
                itemCostCopy.put(itemTable[i][1],itemCostCopy.get(itemTable[i][1])+itemCostCopy.get(itemTable[i][0])*Integer.parseInt(itemTable[i][1]));
            }
        }

        String[][] itemSeries = getItemTable("series");
        for (int i = 0; i < itemSeries.length; i++) {
            int item_t4 = itemCost.get(itemSeries[i][0]);
            int item_t3 = itemCost.get(itemSeries[i][1]);
            int item_t3_decompose = itemCostCopy.get(itemSeries[i][0]);
//            planCal(item_t4,item_t3,item_t3_decompose,itemSeries[i][1]);
        }




    }



    public static void main(String[] args) {
        HashMap<String, Integer> itemCost = new HashMap<>();
        itemCost.put("聚合剂",10);
        itemCost.put("双极纳米片",10);
        itemCost.put("D32钢",10);
        itemCost.put("晶体电子单元",10);
        itemCost.put("烧结核凝晶",4);
        itemCost.put("提纯源岩",10);
        itemCost.put("炽合金块",10);
        itemCost.put("精炼溶剂",10);
        itemCost.put("白马醇",10);
        itemCost.put("聚合凝胶",10);

        HashMap<String, Integer> itemCostCopy = new HashMap<>();
        Set<String> set = itemCost.keySet();
        for (String o : set) {
            itemCostCopy.put(o,itemCost.get(o));
        }

        String[][] itemTable = getItemTable("table");
        for (int i = 0; i < itemTable.length; i++) {
            if(itemCost.get(itemTable[i][0])!=null&&itemCost.get(itemTable[i][1])!=null){
                itemCost.put(itemTable[i][1],itemCost.get(itemTable[i][1])+itemCost.get(itemTable[i][0])*Integer.parseInt(itemTable[i][2]));
//                System.out.println(itemTable[i][1]+"："+itemCost.get(itemTable[i][1]));
            }
        }

        System.out.println(itemCost.get("提纯源岩"));
        System.out.println(itemCostCopy.get("提纯源岩"));
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


    private static String[][] getItemTable(String type){

        String[][] itemTable = new String[][]{
                {"聚合剂","提纯源岩","1"},{"聚合剂","异铁块","1"},{"聚合剂","酮阵列","1"},
                {"双极纳米片","改量装置","1"},{"双极纳米片","白马醇","2"},
                {"D32钢","三水锰矿","1"},{"D32钢","五水研磨石","1"},{"D32钢","RMA70-24","1"},
                {"晶体电子单元","聚合凝胶","2"},{"晶体电子单元","炽合金块","1"},{"晶体电子单元","晶体电路","1"},
                {"烧结核凝晶","转质盐聚块","1"},{"烧结核凝晶","切削原液","1"},{"烧结核凝晶","精炼溶剂","1"},
                {"提纯源岩","固源岩组","4"},
                {"糖聚块","糖组","2"},{"糖聚块","异铁组","1"},{"糖聚块","轻锰矿","1"},
                {"聚酸酯块","聚酸酯组","2"},{"聚酸酯块","酮凝集组","1"},{"聚酸酯块","扭转醇","1"},
                {"异铁块","异铁组","2"},{"异铁块","全新装置","1"},{"异铁块","聚酸酯组","1"},
                {"酮阵列","酮凝集组","2"},{"酮阵列","糖组","1"},{"酮阵列","轻锰矿","1"},
                {"改量装置","全新装置","1"},{"改量装置","固源岩组","2"},{"改量装置","研磨石","1"},
                {"白马醇","扭转醇","1"},{"白马醇","糖组","1"},{"白马醇","RMA70-12","1"},
                {"三水锰矿","轻锰矿","2"},{"三水锰矿","聚酸酯组","1"},{"三水锰矿","扭转醇","1"},
                {"五水研磨石","研磨石","1"},{"五水研磨石","异铁组","1"},{"五水研磨石","全新装置","1"},
                {"RMA70-24","RMA70-12","1"},{"RMA70-24","固源岩组","2"},{"RMA70-24","酮凝集组","1"},
                {"聚合凝胶","凝胶","1"},{"聚合凝胶","异铁组","1"},{"聚合凝胶","炽合金","1"},
                {"炽合金块","全新装置","1"},{"炽合金块","研磨石","1"},{"炽合金块","炽合金","1"},
                {"晶体电路","晶体元件","2"},{"晶体电路","凝胶","1"},{"晶体电路","炽合金","1"},
                {"精炼溶剂","半自然溶剂","1"},{"精炼溶剂","化合切削液","1"},{"精炼溶剂","凝胶","1"},
                {"切削原液","化合切削液","1"},{"切削原液","晶体元件","1"},{"切削原液","RMA70-12","1"},
                {"转质盐聚块","转质盐组","1"},{"转质盐聚块","半自然溶剂","1"},{"转质盐聚块","糖组","1"},
        };

        String[][] itemSeries = new String[][]{
                {"提纯源岩","固源岩组"},{"糖聚块","糖组"},{"聚酸酯块","聚酸酯组"},{"异铁块","异铁组"},{"酮阵列","酮凝集组"},{"改量装置","全新装置"},
                {"白马醇","扭转醇"},{"三水锰矿","轻锰矿"},{"五水研磨石","研磨石"},{"RMA70-24","RMA70-12"},{"聚合凝胶","凝胶"},
                {"炽合金块","炽合金"},{"晶体电路","晶体元件"},{"精炼溶剂","半自然溶剂"},{"切削原液","化合切削液"},{"转质盐聚块","转质盐组"},
        };

        if("table".equals(type)){
            return  itemTable;
        }else {
            return itemSeries;
        }
    }





}
