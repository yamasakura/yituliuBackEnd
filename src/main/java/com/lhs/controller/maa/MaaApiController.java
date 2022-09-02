package com.lhs.controller.maa;


import com.alibaba.fastjson.JSONArray;
import com.lhs.bean.DBPogo.MaaTagData;
import com.lhs.bean.DBPogo.MaaTagDataStatistical;
import com.lhs.bean.vo.MaaTagDataVo;
import com.lhs.bean.vo.MaaTagRequestVo;
import com.lhs.common.util.Result;
import com.lhs.service.MaaApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "MAA接口")
@RequestMapping(value = "/tool")
@CrossOrigin(maxAge = 86400)
public class MaaApiController {

    @Autowired
    private MaaApiService maaApiService;



    @ApiOperation("MAA公招记录")
    @PostMapping("/recruitUpload")
    public Result MaaTagResult(@RequestBody MaaTagRequestVo maaTagRequestVo) {
        String string = maaApiService.maaTagResultSave(maaTagRequestVo);
        return Result.success(string);
    }

    @ApiOperation("查看最近10条MAA公招记录")
    @GetMapping("/recruit/limit10")
    public Result MaaTagResultLimit10() {

        List<MaaTagData> maaTagDataList = maaApiService.selectDataLimit10();
        List<MaaTagDataVo> maaTagDataVoList = new ArrayList<>();
        for(MaaTagData maaTagData:maaTagDataList){
            JSONArray jsonArray  = JSONArray.parseArray(maaTagData.getTagResult());
            MaaTagDataVo maaTagDataVO = new MaaTagDataVo();
            maaTagDataVO.setTag1(maaTagData.getTag1());
            maaTagDataVO.setTag2(maaTagData.getTag2());
            maaTagDataVO.setTag3(maaTagData.getTag3());
            maaTagDataVO.setTag4(maaTagData.getTag4());
            maaTagDataVO.setTag5(maaTagData.getTag5());
            maaTagDataVO.setLevel(maaTagData.getLevel());
            maaTagDataVO.setTagResult(jsonArray);
            maaTagDataVO.setServer(maaTagData.getServer());
            maaTagDataVO.setCreateTime(maaTagData.getCreateTime());
            maaTagDataVoList.add(maaTagDataVO);
        }
        return Result.success(maaTagDataVoList);
    }




    @ApiOperation("各类公招统计结果")
    @GetMapping("/recruit/statistical")
    public Result MaaTagResultStatistical() {
        MaaTagDataStatistical maaTagDataStatistical = maaApiService.getMaaTagDataStatistical();
        return Result.success(maaTagDataStatistical);
    }


}
