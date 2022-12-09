package com.lhs.bot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.pojo.PenguinDataVo;
import com.lhs.bean.pojo.qqMessage;
import com.lhs.common.util.HttpRequestUtil;
import com.lhs.common.util.HttpUtil;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.common.util.SaveFile;
import com.lhs.service.CharTagDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class QqRobotServiceImpl implements QqRobotService {



    @Autowired
    private CharTagDataService charTagDataService;

    @Value("${bot.idAddress}")
    private  String idAddress ;

    @Value("${bot.path}")
    private  String botFilePath ;

    @Value("${penguin.path}")
    private String penguinFilePath;

    @Override
    public void QqRobotEvenHandle(HttpServletRequest request) {
        //JSONObject
        JSONObject jsonParam = this.getJSONParam(request);
        log.info("接收参数为:{}", jsonParam.toString() != null ? "SUCCESS" : "FALSE");

        if ("message".equals(jsonParam.getString("post_type"))) {
            String message = jsonParam.getString("message");
            if ("你好".equals(message)) {
                // user_id 为QQ好友QQ号
                String url = "http://127.0.0.1:5700/send_group_msg?group_id=562528726&message=" +
                        "[CQ:image,file=https://yituliu.site/bot/img/%E7%A0%B4%E6%8D%9F%E8%A3%85%E7%BD%AE.png,type=show,id=40004]";
                String result = HttpRequestUtil.doGet(url);
                log.info("发送成功:==>{}", result);
            }
        }
    }

    @Override
    public boolean imageOcr(String messageId,long group_id) {

        String url = "http://" + idAddress + ":5700/ocr_image?image=" + messageId;
        String result = HttpRequestUtil.doGet(url);
        JSONObject jsonString = JSONObject.parseObject(result);
        Object status = jsonString.get("status");

        if (!"failed".equals(status.toString())) {

            String str = ReadFileUtil.readFile(botFilePath+"tag.json");

            JSONArray texts = JSONArray.parseArray(JSONObject.parseObject(jsonString.get("data").toString()).get("texts").toString());

            JSONObject jsonObject = JSONObject.parseObject(str);
            List<String> list = new ArrayList<>();
            int rarityMax = 5;
            int rarityMin = 2;
            for (Object text : texts) {
                String textStr = (String) JSONObject.parseObject(text.toString()).get("text");
                if (jsonObject.get(textStr) != null) {
                    if ("高级资深干员".equals(textStr)||"高级资深于员".equals(textStr)||"高级资深千员".equals(textStr)) {
                        list.add(0, jsonObject.get(textStr).toString());
                        rarityMax = 6;

                    } else {

                        list.add(textStr);
                    }
                }
            }

            if (list.size() == 5) {

                String message = charTagDataService.OCRResult(list, rarityMin, rarityMax);
                String urlMessage = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                        message;
                String resultMessage = HttpRequestUtil.doGet(urlMessage);
                log.info("发送成功:==>{}", resultMessage);

                return true;
            }

            if(list.size()>2&&list.size()<5){

                String urlMessage = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                        "图片清晰度低或图片过大识别失败，只识别到了"+list.size()+"个TAG，请发送原图或只剪裁TAG部分";
                String resultMessage = HttpRequestUtil.doGet(urlMessage);
                log.info("发送成功:==>{}", resultMessage);
                return true;
            }

        }
        return false;
    }

    //811728224
//189844877
//562528726
    @Override
    public void sendItemImg(String type,long group_id) {
        // user_id 为QQ好友QQ号
        String pathStr = ReadFileUtil.readFile(botFilePath+"Path.json");
        JSONObject path = JSONObject.parseObject(pathStr);
        Object itemPath = path.get("itemPath");
        String url = "http://"+idAddress+":5700/send_group_msg?group_id=" + group_id + "&message=" +
                "[CQ:image,file=" + itemPath + ".png,subType=0,url=https://yituliu.site/bot/item/" +type+ itemPath  + ".png,cache=0]";
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }

    @Override
    public void sendCharImg(long group_id, String roleName) {
        // user_id 为QQ好友QQ号
        String charNameJsonStr = ReadFileUtil.readFile(botFilePath+"charNameJson.json");
        String pathStr = ReadFileUtil.readFile(botFilePath+"Path.json");
        JSONObject charNameJson = JSONObject.parseObject(charNameJsonStr);
        JSONObject path = JSONObject.parseObject(pathStr);
        Object charPath = path.get("charPath");
        if (charNameJson.get(roleName) != null) {
            String url = "http://"+idAddress+":5700/send_group_msg?group_id=" + group_id + "&message=" +
                    "[CQ:image,file=" + roleName + ".png,subType=0,url=https://yituliu.site/bot/char/" + charPath + charNameJson.get(roleName) + ".png,cache=0]";

            String result = HttpRequestUtil.doGet(url);
            log.info("发送成功:==>{}", result);
        }
    }

    @Override
    public void sendSkillImg(long group_id, String roleName) {
        // user_id 为QQ好友QQ号
        String charNameJsonStr = ReadFileUtil.readFile(botFilePath+"charNameJson.json");
        String pathStr = ReadFileUtil.readFile(botFilePath+"Path.json");
        JSONObject charNameJson = JSONObject.parseObject(charNameJsonStr);
        JSONObject path = JSONObject.parseObject(pathStr);
        Object charPath = path.get("skillPath");
        if (charNameJson.get(roleName) != null) {
            String url = "http://"+idAddress+":5700/send_group_msg?group_id=" + group_id + "&message=" +
                    "[CQ:image,file=" + roleName + ".png,subType=0,url=https://yituliu.site/bot/skill/" + charPath + charNameJson.get(roleName) + ".png,cache=0]";
            String result = HttpRequestUtil.doGet(url);
            log.info("发送成功:==>{}", result);
        }
    }

    @Override
    public void sendModImg(long group_id, String roleName) {

        String equipSumStr = ReadFileUtil.readFile(botFilePath+"equipSum.json");
        String pathStr = ReadFileUtil.readFile(botFilePath+"Path.json");
        String charNameJsonStr = ReadFileUtil.readFile(botFilePath+"charNameJson.json");
        JSONObject equipSum = JSONObject.parseObject(equipSumStr);
        JSONObject path = JSONObject.parseObject(pathStr);
        JSONObject charNameJson = JSONObject.parseObject(charNameJsonStr);
        Object modPath = path.get("modPath");
        Object charName = charNameJson.get(roleName);
//                System.out.println("拿到的干员名称"+charName);
        if (equipSum.get(charName) != null) {
            int sum = Integer.parseInt(String.valueOf(equipSum.get(charName)));
            for (int i = 1; i < sum; i++) {
                String url = "http://"+idAddress+":5700/send_group_msg?group_id=" + group_id + "&message=" +
                        "[CQ:image,file=" + charName + ".png,subType=0,url=https://yituliu.site/bot/mod/" + modPath + charName + i + ".png,cache=0]";
                String result = HttpRequestUtil.doGet(url);
                log.info("发送成功:==>{}", result);
            }
        }


    }

    @Override
    public void sendMessage(long group_id,String message) {
        String str = null;

        try {
            str =  URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://"+idAddress+":5700/send_group_msg?group_id=" + group_id + "&message=" +
                str;
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }



    @Override
    public Boolean verificationPenguinsData() {
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
        String saveTime = simpleDateFormat_save.format(new Date(time-3600000*4));
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


        if(message.length()<30){
            sendMessage(562528726,message+"本次检验未发现问题");
            return true;
        }else if(message.length()<3000){
            sendMessage(938710832,message);
            return false;
        }else if(message.length()>3000){
            sendMessage(938710832,"样本被大量污染了");
            return false;
        }


        return null;
    }

    @Override
    public void spaceSend(long group_id){
        String url = "https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space?offset=&host_mid=161775300";
        String con = HttpRequestUtil.doGet(url);
        String cakeInfo = ReadFileUtil.readFile(botFilePath+"cake.json");
        JSONObject cakeInfoJson = JSONObject.parseObject(cakeInfo);
        String cake_id_top = cakeInfoJson.get("id_str_top").toString();
        String cake_id = cakeInfoJson.get("id_str").toString();

        HashMap<Object, Object> cakeMap = new HashMap<>();

        JSONObject biliSpaceJson = JSONObject.parseObject(JSONObject.parseObject(con).get("data").toString());
//        System.out.println(biliSpaceJson);
        JSONArray itemArray = JSONArray.parseArray(biliSpaceJson.get("items").toString());
        SimpleDateFormat simpleDateFormat_today = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String format_system = simpleDateFormat_today.format(new Date());


        for (Object o : itemArray) {
            JSONObject item = JSONObject.parseObject(o.toString());
            String spaceType = item.get("type").toString();
            String id_str = item.get("id_str").toString();

            String message = "";

            if ("DYNAMIC_TYPE_FORWARD".equals(spaceType)) {
                continue;
            }

            JSONObject modules = JSONObject.parseObject(item.get("modules").toString());
            JSONObject module_dynamic = JSONObject.parseObject(modules.get("module_dynamic").toString());
            JSONObject module_author = JSONObject.parseObject(modules.get("module_author").toString());
            Object pub_ts_str = module_author.get("pub_ts") + "000";
            long pub_ts = Long.parseLong(pub_ts_str.toString());
            String format_space = simpleDateFormat_today.format(new Date(pub_ts));
            if (modules.get("module_tag") != null) {
                cakeMap.put("id_str_top", id_str);
                if (id_str.equals(cake_id_top)) {
                    continue;
                }
            } else {
                cakeMap.putIfAbsent("id_str", id_str);
                if (!format_system.equals(format_space) || id_str.equals(cake_id)) {
                    break;
                }
            }

            String spaceDesc = JSONObject.parseObject(module_dynamic.get("desc").toString()).get("text").toString();

            if ("DYNAMIC_TYPE_DRAW".equals(spaceType)) {
                JSONObject module_dynamic_major = JSONObject.parseObject(module_dynamic.get("major").toString());
                JSONObject draw = JSONObject.parseObject(module_dynamic_major.get("draw").toString());
                JSONArray draw_items = JSONArray.parseArray(draw.get("items").toString());
                message = "明日方舟更新了动态\n";
                for(Object draw_item:draw_items){
                    Object src = JSONObject.parseObject(draw_item.toString()).get("src");
                    message=message+ "[CQ:image,file=明日方舟.png,subType=0,url="+src+",cache=0]";
                }
                message = message+spaceDesc;
                sendMessage(group_id,message);
            }

            if ("DYNAMIC_TYPE_AV".equals(spaceType)) {
                JSONObject module_dynamic_major = JSONObject.parseObject(module_dynamic.get("major").toString());
                JSONObject archive = JSONObject.parseObject(module_dynamic_major.get("archive").toString());
                Object cover = archive.get("cover");
                Object jump_url = archive.get("jump_url");
                message = "明日方舟发布了视频"+jump_url+"\n"+ "[CQ:image,file=明日方舟.png,subType=0,url="+cover+",cache=0]"
                        + spaceDesc;
                sendMessage(group_id,message);
            }



        }


        String cakeMapJson = JSONObject.toJSONString(cakeMap);
        SaveFile.save(botFilePath, "cake.json", cakeMapJson);
    }



    public JSONObject getJSONParam(HttpServletRequest request) {
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 数据写入Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
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

