package com.lhs.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CreateJsonFile {

    public static void createJsonFile(HttpServletResponse response, String filePath, String fileName, String jsonForMat) {
        try {
            // 拼接文件完整路径
            String fullPath = filePath + fileName + ".json";

            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 格式化json字符串
            FileInputStream fileInputStream = new FileInputStream(file);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonForMat);
            write.flush();
            write.close();

            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".json");
            OutputStream outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
