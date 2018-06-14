package com.liumeo.jizhi;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class YouKnowApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
