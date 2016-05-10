package com.getpoint.flightmissionmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.getpoint.flightmissionmanager.R;

//使用百度地图返回经纬度
public class NativeGPSInfo implements GPSInfo {

    private final String LOG_TAG = "NativeGPSInfo";
    private Context mContext = null;

    private LocationClient mLocationClient;
    private double mCurrentLatitude = -1;
    private double mCurrentLongitude = -1;
    private double mCurrentHeight= -1;
    private int mSatllitesCounts = 0;
    private boolean mGpsAvailable = false;

    private OnRequestResultListener mOnRequestResultListener;


    NativeGPSInfo(Context ctx) {
        mContext = ctx;
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        mLocationClient.registerLocationListener(new ClientLocationListener());    //注册监听函数

    }

    @Override
    public void request() {
        Log.e(LOG_TAG, "request");
        initLocation();
        mLocationClient.start();
    }

    @Override
    public boolean getRequestResult() {
        return mGpsAvailable;
    }

    @Override
    public String getConectionType() {
        if (mGpsAvailable) {
            return mContext.getResources().getString(R.string.native_gps);
        } else {
            return mContext.getResources().getString(R.string.no_link);
        }
    }

    @Override
    public String getGPSState() {
        if (mGpsAvailable) {
            return mContext.getResources().getString(R.string.on);
        } else {
            return mContext.getResources().getString(R.string.off);
        }
    }

    @Override
    public int getGPSSatlliteCounts() {
        return mSatllitesCounts;
    }

    @Override
    public double getGPSLat() {
        return mCurrentLatitude;
    }

    @Override
    public double getGPSLon() {
        return mCurrentLongitude;
    }

    @Override
    public double getHeight() {
        return mCurrentHeight;
    }

    @Override
    public double getLDop() {
        return -1;
    }

    @Override
    public double getVDop() {
        return -1;
    }

    @Override
    public double getHDop() {
        return -1;
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class ClientLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.e(LOG_TAG, "onReceiveLocation");
            //Receive Location
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {// GPS定位结果
                mGpsAvailable = true;
            } else {
                mGpsAvailable = false;
            }

            mCurrentLatitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();
            mCurrentHeight  = location.getAltitude();
            mSatllitesCounts = location.getSatelliteNumber();

            mOnRequestResultListener.onRequestResult();
        }
    }


    @Override
    public void setOnRequestResultListener(OnRequestResultListener onRequestResultListener) {
        mOnRequestResultListener = onRequestResultListener;
    }

}
