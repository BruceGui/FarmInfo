package com.getpoint.farminfomanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.MUHLink.Connection.MUHLinkClient;
import com.MUHLink.Connection.MUHLinkConnection;
import com.getpoint.farminfomanager.FarmInfoAppPref;
import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.GPSDeviceManager;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.GPSInfo;
import com.getpoint.farminfomanager.entity.NativeGPSInfo;
import com.getpoint.farminfomanager.fragment.BaiduMapFragment;
import com.getpoint.farminfomanager.fragment.Mission.DangerPointFragment;
import com.getpoint.farminfomanager.fragment.Mission.FramePointFragment;
import com.getpoint.farminfomanager.utils.AttributesEvent;
import com.getpoint.farminfomanager.utils.GPS;
import com.getpoint.farminfomanager.utils.LatLong;

import com.getpoint.farminfomanager.weights.FloatingActionButton;
import com.getpoint.farminfomanager.weights.MorphLayout;

import org.w3c.dom.Attr;

/**
 * Created by Gui Zhou on 2016-07-05.
 */
public class FarmInfoActivity extends AppCompatActivity implements MorphLayout.OnMorphListener{

    private static final String TAG = "FarmInfoActivity";

    private FramePointFragment framePointFragment;
    private DangerPointFragment dangerPointFragment;

    private BaiduMapFragment mapFragment;
    private FloatingActionButton mFloatingAct;
    private MorphLayout mPointInfoLayout;

    private FragmentManager fragmentManager;

    private ImageButton mGoToMyLocation;
    private ImageButton mZoomToFit;
    private Button mPointOkBtn;
    private Button mPointCancelBtn;

    private FarmInfoManagerApp farmApp;
    private GPSDeviceManager gpsDeviceManager;
    private LocalBroadcastManager localBroadcastManager;
    private FarmInfoAppPref farmInfoAppPref;
    private GPS gps;

    /**
     *  初始化广播事件过滤器和广播接收器
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

            if(AttributesEvent.GPS_MSG_BESTPOS.equals(action)) {

            }

            if(AttributesEvent.GPS_MSG_BESTVEL.equals(action)) {

            }

            if(AttributesEvent.GPS_MSG_PSRDOP.equals(action)) {

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
        farmApp = (FarmInfoManagerApp)getApplication();
        gpsDeviceManager =  farmApp.getGpsDeviceManager();
        gps = farmApp.getGps();

        fragmentManager = getSupportFragmentManager();
        mFloatingAct = (FloatingActionButton)findViewById(R.id.farm_info_fab);

        mPointInfoLayout = (MorphLayout)findViewById(R.id.point_info_morph);
        mPointInfoLayout.setMorphListener(this);
        mPointInfoLayout.setFab(mFloatingAct);

        if(Build.VERSION.SDK_INT >= 21) {
            mFloatingAct.setElevation(R.dimen.fab_elevation);
        }

        mGoToMyLocation = (ImageButton)findViewById(R.id.my_location_button);
        mZoomToFit = (ImageButton)findViewById(R.id.zoom_to_fit_button);
        mPointOkBtn = (Button)findViewById(R.id.point_ok_btn);
        mPointCancelBtn = (Button)findViewById(R.id.point_cancel_btn);

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
                mFloatingAct.toggle();
                mPointInfoLayout.morph(true, true);
                setupPointDetailFragment();
            }
        });

        mGoToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mapFragment.goToMyLocation();
                LatLong latLong = new LatLong(gps.lat, gps.lon);
                mapFragment.goToLocation(latLong);
            }
        });

        mZoomToFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPointCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPointInfoLayout.getState() == MorphLayout.State.MORPHED) {
                    mFloatingAct.toggle();
                    mPointInfoLayout.revert(true, true);
                }
            }
        });
    }

    private void setupPointDetailFragment() {

        if(framePointFragment == null) {
            framePointFragment = new FramePointFragment();

            fragmentManager.beginTransaction().add(
                    R.id.point_detail_fragment, framePointFragment
            ).commit();
        }

    }

    private void setupMapFragment() {

        if(mapFragment == null) {
            mapFragment = (BaiduMapFragment) fragmentManager.findFragmentById(R.id.farm_info_map_fragment);

            if(mapFragment == null) {
                mapFragment = new BaiduMapFragment();
                fragmentManager.beginTransaction().add(
                        R.id.farm_info_map_fragment, mapFragment
                ).commit();
            }
        }

    }

    private void toggleconnection() {

        if(gpsDeviceManager.isconnect()) {
            gpsDeviceManager.disconnect();
        } else {
            gpsDeviceManager.connect(farmInfoAppPref.getConnectionParameterType());
        }

    }

    /**
     *  航点信息界面的监听函数
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

        if(gpsDeviceManager.isconnect()) {
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

        if(mPointInfoLayout.getState() == MorphLayout.State.MORPHED) {
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
