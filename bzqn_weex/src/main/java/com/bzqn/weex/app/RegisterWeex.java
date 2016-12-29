package com.bzqn.weex.app;

import android.app.Application;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bzqn.weex.base.WeexBaseModel;
import com.bzqn.weex.extend.BZTextArea;
import com.squareup.picasso.Picasso;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.TextAreaEditTextDomObject;
import com.taobao.weex.dom.WXImageQuality;

import java.io.File;

/**
 * Created by 陈红建 on 2016/12/23.
 */
public class RegisterWeex {
    public interface RegisterModuleListener{
        void onRegisterModule();
    }

    public void setRegisterModuleListener(RegisterModuleListener registerModuleListener) {
        mRegisterModuleListener = registerModuleListener;
    }

    public RegisterModuleListener mRegisterModuleListener;

    /**
     * 在回调中注册额外的模块和组件
     * @param application
     * @param listener
     */
    public static void registerWithListener(Application application,RegisterModuleListener listener){
        RegisterWeex rw = new RegisterWeex();
        rw.registerWithApplication(application);
        rw.setRegisterModuleListener(listener);
    }
    public static void register(Application application){
        new RegisterWeex().registerWithApplication(application);
    }
    public  void registerWithApplication(Application application){
        initWeex(application);
    }

    private  void configWx(Application application) {
        WXSDKEngine.initialize(application,
                new InitConfig.Builder()
                        .setImgAdapter(new IWXImgLoaderAdapter() {
                            @Override
                            public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
                                if (view.getLayoutParams().width <= 0 || view.getLayoutParams().height <= 0) {
                                    return;
                                }
                                if (!TextUtils.isEmpty(url)) {
                                    try {
                                        if (url.startsWith("http://") || url.startsWith("file://")) {
                                            if (url.endsWith("gif")){
                                                //如果需要显示gif图片，需要改写WXImage源码，
                                                Glide.with(WXEnvironment.getApplication()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
                                            }else{
                                                Picasso.with(WXEnvironment.getApplication())
                                                        .load(url)
                                                        .resize(view.getLayoutParams().width, view.getLayoutParams().height)
                                                        .centerCrop()
                                                        .into(view);
                                            }

                                        } else {
                                            Picasso.with(WXEnvironment.getApplication())
                                                    .load(new File(url))
                                                    .resize(view.getLayoutParams().width, view.getLayoutParams().height)
                                                    .centerCrop()
                                                    .into(view);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        })
                        .build()
        );
    }
    private  void registerWxModule() throws WXException {
        WXSDKEngine.registerModule("nativemodel", WeexBaseModel.class);
        WXSDKEngine.registerComponent("bztext", BZTextArea.class, false);
        WXSDKEngine.registerDomObject("bztext", TextAreaEditTextDomObject.class);
        if (mRegisterModuleListener != null){
            mRegisterModuleListener.onRegisterModule();
        }
    }
    private  void initWeex(Application application) {
        try {
            registerWxModule();
            configWx(application);//initialize
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
