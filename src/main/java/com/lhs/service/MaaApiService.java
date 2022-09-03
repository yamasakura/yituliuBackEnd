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
    //统计公招各项个数出率
    String maaTagDataCalculation();
    //读取公招统计结果
    String getMaaTagDataStatistical();

    //保存公招统计结果
    String saveStatistical();

}
