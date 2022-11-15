package com.lhs;

import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.DBPogo.MaaTagData;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.dao.MaaTagResultDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)   //这两个注解是为了让测试类能拥有同等的spring boot上下文环境
@SpringBootTest
public class Maa {

    @Autowired
    private MaaTagResultDao maaTagResultDao;

    @Test
    public  void maaCalculation() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long createTime = new Date().getTime();

        List<MaaTagData> list = maaTagResultDao.findByCreateTimeIsLessThan(new Date(1662220800000L));
//        List<MaaTagData> list = maaTagResultDao.findAll();
        System.out.println(list.size()+"条数据");
        System.out.println("第一条"+list.get(0).getCreateTime()+"——最后一条"+list.get(list.size()-1).getCreateTime());
//        for(MaaTagData maaTagData:list){
//            System.out.println(maaTagData.getCreateTime());
//        }

    }

    @Test
    public  void itemCost() {
        String json = ReadFileUtil.readFile("C:\\Users\\李会山\\Desktop\\maa\\skillsMap.json");
        JSONObject jsonObject = JSONObject.parseObject(json);
        Object byName = jsonObject.get("银灰");
        JSONObject byNameJson = JSONObject.parseObject(byName.toString());
        Object o = byNameJson.get("allSkill");
        System.out.println(o);
    }


}
