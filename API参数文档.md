# API
*****

## 请求说明

使用 HTTP GET:

|名称|说明|
|请求 URL|/终结点?参数名=参数值&参数名=参数值......|


### 获取蓝材料最优图(JsonArray)

 ```
url:  /api/find/stage/t3
 ```
请求类型：Get
expCoefficient  类型Double 经验书系数  默认经验书价值为龙门币价值的0.625倍。
有000，076，062，100四个版本。
API示例：/api/find/stage/t3/300/auto062

abc|bcd|cde
abc|bcd|cde
abc|bcd|cde

 ```
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    [
      {
        "stageEfficiency": 103.6,                // 与所有常驻关卡中，无活动加成时综合效率最高者相比，该关卡的效率为103.6%。该效率是统计了所有产物的综合效率，长期最优的结果
        "itemType": "全新装置",                   // 该关卡属于全新装置一系
        "secondaryId": "1",                      // 副产物的物品ID，1为无副产物
        "sampleConfidence": 99.9,                // 样本量的置信度（误差不超过3%的概率）为99.9%，置信度过低的关卡
        "stageState": 1,                         // 关卡状态，0:无事发生 1:SideStory 2:故事集 3:理智小样+物资补给
        "activityName": "第七章",                 // 活动或章节名称
        "knockRating": 0.333,                     // 主产物的掉率，短期急需该系材料的话参考意义较大
        "updateTime": "2022-09-17 09:10:03",      // 数据统计的时间
        "sampleSize": 72537,                      // 样本数量
        "secondary": "1",                         // 副产物的物品名称，1为无副产物
        "apExpect": 54.078,                       // 主产物的期望，短期急需该系材料的话参考意义较大
        "itemId": "30063",                        // 主产物的物品ID
        "spm": "5.4",                              // SanityPerMinute，每分钟理论上可以消耗的理智
        "stageColor": 4,                          // 关卡标注颜色 橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1
        "stageCode": "7-15"                        // 关卡的显示名称
      },
      ……
}
 ```

### 获取绿材料最优图(JsonArray)

 ```
url:  /api/find/stage/t2/{version}
请求类型：Get
参数{version}  //经验书系数   默认经验书价值为龙门币价值的0.625倍。
有000，076，062，100四个版本。
API示例：/api/find/stage/t2/auto062
 ```

 ```
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    [
      {
        "stageEfficiency": 103.6,                // 与所有常驻关卡中，无活动加成时综合效率最高者相比，该关卡的效率为103.6%。该效率是统计了所有产物的综合效率，长期最优的结果
        "itemType": "全新装置",                   // 该关卡属于全新装置一系
        "secondaryId": "1",                      // 副产物的物品ID，1为无副产物
        "sampleConfidence": 99.9,                // 样本量的置信度（误差不超过3%的概率）为99.9%，置信度过低的关卡
        "stageState": 1,                         // 关卡状态，0:无事发生 1:故事集 2:SS 3:理智小样+物资补给
        "activityName": "第七章",                 // 活动或章节名称
        "knockRating": 0.333,                     // 主产物的掉率，短期急需该系材料的话参考意义较大
        "updateTime": "2022-09-17 09:10:03",      // 数据统计的时间
        "sampleSize": 72537,                      // 样本数量
        "secondary": "1",                         // 副产物的物品名称，1为无副产物
        "apExpect": 54.078,                       // 主产物的期望，短期急需该系材料的话参考意义较大
        "itemId": "30063",                        // 主产物的物品ID
        "spm": "5.4",                              // SanityPerMinute，每分钟理论上可以消耗的理智
        "stageColor": 4,                          // 关卡标注颜色 橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1
        "stageCode": "7-15"                        // 关卡的显示名称
      },
      ……
}
 ```

### 获取常驻商店性价比(JsonArray)

 ```
url:  /api/find/store/perm
请求类型：Get
无参数
 ```

 ```
类型String   名称 itemId;  //物品id
类型String   名称 itemName; //物品名称
类型Double   名称 itemValue;  //物品价值
类型Double   名称 cost;     //单位售价  （比如5000龙门币卖7代币  单位售价是7/5000)
类型String   名称 rawCost;    //商店原始售价
类型Double   名称 costPer;     //性价比
类型String   名称 storeType;  //商店类型
 ```

### 获取活动商店性价比(JsonArray)

 ```
url:  /api/find/store/act
请求类型：Get
无参数
 ```

 ```
名称actStartData  类型Long  活动开始时间
名称actEndData  类型Long  活动结束时间
名称actName   类型String  活动名称
名称actTagAera  （暂且不用管）
名称actPPRBase   类型Double  用于前端渲染颜色大于这个数值性价比颜色分级为橙  （顺序为橙，紫，蓝，绿，灰）
名称actPPRStair   类型String  actPPRBase每降低一个actPPRStair，性价比颜色分级降一级

名称actStore :
{
名称itemAra         类型Integer  区域索引，用于判断是无限池区还是有限池区
名称itemName        类型String  材料名称
名称itemPPR         类型Double   材料性价比
名称itemitemPrice   类型Integer  商店售价
名称itemQuantity    类型Integer  商店每次售卖个数
名称itemStock       类型Integer  商店库存
}

 ```

### 获取所有物品价值(JsonArray)

 ```
       接口/表示查找/物品相关/具体的内容/版本
url:  /api/find/item/value/{version}
请求类型：Get
{version}//  经验书系数   默认经验书价值为龙门币价值的0.625倍。
有0.0，0.76，0.625，1.0四个版本。
API示例：/api/find/item/value/auto0.625
 ```

 ```
类型String    名称itemId;  //物品id
类型String    名称itemName; //物品名称
类型Double    名称itemValue; //物品价值
类型String    名称type; //物品稀有度
类型String    名称cardNum;  //前端排序的用索引
 ```

### 获取历史活动最优图(JsonArray)

 ```
url:  /api/find/stage/activity/closed/{version}
请求类型：Get
参数{version}//  经验书系数   默认经验书价值为龙门币价值的0.625倍。
有000，076，062，100四个版本。
API示例： /api/find/stage/activity/closed/auto062
 ```

 ```
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    [
      {
        "stageEfficiency": 103.6,                // 与所有常驻关卡中，无活动加成时综合效率最高者相比，该关卡的效率为103.6%。该效率是统计了所有产物的综合效率，长期最优的结果
        "itemType": "全新装置",                   // 该关卡属于全新装置一系
        "secondaryId": "1",                      // 副产物的物品ID，1为无副产物
        "sampleConfidence": 99.9,                // 样本量的置信度（误差不超过3%的概率）为99.9%，置信度过低的关卡
        "stageState": 1,                         // 关卡状态，0:无事发生 1:故事集 2:SS 3:理智小样+物资补给
        "activityName": "第七章",                 // 活动或章节名称
        "knockRating": 0.333,                     // 主产物的掉率，短期急需该系材料的话参考意义较大
        "updateTime": "2022-09-17 09:10:03",      // 数据统计的时间
        "sampleSize": 72537,                      // 样本数量
        "secondary": "1",                         // 副产物的物品名称，1为无副产物
        "apExpect": 54.078,                       // 主产物的期望，短期急需该系材料的话参考意义较大
        "itemId": "30063",                        // 主产物的物品ID
        "spm": "5.4",                              // SanityPerMinute，每分钟理论上可以消耗的理智
        "stageColor": 4,                          // 关卡标注颜色 橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1
        "stageCode": "7-15"                        // 关卡的显示名称
      },
      ……
}
 ```

### 获取商店礼包性价比(JsonArray)

 ```
url:  /api/find/store/act
请求类型：Get
无参数
 ```

 ```
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "packShowName": "新人寻访组合包",  //礼包展示名称
      "packType": "once",  //礼包售卖类型
      "packDraw": 20,  //礼包包含抽数
      "packPPRDraw": 1.8243243243243241,    //礼包相对大月卡的折扣幅度
      "packPPROriginium": 1.8229166666666667,   //礼包相对648源石的折扣幅度
      "packImg": "新人寻访组合包", //礼包图片名称 
      "packPrice": 128,  //礼包售价
      "packID": 51,     //礼包ID
      "packRmbPerOriginium": 1.92,   //礼包全部物品折算源石后每颗源石的氪金性价比
      "gachaPermit": 0,   //礼包内单抽数量
      "packState": 1,  //是否正在售卖
      "packOriginium": 66.66666666666667   //礼包全部物品折算为源石的数量
      "packRmbPerDraw": 6.4,  //礼包每一抽的氪金性价比
      "gachaPermit10": 2,  //礼包内十连数量
      "gachaOriginium": 0, //礼包内源石数量
      "packName": "新人寻访组合包", //礼包名称
      "gachaOrundum": 0  //礼包内合成玉数量
    },
      ……
     ]
}
 ```



