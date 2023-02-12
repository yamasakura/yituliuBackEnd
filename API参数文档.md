# API


# 使用前须知
*****
### 请求说明

使用 HTTP GET:

|    名称     |             说明              | 
|:---------:|:---------------------------:|
| 请求 URL 格式 | /终结点?参数名=参数值&参数名=参数值......  |

使用 HTTP POST:

|    名称     |     说明     | 
|:---------:|:----------:|
| 请求 URL 格式 |    /终结点    |
|    请求体    | 请求体一般为JSON |

### 响应说明
以下是响应的内容 <br>
其中code（返回值），message（说明）字段：

|  返回值  |    说明    | 
|:-----:|:--------:|
| 50002 |   数据错误   |
| 50001 |  数据未找到   |
|  404  | API 不存在  |
|  200  | 一般代表调用成功 |

data字段：
API的响应数据，一般是一个JSON，部分情况为String

# 参数及响应数据
下面是请求所需 params 和响应包含的 data 格式
### 获取蓝材料最优图(JsonArray)
终结点：`/api/find/stage/t3` 
<br>
请求类型：Get
#### 参数

|      字段名       |   数据类型   | 默认值 |              说明               |
|:--------------:|:--------:|:---:|:-----------------------------:|
| expCoefficient |  Double  |  无  | 经验书系数  一般默认经验书价值为龙门币价值的0.625倍 |

#### 响应数据

|       字段名        |  数据类型  |                                说明                                |
|:----------------:|:------:|:----------------------------------------------------------------:|
| stageEfficiency  | Double | 与所有常驻关卡中，无活动加成时综合效率最高者相比，该关卡的效率为103.6%。该效率是统计了所有产物的综合效率，长期最优的结果  |
|    stageCode     | String |                             关卡的显示名称                              |
|     itemType     | String |                           该关卡属于某一材料体系                            |
|   secondaryId    | String |                         副产物的物品ID，1为无副产物                          |
| sampleConfidence | Double |                样本量的置信度（误差不超过3%的概率）为99.9%，置信度过低的关卡                |
|    stageState    | String |            关卡状态，0:无事发生 1:SideStory 2:故事集 3:理智小样+物资补给             |
|   activityName   | String |                             活动或章节名称                              |
|   knockRating    | Double |                     主产物的掉率，短期急需该系材料的话参考意义较大                      |
|    updateTime    | String |                             数据统计的时间                              |
|    sampleSize    |  Int   |                               样本数量                               |
|    secondary     | String |                         副产物的物品名称，1为无副产物                          |
|     apExpect     | Double |                     主产物的期望，短期急需该系材料的话参考意义较大                      |
|      itemId      | String |                             主产物的物品ID                             |
|       spm        | String |                  SanityPerMinute，每分钟理论上可以消耗的理智                   |
|    stageColor    |  Int   | 关卡标注颜色 橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1 |



### 获取绿材料最优图(JsonArray)
终结点：`/api/find/stage/t2`
<br>
请求类型：Get
#### 参数

|      字段名       |   数据类型   | 默认值 |              说明               |
|:--------------:|:--------:|:---:|:-----------------------------:|
| expCoefficient |  Double  |  无  | 经验书系数  一般默认经验书价值为龙门币价值的0.625倍 |

#### 响应数据

|       字段名        |  数据类型  |                                说明                                |
|:----------------:|:------:|:----------------------------------------------------------------:|
| stageEfficiency  | Double | 与所有常驻关卡中，无活动加成时综合效率最高者相比，该关卡的效率为103.6%。该效率是统计了所有产物的综合效率，长期最优的结果  |
|    stageCode     | String |                             关卡的显示名称                              |
|     itemType     | String |                           该关卡属于某一材料体系                            |
|   secondaryId    | String |                         副产物的物品ID，1为无副产物                          |
| sampleConfidence | Double |                样本量的置信度（误差不超过3%的概率）为99.9%，置信度过低的关卡                |
|    stageState    | String |            关卡状态，0:无事发生 1:SideStory 2:故事集 3:理智小样+物资补给             |
|   activityName   | String |                             活动或章节名称                              |
|   knockRating    | Double |                     主产物的掉率，短期急需该系材料的话参考意义较大                      |
|    updateTime    | String |                             数据统计的时间                              |
|    sampleSize    |  Int   |                               样本数量                               |
|    secondary     | String |                         副产物的物品名称，1为无副产物                          |
|     apExpect     | Double |                     主产物的期望，短期急需该系材料的话参考意义较大                      |
|      itemId      | String |                             主产物的物品ID                             |
|       spm        | String |                  SanityPerMinute，每分钟理论上可以消耗的理智                   |
|    stageColor    |  Int   | 关卡标注颜色 橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1 |



### 获取常驻商店性价比(JsonArray)
终结点：`/api/find/store/perm`
<br>
请求类型：Get
#### 参数
> 该API无需参数

#### 响应数据

|    字段名    |  数据类型  |                    说明                     |
|:---------:|:------:|:-----------------------------------------:|
|  itemId   | String |                   物品id                    |
| itemName  | String |                   物品名称                    |
| itemValue | Double |                   物品价值                    |
|   cost    | Double | 单位售价  （比如5000龙门币卖7代币  单位售价是7/5000=0.0014) |
|  rawCost  | String |                  商店原始售价                   |
|  costPer  | Double |                    性价比                    |
| storeType | String |                   商店类型                    |



### 获取活动商店性价比(JsonArray)
终结点：`/api/find/store/act`
<br>
请求类型：Get
#### 参数
> 该API无需参数

#### 响应数据

|     字段名      |  数据类型  |    说明     |
|:------------:|:------:|:---------:|
| actStartData |  Long  |  活动开始时间   |
|  actEndData  |  Long  |  活动结束时间   |
|   actName    | String |   活动名称    |
|  actTagArea  | String |           |
|  actPPRBase  | Double |  商店原始售价   |
| actPPRStair  | String |    性价比    |
|   actStore   | Object | 商店性价比具体内容 |
其中`actStore`字段包含六个字段

|     字段名      |  数据类型  |          说明          |
|:------------:|:------:|:--------------------:|
|   itemAra    |  Int   | 区域索引，用于判断是无限池区还是有限池区 |
|   itemName   | String |         材料名称         |
|   itemPPR    | Double |        材料性价比         |
|  itemPrice   |  Int   |         商店售价         |
| itemQuantity |  Int   |       商店每次售卖个数       |
|  itemStock   |  Int   |         商店库存         |



### 获取所有物品价值(JsonArray)
终结点：`/api/find/item/value/`
<br>
请求类型：Get
#### 参数

|      字段名       |   数据类型   | 默认值 |              说明               |
|:--------------:|:--------:|:---:|:-----------------------------:|
| expCoefficient |  Double  |  无  | 经验书系数  一般默认经验书价值为龙门币价值的0.625倍 |

#### 响应数据

|    字段名    |  数据类型  |    说明    |
|:---------:|:------:|:--------:|
|  itemId   | String |   物品id   |
| itemName  | String |   物品名称   |
| itemValue |  Int   |   物品价值   |
|   type    | String |  物品稀有度   |
|  cardNum  | String | 前端排序的用索引 |



### 获取历史活动最优图(JsonArray)
终结点：`/api/find/stage/closedStage`
<br>
请求类型：Get
#### 参数

|      字段名       |   数据类型   | 默认值 |              说明               |
|:--------------:|:--------:|:---:|:-----------------------------:|
| expCoefficient |  Double  |  无  | 经验书系数  一般默认经验书价值为龙门币价值的0.625倍 |

#### 响应数据

|       字段名        |  数据类型  |                                说明                                |
|:----------------:|:------:|:----------------------------------------------------------------:|
| stageEfficiency  | Double | 与所有常驻关卡中，无活动加成时综合效率最高者相比，该关卡的效率为103.6%。该效率是统计了所有产物的综合效率，长期最优的结果  |
|    stageCode     | String |                             关卡的显示名称                              |
|     itemType     | String |                           该关卡属于某一材料体系                            |
|   secondaryId    | String |                         副产物的物品ID，1为无副产物                          |
| sampleConfidence | Double |                样本量的置信度（误差不超过3%的概率）为99.9%，置信度过低的关卡                |
|    stageState    | String |            关卡状态，0:无事发生 1:SideStory 2:故事集 3:理智小样+物资补给             |
|   activityName   | String |                             活动或章节名称                              |
|   knockRating    | Double |                     主产物的掉率，短期急需该系材料的话参考意义较大                      |
|    updateTime    | String |                             数据统计的时间                              |
|    sampleSize    |  Int   |                               样本数量                               |
|    secondary     | String |                         副产物的物品名称，1为无副产物                          |
|     apExpect     | Double |                     主产物的期望，短期急需该系材料的话参考意义较大                      |
|      itemId      | String |                             主产物的物品ID                             |
|       spm        | String |                  SanityPerMinute，每分钟理论上可以消耗的理智                   |
|    stageColor    |  Int   | 关卡标注颜色 橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1 |



### 获取商店礼包性价比(JsonArray)
终结点：`/api/find/store/act`
<br>
请求类型：Get
#### 参数
> 该API无需参数

#### 响应数据

|         字段名         |  数据类型  |          说明           |
|:-------------------:|:------:|:---------------------:|
|    packShowName     | String |        礼包展示名称         |
|      packType       | String |        礼包售卖类型         |
|      packDraw       | Double |        礼包包含抽数         |
|     packPPRDraw     | Double |     礼包相对大月卡的折扣幅度      |
|  packPPROriginium   | Double |    礼包相对648源石的折扣幅度     |
|       packImg       | String |        礼包图片名称         |
|      packPrice      |  Int   |         礼包ID          |
|       packID        |  Int   |       商店性价比具体内容       |
| packRmbPerOriginium | Double | 礼包全部物品折算源石后每颗源石的氪金性价比 |
|     gachaPermit     |  Int   |        礼包内单抽数量        |
|      packState      |  Int   |        是否正在售卖         |
|    packOriginium    | Double |    礼包全部物品折算为源石的数量     |
|   packRmbPerDraw    | Double |      礼包每一抽的氪金性价比      |
|    gachaPermit10    |  Int   |        礼包内十连数量        |
|   gachaOriginium    |  Int   |        礼包内源石数量        |
|      packName       | String |         礼包名称          |
|    gachaOrundum     |  Int   |       礼包内合成玉数量        |
|     packContent     | Object |     礼包内除抽卡资源外的内容      |

其中`packContent`字段包含俩个字段

|         字段名         |  数据类型  |  说明  |
|:-------------------:|:------:|:----:|
| packContentQuantity | String | 材料数量 |
|   packContentItem   | String | 材料名称 |






