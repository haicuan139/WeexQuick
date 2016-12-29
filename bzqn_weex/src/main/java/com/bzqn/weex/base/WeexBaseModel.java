package com.bzqn.weex.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.bzqn.weex.WeexPageActivity;
import com.bzqn.weex.utils.CheckNull;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;

import java.util.Map;

/**
 * Created by 陈红建 on 2016/9/8.
 */
public class WeexBaseModel extends WXModule {


    public static final String SHARE_CONFIG = "wx_config";

    @WXModuleAnno(runOnUIThread = true)
    public void startWXPage(Map<String, String> params) {
        Intent intent = new Intent(mWXSDKInstance.getContext(), WeexPageActivity.class);
        for (String key : params.keySet()) {
            intent.putExtra(key, params.get(key));
        }
        mWXSDKInstance.getContext().startActivity(intent);
    }

    @WXModuleAnno(runOnUIThread = true)
    public void startActivity(String classname, Map<String, Object> params) {
        Intent intent = new Intent();
        if (!CheckNull.checkNullWithAnd(classname)) {
            intent.setClassName(mWXSDKInstance.getContext(), classname);
        }
        if (!CheckNull.checkNullWithAnd(params) && params.size() > 0) {
            for (String key : params.keySet()) {
                Object obk = params.get(key);
                if (obk instanceof Boolean) {
                    intent.putExtra(key, Boolean.parseBoolean(obk.toString()));
                }
                if (obk instanceof Integer) {
                    intent.putExtra(key, Integer.parseInt(obk.toString()));
                }
                if (obk instanceof Float) {
                    intent.putExtra(key, Float.parseFloat(obk.toString()));
                }
                if (obk instanceof String) {
                    intent.putExtra(key, obk.toString());
                    if ("action".equals(key)) {
                        intent.setAction(params.get(key).toString());
                    } else if ("actionData".equals(key)) {
                        String urivalue = params.get(key).toString();
                        if (!CheckNull.checkNullWithAnd(urivalue)) {
                            intent.setData(Uri.parse(urivalue));
                        }
                    }
                }
                if (obk instanceof Double) {
                    intent.putExtra(key, Double.parseDouble(obk.toString()));
                }
                if (obk instanceof Long) {
                    intent.putExtra(key, Long.parseLong(obk.toString()));
                }

            }
        }
        mWXSDKInstance.getContext().startActivity(intent);
    }

    @WXModuleAnno(runOnUIThread = true)
    public void spSetValue(String key, String value) {
        SharedPreferences sp = mWXSDKInstance.getContext().getSharedPreferences(SHARE_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @WXModuleAnno(runOnUIThread = true)
    public void spGetValue(String key, String defaultValue, String jsCallback) {
        SharedPreferences sp = mWXSDKInstance.getContext().getSharedPreferences(SHARE_CONFIG, Context.MODE_PRIVATE);
        String value = sp.getString(key, defaultValue);
        WXBridgeManager wxBridgeManager = WXBridgeManager.getInstance();
        wxBridgeManager.callback(mWXSDKInstance.getInstanceId(), jsCallback, value);
    }

    @WXModuleAnno(runOnUIThread = true)
    public void log(String str) {
        Log.i("WX-LOG", str);
    }

}
