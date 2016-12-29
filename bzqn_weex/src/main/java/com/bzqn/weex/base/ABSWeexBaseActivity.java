package com.bzqn.weex.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bzqn.weex.utils.BZToast;

abstract  public class ABSWeexBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void showToast(int resId) {
        BZToast.showToast(this, resId);
    }

    protected void showToast(String msg) {
        BZToast.showToast(this, msg);
    }
}
