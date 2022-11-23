package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lhs.bean.DBPogo.ItemRevise;
import com.lhs.bean.DBPogo.Item;

import com.lhs.bean.vo.ItemValueVo;
import com.lhs.common.util.CreateJsonFile;
import com.lhs.dao.ItemDao;
import com.lhs.dao.ItemReviseDao;
import com.lhs.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemReviseDao itemReviseDao;

    @Value("${frontEnd.path}")
    private String frontEndFilePath;


    /**
     * 重置临时价值表，仅效率计算开始时重置一次
     */
    public void resetItemShopValue(Double version) {
        //拿到物品表的初始信息
        String[][] itemRaw = getItemInfo(version);
        //保存材料的价值和名称，<名称，价值>
        HashMap<String, Double> itemShopValue = new HashMap<>();
         //加工站平均产出t1
        double workShopValue_t1 = (1.667 * 0.263 + 2.5 * 0.175 + 2.5 * 0.175 + 2.92 * 0.14 + 2.92 * 0.14 + 3.75
                * 0.105) * 0.2 - 0.45;
         //加工站平均产出t1
        double workShopValue_t2 = (5.0 * 0.263 + 7.5 * 0.175 + 7.5 * 0.175 + 8.75 * 0.14 + 8.75 * 0.14 + 11.25
                * 0.105) * 0.2 - 0.9;
        //加工站平均产出t3
        double workShopValue_t3 = (0.099 * 25 + 0.082 * 30 + 0.082 * 30 + 0.066 * 35 + 0.066 * 35 + 0.049 * 45 + 0.074 * 30
                + 0.066 * 35 + 0.059 * 40 + 0.049 * 45 + 0.059 * 40 + 0.066 * 35 + 0.066 * 30 + 0.059 * 40
                + 0.059 * 40) * 0.2 - 1.35;
        //加工站平均产出t4
        double workShopValue_t4 = (0.09 * 100 + 0.067 * 130 + 0.067 * 125 + 0.06 * 145 + 0.06 * 130 + 0.045 * 135 + 0.077 * 105 +
                0.067 * 130 + 0.067 * 120 + 0.06 * 130 + 0.077 * 110 + 0.067 * 120 + 0.067 * 135 + 0.067 * 115 + 0.067 * 120) * 0.2 - 1.8;

        itemShopValue.put("固源岩组", 25.0);
        itemShopValue.put("糖组", 30.0);
        itemShopValue.put("聚酸酯组", 30.0);
        itemShopValue.put("异铁组", 35.0);
        itemShopValue.put("酮凝集组", 35.0);
        itemShopValue.put("全新装置", 45.0);
        itemShopValue.put("扭转醇", 30.0);
        itemShopValue.put("轻锰矿", 35.0);
        itemShopValue.put("研磨石", 40.0);
        itemShopValue.put("RMA70-12", 45.0);
        itemShopValue.put("凝胶", 40.0);
        itemShopValue.put("炽合金", 35.0);
        itemShopValue.put("晶体元件", 30.0);
        itemShopValue.put("半自然溶剂", 40.0);
        itemShopValue.put("化合切削液", 40.0);
        itemShopValue.put("转质盐组", 40.0);

        itemShopValue.put("固源岩", (itemShopValue.get("固源岩组") + workShopValue_t2) / 5);
        itemShopValue.put("糖", (itemShopValue.get("糖组") + workShopValue_t2) / 4);
        itemShopValue.put("聚酸酯", (itemShopValue.get("聚酸酯组") + workShopValue_t2) / 4);
        itemShopValue.put("异铁", (itemShopValue.get("异铁组") + workShopValue_t2) / 4);
        itemShopValue.put("酮凝集", (itemShopValue.get("酮凝集组") + workShopValue_t2) / 4);
        itemShopValue.put("装置", (itemShopValue.get("全新装置") + workShopValue_t2) / 4);

        itemShopValue.put("源岩", (itemShopValue.get("固源岩") + workShopValue_t1) / 3);
        itemShopValue.put("代糖", (itemShopValue.get("糖") + workShopValue_t1) / 3);
        itemShopValue.put("酯原料", (itemShopValue.get("聚酸酯") + workShopValue_t1) / 3);
        itemShopValue.put("异铁碎片", (itemShopValue.get("异铁") + workShopValue_t1) / 3);
        itemShopValue.put("双酮", (itemShopValue.get("酮凝集") + workShopValue_t1) / 3);
        itemShopValue.put("破损装置", (itemShopValue.get("装置") + workShopValue_t1) / 3);

        itemShopValue.put("白马醇", itemShopValue.get("糖组") * 1 + itemShopValue.get("扭转醇") + itemShopValue.get("RMA70-12") - workShopValue_t3);
        itemShopValue.put("三水锰矿", itemShopValue.get("轻锰矿") * 2 + itemShopValue.get("扭转醇") + itemShopValue.get("聚酸酯组") - workShopValue_t3);
        itemShopValue.put("五水研磨石", itemShopValue.get("研磨石") * 1 + itemShopValue.get("异铁组") + itemShopValue.get("全新装置") - workShopValue_t3);
        itemShopValue.put("RMA70-24", itemShopValue.get("固源岩组") * 2 + itemShopValue.get("RMA70-12") + itemShopValue.get("酮凝集组") - workShopValue_t3);
        itemShopValue.put("改量装置", itemShopValue.get("固源岩组") * 2 + itemShopValue.get("全新装置") + itemShopValue.get("研磨石") - workShopValue_t3);
        itemShopValue.put("聚酸酯块", itemShopValue.get("聚酸酯组") * 2 + itemShopValue.get("酮凝集组") + itemShopValue.get("扭转醇") - workShopValue_t3);
        itemShopValue.put("糖聚块", itemShopValue.get("糖组") * 2 + itemShopValue.get("异铁组") + itemShopValue.get("轻锰矿") - workShopValue_t3);
        itemShopValue.put("异铁块", itemShopValue.get("异铁组") * 2 + itemShopValue.get("全新装置") + itemShopValue.get("聚酸酯组") - workShopValue_t3);
        itemShopValue.put("酮阵列", itemShopValue.get("酮凝集组") * 2 + itemShopValue.get("糖组") + itemShopValue.get("轻锰矿") - workShopValue_t3);
        itemShopValue.put("聚合凝胶", itemShopValue.get("异铁组") * 1 + itemShopValue.get("凝胶") + itemShopValue.get("炽合金") - workShopValue_t3);
        itemShopValue.put("炽合金块", itemShopValue.get("炽合金") * 1 + itemShopValue.get("全新装置") + itemShopValue.get("研磨石") - workShopValue_t3);
        itemShopValue.put("晶体电路", itemShopValue.get("晶体元件") * 2 + itemShopValue.get("凝胶") + itemShopValue.get("炽合金") - workShopValue_t3);
        itemShopValue.put("精炼溶剂", itemShopValue.get("半自然溶剂") * 1 + itemShopValue.get("化合切削液") + itemShopValue.get("凝胶") - workShopValue_t3);
        itemShopValue.put("切削原液", itemShopValue.get("化合切削液") * 1 + itemShopValue.get("晶体元件") + itemShopValue.get("凝胶") - workShopValue_t3);
        itemShopValue.put("提纯源岩", itemShopValue.get("固源岩组") * 4 - workShopValue_t3);
        itemShopValue.put("转质盐聚块", itemShopValue.get("半自然溶剂") * 1 + itemShopValue.get("糖组") + itemShopValue.get("转质盐组") - workShopValue_t3);

        itemShopValue.put("D32钢", itemShopValue.get("三水锰矿") * 2 + itemShopValue.get("五水研磨石") + itemShopValue.get("RMA70-24") - workShopValue_t4);
        itemShopValue.put("双极纳米片", itemShopValue.get("白马醇") * 1 + itemShopValue.get("白马醇") + itemShopValue.get("改量装置") - workShopValue_t4);
        itemShopValue.put("聚合剂", itemShopValue.get("提纯源岩") * 1 + itemShopValue.get("异铁块") + itemShopValue.get("酮阵列") - workShopValue_t4);
        itemShopValue.put("晶体电子单元", itemShopValue.get("聚合凝胶") * 2 + itemShopValue.get("炽合金块") + itemShopValue.get("晶体电路") - workShopValue_t4);
        itemShopValue.put("烧结核凝晶", itemShopValue.get("转质盐聚块") * 2 + itemShopValue.get("精炼溶剂") + itemShopValue.get("切削原液") - workShopValue_t4);

        List<Item> itemShopList = new ArrayList<>();

        for (String[] str : itemRaw) {
            if (itemShopValue.get(str[1]) != null) {
                Item itemResult = new Item();
                itemResult.setItemId(str[0]);
                itemResult.setItemName(str[1]);
                itemResult.setItemValue(itemShopValue.get(str[1]));
                itemResult.setType(str[3]);
                itemShopList.add(itemResult);
            } else {
                Item itemResult = new Item();
                itemResult.setItemId(str[0]);
                itemResult.setItemName(str[1]);
                itemResult.setItemValue(Double.valueOf(str[2]));
                itemResult.setType(str[3]);
                itemShopList.add(itemResult);
            }
        }

        itemDao.deleteAll();

        itemDao.saveAll(itemShopList);
    }

    @Override
    public void resetItemReviseTable() {
        itemReviseDao.deleteAll(); //清空表
    }


    @Override
    public List<Item> findAll() {
        return itemDao.findByOrderByItemIdAsc();
    }

    @Override
    public List<ItemRevise> findAllItemRevise(String version) {
        List<ItemRevise> all = itemReviseDao.findByVersion(version);

        return all;
    }


    /**
     * 用于计算等效理智价值
     * @param hashMap map为<材料名,1.25/材料的最优常驻关卡的效率>
     * @return
     */
    @Override
    public List<ItemRevise> itemRevise(HashMap<String, Double> hashMap,Double version,String dataType,Integer index) {
//        for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
//            log.info(entry.getKey() + ": " + entry.getValue());
//        }
//        拿到上一次临时计算的材料等效价值
        List<Item> itemTemporaryDataList = itemDao.findAll();
//        list转为map方便调用
        HashMap<String, Double> itemTemporaryMap = new HashMap<>();
        for (Item item : itemTemporaryDataList) {
            itemTemporaryMap.put(item.getItemName(), item.getItemValue());
        }

        //拿到物品表的初始信息
        String[][] itemRaw = getItemInfo(version);

        //加工站的期望产出值近乎无波动，目前用固定值替代，（大概可能也许没准有空改）
        double workShopValue_t1 = 0.513182485 - 0.45;
        double workShopValue_t2 = 1.538558598 - 0.9;
        double workShopValue_t3 = 6.937 - 1.35;
        double workShopValue_t4 = 23.4191630 - 1.8;


        String[] item_t3List = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12", "炽合金", "研磨石", "糖组",
                "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液","转质盐组"};


        HashMap<String, Double> itemValue = new HashMap<>();


        for (String item_t3 : item_t3List) {
            //  材料上次迭代价值*1.25/关卡效率
            //map中保存的是：    （材料名称，map的value（即1.25/关卡效率）*上次计算后的价值）
            itemValue.put(item_t3, hashMap.get(item_t3) * itemTemporaryMap.get(item_t3));
        }


        itemValue.put("固源岩", (itemValue.get("固源岩组") + workShopValue_t2) / 5);
        itemValue.put("糖", (itemValue.get("糖组") + workShopValue_t2) / 4);
        itemValue.put("聚酸酯", (itemValue.get("聚酸酯组") + workShopValue_t2) / 4);
        itemValue.put("异铁", (itemValue.get("异铁组") + workShopValue_t2) / 4);
        itemValue.put("酮凝集", (itemValue.get("酮凝集组") + workShopValue_t2) / 4);
        itemValue.put("装置", (itemValue.get("全新装置") + workShopValue_t2) / 4);

        itemValue.put("源岩", (itemValue.get("固源岩") + workShopValue_t1) / 3);
        itemValue.put("代糖", (itemValue.get("糖") + workShopValue_t1) / 3);
        itemValue.put("酯原料", (itemValue.get("聚酸酯") + workShopValue_t1) / 3);
        itemValue.put("异铁碎片", (itemValue.get("异铁") + workShopValue_t1) / 3);
        itemValue.put("双酮", (itemValue.get("酮凝集") + workShopValue_t1) / 3);
        itemValue.put("破损装置", (itemValue.get("装置") + workShopValue_t1) / 3);

        itemValue.put("白马醇", itemValue.get("糖组") * 1 + itemValue.get("扭转醇") + itemValue.get("RMA70-12") - workShopValue_t3);
        itemValue.put("三水锰矿", itemValue.get("轻锰矿") * 2 + itemValue.get("扭转醇") + itemValue.get("聚酸酯组") - workShopValue_t3);
        itemValue.put("五水研磨石", itemValue.get("研磨石") * 1 + itemValue.get("异铁组") + itemValue.get("全新装置") - workShopValue_t3);
        itemValue.put("RMA70-24", itemValue.get("固源岩组") * 2 + itemValue.get("RMA70-12") + itemValue.get("酮凝集组") - workShopValue_t3);
        itemValue.put("改量装置", itemValue.get("固源岩组") * 2 + itemValue.get("全新装置") + itemValue.get("研磨石") - workShopValue_t3);
        itemValue.put("聚酸酯块", itemValue.get("聚酸酯组") * 2 + itemValue.get("酮凝集组") + itemValue.get("扭转醇") - workShopValue_t3);
        itemValue.put("糖聚块", itemValue.get("糖组") * 2 + itemValue.get("异铁组") + itemValue.get("轻锰矿") - workShopValue_t3);
        itemValue.put("异铁块", itemValue.get("异铁组") * 2 + itemValue.get("全新装置") + itemValue.get("聚酸酯组") - workShopValue_t3);
        itemValue.put("酮阵列", itemValue.get("酮凝集组") * 2 + itemValue.get("糖组") + itemValue.get("轻锰矿") - workShopValue_t3);
        itemValue.put("聚合凝胶", itemValue.get("异铁组") * 1 + itemValue.get("凝胶") + itemValue.get("炽合金") - workShopValue_t3);
        itemValue.put("炽合金块", itemValue.get("炽合金") * 1 + itemValue.get("全新装置") + itemValue.get("研磨石") - workShopValue_t3);
        itemValue.put("晶体电路", itemValue.get("晶体元件") * 2 + itemValue.get("凝胶") + itemValue.get("炽合金") - workShopValue_t3);
        itemValue.put("精炼溶剂", itemValue.get("半自然溶剂") * 1 + itemValue.get("化合切削液") + itemValue.get("凝胶") - workShopValue_t3);
        itemValue.put("切削原液", itemValue.get("化合切削液") * 1 + itemValue.get("晶体元件") + itemValue.get("凝胶") - workShopValue_t3);
        itemValue.put("提纯源岩", itemValue.get("固源岩组") * 4 - workShopValue_t3);
        itemValue.put("转质盐聚块", itemValue.get("半自然溶剂") * 1 + itemValue.get("糖组") + itemValue.get("转质盐组") - workShopValue_t3);

        itemValue.put("D32钢", itemValue.get("三水锰矿") + itemValue.get("五水研磨石") + itemValue.get("RMA70-24") - workShopValue_t4);
        itemValue.put("双极纳米片", itemValue.get("白马醇") + itemValue.get("白马醇") + itemValue.get("改量装置") - workShopValue_t4);
        itemValue.put("聚合剂", itemValue.get("提纯源岩") * 1 + itemValue.get("异铁块") + itemValue.get("酮阵列") - workShopValue_t4);
        itemValue.put("晶体电子单元", itemValue.get("聚合凝胶") * 2 + itemValue.get("炽合金块") + itemValue.get("晶体电路") - workShopValue_t4);
        itemValue.put("烧结核凝晶", itemValue.get("转质盐聚块")  + itemValue.get("精炼溶剂")*2 + itemValue.get("切削原液") - workShopValue_t4);

//        保存入最终材料等效价值表的材料等效价值集合
        List<ItemRevise> itemReviseList = new ArrayList<>();
//        保存入临时材料等效价值表的材料等效价值集合
        List<Item> itemTemporaryList = new ArrayList<>();


        Long id = 0L;

        if(version ==0.0)    id = 1000L;
        if(version ==0.76)   id = 2000L;
        if(version ==1.0)    id = 3000L;
        if(version ==0.625)  id = 4000L;

        if("auto".equals(dataType)) id = id*10;

//        将上面计算后的价值存入俩个集合
        for (String[] str : itemRaw) {
            if (itemValue.get(str[1]) != null) {
                ItemRevise itemResult = new ItemRevise();
                itemResult.setId(id);
                itemResult.setItemId(str[0]);
                itemResult.setItemName(str[1]);
                itemResult.setItemValue(itemValue.get(str[1]));
                itemResult.setType(str[3]);
                itemResult.setCardNum(str[4]);
                itemResult.setVersion(dataType+version);
                itemReviseList.add(itemResult);

                Item item = new Item();
                item.setItemId(str[0]);
                item.setItemName(str[1]);
                item.setItemValue(itemValue.get(str[1]));
                itemTemporaryList.add(item);
            } else {
                ItemRevise itemResult = new ItemRevise();
                itemResult.setId(id);
                itemResult.setItemId(str[0]);
                itemResult.setItemName(str[1]);
                itemResult.setItemValue(Double.valueOf(str[2]));
                itemResult.setType(str[3]);
                itemResult.setCardNum(str[4]);
                itemResult.setVersion(dataType+version);
                itemReviseList.add(itemResult);

                Item item = new Item();
                item.setItemId(str[0]);
                item.setItemName(str[1]);
                item.setItemValue(Double.valueOf(str[2]));
                itemTemporaryList.add(item);
            }
            id++;
        }


        itemDao.deleteAll();  //清空表
        itemDao.saveAll(itemTemporaryList); //存入临时表


        if(index==0) itemReviseDao.saveAll(itemReviseList);  //存入最终表


        return itemReviseList;
    }


    /**
     * //    导出物品价值表，但是用前端专用导出类先转一次
     *
     * @param response
     */
    @Override
    public void exportItemDataToExcel(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 easyExcel没有关系
            String fileName = URLEncoder.encode("itemValue", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<ItemRevise> list = itemReviseDao.findAll();
            DecimalFormat DF3 = new DecimalFormat("0.0000");

            ArrayList<ItemValueVo> valueVoArrayList = new ArrayList<>();
            for (ItemRevise itemRevise : list) {
//                if("31063".equals(itemRevise.getItemId())||"31064".equals(itemRevise.getItemId())||"30155".equals(itemRevise.getItemId())){
//                    continue;
//                }
                ItemValueVo itemValueVo = new ItemValueVo();
                itemValueVo.setItemName(itemRevise.getItemName());
                itemValueVo.setItemValueReason(Double.valueOf(DF3.format(itemRevise.getItemValue() / 1.25)));
                itemValueVo.setItemValueGreen(itemRevise.getItemValue());
                itemValueVo.setItemId(itemRevise.getItemId());
                itemValueVo.setItemType(itemRevise.getType());
                itemValueVo.setVersion(itemRevise.getVersion());
                valueVoArrayList.add(itemValueVo);
            }
            EasyExcel.write(response.getOutputStream(), ItemValueVo.class).sheet("Sheet1").doWrite(valueVoArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportItemDataToJson(HttpServletResponse response) {
        List<ItemRevise> list = itemReviseDao.findAll();
        DecimalFormat DF3 = new DecimalFormat("0.0000");

        ArrayList<ItemValueVo> valueVoArrayList = new ArrayList<>();
        for (ItemRevise itemRevise : list) {
//            if("31063".equals(itemRevise.getItemId())||"31064".equals(itemRevise.getItemId())||"30155".equals(itemRevise.getItemId())){
//                continue;
//            }
            ItemValueVo itemValueVo = new ItemValueVo();
            itemValueVo.setItemName(itemRevise.getItemName());
            itemValueVo.setItemValueReason(Double.valueOf(DF3.format(itemRevise.getItemValue() / 1.25)));
            itemValueVo.setItemValueGreen(itemRevise.getItemValue());
            itemValueVo.setItemId(itemRevise.getItemId());
            itemValueVo.setItemType(itemRevise.getType());
            itemValueVo.setVersion(itemRevise.getVersion());
            valueVoArrayList.add(itemValueVo);
        }



        String jsonForMat = JSON.toJSONString(valueVoArrayList, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty);

        CreateJsonFile.createJsonFile(response,frontEndFilePath,"itemValue",jsonForMat);


    }
    /**
     * 物品表的初始信息，理论上可以用json，但是我懒得改了
     *
     * @return
     */
    private static String[][] getItemInfo(Double version) {
                   String  exp1 = String.valueOf(0.9*version);
                   String  exp2 = String.valueOf(1.8*version);
                   String  exp3 = String.valueOf(4.5*version);
                   String  exp4 = String.valueOf(9*version);

        return new String[][]{
                {"30135", "D32钢", "", "orange", "1"}, {"30125", "双极纳米片", "", "orange", "1"},
                {"30115", "聚合剂", "", "orange", "1"}, {"30145", "晶体电子单元", "", "orange", "1"},
                {"30155", "烧结核凝晶", "", "orange", "1"},

                {"30064", "改量装置", "", "purple", "2"}, {"30044", "异铁块", "", "purple", "2"},
                {"30084", "三水锰矿", "", "purple", "2"}, {"31014", "聚合凝胶", "", "purple", "2"},
                {"30074", "白马醇", "", "purple", "2"}, {"30054", "酮阵列", "", "purple", "2"},
                {"30104", "RMA70-24", "", "purple", "2"}, {"31024", "炽合金块", "", "purple", "2"},
                {"30094", "五水研磨石", "", "purple", "2"}, {"30024", "糖聚块", "", "purple", "2"},
                {"30034", "聚酸酯块", "", "purple", "2"}, {"31034", "晶体电路", "", "purple", "2"},
                {"30014", "提纯源岩", "", "purple", "2"}, {"31044", "精炼溶剂", "", "purple", "2"},
                {"31054", "切削原液", "", "purple", "2"},{"31064", "转质盐聚块", "", "purple", "2"},

                {"30063", "全新装置", "", "blue", "3"}, {"30043", "异铁组", "", "blue", "3"},
                {"30083", "轻锰矿", "", "blue", "3"}, {"31013", "凝胶", "", "blue", "3"},
                {"30073", "扭转醇", "", "blue", "3"}, {"30053", "酮凝集组", "", "blue", "3"},
                {"30103", "RMA70-12", "", "blue", "3"}, {"31023", "炽合金", "", "blue", "3"},
                {"30093", "研磨石", "", "blue", "3"}, {"30023", "糖组", "", "blue", "3"},
                {"30033", "聚酸酯组", "", "blue", "3"}, {"31033", "晶体元件", "", "blue", "3"},
                {"30013", "固源岩组", "", "blue", "3"}, {"31043", "半自然溶剂", "", "blue", "3"},
                {"31053", "化合切削液", "", "blue", "3"}, {"31063", "转质盐组", "", "blue", "3"},


                {"30012", "固源岩", "", "green", "4"}, {"30022", "糖", "", "green", "4"},
                {"30032", "聚酸酯", "", "green", "4"}, {"30042", "异铁", "", "green", "4"},
                {"30052", "酮凝集", "", "green", "4"}, {"30062", "装置", "", "green", "4"},
                {"30011", "源岩", "", "grey", "5"}, {"30021", "代糖", "", "grey", "5"},
                {"30031", "酯原料", "", "grey", "5"}, {"30041", "异铁碎片", "", "grey", "5"},
                {"30051", "双酮", "", "grey", "5"}, {"30061", "破损装置", "", "grey", "5"},


                {"3301", "技巧概要·卷1", "2.111", "purple", "6"}, {"3302", "技巧概要·卷2", "5.278", "blue", "6"},
                {"3303", "技巧概要·卷3", "13.196", "green", "6"}, {"2004", "高级作战记录", exp4, "orange", "6"},
                {"2003", "中级作战记录", exp3, "purple", "6"}, {"2002", "初级作战记录", exp2, "blue", "6"},
                {"2001", "基础作战记录", exp1, "green", "6"}, {"4001", "龙门币", "0.0045", "purple", "6"},

                {"3261", "医疗芯片", "17.8425", "blue", "7"}, {"3271", "辅助芯片", "21.42", "blue", "7"},
                {"3211", "先锋芯片", "21.42", "blue", "7"}, {"3281", "特种芯片", "17.8425", "blue", "7"},
                {"3221", "近卫芯片", "24.99625", "blue", "7"}, {"3231", "重装芯片", "24.99625", "blue", "7"},
                {"3241", "狙击芯片", "21.42", "blue", "7"}, {"3251", "术师芯片", "21.42", "blue", "7"},

                {"3262", "医疗芯片组", "35.685", "blue", "7"}, {"3272", "辅助芯片组", "42.84", "blue", "7"},
                {"3212", "先锋芯片组", "42.84", "blue", "7"}, {"3282", "特种芯片组", "35.685", "blue", "7"},
                {"3222", "近卫芯片组", "49.9925", "blue", "7"}, {"3232", "重装芯片组", "49.9925", "blue", "7"},
                {"3242", "狙击芯片组", "42.84", "blue", "7"}, {"3252", "术师芯片组", "42.84", "blue", "7"},

                {"4003", "合成玉", "0.9375", "orange", "8"}, {"7001", "招聘许可", "30.085", "purple", "8"},
                {"4006", "采购凭证", "1.7", "blue", "8"}, {"7003", "寻访凭证", "562.5", "orange", "8"},
                {"32001", "芯片助剂", "152.99", "purple", "8"}, {"3003", "赤金", "1.25", "purple", "8"},
                {"4002", "至纯源石", "1.25", "purple", "8"}, {"STORY_REVIEW_COIN", "事相碎片", "34", "orange", "8"},
                {"mod_unlock_token", "模组数据块", "204", "orange", "8"},{"base_ap", "无人机", "0.046875", "purple", "8"},



                {"charm_coin_1", "黄金筹码", "9", "grey", "16"}, {"charm_coin_2", "错版硬币", "12", "blue", "16"},
                {"charm_coin_3", "双日城大乐透", "18", "purple", "16"}, {"charm_coin_4", "翡翠庭院至臻", "100", "orange", "16"},
                {"charm_r1", "标志物 - 20代金券", "15", "grey", "16"}, {"charm_r2", "标志物 - 40代金券", "30", "blue", "16"},
                {"trap_oxygen_3", "沙兹专业镀膜装置", "45", "orange", "16"},

                {"ap_supply_lt_010", "应急理智小样",  "0.000001", "purple", "16"},
                {"randomMaterial_7", "罗德岛物资补给Ⅳ",  "14", "green", "16"},
        };
    }
}
