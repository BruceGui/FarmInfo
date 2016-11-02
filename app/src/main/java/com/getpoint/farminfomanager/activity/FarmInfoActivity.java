package com.getpoint.farminfomanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.getpoint.farminfomanager.FarmInfoAppPref;
import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.GPSDeviceManager;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.FramePointMarker;
import com.getpoint.farminfomanager.entity.markers.PointMarker;
import com.getpoint.farminfomanager.entity.markers.StationPointMarker;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.FramePoint;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.entity.points.StationPoint;
import com.getpoint.farminfomanager.entity.points.enumc.DangerPointType;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;
import com.getpoint.farminfomanager.fragment.BaiduMapFragment;
import com.getpoint.farminfomanager.fragment.Mission.BSPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.DangerPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.FramePointFragment;
import com.getpoint.farminfomanager.fragment.Mission.PointDetailFragment;
import com.getpoint.farminfomanager.fragment.OpenMissionFragment;
import com.getpoint.farminfomanager.fragment.PointEditorFragment;
import com.getpoint.farminfomanager.fragment.SaveMissionFragment;
import com.getpoint.farminfomanager.utils.AttributesEvent;
import com.getpoint.farminfomanager.utils.DirectoryChooserConfig;
import com.getpoint.farminfomanager.utils.GPS;

import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.FloatingActionButton;
import com.getpoint.farminfomanager.weights.MorphLayout;
import com.getpoint.farminfomanager.fragment.EditorChooseFragment;

import java.util.ArrayList;
import java.util.List;

import static com.getpoint.farminfomanager.utils.virtualnavbar.VirtualNavBar.getNavigationBarHeight;

/**
 * Created by Gui Zhou on 2016-07-05.
 */

public class FarmInfoActivity extends AppCompatActivity implements
        PointDetailFragment.OnPointDetailListener,
        MorphLayout.OnMorphListener,
        BaiduMapFragment.OnMarkerClickedListener,
        BaiduMapFragment.OnMapClickedListener,
        SaveMissionFragment.OnFragmentInteractionListener,
        OpenMissionFragment.OnFragmentInteractionListener,
        EditorChooseFragment.EditorListener,
        PointEditorFragment.PointEditorListener {

    private static final String TAG = "FarmInfoActivity";

    private FramePointFragment framePointFragment;
    private DangerPointFragment dangerPointFragment;
    private PointDetailFragment pointDetailFragment;
    private BSPointFragment bsPointFragment;

    private SaveMissionFragment mSaveMissionFragment;
    private OpenMissionFragment mOpenMissionFragment;

    private EditorChooseFragment mEditorChooseFragment;
    private PointEditorFragment mPointEditorFragment;

    private PointItemType currentType = PointItemType.FRAMEPOINT;

    private BaiduMapFragment mapFragment;
    private FloatingActionButton mFloatingAct;
    private MorphLayout mPointInfoLayout;

    private FragmentManager fragmentManager;

    private List<PointMarker> mPointSel = new ArrayList<>();

    //private Boolean isEditing = false;
    private Boolean onDeleting = false;

    private Menu mMenu;
    private ImageButton mGoToMyLocation;
    private ImageButton mZoomToFit;
    private Button mPointOkBtn;
    private Button mPointCancelBtn;

    private FarmInfoManagerApp farmApp;
    private GPSDeviceManager gpsDeviceManager;
    private LocalBroadcastManager localBroadcastManager;
    private MissionProxy missionProxy;
    private FarmInfoAppPref farmInfoAppPref;
    private GPS gps;

    /**
     *  显示 GPS 信息的东西
     */
    private TextView mGPSLon;
    private TextView mGPSLat;
    private TextView mGPSHei;
    private TextView mGPSgdop;
    private TextView mGPSpdop;
    private TextView mGPShdop;
    private TextView mGPStarNum;
    private TextView mGPState;

    /**
     * 初始化广播事件过滤器和广播接收器
     */
    private static final IntentFilter eventFilter = new IntentFilter();

    static {
        eventFilter.addAction(AttributesEvent.GPS_MSG_BESTVEL);
        eventFilter.addAction(AttributesEvent.GPS_MSG_BESTPOS);
        eventFilter.addAction(AttributesEvent.GPS_MSG_PSRDOP);
    }

    /**
     *  接受来自 GPS 设备的信息并更新
     */
    private final BroadcastReceiver eventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if (AttributesEvent.GPS_MSG_BESTPOS.equals(action)) {
                updateGPSBestpos();
            }

            if (AttributesEvent.GPS_MSG_BESTVEL.equals(action)) {
                updateGPSBestvel();
            }

            if (AttributesEvent.GPS_MSG_PSRDOP.equals(action)) {
                updateGPSPsrdop();
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_farm_info);

        initCompVariable();
        initCompListener();

        farmApp.getLocalBroadcastManager().registerReceiver(eventReceiver, eventFilter);

    }


    private void initCompVariable() {

        farmInfoAppPref = new FarmInfoAppPref(getApplicationContext());
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        farmApp = (FarmInfoManagerApp) getApplication();
        gpsDeviceManager = farmApp.getGpsDeviceManager();
        missionProxy = farmApp.getMissionProxy();
        gps = farmApp.getGps();

        if (pointDetailFragment == null) {
            pointDetailFragment = new PointDetailFragment();
        }

        fragmentManager = getSupportFragmentManager();
        mFloatingAct = (FloatingActionButton) findViewById(R.id.farm_info_fab);

        mPointInfoLayout = (MorphLayout) findViewById(R.id.point_info_morph);
        mPointInfoLayout.setMorphListener(this);
        mPointInfoLayout.setFab(mFloatingAct);

        /**
          *   根据虚拟键盘 动态调整布局
          */
        ViewGroup.MarginLayoutParams mvm = (ViewGroup.MarginLayoutParams) mPointInfoLayout.getLayoutParams();

        mvm.bottomMargin += getNavigationBarHeight(this);
        mvm.setMargins(mvm.leftMargin, mvm.topMargin,
                mvm.rightMargin, mvm.bottomMargin);
        mPointInfoLayout.requestLayout();

        mGoToMyLocation = (ImageButton) findViewById(R.id.my_location_button);
        mZoomToFit = (ImageButton) findViewById(R.id.zoom_to_fit_button);
        mPointOkBtn = (Button) findViewById(R.id.point_ok_btn);
        mPointCancelBtn = (Button) findViewById(R.id.point_cancel_btn);

        setupMapFragment();
        farmApp.setMapFragment(mapFragment);

        mGPSLon = (TextView) findViewById(R.id.lon_value);
        mGPSLat = (TextView) findViewById(R.id.lat_value);
        mGPSHei = (TextView) findViewById(R.id.hei_value);

        mGPSgdop = (TextView) findViewById(R.id.gps_gdop_value);
        mGPShdop = (TextView) findViewById(R.id.gps_hdop_value);
        mGPSpdop = (TextView) findViewById(R.id.gps_pdop_value);

        mGPStarNum = (TextView) findViewById(R.id.sate_count_value);
        mGPState = (TextView) findViewById(R.id.posstate_value);

        /**
         *  添加所有的fragment,并隐藏所有的fragment
         */
        initAllFragment();

        Log.i(TAG, Build.VERSION.INCREMENTAL);
        Log.i(TAG, Build.DEVICE);
        Log.i(TAG, Build.VERSION.CODENAME);
        Log.i(TAG, Build.VERSION.RELEASE);
        Log.i(TAG, "" + Build.VERSION.SDK_INT);
        Log.i(TAG, Build.BOARD);

    }

    private void initCompListener() {

        mFloatingAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morphCreate();
                addPointDetail(currentType);
                updatePointInfo(currentType);

            }
        });

        mGoToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLong coord = new LatLong(gps.lat, gps.lon);
                mapFragment.goToLocation(coord);
            }
        });

        mZoomToFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<LatLong> points = new ArrayList<>();

                for (MissionItemProxy itemProxy : missionProxy.getBoundaryItemProxies()) {
                    points.add(itemProxy.getPointInfo().getPosition().getLatLong());
                }

                mapFragment.zoomToFit(points);
            }
        });

        mPointCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPointInfoLayout.getState() == MorphLayout.State.MORPHED) {
                    morphDestory();
                }
            }
        });

        mPointOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *   获取当前点的信息，
                 *   在地图上显示
                 */
                final LatLong coord = mapFragment.getCurrentCoord();

                switch (currentType) {
                    case STATIONPOINT:
                        /*
                        if (bsPointFragment.isAddNew()) {
                            addBasePoint(coord);
                        } else {
                            bsPointFragment.updateCurrentBSP();
                        }

                        break;*/
                    case FRAMEPOINT:
                        /*
                        if (framePointFragment.isAddNew()) {
                            addFramePoint(coord, framePointFragment.getAltitude());
                        } else {
                            framePointFragment.updateCurrentFP();
                        }*/
                        morphDestory();
                        break;
                    case DANGERPOINT:
                        //if (dangerPointFragment.isAddNew()) {
                            addDangerPoint(dangerPointFragment.getCurrentDPType());
                        //} else {
                        //    dangerPointFragment.updateCurrentDP();
                        //}
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     *  更新 接受到的 GPS 信息
     */
    private void updateGPSBestpos() {

        mGPSLon.setText(String.valueOf(gps.lon));
        mGPSLat.setText(String.valueOf(gps.lat));
        mGPSHei.setText(String.valueOf(gps.alt));

        mGPStarNum.setText(String.valueOf(gps.used));
        mGPState.setText(String.valueOf(gps.POSState));

    }

    private void updateGPSBestvel() {

    }

    private void updateGPSPsrdop() {

        mGPShdop.setText(String.valueOf(gps.hdop));
        mGPSgdop.setText(String.valueOf(gps.gdop));
        mGPSpdop.setText(String.valueOf(gps.pdop));

    }

    /**
     * 初始化各种添加点的fragment
     */
    private void initAllFragment() {

        fragmentManager.beginTransaction().add(R.id.point_detail_fragment, PointDetailFragment.
                        newInstance(PointItemType.FRAMEPOINT),
                PointDetailFragment.getFragmentTag(PointItemType.FRAMEPOINT)).commit();

        fragmentManager.beginTransaction().add(R.id.point_detail_fragment, PointDetailFragment.
                        newInstance(PointItemType.STATIONPOINT),
                PointDetailFragment.getFragmentTag(PointItemType.STATIONPOINT)).commit();

        fragmentManager.beginTransaction().add(R.id.point_detail_fragment, PointDetailFragment.
                        newInstance(PointItemType.DANGERPOINT),
                PointDetailFragment.getFragmentTag(PointItemType.DANGERPOINT)).commit();

    }

    /**
     * 保存和打开任务文件的相关函数
     */

    @Override
    public void onSaveMission(@NonNull String filepath, @NonNull String filename) {

        Log.i(TAG, filepath + "/" + filename);
        final Context context = getApplicationContext();
        mSaveMissionFragment.dismiss();

        if (missionProxy.writeMissionToFile(filepath, filename)) {
            Toast.makeText(context, R.string.file_saved_success, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Toast.makeText(context, R.string.file_saved_error, Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public void onSaveCancelChooser() {
        mSaveMissionFragment.dismiss();
    }

    @Override
    public void onOpenMission(@NonNull String path, @NonNull String filename) {

        /**
         *   如果成功读取 mission ，然后就在地图上画 marker.
         */
        final Context c = getApplicationContext();

        if (missionProxy.readMissionFromFile(path)) {
            Toast.makeText(c, R.string.open_mission_success, Toast.LENGTH_SHORT).show();
            mapFragment.updateInfoFromMission(missionProxy);
        } else {
            Toast.makeText(c, R.string.open_mission_failed, Toast.LENGTH_SHORT).show();
        }

        mOpenMissionFragment.dismiss();
    }

    @Override
    public void onOpenCancelChooser() {
        mOpenMissionFragment.dismiss();
    }

    /**
     * 地图标记的点击监听函数
     *
     * @param m MissionItemProxy
     * @return b
     */
    @Override
    public boolean onMarkerClick(PointMarker m) {

        final PointItemType itemType = m.getPointInfo()
                .getPointType();

        switch (itemType) {
            case FRAMEPOINT:
                break;
            default:
                break;
        }

        if (!onDeleting) {
            mEditorChooseFragment = EditorChooseFragment.newInstance(this);
            mEditorChooseFragment.show(getSupportFragmentManager(), null);
        } else {
            changeSelPointMarker(true);
        }

        mPointSel.add(m);

        return false;
    }

    /**
     * 点击地图的监听函数
     *
     * @return
     */
    @Override
    public boolean onMapClick(LatLong latLong) {

        if (!onDeleting) {
            addFramePoint(latLong, 120);
        }

        return false;
    }

    /**
     * 编辑点的监听函数
     */
    @Override
    public void onDelete() {

        onDeleting = true;
        mEditorChooseFragment.dismiss();
        setupEditorMenu();
        changeSelPointMarker(true);

    }

    @Override
    public void onEditor() {

        mEditorChooseFragment.dismiss();
        mPointEditorFragment = PointEditorFragment.newInstance(this);
        mPointEditorFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onCancel() {

        mEditorChooseFragment.dismiss();

    }

    /**
     * 修改点高度的监听函数
     */
    @Override
    public void onPConfirm() {

        mPointEditorFragment.dismiss();
    }

    @Override
    public void onPCancel() {
        mPointEditorFragment.dismiss();
    }

    /**
     * 根据选中与否，改变点的标记状态
     *
     * @param state true 表示选中 false 表示没有选择
     */
    private void changeSelPointMarker(Boolean state) {

        for (PointMarker m : mPointSel) {
            PointItemType type = m.getPointInfo().getPointType();
            Log.i(TAG, "marker num:" + m.getMarkerNum());

            switch (type) {
                case FRAMEPOINT:
                    FramePointMarker marker = (FramePointMarker) m;
                    marker.setState(state);
                    mapFragment.updateMarker(marker);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 删除选择的点
     */
    private void deleteSelPoint() {

        List<MissionItemProxy> f = missionProxy.getBoundaryItemProxies();

        for (PointMarker m : mPointSel) {
            f.remove(m.getMissionProxy());
        }

        mPointSel.clear();
        mapFragment.updateInfoFromMission(missionProxy);

    }


    private void saveMissionFile() {

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName(getString(R.string.new_folder))
                .allowNewDirectoryNameModification(true)
                .allowReadOnlyDirectory(false)
                .build();

        mSaveMissionFragment = SaveMissionFragment.newInstance(config);

        //mMissionPagerFragment.show(getFragmentManager(), null);
        startActivity(new Intent(FarmInfoActivity.this, PointDetailActivity.class));
        //mSaveMissionFragment.show(getFragmentManager(), null);

    }

    private void openMissionFile() {

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName(getString(R.string.new_folder))
                .allowNewDirectoryNameModification(true)
                .allowReadOnlyDirectory(false)
                .build();

        mOpenMissionFragment = OpenMissionFragment.newInstance(config);

        mOpenMissionFragment.show(getFragmentManager(), null);

    }

    /**
     * 清除所有的标记点信息
     */
    private void newMissionFile() {
        missionProxy.missionClear();
        mapFragment.clearAllMarker();

        if(dangerPointFragment != null) {
            dangerPointFragment.clearVar();
        }

        if(bsPointFragment != null) {
            bsPointFragment.clearVar();
        }

        if(framePointFragment != null) {
            framePointFragment.clearVar();
        }
    }

    /**
     * 添加一个点
     */
    private void addPointDetail(PointItemType itemType) {


        switch (itemType) {

            case FRAMEPOINT:
                setupFrameDetailFragment();
                break;
            case DANGERPOINT:
                setupDangerPointFragment();
                break;
            case STATIONPOINT:
                setupBStationFragment();
                break;
            default:
                break;
        }

    }

    /**
     * 更新当前点的序号数
     *
     * @param itemType 点的类型
     */
    private void updatePointInfo(PointItemType itemType) {

        switch (itemType) {
            case FRAMEPOINT:
                if (framePointFragment == null) {
                    framePointFragment = (FramePointFragment) fragmentManager.findFragmentByTag(PointDetailFragment.getFragmentTag(
                            PointItemType.FRAMEPOINT));
                    framePointFragment.setMapFragment(mapFragment);
                }
                framePointFragment.setPointIndex(missionProxy.getCurrentFrameNumber());
                framePointFragment.setPointType(PointItemType.FRAMEPOINT.getLabel());
                framePointFragment.updatePointNum(missionProxy);
                break;
            case DANGERPOINT:
                if (dangerPointFragment == null) {
                    dangerPointFragment = (DangerPointFragment) fragmentManager.findFragmentByTag(PointDetailFragment.getFragmentTag(
                            PointItemType.DANGERPOINT));
                    dangerPointFragment.setMapFragment(mapFragment);
                }
                final int currnum = missionProxy.getCurrentDangerNumber();
                dangerPointFragment.setPointIndex(currnum);
                dangerPointFragment.setPointType(PointItemType.DANGERPOINT.getLabel());
                dangerPointFragment.updatePointIND(missionProxy);
                if(currnum == 0 && missionProxy.getDangerItemProxies().isEmpty()) {
                    dangerPointFragment.setInPointIndex(1);
                } else {
                    dangerPointFragment.updateCurrVar(missionProxy.getDangerItemProxies()
                            .get(currnum));
                }
                break;
            case STATIONPOINT:
                if (bsPointFragment == null) {
                    bsPointFragment = (BSPointFragment) fragmentManager.findFragmentByTag(PointDetailFragment.getFragmentTag(
                            PointItemType.STATIONPOINT));
                    bsPointFragment.setMapFragment(mapFragment);
                }
                bsPointFragment.setPointType(PointItemType.STATIONPOINT.getLabel());
                bsPointFragment.updatePointNum(missionProxy);
                bsPointFragment.setPointIndex(missionProxy.getCurrentBaseNumber());
                break;
            default:
                break;
        }

    }

    private void setupFrameDetailFragment() {

        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(
                PointDetailFragment.getFragmentTag(PointItemType.FRAMEPOINT)))
                .hide(fragmentManager.findFragmentByTag(
                        PointDetailFragment.getFragmentTag(PointItemType.DANGERPOINT)))
                .hide(fragmentManager.findFragmentByTag(
                        PointDetailFragment.getFragmentTag(PointItemType.STATIONPOINT)))
                .commit();

    }

    private void setupDangerPointFragment() {

        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(
                PointDetailFragment.getFragmentTag(PointItemType.DANGERPOINT)))
                .hide(fragmentManager.findFragmentByTag(
                        PointDetailFragment.getFragmentTag(PointItemType.FRAMEPOINT)))
                .hide(fragmentManager.findFragmentByTag(
                        PointDetailFragment.getFragmentTag(PointItemType.STATIONPOINT)))
                .commit();

    }

    private void setupBStationFragment() {

        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(
                PointDetailFragment.getFragmentTag(PointItemType.STATIONPOINT)))
                .hide(fragmentManager.findFragmentByTag(
                        PointDetailFragment.getFragmentTag(PointItemType.FRAMEPOINT)))
                .hide(fragmentManager.findFragmentByTag(
                        PointDetailFragment.getFragmentTag(PointItemType.DANGERPOINT)))
                .commit();

    }

    /**
     * 点类型更新时的监听函数
     *
     * @param newType 新的点类型
     */
    @Override
    public void onPointTypeChanged(PointItemType newType) {

        /**
         *  隐藏当前的 fragment
         */
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(
                PointDetailFragment.getFragmentTag(currentType)
        )).show(fragmentManager.findFragmentByTag(
                PointDetailFragment.getFragmentTag(newType))).commit();
        currentType = newType;
        updatePointInfo(currentType);
    }

    /**
     * 添加边界点的函数
     *
     * @param coord
     */
    private void addFramePoint(LatLong coord, float flyheight) {

        final FramePoint framePoint = new FramePoint(coord, flyheight);

        /**
         *   把当前点添加到任务中去
         */
        MissionItemProxy newItem = new MissionItemProxy(missionProxy, framePoint);
        missionProxy.addItem(newItem);

        /**
         *  在地图上产生当前点的标志
         */
        FramePointMarker pointMarker = (FramePointMarker) newItem.getMarker();
        pointMarker.setMarkerNum(missionProxy.getOrder(newItem));
        mapFragment.updateMarker(pointMarker);
        mapFragment.updateFramePointPath(missionProxy.getBoundaryItemProxies());

        if (framePointFragment != null) {
            framePointFragment.updatePointNum(missionProxy);
        }

        framePointFragment.setPointIndex(missionProxy.getCurrentFrameNumber());
        framePointFragment.updatePointNum(missionProxy);

    }

    private void addBasePoint(LatLong coord) {

        final StationPoint stationPoint = new StationPoint(coord, 0);

        /**
         *   把当前点添加到任务中去
         */
        MissionItemProxy newItem = new MissionItemProxy(missionProxy, stationPoint);
        missionProxy.addItem(newItem);

        /**
         *  在地图上产生当前点的标志
         */
        StationPointMarker pointMarker = (StationPointMarker) newItem.getMarker();
        pointMarker.setMarkerNum(missionProxy.getOrder(newItem));
        mapFragment.updateMarker(pointMarker);

        if (bsPointFragment != null) {
            bsPointFragment.updatePointNum(missionProxy);
        }

        bsPointFragment.setPointIndex(missionProxy.getCurrentBaseNumber());
        bsPointFragment.updatePointNum(missionProxy);
    }

    private void addDangerPoint(DangerPointType dPType) {


        List<PointInfo> p = new ArrayList<>();

        final DangerPoint dangerPoint = new DangerPoint(p);
        dangerPoint.setdPType(dPType);
        /**
         *   把当前点添加到任务中去
         */
        MissionItemProxy newItem = new MissionItemProxy(missionProxy, dangerPoint);
        missionProxy.addItem(newItem);

        final int currnum = missionProxy.getCurrentDangerNumber();
        dangerPointFragment.setPointIndex(currnum);
        dangerPointFragment.updatePointIND(missionProxy);
        dangerPointFragment.updateCurrVar(newItem);

        /**
         *  在地图上产生当前点的标志
         */

    }

    private void setupMapFragment() {

        if (mapFragment == null) {
            mapFragment = (BaiduMapFragment) fragmentManager.findFragmentById(R.id.farm_info_map_fragment);

            if (mapFragment == null) {
                mapFragment = new BaiduMapFragment();
                fragmentManager.beginTransaction().add(
                        R.id.farm_info_map_fragment, mapFragment
                ).commit();
            }
        }

    }

    private void toggleconnection() {

        if (gpsDeviceManager.isconnect()) {
            gpsDeviceManager.disconnect();
        } else {
            gpsDeviceManager.connect(farmInfoAppPref.getConnectionParameterType());
        }

    }

    /**
     * Morph Layout return
     */
    private void morphDestory() {
        mFloatingAct.toggle();
        mPointInfoLayout.revert(true, true);
    }

    private void morphCreate() {
        mFloatingAct.toggle();
        mPointInfoLayout.morph(true, true);
    }

    /**
     * 航点信息界面的监听函数
     */

    @Override
    public void onMorphEnd() {

    }

    @Override
    public void onMorphStart(int duration) {

    }

    @Override
    public void onRevertEnd() {

    }

    @Override
    public void onRevertStart(int duration) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home_fragment, menu);

        if (mMenu == null) {
            mMenu = menu;
        }

        return super.onCreateOptionsMenu(menu);
    }


    private void setupEditorMenu() {
        mMenu.setGroupVisible(R.id.menu_normal, false);
        mMenu.setGroupVisible(R.id.menu_edit, true);
        mMenu.setGroupEnabled(R.id.menu_edit, true);
        onDeleting = true;
    }

    private void setupNormalMenu() {
        mMenu.setGroupVisible(R.id.menu_normal, true);
        mMenu.setGroupVisible(R.id.menu_edit, false);
        mMenu.setGroupEnabled(R.id.menu_edit, false);
        onDeleting = false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem toggleConnectionItem = menu.findItem(R.id.id_connect_act);

        if (gpsDeviceManager.isconnect()) {
            toggleConnectionItem.setTitle(R.string.disconnect);
        } else {
            toggleConnectionItem.setTitle(R.string.connect);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.id_connect_act:
                toggleconnection();
                break;
            case R.id.id_setting:
                intent = new Intent(FarmInfoActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.id_menu_open_file:
                openMissionFile();
                break;
            case R.id.id_menu_save_file:
                saveMissionFile();
                break;
            case R.id.id_offline_map:
                startActivity(new Intent(FarmInfoActivity.this, GetOfflineMapActivity.class));
                break;
            case R.id.id_menu_new_mission:
                newMissionFile();
                break;
            case R.id.id_delete:
                deleteSelPoint();
                setupNormalMenu();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    @Override
    public void onBackPressed() {

        if (onDeleting) {

            setupNormalMenu();
            changeSelPointMarker(false);
            mPointSel.clear();

        } else if (mPointInfoLayout.getState() == MorphLayout.State.MORPHED) {
            mFloatingAct.toggle();
            mPointInfoLayout.revert(true, true);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (missionProxy != null) {
            missionProxy.missionClear();
        }
        farmApp.getLocalBroadcastManager().unregisterReceiver(eventReceiver);
    }
}
