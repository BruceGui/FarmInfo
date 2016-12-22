package com.getpoint.farminfomanager.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.fragment.offlinemap.CityListFragment;
import com.getpoint.farminfomanager.fragment.offlinemap.DownloadManFragment;
import com.getpoint.farminfomanager.utils.MKOfflineMapDownloadListener;
import com.getpoint.farminfomanager.utils.adapters.offlinemapadapter.CityListAdapter;
import com.getpoint.farminfomanager.utils.adapters.offlinemapadapter.CityListParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/28.
 */

/**
 * 利用 viewpager 把页面分成两部分
 * 一部分显示 所有可用的地图，另一部分显示下载管理列表
 * <p>
 * 利用 两个 Fragment 来显示
 */

public class GetOfflineMapActivity extends AppCompatActivity implements MKOfflineMapListener,
        MKOfflineMapDownloadListener {

    private static final String TAG = "GetOfflineMap";
    private static final int PAGER_NUM = 2;

    private MKOfflineMap mOfflineMap;

    private Button mDownManBtn;
    private Button mCityListBtn;

    private ViewPager mViewPager;
    private CityListAdapter mAdapter;
    private List<CityListParent> allProvince = new ArrayList<>();

    private DownloadManFragment mDownloadFrag;
    private CityListFragment mCityListFrag;

    private  int NOTIFY_ID = 1200;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_offline_map);

        initComp();

        initListener();

    }

    private void initComp() {

        /**
         *  设置 actionbar
         */

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.offline_map_action_bar, null);

            actionBar.setCustomView(v);

        }

        /**
         *  初始化百度离线地图
         */
        mOfflineMap = ((FarmInfoManagerApp) getApplication()).getOfflineMap();
        mOfflineMap.init(this);

        mViewPager = (ViewPager) findViewById(R.id.id_offline_map_vp);
        mViewPager.setAdapter(new OfflineMapViewAdapter(getSupportFragmentManager()));

        mCityListBtn = (Button) findViewById(R.id.id_city_list_btn);
        mDownManBtn = (Button) findViewById(R.id.id_down_manager_btn);

        mDownloadFrag = DownloadManFragment.newInstance();
        mCityListFrag = CityListFragment.newInstance();

        /**
         *  显示所有 可下载的离线地图 信息
         */
        List<MKOLSearchRecord> records = mOfflineMap.getOfflineCityList();
        if (records != null) {

            for (MKOLSearchRecord r : records) {
                Log.i(TAG, r.cityID + " " + r.cityName + " " + r.cityType);

                if (r.cityType == 1) {

                    //CityDetail toAdd = new CityDetail(r.cityID, r.cityName, r.size, r.cityType);
                    for (MKOLSearchRecord cr : r.childCities) {
                        //CityDetail ctoAdd = new CityDetail(cr.cityID, cr.cityName, cr.size, cr.cityType);
                        //toAdd.addChildCity(ctoAdd);
                    }

                    allProvince.add(new CityListParent(r));
                }

            }

        }


        /**
         *  获取 已经 下线离线地图的城市
         */

        mAdapter = new CityListAdapter(getApplicationContext(), allProvince, this);
        mCityListFrag.setAllCities(allProvince);
        mCityListFrag.setmAdapter(mAdapter);
        mCityListFrag.setDownloadListener(this);

        mCityListFrag.setMKOfflineMap(mOfflineMap);
        mDownloadFrag.setMKOfflineMap(mOfflineMap);

        setDownPagerSel();

    }

    /**
     * 百度地图 离线下载的 监听函数，事件通知接口
     *
     * @param type
     * @param state
     */
    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                MKOLUpdateElement update = mOfflineMap.getUpdateInfo(state);
                if (update != null) {

                    //TODO 数据更新 和 界面显示
                    if(mViewPager.getCurrentItem() == 0) {
                        mDownloadFrag.updateProcess(update);
                    }

                }
                break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                break;
            case MKOfflineMap.TYPE_NETWORK_ERROR:
                break;
            default:
                break;
        }
    }

    private void initListener() {

        mDownManBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mCityListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setDownPagerSel();
                        break;
                    case 1:
                        setCityListPagerSel();
                        break;
                    default:
                        setDownPagerSel();
                        break;
                }
            }
        });

    }

    @Override
    public void startDownload(MKOLSearchRecord city) {

        Log.i(TAG, "City Name: " + city.cityName);
        if (hasOfflineMap(city.cityID)) {
            Toast.makeText(getApplication(), "已经下载", Toast.LENGTH_SHORT).show();
        } else {

            mOfflineMap.start(city.cityID);
            //onDowning.add(city);
            mDownloadFrag.addDownloadCity(city);
        }

    }

    /**
     * 判断是否已经下载 离线地图
     *
     * @param cityId 城市 ID
     * @return 如果已经下载 返回 true 否则 false
     */
    public boolean hasOfflineMap(int cityId) {

        ArrayList<MKOLUpdateElement> rs = mOfflineMap.getAllUpdateInfo();

        if (rs != null) {
            for (MKOLUpdateElement e : rs) {
                if (e.cityID == cityId) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * view pager 的适配器
     */
    public class OfflineMapViewAdapter extends FragmentPagerAdapter {

        public OfflineMapViewAdapter(FragmentManager fm) {
            super(fm);
        }

        //http://blog.csdn.net/qq_28919337/article/details/50911103

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return mDownloadFrag;
                case 1:
                    return mCityListFrag;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return PAGER_NUM;
        }
    }

    public void setDownPagerSel() {
        mDownManBtn.setBackgroundResource(R.drawable.borderless_button_action_bar_selector);
        mCityListBtn.setBackgroundResource(R.drawable.borderless_button_action_bar_normal);
    }

    public void setCityListPagerSel() {

        mCityListBtn.setBackgroundResource(R.drawable.borderless_button_action_bar_selector);
        mDownManBtn.setBackgroundResource(R.drawable.borderless_button_action_bar_normal);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOfflineMap.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
