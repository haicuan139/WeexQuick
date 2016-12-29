package com.bzqn.weex.app;

/**
 * Created by 陈红建 on 2016/9/8.
 */
public class BZQNApplication extends android.support.multidex.MultiDexApplication {
    private static BZQNApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RegisterWeex.registerWithListener(this, new RegisterWeex.RegisterModuleListener() {
            @Override
            public void onRegisterModule() {
                //register other  module and component
                registerModule();
            }
        });
    }

    protected void registerModule(){

    }
    public static BZQNApplication getInstance() {
        return instance;
    }
}
