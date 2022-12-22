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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class QqRobotServiceImpl implements QqRobotService {


    @Autowired
    private CharTagDataService charTagDataService;

    @Value("${bot.idAddress}")
    private String idAddress;

    @Value("${bot.path}")
    private String botFilePath;

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
    public boolean imageOcr(String messageId, long group_id) {

        String url = "http://" + idAddress + ":5700/ocr_image?image=" + messageId;
        String result = HttpRequestUtil.doGet(url);
        JSONObject jsonString = JSONObject.parseObject(result);
        Object status = jsonString.get("status");

        if (!"failed".equals(status.toString())) {

            String str = ReadFileUtil.readFile(botFilePath + "tag.json");

            JSONArray texts = JSONArray.parseArray(JSONObject.parseObject(jsonString.get("data").toString()).get("texts").toString());

            JSONObject jsonObject = JSONObject.parseObject(str);
            List<String> list = new ArrayList<>();
            int rarityMax = 5;
            int rarityMin = 2;
            for (Object text : texts) {
                String textStr = (String) JSONObject.parseObject(text.toString()).get("text");
                if (jsonObject.get(textStr) != null) {
                    if ("高级资深干员".equals(textStr) || "高级资深于员".equals(textStr) || "高级资深千员".equals(textStr)) {
                        list.add(0, jsonObject.get(textStr).toString());
                        rarityMax = 6;

                    } else {

                        list.add(textStr);
                    }
                }
            }

            List<HashMap<Object, Object>> groupMessage = new ArrayList<>();

            if (list.size() == 5) {

                String message = charTagDataService.OCRResult(list, rarityMin, rarityMax);
                HashMap<Object, Object> messageMap = getMessageMap(message, false);
                groupMessage.add(messageMap);
                sendGroupMessage(group_id, JSON.toJSONString(groupMessage));

//                String message = charTagDataService.OCRResult(list, rarityMin, rarityMax);
//                sendMessage(group_id,message,true);
                log.info("发送成功:==>{}", "公招");

                return true;
            }

            if (list.size() > 2 && list.size() < 5) {

                sendMessage(group_id, "图片识别失败，只识别到了" + list.size() + "个TAG，请发送尽量清晰或者只截取TAG部分的图片", true);
                return true;
            }

        }
        return false;
    }

    //811728224
//189844877
//562528726
    @Override
    public void sendItemImg(String type, long group_id) {
        // user_id 为QQ好友QQ号
        String pathStr = ReadFileUtil.readFile(botFilePath + "Path.json");
        JSONObject path = JSONObject.parseObject(pathStr);
        Object itemPath = path.get("itemPath");
        String url = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                "[CQ:image,file=" + itemPath + ".png,subType=0,url=https://yituliu.site/bot/item/" + type + itemPath + ".png,cache=0]";
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }

    @Override
    public void sendCharImg(long group_id, String roleName) {
        // user_id 为QQ好友QQ号
        String charNameJsonStr = ReadFileUtil.readFile(botFilePath + "charNameJson.json");
        String pathStr = ReadFileUtil.readFile(botFilePath + "Path.json");
        JSONObject charNameJson = JSONObject.parseObject(charNameJsonStr);
        JSONObject path = JSONObject.parseObject(pathStr);
        Object charPath = path.get("charPath");
        if (charNameJson.get(roleName) != null) {
            String url = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                    "[CQ:image,file=" + roleName + ".png,subType=0,url=https://yituliu.site/bot/char" + charPath + charNameJson.get(roleName) + ".png,cache=0]";
            String result = HttpRequestUtil.doGet(url);
            log.info("发送成功:==>{}", result);
        }
    }

    @Override
    public void sendSkillImg(long group_id, String roleName) {
        // user_id 为QQ好友QQ号
        String charNameJsonStr = ReadFileUtil.readFile(botFilePath + "charNameJson.json");
        String pathStr = ReadFileUtil.readFile(botFilePath + "Path.json");
        JSONObject charNameJson = JSONObject.parseObject(charNameJsonStr);
        JSONObject path = JSONObject.parseObject(pathStr);
        Object charPath = path.get("skillPath");
        if (charNameJson.get(roleName) != null) {
            String url = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                    "[CQ:image,file=" + roleName + ".png,subType=0,url=https://yituliu.site/bot/skill" + charPath + charNameJson.get(roleName) + ".png,cache=0]";
            String result = HttpRequestUtil.doGet(url);
            log.info("发送成功:==>{}", result);
        }
    }

    @Override
    public void sendModImg(long group_id, String roleName) {

        String equipSumStr = ReadFileUtil.readFile(botFilePath + "equipSum.json");
        String pathStr = ReadFileUtil.readFile(botFilePath + "Path.json");
        String charNameJsonStr = ReadFileUtil.readFile(botFilePath + "charNameJson.json");
        JSONObject equipSum = JSONObject.parseObject(equipSumStr);
        JSONObject path = JSONObject.parseObject(pathStr);
        JSONObject charNameJson = JSONObject.parseObject(charNameJsonStr);
        Object modPath = path.get("modPath");
        if (charNameJson.get(roleName) != null) {
            String charName = charNameJson.get(roleName).toString();
            if (equipSum.get(charName) != null) {
                int sum = Integer.parseInt(String.valueOf(equipSum.get(charName)));
                for (int i = 1; i < sum; i++) {
                    String url = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                            "[CQ:image,file=" + charName + ".png,subType=0,url=https://yituliu.site/bot/mod" + modPath + charName + i + ".png,cache=0]";
                    String result = HttpRequestUtil.doGet(url);
                    log.info("发送成功:==>{}", result);
                }
            }
        }


    }

    @Override
    public void sendMessage(long group_id, String message, boolean toUTF8) {
        String str = null;

        try {
            str = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://" + idAddress + ":5700/send_group_msg?group_id=" + group_id + "&message=" +
                str;
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }


    @Override
    public Boolean verificationPenguinsData() {
        JSONObject matrixJson;
        String url = "https://penguin-stats.io/PenguinStats/api/v2/_private/result/matrix/CN/global/automated";   //API读取
        matrixJson = JSONObject.parseObject(HttpUtil.GetBody(url));
        List<PenguinDataVo> penguinDatalist = JSONObject.parseArray(JSON.toJSONString(matrixJson.get("matrix")), PenguinDataVo.class);  //转为集合

        String url_stage = "https://penguin-stats.io/PenguinStats/api/v2/stages";
        JSONArray stageJson;
        stageJson = JSONArray.parseArray(HttpUtil.GetBody(url_stage));

        HashMap<String, Object> stageNameMap = new HashMap<>();
        for (Object object : stageJson) {
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            stageNameMap.put(jsonObject.get("stageId") + "zoneId", jsonObject.get("zoneId"));
            stageNameMap.put(jsonObject.get("stageId") + "code", jsonObject.get("code"));
        }

        String[][] itemInfo = getItemInfo();
        HashMap<String, String> itemNameMap = new HashMap<>();

        for (String[] name : itemInfo) {
            itemNameMap.put(name[0], name[1]);
            itemNameMap.put(name[0] + "rank", name[2]);

        }

        long time = new Date().getTime();

        SimpleDateFormat simpleDateFormat_save = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
        SimpleDateFormat simpleDateFormat_HH = new SimpleDateFormat("HH");// 设置日期格式
        String saveTime = simpleDateFormat_save.format(new Date(time - 3600000 * 12));
        DecimalFormat decimalFormat_3 = new DecimalFormat("0.000");

        String jsonFile = ReadFileUtil.readFile(penguinFilePath + "matrix" + saveTime + "auto.json");  //从保存文件读取
        matrixJson = JSONObject.parseObject(jsonFile); //json化
        List<PenguinDataVo> penguinBackupDataList = JSONObject.parseArray(JSON.toJSONString(matrixJson.get("matrix")), PenguinDataVo.class);  //转为集合
        HashMap<String, Double> backupDataHashMap = new HashMap<>();

        for (PenguinDataVo penguinDataVo : penguinBackupDataList) {
            Integer quantity = penguinDataVo.getQuantity();
            Integer times = penguinDataVo.getTimes();
            if (times < 300) continue;
            Double knockRating = (double) quantity / times;
            String stageId = penguinDataVo.getStageId();
            String itemId = penguinDataVo.getItemId();

            backupDataHashMap.put(stageId + itemId, knockRating);
        }

        String message = "检验样本截止时间" + saveTime + "\n";

        for (PenguinDataVo penguinDataVo : penguinDatalist) {
            String stageId = penguinDataVo.getStageId();
            String itemId = penguinDataVo.getItemId();
            if (backupDataHashMap.get(stageId + itemId) == null) {
                continue;
            }
            double knockRating_backup = backupDataHashMap.get(stageId + itemId);
            Integer quantity = penguinDataVo.getQuantity();
            Integer times = penguinDataVo.getTimes();
            if (times < 300) continue;
            double knockRating = (double) quantity / times;


            double threshold = 1 - knockRating_backup / knockRating;
            threshold = (threshold < 0) ? -threshold : threshold;
            if (itemNameMap.get(itemId) == null || stageNameMap.get(stageId + "code") == null) continue;

            if ("4".equals(itemNameMap.get(itemId + "rank")) && threshold > 0.1) {
                message = message + stageNameMap.get(stageId + "code") + "的" + itemNameMap.get(itemId) + "掉率：" + decimalFormat_3.format(knockRating_backup * 100) +
                        "%——>" + decimalFormat_3.format(knockRating * 100) + "%，变化幅度" + decimalFormat_3.format(threshold * 100) +
                        "%，链接：penguin-stats.cn/result/stage/" + stageNameMap.get(stageId + "zoneId") + "/" + stageId + "\n";
            }
            if ("3".equals(itemNameMap.get(itemId + "rank")) && knockRating > 0.2 && threshold > 0.04) {
                message = message + stageNameMap.get(stageId + "code") + "的" + itemNameMap.get(itemId) + "掉率：" + decimalFormat_3.format(knockRating_backup * 100) +
                        "%——>" + decimalFormat_3.format(knockRating * 100) + "%，变化幅度" + decimalFormat_3.format(threshold * 100) +
                        "%，链接：penguin-stats.cn/result/stage/" + stageNameMap.get(stageId + "zoneId") + "/" + stageId + "\n";
            }
        }


        if (message.length() < 30) {
            sendMessage(562528726, message + "本次检验未发现问题", true);
            return true;
        } else if (message.length() < 3000) {
            sendMessage(938710832, message, true);
            return false;
        } else if (message.length() > 3000) {
            sendMessage(938710832, "样本被大量污染了", true);
            return false;
        }


        return null;
    }


    @Override
    public void wbSend(Long[] group_ids) {
        String url = "https://weibo.com/ajax/statuses/mymblog?uid=6279793937&page=1&feature=0";
        String weibo = ReadFileUtil.readFile(botFilePath + "weibo.json");
        JSONObject weiboJson = JSONObject.parseObject(weibo);
        HashMap<Object, Object> hashMap_weibo = new HashMap<>();
        SimpleDateFormat simpleDateFormat_today = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String format_system = simpleDateFormat_today.format(new Date()); //获取系统日期
//        format_system = "2022-12-17";

        String cookie = weiboJson.getString("cookie");  //访客cookie读取
        String jsonDate = weiboJson.getString("date");  //上次保存的时间获取
        hashMap_weibo.put("cookie", cookie);  //访客cookie保存
        String wbStr = HttpRequestUtil.doGetAndCookie(url, cookie);    //微博初始数据
        JSONArray wbData = JSONArray.parseArray(JSONObject.parseObject(JSONObject.parseObject(wbStr).getString("data")).getString("list"));  //微博消息数组


        hashMap_weibo.put("date", format_system);  //保存系统日期
        HashMap ids = new HashMap<>();
        ids.put("1111", "1111");

        if (format_system.equals(jsonDate)) {
            ids = JSONObject.parseObject(weiboJson.getString("ids"), HashMap.class);//获取发送过的动态id
        }


        for (Object dynamic : wbData) {  //微博消息数组循环
            JSONObject blog = JSONObject.parseObject(dynamic.toString());  //单条动态

            String cst = blog.getString("created_at");//获取动态时间
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US); // 格式转换
            Date date = null;
            try {
                date = sdf.parse(cst);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            String format_wb = simpleDateFormat_today.format(date);
            if (!format_system.equals(format_wb)) continue;//判断是否是今天

            String text_raw = blog.getString("text_raw");//动态内容
            String idstr = blog.getString("idstr");//动态id
            if (ids.get(idstr) != null) continue;
            ids.put(idstr, idstr);

            List<HashMap<Object, Object>> groupMessage = new ArrayList<>();  //转发消息数组
            String message = "";
            message = message + text_raw;

            JSONArray pic_ids = JSONArray.parseArray(blog.getString("pic_ids"));
            String pic_url = "";
            for (Object pic_id : pic_ids) {
                JSONObject pic_infos = JSONObject.parseObject(blog.getString("pic_infos"));
                JSONObject pic_info = JSONObject.parseObject(pic_infos.getString(pic_id.toString()));
                pic_url = JSONObject.parseObject(pic_info.getString("original")).getString("url");
                System.out.println(pic_url);
                if (pic_url.contains("gif")) {
                    message = message + "[CQ:image,file=111.gif,subType=0,url=" + pic_url + ",cache=0]\n\n";
                } else {
                    message = message + "[CQ:image,file=明日方舟.png,subType=0,url=" + pic_url + ",cache=0]\n\n";
                }
            }

            HashMap<Object, Object> messageMap = getMessageMap(message, true);
            groupMessage.add(messageMap);
            for (Long group_id : group_ids) {
                sendMessage(group_id, "Arknights发布了动态", true);
                sendGroupMessage(group_id, JSON.toJSONString(groupMessage));
            }
        }

        hashMap_weibo.put("ids", ids);

        String weiboMapJson = JSONObject.toJSONString(hashMap_weibo);
        SaveFile.save(botFilePath, "weibo.json", weiboMapJson);
    }



    @Override
    public void sendGroupMessage(long group_id, String message) {
        String str = null;

        try {
            str = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://" + idAddress + ":5700/send_group_forward_msg?group_id=" + group_id + "&messages=" +
                str;
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }

    @Override
    public void deleteMessage(Integer message_id) {
        String url = "http://" + idAddress + ":5700/delete_msg?message_id=" + message_id;
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
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
                {"30064", "改量装置", "4"}, {"30044", "异铁块", "4"},
                {"30084", "三水锰矿", "4"}, {"31014", "聚合凝胶", "4"},
                {"30074", "白马醇", "4"}, {"30054", "酮阵列", "4"},
                {"30104", "RMA70-24", "4"}, {"31024", "炽合金块", "4"},
                {"30094", "五水研磨石", "4"}, {"30024", "糖聚块", "4"},
                {"30034", "聚酸酯块", "4"}, {"31034", "晶体电路", "4"},
                {"30014", "提纯源岩", "4"}, {"31044", "精炼溶剂", "4"},
                {"31054", "切削原液", "4"}, {"31064", "转质盐聚块", "4"},
                {"30063", "全新装置", "3"}, {"30043", "异铁组", "3"},
                {"30083", "轻锰矿", "3"}, {"31013", "凝胶", "3"},
                {"30073", "扭转醇", "3"}, {"30053", "酮凝集组", "3"},
                {"30103", "RMA70-12", "3"}, {"31023", "炽合金", "3"},
                {"30093", "研磨石", "3"}, {"30023", "糖组", "3"},
                {"30033", "聚酸酯组", "3"}, {"31033", "晶体元件", "3"},
                {"30013", "固源岩组", "3"}, {"31043", "半自然溶剂", "3"},
                {"31053", "化合切削液", "3"}, {"31063", "转质盐组", "3"},
                {"2004", "高级作战记录", "3"}, {"2003", "中级作战记录", "3"},
                {"2002", "初级作战记录", "3"}, {"2001", "基础作战记录", "3"},
                {"3003", "赤金", "3"},
        };
    }

    private static HashMap<Object, Object> getMessageMap(String message, boolean messageType) {
        HashMap<Object, Object> messageMap = new HashMap<>();
        HashMap<Object, Object> content = new HashMap<>();
        content.put("name", "桜");
        content.put("uin", "1820702789");
        if (messageType) {
            content.put("content", message);
        } else {
            HashMap<Object, Object> text = new HashMap<>();
            text.put("text", message);
            HashMap<Object, Object> textMap = new HashMap<>();
            textMap.put("type", "text");
            textMap.put("data", text);
            List<HashMap<Object, Object>> list = new ArrayList<>();
            list.add(textMap);
            content.put("content", list);
        }


        messageMap.put("type", "node");
        messageMap.put("data", content);

        return messageMap;
    }


}

