package com.bzqn.weex.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.bzqn.weex.utils.CheckNull;
import com.bzqn.weex.utils.ScreenUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.HashMap;
import java.util.Map;

abstract public class WeexBaseActivity extends ABSWeexBaseActivity implements IWXRenderListener, Runnable {


    private KProgressHUD mKProgressHUD;

    @Override
    public void run() {
        render();
    }

    private AppCompatActivity mActivity;
    private WXSDKInstance mInstance;

    public ViewGroup getContainer() {
        return mContainer;
    }

    public void setContainer(ViewGroup container) {
        mContainer = container;
    }

    private ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        createWeexInstance();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        renderByUrl();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        renderByUrl();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        renderByUrl();
    }

    private void destoryWeexInstance() {
        if (mInstance != null) {
            mInstance.registerRenderListener(null);
            mInstance.destroy();
            mInstance = null;
        }
    }

    private void createWeexInstance() {
        destoryWeexInstance();
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        mInstance = new WXSDKInstance(this);
        mInstance.registerRenderListener(this);
    }

    public void showDefaultLoadingHud() {

        showDefaultHud("加载中..");
    }

    public void showDefaultHud(String message) {
        showDefaultHud(true, message);
    }

    public void dismissHud() {
        if (showRenderHUD()) {
            mKProgressHUD.dismiss();
        }
    }

    public void showDefaultHud(boolean cancellable, String message) {
        if (mKProgressHUD == null) {
            mKProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setCancellable(cancellable)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }
        if (!mKProgressHUD.isShowing()) {
            mKProgressHUD.show();
        }

    }

    final protected void renderByUrl() {
        if (mContainer == null) {
            mContainer = initRenderContainer();
        }
        if (mContainer != null) {
            if (showRenderHUD()) {
                showDefaultLoadingHud();
            }
            mContainer.post(this);
        }
    }

    boolean showRenderHUD() {
        return true;
    }

    protected abstract ViewGroup initRenderContainer();

    private void render() {
        String path = renderPath();
        //优先渲染 renderContent
        String content = renderContent();
        if (CheckNull.checkNullWithAnd(path) && CheckNull.checkNullWithAnd(content)) {
            throw new RuntimeException("renderUrl is null");
        }
        if (!path.startsWith("http://") && CheckNull.checkNullWithAnd(content)) {
            throw new RuntimeException("renderUrl need startsWith http://");
        }
        Map<String, Object> opt = renderOption();
        if (opt == null) {
            opt = new HashMap<>();
        }
        String initJson = renderInitJson();
        int w = renderWidth();
        if (w == 0) {
            w = ScreenUtil.getDisplayWidth(mActivity);
        }
        int h = renderHeight();
        if (h == 0) {
            h = ScreenUtil.getDisplayHeight(mActivity);
        }
        String pageName = renderPageName();

        if (!CheckNull.checkNullWithAnd(content)) {
            mInstance.render(pageName, content, opt, initJson, w, h, WXRenderStrategy.APPEND_ASYNC);
        } else {
            if (CheckNull.checkNullWithAnd(initJson)){
                initJson = JSON.toJSONString(new HashMap<String,Object>());
            }
            mInstance.renderByUrl(pageName, path, opt, initJson, w, h, WXRenderStrategy.APPEND_ASYNC);
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        if (mContainer != null) {
            mContainer.addView(view);
        }
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        dismissHud();
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        showToast(msg);
        dismissHud();
    }

    protected abstract String renderPath();

    protected String renderInitJson() {
        return null;
    }

    protected String renderPageName() {
        return "";
    }

    protected String renderContent() {
        return "";
    }

    protected Map<String, Object> renderOption() {
        return null;
    }


    protected int renderWidth() {
        return 0;
    }

    protected int renderHeight() {
        return 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mInstance != null) {
            mInstance.onActivityStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInstance != null) {
            mInstance.onActivityResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mInstance != null) {
            mInstance.onActivityPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mInstance != null) {
            mInstance.onActivityStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mInstance != null) {
            mInstance.onActivityDestroy();
        }
    }


    /**
     *  var globalEvent = require('@weex-module/globalEvent');
     *   globalEvent.addEventListener("geolocation", function (e) {
     *   console.log("get geolocation")
     *   });
     * @param key
     * @param params
     */
    public void callJsFunc(String key, Map<String, Object> params) {
        if (CheckNull.checkNullWithAnd(params)) {
            params = new HashMap<>();
        }
        mInstance.fireGlobalEventCallback(key, params);
    }

}
