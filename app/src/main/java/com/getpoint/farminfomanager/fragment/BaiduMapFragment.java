package com.getpoint.farminfomanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.markers.FramePointMarker;
import com.getpoint.farminfomanager.entity.markers.PointMarker;
import com.getpoint.farminfomanager.entity.markers.StationPointMarker;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;
import com.getpoint.farminfomanager.utils.DroneHelper;

import com.getpoint.farminfomanager.utils.collections.HashBiMap;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016-07-05.
 */

public class BaiduMapFragment extends SupportMapFragment {

    private static final String TAG = "BaiduMapFragment";

    private static final float GO_TO_MY_LOCATION_ZOOM = 19f;
    public static final int MISSION_PATH_DEFAULT_COLOR = Color.WHITE;
    public static final int MISSION_PATH_BOUNDARY_COLOR = Color.RED;
    public static final int MISSION_PATH_DANGER_COLOR = Color.GRAY;
    public static final int MISSION_PATH_DEFAULT_WIDTH = 4;

    private Polyline framePointPath;

    private final HashBiMap<PointMarker, Marker> mBiMarkersMap = new HashBiMap<>();
    private List<Polygon> mPolygonsPaths = new ArrayList<>();

    private OnMarkerClickedListener markerClickListener;
    private OnMapClickedListener mapClickedListener;
    protected MapView mMapView;
    protected LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof OnMarkerClickedListener)) {
            throw new IllegalStateException("Parent Activity must implement " +
                    OnMarkerClickedListener.class.getName());
        }

        markerClickListener = (OnMarkerClickedListener) activity;
        mapClickedListener  = (OnMapClickedListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        final FragmentActivity activity = getActivity();
        final Context context = activity.getApplicationContext();

        final View view = super.onCreateView(layoutInflater, viewGroup, bundle);


        mMapView = getMapView();
        /**
         * 设置地图相关属性
         */
        getBaiduMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
        getBaiduMap().setMyLocationEnabled(true);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);


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
     * 地图的各种监听事件
     */
    private void setupMapListener() {

        final BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               // mapClickedListener.onMapClick(DroneHelper.BaiduLatLngToCoord(latLng));
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

                //MissionItemProxy m = mBiMarkersMap.getKey(marker).getmMarkerOrigin();

                PointMarker m = mBiMarkersMap.getKey(marker);

                if (m != null) {

                    if (markerClickListener != null) {
                        markerClickListener.onMarkerClick(m);
                    }
                }

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
     * 一些在地图上标记点信息的函数
     */

    public Marker updateMarker(PointMarker markerInfo) {
        return updateMarker(markerInfo, markerInfo.isDraggable());
    }

    public Marker updateMarker(PointMarker markerInfo, boolean isDraggable) {

        final LatLong coord = markerInfo.getPosition();
        if (coord == null) {
            return null;
        }

        final LatLng position = DroneHelper.CoordToBaiduLatLang(coord);
        Marker marker = mBiMarkersMap.getValue(markerInfo);
        if (marker == null) {
            return generateMarker(markerInfo, position, isDraggable);
        } else {
            return updateMarker(marker, markerInfo, position, isDraggable);
        }

    }

    private Marker generateMarker(PointMarker markerInfo, LatLng position, boolean isDraggable) {

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .draggable(isDraggable)
                .anchor(markerInfo.getAnchorU(), markerInfo.getAnchorV());

        Bitmap markerIcon = null;

        switch (markerInfo.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                 markerIcon = ((FramePointMarker)markerInfo).getIcon(getResources());
                break;
            case DANGERPOINT:
                 markerIcon = ((DangerPointMarker)markerInfo).getIcon(getResources());
                break;
            default:
                break;
        }


        if (markerIcon != null) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        } else {
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_marker_white));
        }

        Marker marker = (Marker) getBaiduMap().addOverlay(markerOptions);
        mBiMarkersMap.put(markerInfo, marker);

        return marker;
    }

    private Marker updateMarker(Marker marker, PointMarker markerInfo, LatLng position,
                              boolean isDraggable) {

        Bitmap markerIcon = null;

        switch (markerInfo.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                markerIcon = ((FramePointMarker)markerInfo).getIcon(getResources());
                break;
            case DANGERPOINT:
                markerIcon = ((DangerPointMarker)markerInfo).getIcon(getResources());
                break;
            default:
                break;
        }

        if (markerIcon != null) {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        }
        marker.setAnchor(markerInfo.getAnchorU(), markerInfo.getAnchorV());
        marker.setPosition(position);
        marker.setDraggable(isDraggable);
        marker.setVisible(markerInfo.isVisible());

        return marker;
    }

    public void clearAllMarker() {
        getBaiduMap().clear();
        framePointPath = null;
        mBiMarkersMap.clear();
    }


    /**
     * 从 Mission 中更新地图marker和polyline的信息
     *
     * @param mission 要更新的任务
     */
    public void updateInfoFromMission(MissionProxy mission) {

        clearAllMarker();

        final List<MissionItemProxy> stationPoints = mission.getBaseStationProxies();
        final List<MissionItemProxy> boundaryPoints = mission.getBoundaryItemProxies();
        final List<MissionItemProxy> dangerPoints = mission.getDangerItemProxies();

        /**
         *   基站 marker
         */
        for (MissionItemProxy itemProxy : stationPoints) {
            itemProxy.getPointInfo().setPointType(PointItemType.STATIONPOINT);
            StationPointMarker pointMarker = new StationPointMarker(itemProxy.getPointInfo(),
                    itemProxy);
            pointMarker.setMarkerNum(mission.getOrder(itemProxy));
            updateMarker(pointMarker);
        }

        /**
         *   边界点的 marker
         */
        for (MissionItemProxy itemProxy : boundaryPoints) {
            itemProxy.getPointInfo().setPointType(PointItemType.FRAMEPOINT);
            FramePointMarker pointMarker = new FramePointMarker(itemProxy.getPointInfo(),
                                            itemProxy);
            pointMarker.setMarkerNum(mission.getOrder(itemProxy));
            updateMarker(pointMarker);
        }

        /**
         *  障碍点的 marker
         */
        for (MissionItemProxy itemProxy : dangerPoints) {

            final List<PointInfo> ips = ((DangerPoint)itemProxy.getPointInfo())
                    .getInnerPoints();

            if(ips.size() > 1) {
                drawPolylineFromPointInfo(ips);
            }

            for(PointInfo ip : ips) {
                ip.setPointType(PointItemType.DANGERPOINT);
                DangerPointMarker pointMarker = new DangerPointMarker(ip, itemProxy);
                pointMarker.setMarkerNum(ips.indexOf(ip)+1);
                pointMarker.setOrderNum(dangerPoints.indexOf(itemProxy));
                updateMarker(pointMarker);
            }
        }

        /**
         *  更新polyline信息
         *  首先清除所有的 点的画线信息。然后再更新
         */
        List<LatLong> pathCoords = new ArrayList<>();

        /**
         *  画边界点的线
         */
        updateFramePointPath(boundaryPoints);
    }

    /**
     * 画地图上的点和点之间的连线 Polyline path
     * <p>
     * 首先获得一个 List<LatLng> 的路径点的list
     * 然后再新建一个 PolylineOptions 并设置一些参数 color points
     * 接着直接 getBaiduMap().addOverlay(polyoptions);
     * <p>
     * <p>
     * public void updatePolylineFromMission(MissionProxy m) {
     * <p>
     * }
     */
    public void updateFramePointPath(List<MissionItemProxy> framepoints) {

        List<LatLong> pathCoords = new ArrayList<>();

        /**
         *  画边界点的线
         */
        for (MissionItemProxy itemProxy : framepoints) {
            pathCoords.add(itemProxy.getPointInfo().getPosition().getLatLong());
        }

        if(!pathCoords.isEmpty()) {
            pathCoords.add(pathCoords.get(0));
        }

        final List<LatLng> pathPoints = new ArrayList<>(pathCoords.size());
        for (LatLong coord : pathCoords) {
            pathPoints.add(DroneHelper.CoordToBaiduLatLang(coord));
        }

        if (pathPoints.size() <2)
        {
            if(framePointPath != null)
            {
                framePointPath.remove();
                framePointPath = null;
            }
            return;
        }

        if (framePointPath == null) {

            PolylineOptions pathOptions = new PolylineOptions();
            pathOptions.color(MISSION_PATH_BOUNDARY_COLOR).width(
                    MISSION_PATH_DEFAULT_WIDTH);
            pathOptions.points(pathPoints);
            framePointPath = (Polyline)getBaiduMap().addOverlay(pathOptions);
        }


        framePointPath.setPoints(pathPoints);

    }

    public void drawPolylineFromPointInfo(List<PointInfo> ps) {
        List<LatLong> pathCoords = new ArrayList<>();

        for(PointInfo p : ps) {
            pathCoords.add(p.getPosition().getLatLong());
        }

        if(!pathCoords.isEmpty()) {
            pathCoords.add(pathCoords.get(0));
        }

        final List<LatLng> pathPoints = new ArrayList<>(pathCoords.size());
        for (LatLong coord : pathCoords) {
            pathPoints.add(DroneHelper.CoordToBaiduLatLang(coord));
        }

        PolylineOptions pathOptions = new PolylineOptions();
        pathOptions.color(MISSION_PATH_DANGER_COLOR).width(
                MISSION_PATH_DEFAULT_WIDTH);
        pathOptions.points(pathPoints);
        getBaiduMap().addOverlay(pathOptions);
    }

    /**
     * 画线的方法 首先存储点的集合，然后再画点线
     * @param pathCoords 点的集合
     */
    public void updatePolyline(List<LatLong> pathCoords) {

        final List<LatLng> pathPoints = new ArrayList<>(pathCoords.size());
        for (LatLong coord : pathCoords) {
            pathPoints.add(DroneHelper.CoordToBaiduLatLang(coord));
        }

        PolylineOptions pathOptions = new PolylineOptions();
        pathOptions.color(MISSION_PATH_DEFAULT_COLOR)
                .width(MISSION_PATH_DEFAULT_WIDTH);
        pathOptions.points(pathPoints);
        getBaiduMap().addOverlay(pathOptions);
    }

    /**
     * 以动画的形式放大、缩小地图
     *
     * @param amount 正缩小、负放大
     */
    public void zoomMap(float amount) {
        getBaiduMap().animateMapStatus(MapStatusUpdateFactory.zoomBy(amount));
    }

    /**
     * 放大到适应坐标点系的位置
     *
     * @param coords 坐标点系
     */
    public void zoomToFit(List<LatLong> coords) {
        if (!coords.isEmpty()) {
            final List<LatLng> points = new ArrayList<>();
            for (LatLong coord : coords) {
                points.add(DroneHelper.CoordToBaiduLatLang(coord));
            }

            final LatLngBounds bounds = getBounds(points);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds);
            getBaiduMap().animateMapStatus(update);
        }
    }

    private void updateCamera(final LatLong coord) {
        if (coord != null) {
            final float zoomLevel = getBaiduMap().getMapStatus().zoom;
            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(DroneHelper.CoordToBaiduLatLang(coord), zoomLevel));
        }
    }

    public void goToMyLocation() {
        MyLocationData locationData = getBaiduMap().getLocationData();
        if (locationData != null)
            updateCamera(DroneHelper.BDLocationToCoord(locationData), GO_TO_MY_LOCATION_ZOOM);
    }

    public void goToLocation(LatLong latLng) {
        //updateCamera(DroneHelper.BaiduLatLngToCoord(latLng), GO_TO_MY_LOCATION_ZOOM);

        LatLng l = DroneHelper.CoordToBaiduLatLang(latLng);

        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(10)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(l.latitude)
                .longitude(l.longitude).build();
        getBaiduMap().setMyLocationData(locData);

        //updateCamera(latLng);
    }

    public void updateCamera(final LatLong coord, final float zoomLevel) {
        if (coord != null) {

            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(DroneHelper.CoordToBaiduLatLang(coord), zoomLevel));
        }
    }

    public LatLong getCurrentCoord() {
        return DroneHelper.BDLocationToCoord(getBaiduMap().getLocationData());
    }

    private LatLngBounds getBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        return builder.build();
    }

    /**
     * 处理地图标记的监听接口，供类实现
     */
    public interface OnMarkerClickedListener {

        boolean onMarkerClick(PointMarker m);
    }

    public interface OnMapClickedListener {
        boolean onMapClick(LatLong latLong);
    }


    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        if (mMapView != null)
            mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        if (mMapView != null)
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
