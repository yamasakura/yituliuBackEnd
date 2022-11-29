package com.lhs.bot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.pojo.qqMessage;
import com.lhs.common.util.HttpRequestUtil;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.service.CharTagDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
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
    public List<qqMessage> getGroupMessage(long group_id, HttpServletRequest request) {
        String url = "http://"+idAddress+":5700/get_group_msg_history?group_id=" + group_id;

        String result = HttpRequestUtil.doGet(url);
        log.info("获取成功:==>{}");
        Map json_data = JSONObject.parseObject(result);
        Object data = json_data.get("data");
        Map json_messages = JSONObject.parseObject(data.toString());
        Object messages = json_messages.get("messages");

        List<qqMessage> list = JSONObject.parseArray(JSON.toJSONString(messages), qqMessage.class);
        return list;
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


}

