package com.bzqn.weex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.bzqn.weex.app.WeexConstants;
import com.bzqn.weex.base.WeexBaseActivity;
import com.bzqn.weex.utils.CheckNull;
import com.bzqn.weex.utils.FileUtils;
import com.taobao.weex.WXRenderErrorCode;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.utils.WXFileUtils;

import java.util.Map;

public class WeexPageActivity extends WeexBaseActivity implements WeexConstants{

    private RelativeLayout mNoneLayout;
    private FrameLayout mContainer;

    public static void startPageActivity(Context context, String httpjs){
        Intent intent = new Intent(context,WeexPageActivity.class);
        intent.putExtra(WeexConstants.INTENT_PAGE_URL_STR, httpjs);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = new FrameLayout(this);
        setContentView(mContainer);
        mNoneLayout = (RelativeLayout) View.inflate(this, R.layout.none_layout, null);
        mNoneLayout.setVisibility(View.GONE);
        mContainer.addView(mNoneLayout);
    }
    @Override
    protected ViewGroup initRenderContainer() {
        return mContainer;
    }
    @Override
    protected String renderPath() {
        String path = getIntent().getStringExtra(WeexConstants.INTENT_PAGE_URL_STR);
        if (CheckNull.checkNullWithAnd(path)){
            path = WeexConstants.WEEX_URL_MAIN;
        }
        return path;
    }

    @Override
    protected String renderInitJson() {
        Object obj = getIntent().getSerializableExtra(WeexConstants.INTENT_PARAMS);
        String params=null;
        if (obj instanceof Map){
            params = JSON.toJSONString(obj);
        }else if(obj instanceof String){
            params = (String)obj;
        }
        return params;
    }

    @Override
    protected String renderContent() {
        String cacheFile = getIntent().getStringExtra(WeexConstants.INTENT_CACHE_PATH);
        if (CheckNull.checkNullWithAnd(cacheFile)){
            return "";
        }else {
            if (cacheFile.startsWith("file:")){
                return WXFileUtils.loadAsset(cacheFile, this);
            }
            //加载cache中的文件
            return FileUtils.getStringForCache(cacheFile);
        }
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        super.onException(instance, errCode, msg);
        if(TextUtils.equals(errCode, WXRenderErrorCode.WX_NETWORK_ERROR)) {
            mNoneLayout.setVisibility(View.VISIBLE);
            mNoneLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    renderByUrl();
                }
            });
        }
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        super.onRenderSuccess(instance, width, height);
        mNoneLayout.setVisibility(View.GONE);
    }

}
