package com.lhs.service;

import com.lhs.bean.DBPogo.MaaTagData;
import com.lhs.bean.DBPogo.MaaTagDataStatistical;
import com.lhs.bean.vo.MaaTagRequestVo;

import java.util.HashMap;
import java.util.List;

public interface MaaApiService {

    //保存maa公招数据
    String maaTagResultSave(MaaTagRequestVo maaTagRequestVo);
    //拿到最新十条收录数据
    List<MaaTagData> selectDataLimit10();
    //拿到所有数据
    List<MaaTagData>  findAllMaaTagData();
    //计算各项公招结果
    HashMap<String,Object> maaTagDataCalculation();
    //计算拿到计算完的结果
    MaaTagDataStatistical getMaaTagDataStatistical();

}
