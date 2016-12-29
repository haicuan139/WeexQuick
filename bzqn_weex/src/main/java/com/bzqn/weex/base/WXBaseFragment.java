package com.bzqn.weex.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bzqn.weex.R;
import com.bzqn.weex.utils.ScreenUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXRenderErrorCode;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.HashMap;

/**
 * Created by 陈红建 on 2016/11/3.
 */
public class WXBaseFragment extends Fragment implements IWXRenderListener, View.OnClickListener {


    public WXSDKInstance getWxsdkInstance() {
        return mWxsdkInstance;
    }

    private WXSDKInstance mWxsdkInstance;
    private FrameLayout container;
    private RelativeLayout mNoneLayout;
    private String initJson;
    private String mUrl;
    private KProgressHUD mKProgressHUD;

    /**
     * 是否可以显示HUD
     *
     * @return
     */
    public boolean showHud() {
        return true;
    }





    public int renderWidth(){
        return ScreenUtil.getDisplayWidth((AppCompatActivity) getActivity());
    }
    public int renderHeight(){
        return ScreenUtil.getDisplayHeight((AppCompatActivity) getActivity());
    }
    public void onRenderWXSDKInstanceCreate() {

    }


    public void renderUrl(final String initJson, final FrameLayout container, final String url) {
        showDefaultHud(true, "加载中请稍后..");
        this.mUrl = url;
        this.container = container;
        this.initJson = initJson;
        mWxsdkInstance = new WXSDKInstance(getActivity());
        onRenderWXSDKInstanceCreate();
        final int w = renderWidth();
        final int h = renderHeight();
        container.post(new Runnable() {
            @Override
            public void run() {
                mWxsdkInstance.renderByUrl(getClass().toString(), url, new HashMap<String, Object>(), initJson, w, h, WXRenderStrategy.APPEND_ASYNC);
            }
        });
        mWxsdkInstance.registerRenderListener(this);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoneLayout = (RelativeLayout) view.findViewById(R.id.none_layout);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        container.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        mNoneLayout.setVisibility(View.GONE);
        dismissHud();
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        dismissHud();
        if (TextUtils.equals(errCode, WXRenderErrorCode.WX_NETWORK_ERROR)) {
            mNoneLayout.setVisibility(View.VISIBLE);
            mNoneLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        renderUrl(initJson, container, mUrl);
    }

    public void showDefaultHud(boolean cancellable, String message) {
        if (!showHud()) {
            return;
        }
        if (mKProgressHUD == null) {
            mKProgressHUD = KProgressHUD.create(getActivity())
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

    public void dismissHud() {
        if (mKProgressHUD != null) {
            mKProgressHUD.dismiss();
        }
    }

}
