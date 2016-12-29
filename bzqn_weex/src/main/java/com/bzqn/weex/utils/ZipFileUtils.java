package com.bzqn.weex.utils;

import android.content.Context;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 陈红建 on 2016/9/26.
 * 这个方法应该在应用初始化的时候使用
 */
public class ZipFileUtils {
    public static File weexPath(Context context) {
        return new File(context.getCacheDir().getPath(), "weex");
    }

    public static void unzip(final String fromPath, final String toPath, final String password) throws ZipException {
        ZipFile zipFile = new ZipFile(fromPath);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(toPath);

    }

    public static final int FILE_EXISTS = 1;
    public static final int FILE_UNZIP_SUCCESS = 0;

    public static Observable<Integer> unzipForAssetFile(final String assetZip, final Context context, final String password) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                try {
                    File weexPath = weexPath(context);
                    File target = new File(weexPath.getAbsolutePath(), assetZip);
                    if (weexPath.exists()) {
                        subscriber.onNext(FILE_EXISTS);
                        subscriber.onCompleted();
                        return;
                    }
                    weexPath.mkdir();
                    CopyFileUtil.copyFile(target, assetZip, context);
                    unzip(target.getAbsolutePath(),weexPath.getAbsolutePath(),password);
                    //复制完成后解压缩
                    subscriber.onNext(FILE_UNZIP_SUCCESS);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                    subscriber.onCompleted();
                }

            }
        });
    }
}
