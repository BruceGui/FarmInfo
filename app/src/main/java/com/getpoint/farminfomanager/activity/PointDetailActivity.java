package com.getpoint.farminfomanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private SaveMissionFragment mSaveMissionFragment;

    private MissionProxy missionProxy;

    private PointContainerAdapter mAdapter;

    private List<MissionItemProxy> stationItemProxies = new ArrayList<>();
    private List<MissionItemProxy> boundaryItemProxies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_detail_activity);

        final FarmInfoManagerApp mFarmInfoApp = (FarmInfoManagerApp) getApplication();

        missionProxy = mFarmInfoApp.getMissionProxy();
        stationItemProxies = missionProxy.getBaseStationProxies();
        boundaryItemProxies = missionProxy.getBoundaryItemProxies();

        PointContainer stationP = new PointContainer(PointItemType.STATIONPOINT
                .getLabel(), stationItemProxies);
        PointContainer frameP = new PointContainer(PointItemType.FRAMEPOINT
                .getLabel(), boundaryItemProxies);

        final List<PointContainer> containers = Arrays.asList(stationP, frameP);

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
                saveMissionFile();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveMissionFile() {

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName(getString(R.string.new_folder))
                .allowNewDirectoryNameModification(true)
                .allowReadOnlyDirectory(false)
                .build();

        mSaveMissionFragment = SaveMissionFragment.newInstance(config);


        mSaveMissionFragment.show(getFragmentManager(), null);

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
