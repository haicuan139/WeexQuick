package com.bzqn.weex.http;

import android.util.Log;

import com.bzqn.weex.app.BZQNApplication;
import com.bzqn.weex.utils.ZipFileUtils;
import com.lidroid.xutils.exception.HttpException;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by 陈红建 on 2016/9/28.
 */
public class DownloadWeexZip {

    public static void downloadAndUnzip(String url, final String pass){
        DownloadUtil.download(url, new File(ZipFileUtils.weexPath(BZQNApplication.getInstance()).getAbsolutePath(),"weex_1.zip").getAbsolutePath(), new DownloadUtil.DownloadListener() {
            @Override
            public void onSuccess(final File file) {
                //下载完成之后解压
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            ZipFileUtils.unzip(file.getAbsolutePath(),ZipFileUtils.weexPath(BZQNApplication.getInstance()).getAbsolutePath(),pass);
                            subscriber.onNext("success");
                            subscriber.onCompleted();
                        } catch (ZipException e) {
                            subscriber.onError(e);
                            subscriber.onCompleted();
                            e.printStackTrace();
                        }
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("TAG",msg);
            }
        });
    }
}
