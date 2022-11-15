package com.lhs.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArkGacha {
    public static String[] sixstar = {
            "能天使", "黑", "早露", "空弦", "伊芙利特",
            "艾雅法拉", "莫斯提马", "刻俄柏", "推进之王", "风笛",
            "陈", "银灰", "斯卡蒂", "赫拉格", "煌",
            "棘刺", "史尔特尔", "山", "塞雷亚", "星熊",
            "森蚺", "瑕光", "泥岩", "夜莺", "闪灵",
            "安洁莉娜", "麦哲伦", "铃兰", "阿", "傀影",
            "温蒂", "刻俄柏", "风笛", "傀影", "温蒂", "早露", "铃兰"
            , "棘刺", "森蚺", "史尔特尔", "瑕光", "泥岩", "迷迭香", "山",
            "空弦", "嵯峨", "异客", "凯尔希", "卡涅利安", "帕拉斯", "水月",
            "琴柳", "远牙", "焰尾", "灵知", "老鲤", "澄闪", "菲亚梅塔",
            "号角", "艾丽妮", "黑键", "多萝西",
    };

//    public static String[] fivestar = {
//            "蓝毒","普罗旺斯","白金","守林人","陨星","送葬人","灰喉","慑砂","安哲拉",
//            "四月","奥斯塔","夜魔","天火","惊蛰","莱恩哈特","蜜蜡","爱丽丝","德克萨斯","凛冬",
//            "芦草","极境","贾维","芙兰卡","拉普兰德","幽灵鲨","诗怀雅", "星极","布洛卡","断崖",
//            "燧石","雷蛇","临光","可颂","吽","石棉","赫默","白面鸮","华法琳","絮雨",
//            "真理","初雪","梅尔","空","格劳克斯","巫恋","月禾","稀音","狮蝎","红",
//            "食铁兽","崖心","槐虎","卡夫卡","爱丽丝","乌有","熔泉","赤冬",
//            "贝娜","崖心","绮良","羽毛笔","食铁兽","崖心","槐虎","卡夫卡",
//            "食铁兽","崖心","槐虎","卡夫卡","食铁兽","崖心","槐虎","卡夫卡",
//            "食铁兽","崖心","槐虎","卡夫卡","食铁兽","崖心","槐虎","卡夫卡",
//            "食铁兽","崖心","槐虎","卡夫卡","食铁兽","崖心","槐虎","卡夫卡",
//    };
//    public static String[] fourstar = {
//            "梅","白雪","流星","杰西卡","红云","安比尔","酸糖","松果","夜烟","远山",
//            "格雷伊","卡达","红豆","清道夫","桃金娘","豆苗","缠丸","杜宾","猎蜂","慕斯",
//            "霜叶","宴","刻刀","芳汀","杰克","蛇屠箱","古米","角峰","泡泡","末药",
//            "调香师","苏苏洛","深海色","地灵","波登可","阿消","砾","暗锁","孑"
//    };
//    public static String[] threestar = {
//            "空爆","克洛丝","史都华德","炎熔","香草","翎羽","芬","玫兰莎","月见夜","泡普卡",
//            "卡缇","米格鲁","斑点","安塞尔","芙蓉","梓兰"
//    };


    public static String get(Integer sum) {

        int gachaSum = sum;

        Random rand = new Random();
        List<String> sixList = new ArrayList<>();
        int random, randomSix, randomUp;
        int sixSum, fiveSum, fourSum, threeSum,sixUp,sixLast;
        sixSum = fiveSum = fourSum = threeSum =sixUp=sixLast= 0;
        int up = 0; //计算器
        int count = 0; //计算器

        for (int i = 0; i < gachaSum; i++) {
            count++;
            if (count > 50) {
                up = count - 50;
            }
            random = rand.nextInt(100);
//                System.out.print(i);
            if (random < (2 + 2 * up)) {
                randomUp = rand.nextInt(100);
                if (randomUp < 50) {
                    sixList.add(sixstar[sixstar.length - 1]);
                    sixUp++;
                } else {
                    randomSix = rand.nextInt(sixstar.length - 2);
                    sixList.add(sixstar[randomSix]);
                }
//                System.out.println("第" + count + "抽到了六星");
                sixSum++;
                count = 0;
                up = 0;
                sixLast=i;
                continue;
            }
            if (random >= (2 + 2 * up) && random < (10 + 2 * up)) {
                fiveSum++;
                continue;
            }
            if (random >= (10 + 2 * up) && random < (60 + 2 * up)) {
                fourSum++;
            } else {
                threeSum++;
            }

        }

//        double expcet = sixSum/gachaSum;


             int greenSum = threeSum*10+fourSum*30;
             int yellowSum = fourSum+(fiveSum)*5+(sixSum)*10;
             String strTip="符合期望，亚洲水平";
        double expcet = Double.parseDouble(String.valueOf(sixLast)) / Double.parseDouble(String.valueOf(sixSum));
        if(expcet<30){
            strTip="欧洲人吃我一矛";
        }
        if(expcet>38) {
            strTip="酋长回家吧";
        }

        if(sixSum==0){
            strTip="一个六星也没有太惨了";
        }

        String value = String.valueOf(expcet);
                System.out.println(value);
        String sixStr = sum+"次普池寻访结果：\n⭐6x"+sixSum+"  ⭐5x"+fiveSum+"  ⭐4x"+fourSum+"  ⭐3x"+threeSum+"\n抽到UP⭐6x"+sixUp+
                "\n获得绿票x"+greenSum+"获得黄票x"+yellowSum+"\n本次抽卡期望为"+value.substring(0,value.indexOf(".")+2)+"\n"+strTip;


                System.out.println("期望是" + expcet);
//            for (String str : sixList) {
//                System.out.println(str);
//            }
        return sixStr;
    }


}

