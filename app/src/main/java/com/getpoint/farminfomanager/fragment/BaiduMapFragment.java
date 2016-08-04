package com.getpoint.farminfomanager.fragment;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.PointMarker;
import com.getpoint.farminfomanager.utils.DroneHelper;

import com.getpoint.farminfomanager.utils.collections.HashBiMap;

/**
 * Created by Gui Zhou on 2016-07-05.
 */

public class BaiduMapFragment extends SupportMapFragment {

    private static final String TAG = "BaiduMapFragment";

    private static final float GO_TO_MY_LOCATION_ZOOM = 19f;

    private final HashBiMap<PointMarker, Marker> mBiMarkersMap = new HashBiMap<>();

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
        getMapView().showZoomControls(false);

        MyLocationConfiguration.LocationMode mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        getBaiduMap().setMyLocationConfigeration(new
                MyLocationConfiguration(mLocationMode, true, null));

        setupMapListener();

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setScanSpan(0);
        option.setCoorType("bd09ll"); // 设置坐标类型

        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        mLocClient.setLocOption(option);
        mLocClient.start();

        return view;

    }

    /**
     *  地图的各种监听事件
     */
    private void setupMapListener() {

        final BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        };

        getBaiduMap().setOnMapClickListener(onMapClickListener);

        getBaiduMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
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
     *  一些在地图上标记点信息的函数
     */

    public void updateMarker(PointMarker markerInfo) {
        updateMarker(markerInfo, markerInfo.isDraggable());
    }

    public void updateMarker(PointMarker markerInfo, boolean isDraggable) {

        final LatLong coord = markerInfo.getPosition();
        if(coord == null) {
            return;
        }

        final LatLng position = DroneHelper.CoordToBaiduLatLang(coord);
        Marker marker = mBiMarkersMap.getValue(markerInfo);
        if(marker == null) {
            generateMarker(markerInfo, position, isDraggable);
        } else {
            updateMarker(marker, markerInfo, position, isDraggable);
        }

    }

    private void generateMarker(PointMarker markerInfo, LatLng position, boolean isDraggable) {

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .draggable(isDraggable)
                .anchor(markerInfo.getAnchorU(), markerInfo.getAnchorV());

        final Bitmap markerIcon = markerInfo.getIcon(getResources());

        if(markerIcon != null) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        } else {
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_marker_white));
        }

        Marker marker = (Marker)getBaiduMap().addOverlay(markerOptions);
        mBiMarkersMap.put(markerInfo, marker);
    }

    private void updateMarker(Marker marker, PointMarker markerInfo, LatLng position,
                              boolean isDraggable) {
        final Bitmap markerIcon = markerInfo.getIcon(getResources());
        if (markerIcon != null) {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        }
        marker.setAnchor(markerInfo.getAnchorU(), markerInfo.getAnchorV());
        marker.setPosition(position);
        marker.setDraggable(isDraggable);
        marker.setVisible(markerInfo.isVisible());
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

    public LatLong getCurrentCoord() {
        return DroneHelper.BDLocationToCoord(getBaiduMap().getLocationData());
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
