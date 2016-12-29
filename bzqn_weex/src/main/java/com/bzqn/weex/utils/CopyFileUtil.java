package com.bzqn.weex.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 陈红建 on 2016/9/26.
 */
public class CopyFileUtil {
    public static void copyFile(File target, String source, Context context) {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(context.getAssets().open(source));
            fos = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
            close(fos);
        }
    }

    private static void close(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void close(OutputStream inStream) {
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
