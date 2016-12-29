package com.bzqn.weex.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by 陈红建 on 2016/9/28.
 */
public class DownloadUtil {
    public interface DownloadListener{
        void onSuccess(File file);
        void onFailure(HttpException error, String msg);
    }
    public static void download(String url, String toPath, final DownloadListener listener) {
        HttpUtils http = new HttpUtils();
        http.download(url,
                toPath,
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        if (listener != null){
                            listener.onSuccess(responseInfo.result);
                        }
                    }


                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (listener != null){
                            listener.onFailure(error,msg);
                        }
                    }
                });
    }
}
