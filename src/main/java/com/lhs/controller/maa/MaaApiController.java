package com.lhs.controller.maa;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.bean.DBPogo.MaaTagData;
import com.lhs.bean.vo.MaaTagDataVo;
import com.lhs.bean.pojo.MaaTagRequestVo;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.ReadJsonUtil;
import com.lhs.common.util.Result;
import com.lhs.common.util.ResultCode;
import com.lhs.service.MaaApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            MaaTagDataVo maaTagDataVO = new MaaTagDataVo();
            maaTagDataVO.setTag1(maaTagData.getTag1());
            maaTagDataVO.setTag2(maaTagData.getTag2());
            maaTagDataVO.setTag3(maaTagData.getTag3());
            maaTagDataVO.setTag4(maaTagData.getTag4());
            maaTagDataVO.setTag5(maaTagData.getTag5());
            maaTagDataVO.setLevel(maaTagData.getLevel());
            maaTagDataVO.setServer(maaTagData.getServer());
            maaTagDataVO.setUid(maaTagData.getUid());
            maaTagDataVO.setCreateTime(maaTagData.getCreateTime());
            maaTagDataVoList.add(maaTagDataVO);
        }
        return Result.success(maaTagDataVoList);
    }


    @ApiOperation("各类公招统计结果")
    @GetMapping("/recruit/statistical")
    public Result MaaTagResultStatistical() {
        String result = maaApiService.getMaaTagDataStatistical();
        JSONObject jsonObject = JSONObject.parseObject(result);
        return Result.success(jsonObject);
    }

    @ApiOperation("查询干员专精或进阶材料")
    @GetMapping("/itemCost/{name}")
    public Result MaaTagResultStatistical(@PathVariable("name") String name) {

        String json = ReadJsonUtil.readJson("//springboot1//bot//itemCost.json");
        JSONObject jsonObject = JSONObject.parseObject(json);
        if(jsonObject.get(name)==null){
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        Object byName = jsonObject.get(name);

        return Result.success(byName);
    }


}
