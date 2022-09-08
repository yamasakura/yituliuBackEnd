package com.lhs.bean.vo;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lhs.bean.jsonObject.JsonEfficiency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 最优效率图返回前端实体
 *
 * @author yucan
 * @date 2022/9/8 9:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EfficiencyVo {
    //更新时间
    private String updateTime;

    //材料类型
    private String type;

    //材料数据封装结果
    private List<MaterialInfoVo> result;

    /**
     * 根据JSONArry对象获取到材料信息数组
     */
    public static List<EfficiencyVo> getListByJSONArray(JSONArray jsonArray) {
        List<EfficiencyVo> resultList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
            List<JsonEfficiency> jsonEfficiencies = JSON.parseArray(jsonArray1.toJSONString(), JsonEfficiency.class);
            resultList.add(getEfficiencyVoByJsonEfficiency(jsonEfficiencies));
        }
        return resultList;
    }

    /**
     * 根据一维的jsonEfficiencies生成EfficiencyVo对象
     */
    private static EfficiencyVo getEfficiencyVoByJsonEfficiency(List<JsonEfficiency> jsonEfficiencies) {
        EfficiencyVo efficiencyVo = new EfficiencyVo();
        if (CollectionUtils.isEmpty(jsonEfficiencies)) {
            return null;
        }
        efficiencyVo.setUpdateTime(jsonEfficiencies.get(0).getUpdateDate());
        efficiencyVo.setType(jsonEfficiencies.get(0).getType());
        efficiencyVo.setResult(getResultByJsonEfficiency(jsonEfficiencies));
        return efficiencyVo;
    }

    /**
     * 提取jsonEfficiencies的result信息
     */
    public static List<MaterialInfoVo> getResultByJsonEfficiency(List<JsonEfficiency> jsonEfficiencies) {
        List<MaterialInfoVo> materialInfoVos = new ArrayList<>();
        for (JsonEfficiency jsonEfficiency : jsonEfficiencies) {
            materialInfoVos.add(new MaterialInfoVo(jsonEfficiency));
        }
        return materialInfoVos;
    }
}
