package com.lhs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.DBPogo.PoolData;
import com.lhs.bean.vo.Chars;
import com.lhs.bean.vo.GachaClass;
import com.lhs.common.util.HttpUtil;
import com.lhs.dao.PoolDataDao;
import com.lhs.service.PoolDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PoolDataServiceImpl implements PoolDataService {


    @Autowired
    private PoolDataDao poolDataDao;


    @Override
    public  HashMap<Object, Object> savePoolData(String tokenStr) {
        JSONObject tokenData = JSONObject.parseObject(tokenStr);
        String token = JSONObject.parseObject(tokenData.getString("data")).getString("content");
        HashMap<Object, Object> requestMap = new HashMap<>();
        requestMap.put("appId", 1);
        requestMap.put("channelMasterId", 1);
        HashMap<Object, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        requestMap.put("channelToken", tokenMap);

        String tokenEncoder = getURLEncoderString(token);
        String requestJson = JSON.toJSONString(requestMap);

        String userDataResponse = HttpUtil.postBody("https://as.hypergryph.com/u8/user/info/v1/basic", requestJson);
        JSONObject userData = JSONObject.parseObject(JSONObject.parseObject(userDataResponse).getString("data"));

        String nickName = userData.getString("nickName");
        String uid = userData.getString("uid");

        String ulr_head = "https://ak.hypergryph.com/user/api/inquiry/gacha?page=";
        String url = "https://ak.hypergryph.com/user/api/inquiry/gacha?page=1&token=" + token + "&channelId=1";
        String poolDataResponse = HttpUtil.GetBody(url);
        JSONObject poolDataJson = JSONObject.parseObject(poolDataResponse);
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(poolDataJson.get("data")));
        JSONObject pagination = JSONObject.parseObject(JSON.toJSONString(data.get("pagination")));
        int current = Integer.parseInt(JSON.toJSONString(pagination.get("total")));
        int pageTotal = 0;
        if (current % 10 == 0)
            pageTotal = current / 10;
        else
            pageTotal = (current / 10) + 1;


        List<PoolData> poolDataList = new ArrayList<>();

        for (int i = 1; i <= pageTotal; i++) {
            url = ulr_head + i + "&token=" + token;

            poolDataResponse = HttpUtil.GetBody(url);
            poolDataJson = JSONObject.parseObject(poolDataResponse);
            data = JSONObject.parseObject(JSON.toJSONString(poolDataJson.get("data")));
            List<GachaClass> poolList = JSONObject.parseArray(JSON.toJSONString(data.get("list")), GachaClass.class);

            for (int j = 0; j < poolList.size(); j++) {
                String pool = poolList.get(j).getPool();
                Long ts = poolList.get(j).getTs();
                List<Chars> charsList = poolList.get(j).getChars();
                for (int c = 0; c < charsList.size(); c++) {
                    PoolData poolData = new PoolData();
                    String idStr = uid+ts+c;
                    long id = Long.parseLong(idStr);
                    poolData.setId(id);
                    Chars chars = charsList.get(c);
                    poolData.setPool(pool);
                    poolData.setUid(uid);
                    poolData.setTs(ts);
                    poolData.setName(chars.getName());
                    poolData.setPoolType(getPoolType(pool));
                    poolData.setRarity(chars.getRarity());
                    poolData.setIsNew(chars.isNew());
                    poolDataList.add(poolData);
                }
            }
        }

        poolDataDao.saveAll(poolDataList);

        HashMap<Object, Object> resultMap = new HashMap<>();

        resultMap.put("nickName",nickName);
        resultMap.put("uid",uid);
        return resultMap;
    }

    @Override
    public  HashMap<Object, Object> findPoolDataByUid(String uid) {
        List<PoolData> dataByUid = poolDataDao.findByUid(uid);
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("poolData",dataByUid);
        resultMap.put("nickName","山桜#9180");
        resultMap.put("uid","81708745");

        return resultMap;
    }


    private final static String ENCODE = "GBK";

    private static Integer getPoolType(String pool){
        String[] limitedPool = new String[]{
         "浊酒澄心","万象伶仃","斩荆辟路","巨斧与笔尖","海蚀","","","","",""
        };
        for(String poolName:limitedPool){
            if(poolName.equals(pool)) return 1;
        }

        String[] SpecialPool = new String[]{

        };

        for(String poolName:SpecialPool){
            if(poolName.equals(pool)) return 2;
        }

        return 0;
    }

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


}



