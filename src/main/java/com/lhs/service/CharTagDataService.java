package com.lhs.service;

import com.lhs.bean.DBPogo.CharTagData;
import com.lhs.bean.vo.RecResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CharTagDataService {

    void importTagExcel(MultipartFile file);

    String OCRResult(List<String> list ,Integer rarityMin, Integer rarityMax);

    void exportTagExcel(HttpServletResponse response);

    List<RecResultVo> findAllByTypeAndRarityNew(Integer type, String[] tags,Integer rarityMax);

    List<CharTagData> findAll();



}
