package com.lhs.controller.yituliu;


import com.alibaba.fastjson.JSONArray;
import com.lhs.bean.DBPogo.ItemRevise;
import com.lhs.bean.DBPogo.StoreCostPer;
import com.lhs.bean.vo.EfficiencyVo;
import com.lhs.bean.vo.RecResultVo;
import com.lhs.bean.vo.StageResultApiVo;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.common.util.Result;
import com.lhs.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "获取数据API")
@RequestMapping(value = "/api")
@CrossOrigin(maxAge = 86400)
public class ApiController {


    @Autowired
    private ApiService apiService;

    @Autowired
    private StoreCostPerService storeCostPerService;

    @Autowired
    private CharTagDataService charTagDataService;

    @Autowired
    private ItemService itemService;

    @ApiOperation("获取蓝材料最优图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "times", value = "样本数默认值500", required = true, paramType = "path", defaultValue = "500"),
            @ApiImplicitParam(name = "efficiency", value = "效率最小值", required = true, paramType = "path", defaultValue = "1.0")})
    @GetMapping("/find/stage/t3/{times}/{efficiency}")
    public Result findByTypeAndEndsOrderByEfficiencyDesc(@PathVariable("times") Integer times,
                                                         @PathVariable("efficiency") Double efficiency) {
        String stageFileT3 = apiService.readStageFileT3();
        JSONArray jsonArray = JSONArray.parseArray(stageFileT3);
        List<EfficiencyVo> resultList = EfficiencyVo.getListByJSONArray(jsonArray);
        return Result.success(resultList);
    }


    @ApiOperation("获取绿材料最优图")
    @GetMapping("/find/stage/t2")
    public Result findByMainOrderByExpectAsc() {
        String stageFileT2 = apiService.readStageFileT2();
        JSONArray jsonArray = JSONArray.parseArray(stageFileT2);
        return Result.success(jsonArray);
    }


    @ApiOperation("获取活动商店性价比")
    @GetMapping("/find/store/act")
    public Result findStoreAct() {
        String string = storeCostPerService.readActStoreJson();
        JSONArray jsonArray = JSONArray.parseArray(string);
        return Result.success(jsonArray);
    }


    @ApiOperation("获取常驻商店性价比")
    @GetMapping("/find/store/perm")
    public Result findAll() {

        String storeFile = storeCostPerService.readPermStoreJson();
        JSONArray jsonArray = JSONArray.parseArray(storeFile);
        return Result.success(jsonArray);
    }


    @ApiOperation("获取物品价值")
    @GetMapping("/find/item/value")
    public Result findAllItem() {
        List<ItemRevise> all = itemService.findAllItemRevise();
        return Result.success(all);
    }


    @ApiOperation("获取全部关卡效率")
    @GetMapping("/find/stage/all")
    public Result findAllStageEff() {
        List<StageResultVo> all = apiService.findByMainNotNull();
        return Result.success(all);
    }


    @ApiOperation("获取往期活动地图效率")
    @GetMapping("/find/stage/activity/closed")
    public Result findAllByStageId() {
        String StageClosedFile = apiService.readStageClosedFile();
        JSONArray jsonArray = JSONArray.parseArray(StageClosedFile);
        return Result.success(jsonArray);
    }


    @ApiOperation(value = "按照商店类型获取")
    @GetMapping("/find/store/{type}")
    public Result findStoreType(@PathVariable("type") String type) {
        List<StoreCostPer> all = storeCostPerService.findAll(type);
        return Result.success(all);
    }


    @ApiOperation("公招计算")
    @GetMapping("/find/recruit/{type}/{tags}/{rarityMax}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "是否可招募", required = true, paramType = "path", defaultValue = "1"),
            @ApiImplicitParam(name = "tags", value = "TAG集合", required = true, paramType = "path"),
            @ApiImplicitParam(name = "rarityMax", value = "最高星级", required = true, paramType = "path", defaultValue = "5")})
    public Result findAllRecruitmentInfoDataByType(@PathVariable("type") Integer type,
                                                   @PathVariable("tags") String[] tags,
                                                   @PathVariable("rarityMax") Integer rarityMax) {
        List<RecResultVo> resultListVo = charTagDataService.findAllByTypeAndRarityNew(type, tags, rarityMax);
        return Result.success(resultListVo);
    }


    @ApiOperation("关卡效率API（app用）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "times", value = "样本数默认值500", required = true, paramType = "path", defaultValue = "500"),
            @ApiImplicitParam(name = "efficiency", value = "效率最小值", required = true, paramType = "path", defaultValue = "1.0")})
    @GetMapping("/app/{times}/{efficiency}")

    public Result findEfficiencyDescAPI(@PathVariable("times") Integer times,
                                        @PathVariable("efficiency") Double efficiency) {
        List<List<StageResultApiVo>> byTimesApi = apiService.getDataByEffAndTimesOrderByEffDescAppApi(times, efficiency);
        return Result.success(byTimesApi);
    }


}
