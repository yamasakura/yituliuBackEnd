package com.lhs.controller.yituliu;

import com.lhs.common.util.FolderToZipUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/yituliu/")
public class YituliuController {

    @ApiOperation(value = "打包文件并下载zip文件")
    @GetMapping(value = "/downloadZip")
    public void downloadZip(HttpServletResponse response) {
        String zipPath = "D:/test";
        String newName = "test";
        try {
            FolderToZipUtil.zip(zipPath,newName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
