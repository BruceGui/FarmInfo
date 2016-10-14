package com.getpoint.farminfomanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;
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

public class PointDetailActivity extends AppCompatActivity {

    private static final String TAG = "PointDetailActivity";

    private List<MissionItemProxy> boundaryItemProxies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_detail_activity);

        final FarmInfoManagerApp mFarmInfoApp = (FarmInfoManagerApp) getApplication();
        MissionProxy mMissionProxy = mFarmInfoApp.getMissionProxy();
        boundaryItemProxies = mMissionProxy.getBoundaryItemProxies();

        /**
         *  显示 边界点 详细信息
         */
        ListView framePointsList = (ListView) findViewById(R.id.framePointsList);
        FramePointsAdapter framePointsAdapter = new FramePointsAdapter(this);
        framePointsList.setAdapter(framePointsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, FarmInfoActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 使用 viewholder 来保持view，以免每次都是用 findviewid
     */
    static class ViewHolder {
        private TextView serialNum;
        private TextView longitude;
        private TextView latitude;
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

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /**
             *  这里要使用 String.valueOf() 否则会产生 ResourceNotFound 异常
             */
            holder.serialNum.setText(String.valueOf(position+1));
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

            return convertView;
        }
    }
}