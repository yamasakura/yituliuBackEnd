package com.lhs.common.util;



import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: microfront-server
 * @description: 文件夹压缩为zip文件
 * @author: canye
 * @create: 2023-3-3 12:43
 */
public class FolderToZipUtil {

    public static void zip(String sourceFileName, String newName, HttpServletResponse response){
        try {
            //将zip以流的形式输出到前台
            response.setHeader("content-type", "application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 设置浏览器响应头对应的Content-disposition
            //参数中 testZip 为压缩包文件名，尾部的.zip 为文件后缀
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(newName.getBytes("gbk"), "iso8859-1")+".zip");
            //创建zip输出流
            try(ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
                //创建缓冲输出流
                BufferedOutputStream bos = new BufferedOutputStream(out)) {
                File sourceFile = new File(sourceFileName);
                //调用压缩函数
                compress(out, bos, sourceFile, sourceFile.getName());
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件压缩
     * @param out
     * @param bos
     * @param sourceFile
     * @param base
     */
    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base){
        try {
            //如果路径为目录（文件夹）
            if (sourceFile.isDirectory()) {
                //取出文件夹中的文件（或子文件夹）
                File[] flist = sourceFile.listFiles();
                if (flist.length == 0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    out.putNextEntry(new ZipEntry(base + "/"));
                } else {//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for (int i = 0; i < flist.length; i++) {
                        compress(out, bos, flist[i], base + "/" + flist[i].getName());
                    }
                }
            } else {//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
                out.putNextEntry(new ZipEntry(base));
                try(FileInputStream fos = new  FileInputStream(sourceFile);
                    //创建缓冲输出流
                    BufferedInputStream bis = new BufferedInputStream(fos)){

                    int tag;
                    //将源文件写入到zip文件中
                    while ((tag = bis.read()) != -1) {
                        out.write(tag);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

