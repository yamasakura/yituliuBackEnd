package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lhs.bean.DBPogo.CharTagData;
import com.lhs.bean.vo.RecResultVo;
import com.lhs.dao.CharTagDataDao;
import com.lhs.service.CharTagDataService;
import com.lhs.service.MaaApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
@Slf4j
public class CharTagDataServiceImpl implements CharTagDataService {

    @Autowired
    private CharTagDataDao charTagDataDao;

    @Autowired
    private MaaApiService maaApiService;

    @Autowired
    private CharTagDataService charTagDataService;

    @Override
    public void importTagExcel(MultipartFile file) {
        List<CharTagData> list = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), CharTagData.class, new AnalysisEventListener<CharTagData>() {
                @Override
                public void invoke(CharTagData charTagData, AnalysisContext analysisContext) {
                    list.add(charTagData);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                }
            }).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        charTagDataDao.saveAll(list);
    }

    @Override
    public void exportTagExcel(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("recruitmentInfo", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<CharTagData> list = charTagDataDao.findAll();
            EasyExcel.write(response.getOutputStream(), CharTagData.class).sheet("Sheet1").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String OCRResult(List<String> orcTagList, Integer rarityMin, Integer rarityMax) {
        for (String string : orcTagList) {
            log.info(string);
        }

        List<CharTagData> allData = charTagDataDao.findByTypeAndRarityBetween(0, rarityMin, rarityMax);

        HashMap<String, List<String>> resultMap = new LinkedHashMap<>();
        HashMap<String, Integer> resultRarityMap = new LinkedHashMap<>();

        for (CharTagData charTagData : allData) {
            ArrayList<String> tagList = new ArrayList<>();
            for (String str : orcTagList) {
                if (str.equals(charTagData.getGrade())) {
                    tagList.add(str);
                } else if (str.equals(charTagData.getPosition())) {
                    tagList.add(str);
                } else if (str.equals(charTagData.getOccupation())) {
                    tagList.add(str);
                } else if (str.equals(charTagData.getTag1())) {
                    tagList.add(str);
                } else if (str.equals(charTagData.getTag2())) {
                    tagList.add(str);
                } else if (str.equals(charTagData.getTag3())) {
                    tagList.add(str);
                }
            }
            if (tagList.size() < 1) continue;

            for (int i = 0; i < tagList.size(); i++) {
                String tagOne = tagList.get(i);
                if ((!"高级资深干员".equals(tagList.get(i))) && charTagData.getRarity() == 6) {
                    continue;
                }
                if (!resultMap.containsKey(tagOne)) {
                    resultMap.put(tagOne, new ArrayList<>(Collections.singleton(charTagData.getRoleName())));
                } else {
                    List<String> roleNames = new ArrayList<>(resultMap.get(tagOne));
                    roleNames.add(charTagData.getRoleName());
                    resultMap.put(tagOne, roleNames);
                }
                resultRarityMap.put(tagOne, charTagData.getRarity());

                for (int j = i + 1; j < tagList.size(); j++) {
                    if ((!"高级资深干员".equals(tagList.get(i))) && charTagData.getRarity() == 6) {
                        continue;
                    }
                    String tagTwo = tagList.get(i) + "+" + tagList.get(j);
                    if (!resultMap.containsKey(tagTwo)) {
                        resultMap.put(tagTwo, new ArrayList<>(Collections.singleton(charTagData.getRoleName())));
                    } else {
                        List<String> roleNames = new ArrayList<>(resultMap.get(tagTwo));
                        roleNames.add(charTagData.getRoleName());
                        resultMap.put(tagTwo, roleNames);
                    }

                    resultRarityMap.put(tagTwo, charTagData.getRarity());
                    for (int k = j + 1; k < tagList.size(); k++) {
                        if ((!"高级资深干员".equals(tagList.get(i))) && charTagData.getRarity() == 6) {
                            continue;
                        }
                        String tagThree = tagList.get(i) + "+" + tagList.get(j) + "+" + tagList.get(k);
                        if (!resultMap.containsKey(tagThree)) {
                            resultMap.put(tagThree, new ArrayList<>(Collections.singleton(charTagData.getRoleName())));
                        } else {
                            List<String> roleNames = new ArrayList<>(resultMap.get(tagThree));
                            roleNames.add(charTagData.getRoleName());
                            resultMap.put(tagThree, roleNames);
                        }

                        resultRarityMap.put(tagThree, charTagData.getRarity());
                    }
                }
            }

        }

        List<RecResultVo> resultList = new ArrayList<>();
        Set<String> keySet = resultMap.keySet();

        for (String key : keySet) {
            RecResultVo recResultVo = new RecResultVo();
            recResultVo.setTags(key);
            List<Object> roleNames = new ArrayList<>(resultMap.get(key));
            LinkedHashSet<Object> hashSet = new LinkedHashSet<>(roleNames);
            recResultVo.setResult(new ArrayList<>(hashSet));
            recResultVo.setLessRarity(resultRarityMap.get(key).toString());
            resultList.add(recResultVo);
        }

        resultList.sort(comparing(RecResultVo::getLessRarity).reversed());
        StringBuilder result = new StringBuilder(" ");


        int resultMaxRarity = 3;
        if (resultList.get(0).getLessRarity() != null) {
            resultMaxRarity = Integer.parseInt(resultList.get(0).getLessRarity());
        }

//        MaaTagRequestVo maaTagRequestVo = new MaaTagRequestVo();
//        maaTagRequestVo.setLevel(resultMaxRarity);
//        maaTagRequestVo.setServer("CN");
//        maaTagRequestVo.setTags(orcTagList);
//        maaTagRequestVo.setVersion("DEBUG VERSION");
//        maaTagRequestVo.setServer("Bot");

        ArrayList<HashMap> voResult = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            if (Integer.parseInt(resultList.get(i).getLessRarity()) > 3) {
                if (Integer.parseInt(resultList.get(i).getLessRarity()) < resultMaxRarity) break;
                boolean repeatFlag = false;
                if (i > 0&& resultList.get(i - 1).getResult().size() == resultList.get(i).getResult().size()) {
                    repeatFlag = true;
                    for (int j = 0; j < resultList.get(i - 1).getResult().size(); j++) {
                        if (!resultList.get(i - 1).getResult().get(j).equals(resultList.get(i).getResult().get(j))) {
                            repeatFlag = false;
                            break;
                        }
                    }
                }
                if(repeatFlag) continue ;


                HashMap<Object, Object> botResult = new HashMap<>();
                String[] tagSplit = resultList.get(i).getTags().split("[+]");
                botResult.put("tags",tagSplit);
                botResult.put("level",resultMaxRarity);
                ArrayList<HashMap> charList = new ArrayList<>();
                List<Object> charResultList = resultList.get(i).getResult();
                for(Object object: charResultList){
                    HashMap<Object, Object> charMap = new HashMap<>();
                    charMap.put("name",object);
                    charMap.put("level",resultMaxRarity);
                    charList.add(charMap);
                }
                botResult.put("opers",charList);
                voResult.add(botResult);



                if ("资深干员".equals(resultList.get(i).getTags())) {
                    result.append("【").append(resultList.get(i).getTags()).append("】:\n").append("当前版本可招募的五星").append("\n");
                } else if ("高级资深干员".equals(resultList.get(i).getTags())) {
                    result.append("【").append(resultList.get(i).getTags()).append("】:\n ").append("当前版本可招募的六星").append("\n");
                } else {
                    result.append("【").append(resultList.get(i).getTags()).append("】:\n ").append(resultList.get(i).getResult()).append("\n");
                }
            }
        }

//        try {
//            maaTagRequestVo.setResult(JSONArray.parseArray(JSONObject.toJSONString(voResult)));
//            maaApiService.maaTagResultSave(maaTagRequestVo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        if (result.length() < 5) {
            result.append("三星");
        }

        String string = result.toString().replace("[", "").replace("]", "")
                .replace(",", "  ");

        try {
            string = URLEncoder.encode("本次公招结果 ：\n" + string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return string;
    }


    @Override
    public List<RecResultVo> findAllByTypeAndRarityNew(Integer type, String[] reqTags, Integer rarityMin) {
        int r1Type = 3;
        int r6Type = 5;
        for (String tag : reqTags) {

            if ("高级资深干员".equals(tag)) {
                r6Type = 6;
            }

        }
        r1Type = rarityMin;

        List<CharTagData> allData = charTagDataDao.findByTypeAndRarityBetween(type, r1Type, r6Type);

        HashMap<String, List<String>> hashMap = new HashMap<>();
        for (CharTagData allRole : allData) {
            StringBuilder tags = new StringBuilder("_");
            Integer sixCode = 0;
            for (String reqTag : reqTags) {
                if (reqTag.equals(allRole.getGrade())) {
                    tags.append(",").append(reqTag);
//                    if("高级资深干员".equals(reqTag)){
//                        sixCode=1;
//                    }else if("资深干员".equals(reqTag)){
//                        sixCode=2;
//                    }
                } else if (reqTag.equals(allRole.getPosition())) {
                    tags.append(",").append(reqTag);
                } else if (reqTag.equals(allRole.getOccupation())) {
                    tags.append(",").append(reqTag);
                } else if (reqTag.equals(allRole.getTag1())) {
                    tags.append(",").append(reqTag);
                } else if (reqTag.equals(allRole.getTag2())) {
                    tags.append(",").append(reqTag);
                } else if (reqTag.equals(allRole.getTag3())) {
                    tags.append(",").append(reqTag);
                }
            }

            if ("_".equals(tags.toString())) {
                continue;
            }


            String[] split = tags.toString().split(",");
            for (int i = 1; i < split.length; i++) {
                String tagOne = "";
                tagOne = split[i];
                if ((!"高级资深干员".equals(split[i])) && allRole.getRarity() == 6) {
//                    System.out.println(split[i]);
//                    System.out.println(allRole.getRoleName());
                    continue;
                }
                if (hashMap.get(tagOne) == null) {
                    hashMap.put(tagOne, new ArrayList<>(Collections.singleton(allRole.getRarity() + "_" + allRole.getRoleName())));
                } else {
                    List<String> roleNames = new ArrayList<>(hashMap.get(tagOne));
                    roleNames.add(allRole.getRarity() + "_" + allRole.getRoleName());
                    hashMap.put(tagOne, roleNames);
                }

                for (int j = i + 1; j < split.length; j++) {
                    String tagTwo = "";
                    tagTwo = split[i] + "-" + split[j];
//                    System.out.println(split[i]+"-"+split[j]);
                    if ((!"高级资深干员".equals(split[i])) && allRole.getRarity() == 6) {
//                        System.out.print(split[i]+"-"+split[j]);
//                        System.out.println(allRole.getRoleName());
                        continue;
                    }


                    if (hashMap.get(tagTwo) == null) {
                        hashMap.put(tagTwo, new ArrayList<>(Collections.singleton(allRole.getRarity() + "_" + allRole.getRoleName())));
                    } else {
                        List<String> roleNames = new ArrayList<>(hashMap.get(tagTwo));
                        roleNames.add(allRole.getRarity() + "_" + allRole.getRoleName());
                        hashMap.put(tagTwo, roleNames);
                    }

                    for (int k = j + 1; k < split.length; k++) {
                        String tagThree = "";
                        tagThree = split[i] + "-" + split[j] + "-" + split[k];
//                        System.out.println(split[i]+"-"+split[j]+"-"+split[k]);
                        if ((!"高级资深干员".equals(split[i])) && allRole.getRarity() == 6) {
//                            System.out.println(split[i]+"-"+split[j]+"-"+split[k]);
//                            System.out.println(allRole.getRoleName());
                            continue;
                        }
                        if (hashMap.get(tagThree) == null) {
                            hashMap.put(tagThree, new ArrayList<>(Collections.singleton(allRole.getRarity() + "_" + allRole.getRoleName())));
                        } else {
                            List<String> roleNames = new ArrayList<>(hashMap.get(tagThree));
                            roleNames.add(allRole.getRarity() + "_" + allRole.getRoleName());
                            hashMap.put(tagThree, roleNames);
                        }

                    }
                }
            }


        }

        List<RecResultVo> resultList = new ArrayList<>();
        Set<String> keySet = hashMap.keySet();
        for (String key : keySet) {
            RecResultVo recResultVo = new RecResultVo();
            recResultVo.setTags(key.replace("_,", ""));
            List<Object> roleNames = new ArrayList<>(hashMap.get(key));
            LinkedHashSet<Object> hashSet = new LinkedHashSet<>(roleNames);
            recResultVo.setResult(new ArrayList<>(hashSet));
            recResultVo.setLessRarity(hashMap.get(key).get(hashMap.get(key).size() - 1).split("_")[0]);
            resultList.add(recResultVo);
        }

        resultList.sort(comparing(RecResultVo::getLessRarity).reversed());


        return resultList;
    }


    @Override
    public List<CharTagData> findAll() {
        return charTagDataDao.findByTypeAndRarityBetween(0, 1, 6);
    }


}


