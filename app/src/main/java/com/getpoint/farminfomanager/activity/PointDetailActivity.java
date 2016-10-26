package com.getpoint.farminfomanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.fragment.SaveMissionFragment;
import com.getpoint.farminfomanager.utils.DirectoryChooserConfig;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

import java.util.ArrayList;
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
    private List<MissionItemProxy> boundaryItemProxies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_detail_activity);

        final FarmInfoManagerApp mFarmInfoApp = (FarmInfoManagerApp) getApplication();
        missionProxy = mFarmInfoApp.getMissionProxy();
        boundaryItemProxies = missionProxy.getBoundaryItemProxies();

        /**
         *  显示 边界点 详细信息
         */
        ListView framePointsList = (ListView) findViewById(R.id.framePointsList);
        FramePointsAdapter framePointsAdapter = new FramePointsAdapter(this);
        framePointsList.setAdapter(framePointsAdapter);
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
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                //NavUtils.navigateUpTo(this, new Intent(this, FarmInfoActivity.class));
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

        //mMissionPagerFragment.show(getFragmentManager(), null);
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

    /**
     * 使用 viewholder 来保持view，以免每次都是用 findviewid
     */
    static class ViewHolder {
        private TextView serialNum;
        private TextView longitude;
        private TextView latitude;
        private TextView altitude;
    }

    /**
     * 显示边界点信息的 ListView 适配器
     */

    public class FramePointsAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;

        private FramePointsAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            if (boundaryItemProxies != null) {
                Log.i(TAG, "Size: " + boundaryItemProxies.size());
                return boundaryItemProxies.size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.frame_point_detail_list, null);
                holder.serialNum = (TextView) convertView.findViewById(R.id.pointSerialNum);
                holder.longitude = (TextView) convertView.findViewById(R.id.pointLongitude);
                holder.latitude = (TextView) convertView.findViewById(R.id.pointLatitude);
                holder.altitude = (TextView) convertView.findViewById(R.id.pointHeight);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /**
             *  这里要使用 String.valueOf() 否则会产生 ResourceNotFound 异常
             */
            holder.serialNum.setText(String.valueOf(position + 1));
            holder.longitude.setText(String.valueOf(boundaryItemProxies.get(position)
                    .getPointInfo()
                    .getPosition()
                    .getLatLong()
                    .getLongitude()));
            holder.latitude.setText(String.valueOf(boundaryItemProxies.get(position)
                    .getPointInfo()
                    .getPosition()
                    .getLatLong()
                    .getLatitude()));
            holder.altitude.setText(String.valueOf(boundaryItemProxies.get(position)
                    .getPointInfo()
                    .getPosition()
                    .getAltitude()));

            return convertView;
        }
    }
}
