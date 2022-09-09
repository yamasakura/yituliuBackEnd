package com.lhs.controller.yituliu;


import com.alibaba.fastjson.JSON;
import com.lhs.bean.DBPogo.StageResultData;
import com.lhs.bean.DBPogo.StoreCostPer;
import com.lhs.bean.vo.StageResultApiVo;
import com.lhs.common.util.HttpRequestUtil;
import com.lhs.common.util.Result;
import com.lhs.common.util.SaveFile;
import com.lhs.dao.StageResultVoApiDao;
import com.lhs.service.*;
import io.swagger.annotations.Api;
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


    @ApiOperation("更新关卡数据")
    @GetMapping("/update/{mini}")
    public Result update(@PathVariable("mini") Integer mini) {
        double start = System.currentTimeMillis();//记录程序启动时间
        itemService.resetItemShopValue();

        List<StageResultData> list = new ArrayList<>();

        int countNum = 7;
        for(int i=0;i<countNum;i++){
            list =  stageResultCalcService.stageResult(i,countNum);
        }

        List<StageResultData> stageResultDataList = new ArrayList<>();
        List<StageResultApiVo> stageResultApiVoList = new ArrayList<>();
        for(StageResultData rawData:list ){
            if(rawData.getIsSpecial()==1&&mini==1){
                rawData.setEfficiency(rawData.getEfficiency()+0.045);
            }
//            System.out.println(rawData);
            if ("1-7".equals((rawData.getStageName()))&&"30012".equals(rawData.getItemId())) {
                rawData.setExpect(rawData.getExpect()/5);
            } else {
                rawData.setExpect(rawData.getExpect());
            }

            StageResultApiVo stageResultApiVo = new StageResultApiVo();
            stageResultApiVo.setId(rawData.getId());
            BeanUtils.copyProperties(rawData,stageResultApiVo);
            if ("1-7".equals((rawData.getStageName()))&&"30012".equals(rawData.getItemId())) {
                stageResultApiVo.setExpect(rawData.getExpect()/5);
            } else {
                stageResultApiVo.setExpect(rawData.getExpect());
            }

            if(rawData.getIsShow()==1){
                stageResultApiVoList.add(stageResultApiVo);
            }
        }

        stageResultCalcService.deleteAllInBatch();
        stageResultVoApiDao.deleteAll();
        stageResultCalcService.saveAll(list);
        stageResultVoApiDao.saveAll(stageResultApiVoList);


        double end = System.currentTimeMillis();
        return Result.success("本次计算用时"+(end - start)/1000+"s");
    }



    @ApiOperation("导出物品价值表")
    @GetMapping("/export/item/value")
    public void exportItemData(HttpServletResponse response) {
        itemService.exportItemData(response);
    }



    @ApiOperation("保存企鹅物流数据")
    @GetMapping("/save/PenguinsData")
    public Result savePenguinsData() {
        String url = "https://penguin-stats.io/PenguinStats/api/v2/result/matrix?show_closed_zones=true";
        String con = HttpRequestUtil.doGet(url);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");// 设置日期格式
        String saveTime = simpleDateFormat.format(new Date());
        SaveFile.save(penguinFilePath,"matrix"+saveTime+".json",con);
        File file = new File(penguinFilePath+"matrix"+saveTime+".json");
        HashMap<Object, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("企鹅文件",file.exists());
        hashMap.put("文件大小",file.length());
        return Result.success();
    }


    @ApiOperation("保存关卡数据文件")
    @GetMapping("/save/stage/result")
    public Result saveStageData() {
        String[] actNameList = new String[]{
                "act_side20", "act_side19",
                "act_side18", "act_side17", "act_side16", "act_side15", "act_side14", "act_side13",
                "act_side12_rep_", "act_side11", "act_side10", "act_side9", "act_side8",
                "act_side7", "act_side6", "act_side5", "act_side4", "act_side3", "act_side0"
        };

        List<List<StageResultData>> pageListT3 = stageResultSetInfoService.setStageResultPercentageT3(500, 1.0);
        List<List<StageResultData>> pageListT2 = stageResultSetInfoService.setStageResultPercentageT2(100,50.0);
        List<List<StageResultData>> closedStageList = stageResultSetInfoService.setClosedActivityStagePercentage(actNameList);

        String stageFileT3 = JSON.toJSONString(pageListT3);
        SaveFile.save(frontEndFilePath,"stageT3.json",stageFileT3);

        String stageFileT2 = JSON.toJSONString(pageListT2);
        SaveFile.save(frontEndFilePath,"stageT2.json",stageFileT2);

        String closedStage = JSON.toJSONString(closedStageList);
        SaveFile.save(frontEndFilePath,"closedStage.json",closedStage);


        HashMap<Object, Object> hashMap = new LinkedHashMap<>();
        File fileT3 = new File(frontEndFilePath+"closedStage.json");
        File fileT2 = new File(frontEndFilePath+"closedStage.json");
        File fileClosed = new File(frontEndFilePath+"closedStage.json");
        hashMap.put("t3文件",fileT3.exists());
        hashMap.put("t3文件大小",fileT3.length());
        hashMap.put("t2文件",fileT2.exists());
        hashMap.put("t2文件大小",fileT2.length());
        hashMap.put("closed文件",fileClosed.exists());
        hashMap.put("closed文件大小",fileClosed.length());
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

}
