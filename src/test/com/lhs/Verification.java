package com.lhs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.pojo.PenguinDataVo;
import com.lhs.bot.QqRobotService;
import com.lhs.common.util.HttpUtil;
import com.lhs.common.util.ReadFileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)   //这两个注解是为了让测试类能拥有同等的spring boot上下文环境
@SpringBootTest
public class Verification {

    @Autowired
    private QqRobotService robotService;

    @Value("${penguin.path}")
    private String penguinFilePath;

    @Test
    public void verificationPenguinsData() {
        JSONObject matrixJson ;
        String url = "https://penguin-stats.io/PenguinStats/api/v2/_private/result/matrix/CN/global/automated";   //API读取
        matrixJson =  JSONObject.parseObject(HttpUtil.GetBody(url));
        List<PenguinDataVo> penguinDatalist = JSONObject.parseArray(JSON.toJSONString(matrixJson.get("matrix")), PenguinDataVo.class);  //转为集合

        String url_stage = "https://penguin-stats.io/PenguinStats/api/v2/stages";
        JSONArray stageJson ;
        stageJson =  JSONArray.parseArray(HttpUtil.GetBody(url_stage));

        HashMap<String, Object> stageNameMap = new HashMap<>();
        for(Object object:stageJson){
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            stageNameMap.put(jsonObject.get("stageId")+"zoneId",jsonObject.get("zoneId"));
            stageNameMap.put(jsonObject.get("stageId")+"code",jsonObject.get("code"));
        }

        String[][] itemInfo = getItemInfo();
        HashMap<String, String> itemNameMap = new HashMap<>();

        for (String[] name:itemInfo){
            itemNameMap.put(name[0],name[1]);
            itemNameMap.put(name[0]+"rank",name[2]);

        }

        long time = new Date().getTime();

        SimpleDateFormat simpleDateFormat_save = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
        SimpleDateFormat simpleDateFormat_HH = new SimpleDateFormat("HH");// 设置日期格式
        String saveTime = simpleDateFormat_save.format(new Date(time-3600000));
        DecimalFormat decimalFormat_3 = new DecimalFormat("0.000");

        String jsonFile = ReadFileUtil.readFile(penguinFilePath+"matrix"  + saveTime + "auto.json");  //从保存文件读取
        matrixJson = JSONObject.parseObject(jsonFile); //json化
        List<PenguinDataVo> penguinBackupDataList = JSONObject.parseArray(JSON.toJSONString(matrixJson.get("matrix")), PenguinDataVo.class);  //转为集合
        HashMap<String, Double> backupDataHashMap = new HashMap<>();

        for (PenguinDataVo penguinDataVo : penguinBackupDataList) {
            Integer quantity = penguinDataVo.getQuantity();
            Integer times = penguinDataVo.getTimes();
            Double knockRating = (double) quantity / times;
            String stageId = penguinDataVo.getStageId();
            String itemId = penguinDataVo.getItemId();
            backupDataHashMap.put(stageId+itemId, knockRating);
        }

        String  message = "检验样本截止时间"+saveTime+"\n";

        for (PenguinDataVo penguinDataVo : penguinDatalist) {
            String stageId = penguinDataVo.getStageId();
            String itemId = penguinDataVo.getItemId();
            if (backupDataHashMap.get(stageId+itemId) == null) {
                continue;
            }
            double knockRating_backup = backupDataHashMap.get(stageId+itemId);
            Integer quantity = penguinDataVo.getQuantity();
            Integer times = penguinDataVo.getTimes();
            if(times<300) continue;
            double knockRating = (double) quantity / times;

            double threshold = 1 - knockRating_backup / knockRating;
            threshold = (threshold<0) ? -threshold : threshold;
            if(itemNameMap.get(itemId)==null||stageNameMap.get(stageId+"code")==null) continue;


            if("4".equals(itemNameMap.get(itemId+"rank")) && threshold>0.1){
                message = message + stageNameMap.get(stageId+"code")+"的"+itemNameMap.get(itemId)+"掉率："+decimalFormat_3.format(knockRating_backup*100)+
                        "%——>"+decimalFormat_3.format(knockRating*100)+"%，变化幅度"+decimalFormat_3.format(threshold*100)+
                        "%，链接：penguin-stats.cn/result/stage/"+stageNameMap.get(stageId+"zoneId")+"/"+stageId+"\n";
            }
            if("3".equals(itemNameMap.get(itemId+"rank")) && knockRating>0.2 && threshold>0.04){
                message = message + stageNameMap.get(stageId+"code")+"的"+itemNameMap.get(itemId)+"掉率："+decimalFormat_3.format(knockRating_backup*100)+
                        "%——>"+decimalFormat_3.format(knockRating*100)+"%，变化幅度"+decimalFormat_3.format(threshold*100)+
                        "%，链接：penguin-stats.cn/result/stage/"+stageNameMap.get(stageId+"zoneId")+"/"+stageId+"\n";
            }
        }

        String nowDate = simpleDateFormat_HH.format(time);


        if(message.length()<30){
            Integer nowDate_HH = Integer.parseInt(nowDate);

            robotService.sendMessage(938710832,message+"本次检验未发现问题");

        }else if(message.length()<3000){
            robotService.sendMessage(938710832,message);

        }else if(message.length()>3000){
            robotService.sendMessage(938710832,"样本被大量污染了");

        }

    }

    @Test
    public void IntTest(){
        String dateStr = "08";
        System.out.println(Integer.parseInt(dateStr));
    }

    private static String[][] getItemInfo() {

        return new String[][]{
                {"30064", "改量装置","4"}, {"30044", "异铁块","4"},
                {"30084", "三水锰矿","4"}, {"31014", "聚合凝胶","4"},
                {"30074", "白马醇","4"}, {"30054", "酮阵列","4"},
                {"30104", "RMA70-24","4"}, {"31024", "炽合金块","4"},
                {"30094", "五水研磨石","4"}, {"30024", "糖聚块","4"},
                {"30034", "聚酸酯块","4"}, {"31034", "晶体电路","4"},
                {"30014", "提纯源岩","4"}, {"31044", "精炼溶剂","4"},
                {"31054", "切削原液","4"},{"31064", "转质盐聚块","4"},
                {"30063", "全新装置","3"}, {"30043", "异铁组","3"},
                {"30083", "轻锰矿","3"}, {"31013", "凝胶","3"},
                {"30073", "扭转醇","3"}, {"30053", "酮凝集组","3"},
                {"30103", "RMA70-12","3"}, {"31023", "炽合金","3"},
                {"30093", "研磨石","3"}, {"30023", "糖组","3"},
                {"30033", "聚酸酯组","3"}, {"31033", "晶体元件","3"},
                {"30013", "固源岩组","3"}, {"31043", "半自然溶剂","3"},
                {"31053", "化合切削液","3"}, {"31063", "转质盐组","3"},
                {"2004", "高级作战记录","3"},{"2003", "中级作战记录","3"},
                {"2002", "初级作战记录","3"},{"2001", "基础作战记录","3"},
                {"3003", "赤金","3"},
        };
    }

}
