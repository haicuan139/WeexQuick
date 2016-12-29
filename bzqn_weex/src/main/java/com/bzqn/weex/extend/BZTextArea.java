package com.bzqn.weex.extend;

import android.view.Gravity;
import android.widget.EditText;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.Textarea;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by 陈红建 on 2016/9/7.
 */
public class BZTextArea extends Textarea {
    public BZTextArea(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
        super(instance, dom, parent, isLazy);
    }
    @WXComponentProp(name = "gravity")
    public void setGravity(String gravity) {
        if ("start".equals(gravity)){
            ((EditText) mHost).setGravity(Gravity.START);
        }
    }
}
