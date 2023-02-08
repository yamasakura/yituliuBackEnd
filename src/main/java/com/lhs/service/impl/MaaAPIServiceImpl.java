package com.lhs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lhs.bean.DBPogo.BuildingSchedule;
import com.lhs.bean.DBPogo.MaaTagData;
import com.lhs.bean.DBPogo.MaaTagDataStatistical;
import com.lhs.bean.vo.MaaTagRequestVo;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.CreateJsonFile;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.common.util.ResultCode;
import com.lhs.common.util.SaveFile;
import com.lhs.dao.MaaTagDataStatisticalDao;
import com.lhs.dao.MaaTagResultDao;
import com.lhs.dao.ScheduleDao;
import com.lhs.service.MaaApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class MaaAPIServiceImpl implements MaaApiService {

    @Autowired
    private MaaTagResultDao maaTagResultDao;

    @Autowired
    private MaaTagDataStatisticalDao maaTagDataStatisticalDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Value("${frontEnd.path}")
    private String frontEndFilePath;

    @Value("${frontEnd.buildingSchedule}")
    private String buildingSchedulePath;

    @Override
    public String maaTagResultSave(MaaTagRequestVo maaTagRequestVo) {
        List<String> tags = maaTagRequestVo.getTags();
        if (tags.size() < 5) return "TAG个数少于5个";
        int level = maaTagRequestVo.getLevel();

        MaaTagData maaTagData = new MaaTagData();
        maaTagData.setTag1(tags.get(0));
        maaTagData.setTag2(tags.get(1));
        maaTagData.setTag3(tags.get(2));
        maaTagData.setTag4(tags.get(3));
        maaTagData.setTag5(tags.get(4));
        maaTagData.setLevel(level);
        maaTagData.setServer(maaTagRequestVo.getServer());
        if (maaTagRequestVo.getUuid() != null) {
            maaTagData.setUid(maaTagRequestVo.getUuid());
        }
        Date date = new Date();
        Random random = new Random();
        String id = String.valueOf(date.getTime()) + random.nextInt(1000);
        maaTagData.setCreateTime(date);
//        if (maaTagRequestVo.getResult() != null && level > 3) {
//            maaTagData.setTagResult(maaTagRequestVo.getResult().toJSONString());
//        }
        maaTagData.setSource(maaTagRequestVo.getSource());
        maaTagData.setVersion(maaTagRequestVo.getVersion());
        maaTagData.setId(Long.parseLong(id));
        maaTagData.setUid(maaTagRequestVo.getUuid());


        if(maaTagRequestVo.getVersion().startsWith("v4.10")||maaTagRequestVo.getVersion().startsWith("v4.11")
           ||maaTagRequestVo.getVersion().startsWith("v4.12")||maaTagRequestVo.getVersion().startsWith("v4.13")){
            maaTagResultDao.save(maaTagData);
        }else {

        }


        return "新增成功";
    }

    @Override
    public List<MaaTagData> selectDataLimit10() {
        return maaTagResultDao.selectDataLimit10();
    }


    @Override
    public String maaTagDataCalculation() {
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat DecimalFormat_2 = new DecimalFormat("0.00");

        List<MaaTagDataStatistical> maaTagDataStatistical = maaTagDataStatisticalDao.getMaaTagDataStatistical();
        Date startDate = maaTagDataStatistical.get(0).getLastTime();
        Date endDate = new Date();

        List<MaaTagData> maaTagDataList = maaTagResultDao.findByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThan(startDate, endDate);

        int topOperator = 0;  //高级资深总数
        int seniorOperator = 0; //资深总数
        int topAndSeniorOperator = 0; //高级资深含有资深总数
        int seniorOperatorCount = 0;  //五星TAG总数
        int rareOperatorCount = 0;   //四星TAG总数
        int commonOperatorCount = 0; //三星TAG总数
        int robot = 0;                //小车TAG总数
        int robotChoice = 0;       //小车和其他组合共同出现次数
        int vulcan = 0;             //火神出现次数
        int gravel = 0;            //砾出现次数
        int jessica = 0;         //杰西卡次数
        int count = 1;
        for (MaaTagData maaTagData : maaTagDataList) {

            int topAndSeniorOperatorSign = 0;  //高资与资深标记
            boolean vulcanSignMain = false; //火神标记
            boolean vulcanSignItem = false; //火神标记
            boolean jessicaSignMain = false; //杰西卡标记
            boolean jessicaSignItem = false;  //杰西卡标记
            boolean gravelSign = false; //砾标记

            ArrayList<String> tags = new ArrayList<>(Arrays.asList(maaTagData.getTag1(), maaTagData.getTag2()
                    , maaTagData.getTag3(), maaTagData.getTag4(), maaTagData.getTag5()));


            for (String tag : tags) {
                if ("高级资深干员".equals(tag)) {
                    topOperator++;
                    topAndSeniorOperatorSign++;
                }
                if ("资深干员".equals(tag)) {
                    seniorOperator++;
                    topAndSeniorOperatorSign++;
                }
                if ("支援机械".equals(tag)) {
                    robot++;
                    if (maaTagData.getLevel() > 3) robotChoice++;
                }
                if ("生存".equals(tag)) {
                    vulcanSignMain = true;
                    jessicaSignMain = true;
                }
                if ("重装干员".equals(tag) || "防护".equals(tag)) {
                    vulcanSignItem = true;
                }
                if ("狙击干员".equals(tag) || "远程位".equals(tag)) {
                    jessicaSignItem = true;
                }
                if ("快速复活".equals(tag)) {
                    gravelSign = true;
                }

            }


            if (maaTagData.getLevel() == 6) {
                vulcanSignMain = false;
                gravelSign = false;

                jessicaSignMain = false;
            }
            if (maaTagData.getLevel() == 5) {
                seniorOperatorCount++;
                gravelSign = false;

                jessicaSignMain = false;
            }
            if (maaTagData.getLevel() == 4) rareOperatorCount++;
            if (maaTagData.getLevel() == 3) commonOperatorCount++;
            if (topAndSeniorOperatorSign > 1) topAndSeniorOperator++;


            if (vulcanSignMain && vulcanSignItem) {
                vulcan++;
            }

            if (jessicaSignMain && jessicaSignItem) {
                jessica++;
            }

            if (gravelSign) {
                gravel++;
            }


            if (jessicaSignMain && jessicaSignItem) {
                jessica++;
            }


            count++;
        }


        MaaTagDataStatistical maaStatistical = new MaaTagDataStatistical();
        maaStatistical.setId(endDate.getTime());
        maaStatistical.setTopOperator(topOperator);
        maaStatistical.setSeniorOperator(seniorOperator);
        maaStatistical.setTopAndSeniorOperator(topAndSeniorOperator);
        maaStatistical.setSeniorOperatorCount(seniorOperatorCount);
        maaStatistical.setRareOperatorCount(rareOperatorCount);
        maaStatistical.setCommonOperatorCount(commonOperatorCount);
        maaStatistical.setRobot(robot);
        maaStatistical.setRobotChoice(robotChoice);
        maaStatistical.setVulcan(vulcan);
        maaStatistical.setGravel(gravel);

        maaStatistical.setJessica(jessica);
        maaStatistical.setMaaTagsDataCount(maaTagDataList.size());
        maaStatistical.setCreateTime(endDate);
        maaStatistical.setLastTime(maaTagDataList.get(maaTagDataList.size() - 1).getCreateTime());
        maaTagDataStatisticalDao.save(maaStatistical);


        String json = saveStatistical();
        return json;
    }

    public String maaTagDataCalculationLocal() {

        long startTime = 1672243200000L;

        for (int i = 0; i < 30; i++) {

          Date startDate = new Date(startTime+86400000L*i);
          Date endDate = new Date(startTime+86400000L*(i+1));

//        List<MaaTagDataStatistical> maaTagDataStatistical = maaTagDataStatisticalDao.getMaaTagDataStatistical();
//        Date startDate = maaTagDataStatistical.get(0).getLastTime();
//        Date endDate = new Date();

        List<MaaTagData> maaTagDataList = maaTagResultDao.findByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThan(startDate, endDate);


            if(maaTagDataList.size()<1)continue;
            Object o = JSON.toJSON(maaTagDataList);
            String fileName = startDate.getTime()+".json";
            SaveFile.save("E:\\明日方舟公招数据\\公招20221101\\",fileName,o.toString());
            if(maaTagDataList.size()>1)continue;

        int topOperator = 0;  //高级资深总数
        int seniorOperator = 0; //资深总数
        int topAndSeniorOperator = 0; //高级资深含有资深总数
        int seniorOperatorCount = 0;  //五星TAG总数
        int rareOperatorCount = 0;   //四星TAG总数
        int commonOperatorCount = 0; //三星TAG总数
        int robot = 0;                //小车TAG总数
        int robotChoice = 0;       //小车和其他组合共同出现次数
        int vulcan = 0;             //火神出现次数
        int gravel = 0;            //砾出现次数
        int jessica = 0;         //杰西卡次数
        int count = 1;
        for (MaaTagData maaTagData : maaTagDataList) {

            int topAndSeniorOperatorSign = 0;  //高资与资深标记
            boolean vulcanSignMain = false; //火神标记
            boolean vulcanSignItem = false; //火神标记
            boolean jessicaSignMain = false; //杰西卡标记
            boolean jessicaSignItem = false;  //杰西卡标记
            boolean gravelSign = false; //砾标记

            ArrayList<String> tags = new ArrayList<>(Arrays.asList(maaTagData.getTag1(), maaTagData.getTag2()
                    , maaTagData.getTag3(), maaTagData.getTag4(), maaTagData.getTag5()));


            for (String tag : tags) {
                if ("高级资深干员".equals(tag)) {
                    topOperator++;
                    topAndSeniorOperatorSign++;
                }
                if ("资深干员".equals(tag)) {
                    seniorOperator++;
                    topAndSeniorOperatorSign++;
                }
                if ("支援机械".equals(tag)) {
                    robot++;
                    if (maaTagData.getLevel() > 3) robotChoice++;
                }
                if ("生存".equals(tag)) {
                    vulcanSignMain = true;
                    jessicaSignMain = true;
                }
                if ("重装干员".equals(tag) || "防护".equals(tag)) {
                    vulcanSignItem = true;
                }
                if ("狙击干员".equals(tag) || "远程位".equals(tag)) {
                    jessicaSignItem = true;
                }
                if ("快速复活".equals(tag)) {
                    gravelSign = true;
                }

            }


            if (maaTagData.getLevel() == 6) {
                vulcanSignMain = false;
                gravelSign = false;

                jessicaSignMain = false;
            }
            if (maaTagData.getLevel() == 5) {
                seniorOperatorCount++;
                gravelSign = false;

                jessicaSignMain = false;
            }
            if (maaTagData.getLevel() == 4) rareOperatorCount++;
            if (maaTagData.getLevel() == 3) commonOperatorCount++;
            if (topAndSeniorOperatorSign > 1) topAndSeniorOperator++;


            if (vulcanSignMain && vulcanSignItem) {
                vulcan++;
            }

            if (jessicaSignMain && jessicaSignItem) {
                jessica++;
            }

            if (gravelSign) {
                gravel++;
            }


            if (jessicaSignMain && jessicaSignItem) {
                jessica++;
            }


            count++;
        }


        MaaTagDataStatistical maaStatistical = new MaaTagDataStatistical();
        maaStatistical.setId(endDate.getTime());
        maaStatistical.setTopOperator(topOperator);
        maaStatistical.setSeniorOperator(seniorOperator);
        maaStatistical.setTopAndSeniorOperator(topAndSeniorOperator);
        maaStatistical.setSeniorOperatorCount(seniorOperatorCount);
        maaStatistical.setRareOperatorCount(rareOperatorCount);
        maaStatistical.setCommonOperatorCount(commonOperatorCount);
        maaStatistical.setRobot(robot);
        maaStatistical.setRobotChoice(robotChoice);
        maaStatistical.setVulcan(vulcan);
        maaStatistical.setGravel(gravel);

        maaStatistical.setJessica(jessica);
        maaStatistical.setMaaTagsDataCount(maaTagDataList.size());
        maaStatistical.setCreateTime(endDate);
        maaStatistical.setLastTime(maaTagDataList.get(maaTagDataList.size() - 1).getCreateTime());
        maaTagDataStatisticalDao.save(maaStatistical);

            String json = saveStatistical();
            System.out.println(json);
        }
        return "";
    }


    @Override
    public String getMaaTagDataStatistical() {
        return ReadFileUtil.readFile(frontEndFilePath + "maaStatistical.json");
    }

    @Override
    public String saveStatistical() {
        List<MaaTagDataStatistical> list = maaTagDataStatisticalDao.findAll();

        MaaTagDataStatistical statistical = new MaaTagDataStatistical();
        statistical.setTopOperator(list.stream().mapToInt(MaaTagDataStatistical::getTopOperator).sum());
        statistical.setSeniorOperator(list.stream().mapToInt(MaaTagDataStatistical::getSeniorOperator).sum());
        statistical.setTopAndSeniorOperator(list.stream().mapToInt(MaaTagDataStatistical::getTopAndSeniorOperator).sum());
        statistical.setSeniorOperatorCount(list.stream().mapToInt(MaaTagDataStatistical::getSeniorOperatorCount).sum());
        statistical.setRareOperatorCount(list.stream().mapToInt(MaaTagDataStatistical::getRareOperatorCount).sum());
        statistical.setCommonOperatorCount(list.stream().mapToInt(MaaTagDataStatistical::getCommonOperatorCount).sum());
        statistical.setRobot(list.stream().mapToInt(MaaTagDataStatistical::getRobot).sum());
        statistical.setRobotChoice(list.stream().mapToInt(MaaTagDataStatistical::getRobotChoice).sum());
        statistical.setVulcan(list.stream().mapToInt(MaaTagDataStatistical::getVulcan).sum());
        statistical.setGravel(list.stream().mapToInt(MaaTagDataStatistical::getGravel).sum());
        statistical.setJessica(list.stream().mapToInt(MaaTagDataStatistical::getJessica).sum());
        statistical.setMaaTagsDataCount(list.stream().mapToInt(MaaTagDataStatistical::getMaaTagsDataCount).sum());
//        statistical.set(list.stream().mapToInt(MaaTagDataStatistical::get).sum());


        statistical.setCreateTime(new Date());
        String json = JSON.toJSONString(statistical);
        SaveFile.save(frontEndFilePath, "maaStatistical.json", json);

        return json;

    }

    @Override
    public void saveScheduleJson(String scheduleJson, Long id) {

        Date date = new Date();

        BuildingSchedule buildingSchedule = new BuildingSchedule();
        buildingSchedule.setUid(id);
        buildingSchedule.setCreateTime(date);
        scheduleDao.save(buildingSchedule);

        SaveFile.save(buildingSchedulePath,id+".json",scheduleJson);

    }



    @Override
    public void exportScheduleFile(HttpServletResponse response, Long id) {
        String str = null;

        str =  ReadFileUtil.readFile(buildingSchedulePath+id+".json");
        String jsonForMat = JSON.toJSONString(JSONObject.parseObject(str), SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty);
         String idStr = String.valueOf(id);
         CreateJsonFile.createJsonFile(response,buildingSchedulePath,idStr,jsonForMat);

    }

    @Override
    public String exportScheduleJson(Long id) {
        String filePath = buildingSchedulePath + id + ".json";
        String str = ReadFileUtil.readFile(filePath);
        if(str==null){
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        return  str;
    }


    private static String getTimestampUid(){
        Date date = new Date();
        Random random = new Random();
        return String.valueOf(date.getTime()) + random.nextInt(1000);
    }


}
