package com.lhs.controller.yituliu;


import com.alibaba.fastjson.JSON;
import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.DBPogo.StoreCostPer;
import com.lhs.bean.vo.StageOrundumVo;
import com.lhs.bean.vo.StageResultVo;
import com.lhs.common.util.HttpRequestUtil;
import com.lhs.common.util.Result;
import com.lhs.common.util.SaveFile;
import com.lhs.dao.StageResultVoApiDao;
import com.lhs.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(tags = "文件导出导入API")
@CrossOrigin
@RequestMapping(value = "/save")
public class SaveController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private StageResultSetInfoService stageResultSetInfoService;

    @Value("${penguin.path}")
    private  String penguinFilePath ;

    @Value("${frontEnd.path}")
    private  String frontEndFilePath ;

    @Autowired
    private StoreCostPerService storeCostPerService;

    @Autowired
    private StageResultCalcService stageResultCalcService;

    @Autowired
    private StageResultVoApiDao stageResultVoApiDao;


    @ApiOperation("导出物品价值表(Excel)")
    @GetMapping("/export/item/value/excel")
    public void exportItemDataToExcel(HttpServletResponse response) {
        itemService.exportItemDataToExcel(response);
    }

    @ApiOperation("导出物品价值表(json)")
    @GetMapping("/export/item/value/json")
    public void exportItemDataToJson(HttpServletResponse response) {
        itemService.exportItemDataToJson(response);
    }

    @ApiOperation("保存企鹅物流数据")
    @GetMapping("/save/PenguinsData/{dateType}")
    @ApiImplicitParam(name="dateType",value = "数据源",dataType = "String",paramType = "path",defaultValue="all",required = false)
    public Result savePenguinsData(@PathVariable("dateType") String dateType) {
        String url = "";
        if("auto".equals(dateType)){
            url = "https://penguin-stats.io/PenguinStats/api/v2/_private/result/matrix/CN/global/automated";
        }else {
            url = "https://penguin-stats.io/PenguinStats/api/v2/result/matrix?show_closed_zones=true";
        }
        String con = HttpRequestUtil.doGet(url);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
        String saveTime = simpleDateFormat.format(new Date());
        SaveFile.save(penguinFilePath, "matrix_"+dateType + saveTime + ".json", con);
        File file = new File(penguinFilePath + "matrix_"+dateType + saveTime + ".json");
        HashMap<Object, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("企鹅文件", file.exists());
        hashMap.put("文件大小", file.length());
        return Result.success();
    }


    @ApiOperation("保存关卡数据文件")
    @GetMapping("/save/stage/result/{stageState}/{times}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="stageState",value = "关卡类型",dataType = "Integer",paramType = "path",defaultValue="0",required = false),
            @ApiImplicitParam(name="times",value = "样本量",dataType = "Integer",paramType = "path",defaultValue="300",required = false)})
    public Result saveStageData(@PathVariable("stageState") Integer stageState,@PathVariable("times") Integer times) {

        Double[] versionList = new Double[]{1.0,0.76,0.0,0.625};
        String[] dataTypeList = new String[]{"all", "auto"};
        HashMap<Object, Object> hashMap = new LinkedHashMap<>();
        for (int dt = 0; dt < dataTypeList.length; dt++) {
            for (int v = 0; v < versionList.length; v++) {
                String[] actNameList = new String[]{
                        "act_side20", "act_side19",
                        "act_side18", "act_side17", "act_side16", "act_side15", "act_side14", "act_side13",
                        "act_side12_rep_", "act_side11", "act_side10", "act_side9", "act_side8",
                        "act_side7", "act_side6", "act_side5", "act_side4", "act_side3", "act_side0"
                };
                double version = versionList[v];
                String versionCode = "000";

                if (version == 0.76) versionCode = "076";
                if (version == 1.0) versionCode = "100";
                if (version == 0.625) versionCode = "062";


                List<List<StageResultData>> pageListT3 = stageResultSetInfoService.setStageResultPercentageT3(times, 1.0, stageState, version, dataTypeList[dt]);

                List<List<StageResultData>> pageListT2 = stageResultSetInfoService.setStageResultPercentageT2(times, 50.0, stageState, version, dataTypeList[dt]);

                List<List<StageResultData>> closedStageList = stageResultSetInfoService.setClosedActivityStagePercentage(actNameList, stageState, version, dataTypeList[dt]);

                List<StageOrundumVo> stageOrundumList = stageResultSetInfoService.setOrundumEfficiency(version, dataTypeList[dt]);


                List<List<StageResultVo>> pageListT3Vo = getStageResultVo(pageListT3);
                String stageFileT3 = JSON.toJSONString(pageListT3Vo);
                SaveFile.save(frontEndFilePath, "stageT3" + dataTypeList[dt] + versionCode + ".json", stageFileT3);

                List<List<StageResultVo>> pageListT2Vo = getStageResultVo(pageListT2);
                String stageFileT2 = JSON.toJSONString(pageListT2Vo);
                SaveFile.save(frontEndFilePath, "stageT2" + dataTypeList[dt] + versionCode + ".json", stageFileT2);


                List<List<StageResultVo>> closedStageListVo = getStageResultVo(closedStageList);
                String closedStage = JSON.toJSONString(closedStageListVo);
                SaveFile.save(frontEndFilePath, "closedStage" + dataTypeList[dt] + versionCode + ".json", closedStage);


                String stageOrundum = JSON.toJSONString(stageOrundumList);
                SaveFile.save(frontEndFilePath, "stageOrundum" + dataTypeList[dt] + versionCode + ".json", stageOrundum);


                File fileT3 = new File(frontEndFilePath + "closedStage" + dataTypeList[dt] + versionCode + ".json");
                File fileT2 = new File(frontEndFilePath + "closedStage" + dataTypeList[dt] + versionCode + ".json");
                File fileClosed = new File(frontEndFilePath + "closedStage" + dataTypeList[dt] + versionCode + ".json");
                File fileOrundum = new File(frontEndFilePath + "stageOrundum" + dataTypeList[dt] + versionCode + ".json");
                hashMap.put("t3文件版本：" + versionCode, fileT3.exists() + "文件大小：" + fileT3.length());
                hashMap.put("t2文件版本：" + versionCode, fileT2.exists() + "文件大小：" + fileT2.length());
                hashMap.put("活动关卡文件版本：" + versionCode, fileClosed.exists() + "文件大小：" + fileClosed.length());
                hashMap.put("搓玉文件版本：" + versionCode, fileOrundum.exists() + "文件大小：" + fileOrundum.length());
            }
        }


        return Result.success(hashMap);
    }


    @ApiOperation(value = "更新常驻商店性价比")
    @GetMapping("/update/store/perm")
    public Result updateStore() {
        storeCostPerService.updateByJsonPerm();
        List<List<StoreCostPer>> storeAllData = new ArrayList<>();
        storeAllData.add(storeCostPerService.findAll("green"));
        storeAllData.add(storeCostPerService.findAll("orange"));
        storeAllData.add(storeCostPerService.findAll("purple"));
        storeAllData.add(storeCostPerService.findAll("yellow"));
        storeAllData.add(storeCostPerService.findAll("grey"));
        String stageFileT2 = JSON.toJSONString(storeAllData);
        SaveFile.save(frontEndFilePath,"storePerm.json",stageFileT2);
        return Result.success("成功更新");
    }

    @ApiOperation(value = "更新活动商店性价比(用json文件更新)")
    @PostMapping("/update/store/act")
    public Result updateStoreNew(MultipartFile file) {
        storeCostPerService.updateByJsonAct(file);
        return Result.success("成功更新");
    }


    private static List<List<StageResultVo>> getStageResultVo(List<List<StageResultData>> list) {
        List<List<StageResultVo>> stageResultVo = new ArrayList<>();
        for (List<StageResultData> stageResultData : list) {
            List<StageResultVo> stageResultList = new ArrayList<>();
            for (StageResultData stageResultDatum : stageResultData) {
                StageResultVo stageResult = new StageResultVo();

                BeanUtils.copyProperties(stageResultDatum, stageResult);
                stageResultList.add(stageResult);
            }
            stageResultVo.add(stageResultList);
        }
        return stageResultVo;
    }
}
