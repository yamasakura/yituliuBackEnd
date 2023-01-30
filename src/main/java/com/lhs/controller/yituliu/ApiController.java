package com.lhs.controller.yituliu;


import com.alibaba.fastjson.JSONArray;
import com.lhs.bean.DBPogo.ItemRevise;
import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.DBPogo.StoreCostPer;
import com.lhs.bean.vo.RecResultVo;
import com.lhs.bot.QqRobotService;
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
    private QqRobotService robotService;

    @Autowired
    private ItemService itemService;


    @ApiOperation("获取蓝材料最优图")
    @ApiImplicitParam(name = "times", value = "样本数默认值500", required = true, paramType = "path", defaultValue = "500")
    @GetMapping("/find/stage/t3/{times}/{version}")
    public Result findByTypeAndEndsOrderByEfficiencyDesc(@PathVariable("times") Integer times,@PathVariable("version") String version) {
        String stageFileT3 = apiService.readStageFileT3(version);
        JSONArray jsonArray = JSONArray.parseArray(stageFileT3);
        return Result.success(jsonArray);
    }


    @ApiOperation("获取绿材料最优图")
    @GetMapping("/find/stage/t2/{version}")
    public Result findByMainOrderByExpectAsc(@PathVariable("version") String version) {
        String stageFileT2 = apiService.readStageFileT2(version);
        JSONArray jsonArray = JSONArray.parseArray(stageFileT2);
        return Result.success(jsonArray);
    }

    @ApiOperation("获取搓玉最优图")
    @GetMapping("/find/stage/orundum/{version}")
    public Result findByStageCode(@PathVariable("version") String version) {
        String stageFileOrundum = apiService.readStageFileOrundum(version);
        JSONArray jsonArray = JSONArray.parseArray(stageFileOrundum);
//        String[] stageCode  =new String[]{"main_01-07","act13side_06_rep","act13side_07_rep"};
//        List<StageOrundumVo> list = stageResultSetInfoService.setOrundumEfficiency();
        return Result.success(jsonArray);
    }


    @ApiOperation("获取动态")
    @GetMapping("/get/space/bili/{group_ids}")
    public Result getBiliSpace(@PathVariable("group_ids") Long[] group_ids) {

        robotService.wbSendByPhone(group_ids);

     return Result.success("发送成功");
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
    public Result findStoreByPerm() {

        String storeFile = storeCostPerService.readPermStoreJson();
        JSONArray jsonArray = JSONArray.parseArray(storeFile);
        return Result.success(jsonArray);
    }

    @ApiOperation("获取商店礼包性价比")
    @GetMapping("/find/store/pack")
    public Result findStoreByPack() {

        String storeFile = storeCostPerService.readPackStoreJson();
        JSONArray jsonArray = JSONArray.parseArray(storeFile);
        return Result.success(jsonArray);
    }

    @ApiOperation("获取物品价值")
    @GetMapping("/find/item/value/{version}")
    public Result findAllItem(@PathVariable("version") String version) {
        List<ItemRevise> all = itemService.findAllItemRevise(version);
        return Result.success(all);
    }


    @ApiOperation("获取全部关卡效率")
    @GetMapping("/find/stage/all")
    public Result findStageDataAll() {
        List<StageResultData> all = apiService.findByMainNotNull();
        return Result.success(all);
    }


    @ApiOperation("获取往期活动地图效率")
    @GetMapping("/find/stage/activity/closed/{version}")
    public Result findStageDataByStageId(@PathVariable("version") String version) {
        String StageClosedFile = apiService.readStageClosedFile(version);
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





}