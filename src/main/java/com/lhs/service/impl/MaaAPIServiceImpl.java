package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lhs.bean.DBPogo.BuildingSchedule;
import com.lhs.bean.DBPogo.ItemRevise;
import com.lhs.bean.DBPogo.MaaTagData;
import com.lhs.bean.DBPogo.MaaTagDataStatistical;
import com.lhs.bean.pojo.MaaTagRequestVo;
import com.lhs.bean.vo.ItemValueVo;
import com.lhs.common.util.ReadJsonUtil;
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
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        maaTagResultDao.save(maaTagData);

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
//        List<MaaTagData> maaTagDataList = maaTagResultDao.findByCreateTimeIsLessThan(new Date(1662220800000L));

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

    @Override
    public String getMaaTagDataStatistical() {
        return ReadJsonUtil.readJson(frontEndFilePath + "maaStatistical.json");
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
    public Long saveScheduleJson( String scheduleJson) {

        Date date = new Date();
        Random random = new Random();
        String timestampId = String.valueOf(date.getTime()) + random.nextInt(1000);
        long uid = Long.parseLong(timestampId);

        BuildingSchedule buildingSchedule = new BuildingSchedule();
        buildingSchedule.setUid(uid);
        buildingSchedule.setCreateTime(date);
        scheduleDao.save(buildingSchedule);

        SaveFile.save(buildingSchedulePath,uid+".json",scheduleJson);

        return  uid;
    }



    @Override
    public void exportScheduleJson(HttpServletResponse response, Long uid) {
        String str = null;

        str =  ReadJsonUtil.readJson(buildingSchedulePath+uid+".json");
        String jsonForMat = JSON.toJSONString(JSONObject.parseObject(str), SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty);

        try {

            // 拼接文件完整路径
            String fullPath = buildingSchedulePath + uid + ".json";

            // 生成json格式文件

            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 格式化json字符串
            FileInputStream fileInputStream = new FileInputStream(file);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonForMat);
            write.flush();
            write.close();

            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + uid + ".json");
            OutputStream outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private static String getTimestampUid(){
        Date date = new Date();
        Random random = new Random();
        return String.valueOf(date.getTime()) + random.nextInt(1000);
    }


}
