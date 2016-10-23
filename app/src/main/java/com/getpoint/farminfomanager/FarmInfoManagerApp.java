package com.getpoint.farminfomanager;

import android.app.Application;
import android.support.v4.content.LocalBroadcastManager;

import com.MUHLink.Connection.MUHLinkConnection;
import com.baidu.mapapi.SDKInitializer;
import com.getpoint.farminfomanager.fragment.BaiduMapFragment;
import com.getpoint.farminfomanager.utils.FarmInfoUtils;
import com.getpoint.farminfomanager.utils.GPS;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

public class FarmInfoManagerApp extends Application {

    private LocalBroadcastManager localBroadcastManager;
    private GPSDeviceManager gpsDeviceManager;
    private MissionProxy missionProxy;
    private BaiduMapFragment mapFragment;
    private MUHLinkConnection muhLinkConnection;
    private FarmInfoAppPref farmInfoAppPref;
    private GPS gps;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        FarmInfoUtils.init(this);

        gps = new GPS();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        farmInfoAppPref = new FarmInfoAppPref(getApplicationContext());
        gpsDeviceManager = new GPSDeviceManager(getApplicationContext(),
                gps, muhLinkConnection, localBroadcastManager);
        missionProxy = new MissionProxy();

    }

    public void setMapFragment(BaiduMapFragment m) {
        this.mapFragment = m;
    }

    public BaiduMapFragment getMapFragment() {
        return this.mapFragment;
    }

    public GPS getGps() {
        return this.gps;
    }

    public GPSDeviceManager getGpsDeviceManager() {
        return this.gpsDeviceManager;
    }

    public LocalBroadcastManager getLocalBroadcastManager() {
        return this.localBroadcastManager;
    }

    public MissionProxy getMissionProxy() {
        return this.missionProxy;
    }
}
