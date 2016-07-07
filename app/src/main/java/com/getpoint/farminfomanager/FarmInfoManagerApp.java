package com.getpoint.farminfomanager;

import android.app.Application;
import android.support.v4.content.LocalBroadcastManager;

import com.MUHLink.Connection.MUHLinkConnection;
import com.baidu.mapapi.SDKInitializer;
import com.getpoint.farminfomanager.utils.FarmInfoUtils;

public class FarmInfoManagerApp extends Application {

    private LocalBroadcastManager localBroadcastManager;
    private GPSDeviceManager gpsDeviceManager;
    private MUHLinkConnection muhLinkConnection;
    private FarmInfoAppPref farmInfoAppPref;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        FarmInfoUtils.init(this);

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        farmInfoAppPref = new FarmInfoAppPref(getApplicationContext());
        gpsDeviceManager = new GPSDeviceManager(getApplicationContext(),
                muhLinkConnection, localBroadcastManager);

    }

    public GPSDeviceManager getGpsDeviceManager() {
        return this.gpsDeviceManager;
    }

    public LocalBroadcastManager getLocalBroadcastManager() {
        return this.localBroadcastManager;
    }
}
