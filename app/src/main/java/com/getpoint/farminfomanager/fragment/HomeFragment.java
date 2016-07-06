package com.getpoint.farminfomanager.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.MUHLink.Connection.MUHLinkClient;
import com.MUHLink.Connection.MUHLinkConnection;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import com.getpoint.farminfomanager.utils.FarmInfoUtils;
import com.getpoint.farminfomanager.GPSDeviceManager;
import com.getpoint.farminfomanager.entity.GPSInfo;
import com.getpoint.farminfomanager.entity.NativeGPSInfo;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.activity.MissionManagerActivity;
import com.getpoint.farminfomanager.activity.NewMissionActivity;
import com.getpoint.farminfomanager.activity.SettingActivity;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private LocationClient mLocationClient;
    private double mCurrentLatitude;
    private double mCurrentLongitude;

    /*
    private TextView mConnectStateTextView;
    private TextView mGPSStateTextView;
    private TextView mSatliliteCountsTextView;
    private TextView mLonValueTextView;
    private TextView mLatValueTextView;
    private TextView mHeightValueTextView;
    private TextView mLDopTextView;
    private TextView mVDopTextView;
    private TextView mHDopTextView;*/
    private GPSInfo mGPSInfo;



    private MUHLinkClient deviceClient;
    private MUHLinkConnection deviceConnection;
    private GPSDeviceManager gpsDeviceManager;
    private LocalBroadcastManager localBroadcastManager;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == FarmInfoUtils.GPS_INFO_RESULT) {
                if (mGPSInfo.getRequestResult()) {
                    Toast.makeText(getActivity(), "get GPS info", Toast.LENGTH_SHORT).show();
                    updateGPSInfoTextView(mGPSInfo);
                } else {
                    Toast.makeText(getActivity(), "failed to get GPS info", Toast.LENGTH_SHORT).show();
                    restoreGPSInfo();
                }
            }
        }
    };

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        localBroadcastManager = LocalBroadcastManager.getInstance(activity.getApplicationContext());
        gpsDeviceManager = new GPSDeviceManager(activity.getApplicationContext(), deviceConnection,
                localBroadcastManager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initView(view);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(new ClientLocationListener());    //注册监听函数
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);

        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }

        IntentFilter intentFilter = new IntentFilter(FarmInfoUtils.GPS_INFO_RESULT);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        final MenuItem toggleConnectionItem = menu.findItem(R.id.id_connect_act);

        if(gpsDeviceManager.isconnect()) {
            toggleConnectionItem.setTitle(R.string.disconnect);
            menu.setGroupEnabled(R.id.id_menu_connected, true);
            menu.setGroupVisible(R.id.id_menu_connected, true);
        } else {
            toggleConnectionItem.setTitle(R.string.connect);
            menu.setGroupEnabled(R.id.id_menu_connected, false);
            menu.setGroupVisible(R.id.id_menu_connected, false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.id_connect_act:
                break;
            case R.id.id_new_mission:
                intent = new Intent(getActivity(), NewMissionActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.id_mission_manager:
                intent = new Intent(getActivity(), MissionManagerActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.id_on_off:
                mGPSInfo = new NativeGPSInfo(this.getActivity());
                mGPSInfo.setOnRequestResultListener(new GPSInfo.OnRequestResultListener() {
                    @Override
                    public void onRequestResult() {
                        if (mGPSInfo.getRequestResult()) {
                            Toast.makeText(getActivity(), "get GPS info", Toast.LENGTH_SHORT).show();
                            updateGPSInfoTextView(mGPSInfo);
                        } else {
                            Toast.makeText(getActivity(), "failed to get GPS info", Toast.LENGTH_SHORT).show();
                            restoreGPSInfo();
                        }
                    }
                });
                mGPSInfo.request();
                break;
            case R.id.id_setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                Log.i(TAG, "Not Online");
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    private void locateToCurrentPosition(){
    }


    private void updateGPSInfoTextView(GPSInfo gpsInfo) {
        /*
        mConnectStateTextView.setText(gpsInfo.getConectionType());
        mGPSStateTextView.setText(gpsInfo.getGPSState());
        mSatliliteCountsTextView.setText(""+gpsInfo.getGPSSatlliteCounts());
        mLonValueTextView.setText("" + gpsInfo.getGPSLon());
        mLatValueTextView.setText("" + gpsInfo.getGPSLat());
        mHeightValueTextView.setText("" + gpsInfo.getHeight());
        mLDopTextView.setText(""+gpsInfo.getLDop());
        mVDopTextView.setText(""+gpsInfo.getVDop());
        mHDopTextView.setText(""+gpsInfo.getHDop());
        */
    }

    private void restoreGPSInfo() {
        /*
        mConnectStateTextView.setText(R.string.no_link);
        mGPSStateTextView.setText(R.string.off);
        mSatliliteCountsTextView.setText("0");
        mLonValueTextView.setText(R.string.not_applicable);
        mLatValueTextView.setText(R.string.not_applicable);
        mHeightValueTextView.setText(R.string.not_applicable);
        mLDopTextView.setText(R.string.not_applicable);
        mVDopTextView.setText(R.string.not_applicable);
        mHDopTextView.setText(R.string.not_applicable);
        */
    }

    private void initView(View view) {
        mMapView = (MapView)view.findViewById(R.id.id_bmapView);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        /*
        mConnectStateTextView = (TextView) view.findViewById(R.id.id_connect_type_value);
        mGPSStateTextView = (TextView)view.findViewById(R.id.id_GPS_state_value);
        mSatliliteCountsTextView = (TextView)view.findViewById(R.id.id_GPS_statellite_counts_value);
        mLonValueTextView = (TextView)view.findViewById(R.id.id_lon_value);
        mLatValueTextView = (TextView)view.findViewById(R.id.id_lat_value);
        mHeightValueTextView = (TextView)view.findViewById(R.id.id_height_value);
        mLDopTextView = (TextView)view.findViewById(R.id.id_ldop_value);
        mVDopTextView = (TextView)view.findViewById(R.id.id_vdop_value);
        mHDopTextView = (TextView)view.findViewById(R.id.id_hdop_value);
        */

        restoreGPSInfo();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息

            Log.i("BaiduLocationApiDem", sb.toString());
            mCurrentLatitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();

            moveMapCenterByLatLng(new LatLng(mCurrentLatitude, mCurrentLongitude));
        }
    }

    private void moveMapCenterByLatLng(LatLng latLng) {
        final int targetLevel = 15;
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(targetLevel).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }
}
