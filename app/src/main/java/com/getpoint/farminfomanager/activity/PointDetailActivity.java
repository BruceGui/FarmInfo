package com.getpoint.farminfomanager.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;
import com.getpoint.farminfomanager.fragment.SaveMissionFragment;
import com.getpoint.farminfomanager.utils.DirectoryChooserConfig;
import com.getpoint.farminfomanager.utils.adapters.pointdetailadapter.PointContainer;
import com.getpoint.farminfomanager.utils.adapters.pointdetailadapter.PointContainerAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.getpoint.farminfomanager.utils.file.DirectoryPath.getFarmInfoPath;

/**
 * Created by Gui Zhou on 2016/10/9.
 */

/**
 * 设置显示所有的点的信息
 * 使用 list view
 */

public class PointDetailActivity extends AppCompatActivity implements
        SaveMissionFragment.OnFragmentInteractionListener {

    private static final String TAG = "PointDetailActivity";

    private final int MY_PERMISSION_CODE_WEITE_EXTERNAL_PERMISSION = 1;

    private SaveMissionFragment mSaveMissionFragment;

    private MissionProxy missionProxy;

    private PointContainerAdapter mAdapter;

    private List<MissionItemProxy> stationItemProxies = new ArrayList<>();
    private List<MissionItemProxy> boundaryItemProxies = new ArrayList<>();
    private List<MissionItemProxy> dangerItemProxies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_detail_activity);

        final FarmInfoManagerApp mFarmInfoApp = (FarmInfoManagerApp) getApplication();

        missionProxy = mFarmInfoApp.getMissionProxy();
        stationItemProxies = missionProxy.getBaseStationProxies();
        boundaryItemProxies = missionProxy.getBoundaryItemProxies();
        dangerItemProxies = missionProxy.getAllDangerPoints();

        PointContainer stationP = new PointContainer(PointItemType.STATIONPOINT
                .getLabel(), stationItemProxies);
        PointContainer frameP = new PointContainer(PointItemType.FRAMEPOINT
                .getLabel(), boundaryItemProxies);
        PointContainer dangerP = new PointContainer(PointItemType.DANGERPOINT
                .getLabel(), dangerItemProxies);

        final List<PointContainer> containers = Arrays.asList(stationP, frameP, dangerP);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pointDetailRecView);
        mAdapter = new PointContainerAdapter(this, containers);
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onParentExpanded(int parentPosition) {

            }

            @Override
            public void onParentCollapsed(int parentPosition) {

            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.point_detail, menu);

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.id_menu_save_file:
                requestWritePermission();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveMissionFile() {

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName(getString(R.string.new_folder))
                .initialDirectory(getFarmInfoPath())
                .allowNewDirectoryNameModification(true)
                .allowReadOnlyDirectory(false)
                .build();

        mSaveMissionFragment = SaveMissionFragment.newInstance(config);


        mSaveMissionFragment.show(getFragmentManager(), null);

    }

    /**
     *  android 6.0 以上动态申请权限
     */

    private void requestWritePermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {


            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_CODE_WEITE_EXTERNAL_PERMISSION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                saveMissionFile();
            }

        } else {
            saveMissionFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CODE_WEITE_EXTERNAL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    saveMissionFile();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

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


}
