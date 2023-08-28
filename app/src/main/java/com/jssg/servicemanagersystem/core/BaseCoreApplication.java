package com.jssg.servicemanagersystem.core;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * Created by gongdongyang on 2018/9/17.
 */

public class BaseCoreApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static BaseCoreApplication _instance;

    public static BaseCoreApplication getInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }


}
