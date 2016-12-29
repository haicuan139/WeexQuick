package com.bzqn.weex.utils;

import com.bzqn.weex.app.BZQNApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by 陈红建 on 2016/9/26.
 */
public class FileUtils {
    public static String getStringForCache(String fileName){

        try {
            File file = new File(ZipFileUtils.weexPath(BZQNApplication.getInstance()),fileName);
            if (!file.exists()){
                return null;
            }
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String content = new String(buffer, "UTF-8");
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
