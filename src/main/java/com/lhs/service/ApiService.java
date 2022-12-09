package com.lhs.service;

import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.vo.StageResultApiVo;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface ApiService {

    //读取T3材料关卡效率文件
    String readStageFileT3(String version);

    //读取T2材料关卡效率文件
    String readStageFileT2(String version);

    //搓玉计算
    String readStageFileOrundum(String version);

    //读取已结束活动材料关卡效率文件
    String readStageClosedFile(String version);


    //根据掉落物品类型和样本数查询关卡  按效率倒序 数据库查询用
    Page<StageResultData> findDataByTypeAndTimesAndEffOrderByEffDesc(String main, Integer times, Double efficiency,
                                                                     Integer pageNum, Integer pageSize);
    //根据材料名称查询关卡  按效率倒序 数据库查询用
    Page<StageResultData> findDataByMainOrderByExpectAsc(String main, Double expect, Integer times,
                                                         Integer pageNum, Integer pageSize);

    //全部关卡效率
    List<StageResultData> findByMainNotNull();


    //给hguandl的api   这个是给别人的api可以不用管
    List<List<StageResultApiVo>> getDataByEffAndTimesOrderByEffDescAppApi(Integer times, Double efficiency);

    //给hguandl的api   这个是给别人的api可以不用管
    Page<StageResultApiVo> findDataByTypeAndTimesAndEffOrderByEffDescAppApi(String main, Integer times,
                                                                            Double efficiency, Integer pageNum, Integer pageSize);

    void addVisitsAndIp(HttpServletRequest request, String domainName);  //记录访问量

    void addVisits(String domainName);  //记录访问量

    List<Object> selectVisits(Date start, Date end); //查询访问量

    void itemCostPlan (HashMap<String,Integer> itemCost);



}
