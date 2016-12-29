package com.bzqn.weex.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 陈红建 on 2016/6/30.
 */
public class BZToast {
    public static void showToast(Context ctx, String str) {
        if (CheckNull.checkNullWithAnd(str)){
            return;
        }
        Toast toast = Toast.makeText(ctx,str, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showToast(Context ctx,int sid){
        if (sid == 0) {
            return;
        }
        Toast toast = Toast.makeText(ctx,sid, Toast.LENGTH_SHORT);
        toast.show();
    }
}
