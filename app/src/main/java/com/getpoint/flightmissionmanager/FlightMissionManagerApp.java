package com.getpoint.flightmissionmanager;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class FlightMissionManagerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        FlightMissionUtils.init(this);
    }
}
