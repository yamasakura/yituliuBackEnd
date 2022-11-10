package com.lhs.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveFile {
    public static void save(String filepath,String filename,String json){
        File file = new File(filepath);

        if(!file.exists()){
            file.mkdir();
        }

        File file1 = new File(filepath,filename);
        if(!file1.exists()){
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filepath+filename);

            byte[] bytes = json.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
