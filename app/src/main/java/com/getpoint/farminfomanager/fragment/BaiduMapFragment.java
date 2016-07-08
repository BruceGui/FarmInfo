package com.getpoint.farminfomanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.getpoint.farminfomanager.utils.DroneHelper;
import com.getpoint.farminfomanager.utils.LatLong;

/**
 * Created by Gui Zhou on 2016-07-05.
 */

public class BaiduMapFragment extends SupportMapFragment {

    private static final String TAG = "BaiduMapFragment";

    private static final float GO_TO_MY_LOCATION_ZOOM = 19f;

    protected MapView mMapView;
    protected LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        final FragmentActivity activity = getActivity();
        final Context context = activity.getApplicationContext();

        final View view = super.onCreateView(layoutInflater, viewGroup, bundle);


        mMapView = getMapView();
        /**
         * 设置地图相关属性
         */
        getBaiduMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        getBaiduMap().setMyLocationEnabled(true);
        MyLocationConfiguration.LocationMode mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        getBaiduMap().setMyLocationConfigeration(new
                MyLocationConfiguration(mLocationMode, true, null));
        getMapView().showZoomControls(false);
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);// 打开gps
        option.setScanSpan(0);
        option.setCoorType("bd09ll"); // 设置坐标类型

        mLocClient.setLocOption(option);
        mLocClient.start();

        return view;

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            getBaiduMap().setMyLocationData(locData);
            Log.i(TAG, "LOCATION-LISTENER");
        }

    }

    /**
     * 以动画的形式放大、缩小地图
     * @param amount 正缩小、负放大
     */
    public void zoomMap(float amount) {
        getBaiduMap().animateMapStatus(MapStatusUpdateFactory.zoomBy(amount));
    }

    private void updateCamera(final LatLong coord){
        if(coord != null){
            final float zoomLevel = getBaiduMap().getMapStatus().zoom;
            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(DroneHelper.CoordToBaiduLatLang(coord), zoomLevel));
        }
    }

    public void goToMyLocation() {
        MyLocationData locationData = getBaiduMap().getLocationData();
        if(locationData != null)
            updateCamera(DroneHelper.BDLocationToCoord(locationData), GO_TO_MY_LOCATION_ZOOM);
    }

    public void goToLocation(LatLong latLng) {
        //updateCamera(DroneHelper.BaiduLatLngToCoord(latLng), GO_TO_MY_LOCATION_ZOOM);
        updateCamera(latLng);
    }

    public void updateCamera(final LatLong coord, final float zoomLevel) {
        if (coord != null) {
            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(DroneHelper.CoordToBaiduLatLang(coord), zoomLevel));
        }
    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        if(mMapView != null)
            mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        if(mMapView != null)
            mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mLocClient.stop();
        super.onDestroy();
    }
}
