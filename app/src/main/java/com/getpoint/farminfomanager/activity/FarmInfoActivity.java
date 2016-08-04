package com.getpoint.farminfomanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.getpoint.farminfomanager.FarmInfoAppPref;
import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.GPSDeviceManager;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.markers.FramePointMarker;
import com.getpoint.farminfomanager.entity.points.BypassPoint;
import com.getpoint.farminfomanager.entity.points.ClimbPoint;
import com.getpoint.farminfomanager.entity.points.ForwardPoint;
import com.getpoint.farminfomanager.entity.points.FramePoint;
import com.getpoint.farminfomanager.entity.points.PointItemType;
import com.getpoint.farminfomanager.fragment.BaiduMapFragment;
import com.getpoint.farminfomanager.fragment.Mission.BypassPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.ClimbPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.DangerPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.ForwardPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.FramePointFragment;
import com.getpoint.farminfomanager.fragment.Mission.PointDetailFragment;
import com.getpoint.farminfomanager.utils.AttributesEvent;
import com.getpoint.farminfomanager.utils.GPS;

import com.getpoint.farminfomanager.utils.file.FileStream;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.FloatingActionButton;
import com.getpoint.farminfomanager.weights.MorphLayout;
import com.getpoint.farminfomanager.weights.dialogs.EditInputDialog;

import org.w3c.dom.Attr;

/**
 * Created by Gui Zhou on 2016-07-05.
 */
public class FarmInfoActivity extends AppCompatActivity implements
        PointDetailFragment.OnPointDetailListener, MorphLayout.OnMorphListener {

    private static final String TAG = "FarmInfoActivity";

    private FramePointFragment framePointFragment;
    private BypassPointFragment bypassPointFragment;
    private ClimbPointFragment climbPointFragment;
    private ForwardPointFragment forwardPointFragment;
    private PointDetailFragment pointDetailFragment;

    private PointItemType currentType = PointItemType.FRAMEPOINT;

    private BaiduMapFragment mapFragment;
    private FloatingActionButton mFloatingAct;
    private MorphLayout mPointInfoLayout;

    private FragmentManager fragmentManager;

    private ImageButton mGoToMyLocation;
    private ImageButton mZoomToFit;
    private ImageButton mSaveMission;
    private Button mPointOkBtn;
    private Button mPointCancelBtn;

    private FarmInfoManagerApp farmApp;
    private GPSDeviceManager gpsDeviceManager;
    private LocalBroadcastManager localBroadcastManager;
    private MissionProxy missionProxy;
    private FarmInfoAppPref farmInfoAppPref;
    private GPS gps;

    /**
     * 初始化广播事件过滤器和广播接收器
     */
    private static final IntentFilter eventFilter = new IntentFilter();

    static {
        eventFilter.addAction(AttributesEvent.GPS_MSG_BESTVEL);
        eventFilter.addAction(AttributesEvent.GPS_MSG_BESTPOS);
        eventFilter.addAction(AttributesEvent.GPS_MSG_PSRDOP);
    }

    private final BroadcastReceiver eventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            final GPS gps = farmApp.getGps();

            if (AttributesEvent.GPS_MSG_BESTPOS.equals(action)) {

            }

            if (AttributesEvent.GPS_MSG_BESTVEL.equals(action)) {

            }

            if (AttributesEvent.GPS_MSG_PSRDOP.equals(action)) {

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

        Log.i(TAG, "ACtivity FarmInfo");
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

        if (Build.VERSION.SDK_INT >= 21) {
            mFloatingAct.setElevation(R.dimen.fab_elevation);
        }

        mGoToMyLocation = (ImageButton) findViewById(R.id.my_location_button);
        mZoomToFit    = (ImageButton) findViewById(R.id.zoom_to_fit_button);
        mSaveMission  = (ImageButton) findViewById(R.id.save_mission_button);
        mPointOkBtn   = (Button) findViewById(R.id.point_ok_btn);
        mPointCancelBtn = (Button) findViewById(R.id.point_cancel_btn);

        setupMapFragment();


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
                //setupPointDetailFragment();
                addPointDetail(currentType);

            }
        });

        mGoToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.goToMyLocation();
                //LatLong latLong = new LatLong(gps.lat, gps.lon);
                //mapFragment.goToLocation(latLong);
            }
        });

        mZoomToFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mSaveMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMissionFile();
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
                //TODO
                final LatLong coord = mapFragment.getCurrentCoord();

                switch (currentType) {
                    case FRAMEPOINT:
                        addFramePoint(coord);
                        break;
                    case BYPASSPOINT:
                        addBypassPoint(coord);
                        break;
                    case CLIMBPOINT:
                        addClimbPoint(coord);
                        break;
                    case FORWAEDPOINT:
                        addForwardPoint(coord);
                        break;
                    default:
                        break;
                }

                /**
                 * 关闭取点的界面
                 */
                morphDestory();
            }
        });
    }

    private void saveMissionFile() {
        final Context context = getApplicationContext();
        final String defaultFilename =  FileStream.getWaypointFilename("points");

        final EditInputDialog dialog = EditInputDialog.newInstance(context, getString(R.string.label_enter_filename),
                defaultFilename, new EditInputDialog.Listener() {
                    @Override
                    public void onOk(CharSequence input) {
                        if (missionProxy.writeMissionToFile(input.toString())) {
                            Toast.makeText(context, R.string.file_saved_success, Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        Toast.makeText(context, R.string.file_saved_error, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        dialog.show(getSupportFragmentManager(), "Mission filename");
    }

    /**
     * 添加一个点
     */
    private void addPointDetail(PointItemType itemType) {

        switch (itemType) {

            case FRAMEPOINT:
                Log.i(TAG, "setup frame");
                setupFrameDetailFragment();
                break;
            case BYPASSPOINT:
                Log.i(TAG, "setup bypass");
                setupBypassDetailFragment();
                break;
            case CLIMBPOINT:
                Log.i(TAG, "setup climb");
                setupClimbDetailFragment();
                break;
            case FORWAEDPOINT:
                Log.i(TAG, "setup forward");
                setupForwardDetailFragment();
                break;
            default:
                break;
        }

    }

    private void setupFrameDetailFragment() {

        if(framePointFragment == null) {
            framePointFragment = new FramePointFragment();

            fragmentManager.beginTransaction().add(
                    R.id.point_detail_fragment, framePointFragment
            ).commit();
            Log.i(TAG, "new commit");
        } else {
            framePointFragment.setPointIndex(missionProxy.getCurrentFrameNumber());
        }

    }

    private void setupBypassDetailFragment() {

        if(bypassPointFragment == null) {
            bypassPointFragment = new BypassPointFragment();

            fragmentManager.beginTransaction().add(
                    R.id.point_detail_fragment, bypassPointFragment
            ).commit();
        } else {
            bypassPointFragment.setPointIndex(missionProxy.getCurrentBypassNumber());
        }

    }

    private void setupClimbDetailFragment() {

        if(climbPointFragment == null) {
            climbPointFragment = new ClimbPointFragment();

            fragmentManager.beginTransaction().add(
                    R.id.point_detail_fragment, climbPointFragment
            ).commit();
        } else {
            climbPointFragment.setPointIndex(missionProxy.getCurrentClimbNumber());
        }

    }

    private void setupForwardDetailFragment() {
        if(forwardPointFragment == null) {
            forwardPointFragment = new ForwardPointFragment();

            fragmentManager.beginTransaction().add(
                    R.id.point_detail_fragment, forwardPointFragment
            ).commit();
        } else {
            forwardPointFragment.setPointIndex(missionProxy.getCurrentForwardNumber());
        }

    }

    @Override
    public void onPointTypeChanged(PointItemType newType) {

        //addPointDetail(newType);
        currentType = newType;
        updateSetupFragment(newType);


    }

    /**
     *  添加边界点的函数
     * @param coord
     */
    private void addFramePoint(LatLong coord) {

        final FramePoint framePoint = new FramePoint(coord);

        /**
         *   把当前点添加到任务中去
         */
        MissionItemProxy newItem = new MissionItemProxy(missionProxy, framePoint);
        missionProxy.addItem(newItem);

        /**
         *  在地图上产生当前点的标志
         */
        FramePointMarker pointMarker = new FramePointMarker(newItem);
        mapFragment.updateMarker(pointMarker);
    }

    private void addBypassPoint(LatLong coord) {

        final BypassPoint bypassPoint = new BypassPoint(coord);

        MissionItemProxy newItem = new MissionItemProxy(missionProxy, bypassPoint);
        missionProxy.addItem(newItem);

        DangerPointMarker pointMarker = new DangerPointMarker(newItem);
        mapFragment.updateMarker(pointMarker);

    }

    private void addClimbPoint(LatLong coord) {

        final ClimbPoint climbPoint = new ClimbPoint(coord);

        MissionItemProxy newItem = new MissionItemProxy(missionProxy, climbPoint);
        missionProxy.addItem(newItem);

        DangerPointMarker pointMarker = new DangerPointMarker(newItem);
        mapFragment.updateMarker(pointMarker);

    }

    private void addForwardPoint(LatLong coord) {

        final ForwardPoint forwardPoint = new ForwardPoint(coord);

        MissionItemProxy newItem = new MissionItemProxy(missionProxy, forwardPoint);
        missionProxy.addItem(newItem);

        DangerPointMarker pointMarker = new DangerPointMarker(newItem);
        mapFragment.updateMarker(pointMarker);

    }

    private void updateSetupFragment(PointItemType newType) {

        Log.i(TAG, newType.getLabel());

        fragmentManager.beginTransaction().replace(
                R.id.point_detail_fragment, PointDetailFragment.newInstance(newType)
        ).commit();

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
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem toggleConnectionItem = menu.findItem(R.id.id_connect_act);

        if (gpsDeviceManager.isconnect()) {
            toggleConnectionItem.setTitle(R.string.disconnect);
            menu.setGroupEnabled(R.id.id_menu_connected, true);
            menu.setGroupVisible(R.id.id_menu_connected, true);
        } else {
            toggleConnectionItem.setTitle(R.string.connect);
            menu.setGroupEnabled(R.id.id_menu_connected, false);
            menu.setGroupVisible(R.id.id_menu_connected, false);
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
                Log.i(TAG, "Not Online");
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    @Override
    public void onBackPressed() {

        if (mPointInfoLayout.getState() == MorphLayout.State.MORPHED) {
            mFloatingAct.toggle();
            mPointInfoLayout.revert(true, true);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        farmApp.getLocalBroadcastManager().unregisterReceiver(eventReceiver);
    }
}
