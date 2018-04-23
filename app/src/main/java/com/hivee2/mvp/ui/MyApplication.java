package com.hivee2.mvp.ui;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.hivee2.utils.SharePreferenceUtil;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
//        builder.setDispatcher(new Dispatcher(Executors.newFixedThreadPool(5)));
        OkHttpFinal.getInstance().init(builder.build());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JAnalyticsInterface.setDebugMode( true);
        JAnalyticsInterface.init( this);
        SDKInitializer.initialize(getApplicationContext());
        SharePreferenceUtil.init(getApplicationContext());
    }


}
