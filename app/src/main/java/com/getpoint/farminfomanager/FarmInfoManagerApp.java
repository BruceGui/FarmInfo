package com.getpoint.farminfomanager;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.getpoint.farminfomanager.utils.FarmInfoUtils;

public class FarmInfoManagerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        FarmInfoUtils.init(this);
    }
}
