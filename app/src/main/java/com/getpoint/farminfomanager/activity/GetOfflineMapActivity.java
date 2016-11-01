package com.getpoint.farminfomanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.fragment.offlinemap.CityListFragment;
import com.getpoint.farminfomanager.fragment.offlinemap.DownloadManFragment;
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
        DownloadManFragment.OnOfflineMapDownloadListener {

    private static final String TAG = "GetOfflineMap";

    private static final int PAGER_NUM = 2;

    private MKOfflineMap mOfflineMap;

    private Button mDownManBtn;
    private Button mCityListBtn;

    private ViewPager mViewPager;
    private CityListAdapter mAdapter;
    private List<CityListParent> allCities = new ArrayList<>();

    private DownloadManFragment mDownloadFrag;
    private CityListFragment mCityListFrag;

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


        /**
         *  显示所有 可下载的离线地图 信息
         */
        List<MKOLSearchRecord> records = mOfflineMap.getOfflineCityList();
        if (records != null) {

            for (MKOLSearchRecord r : records) {
                Log.i(TAG, r.cityID + " " + r.cityName + " " + r.cityType);
                CityDetail toAdd = new CityDetail(r.cityID, r.cityName, r.size, r.cityType);

                if (r.cityType == 1) {
                    for (MKOLSearchRecord cr : r.childCities) {
                        CityDetail ctoAdd = new CityDetail(cr.cityID, cr.cityName, cr.size, cr.cityType);
                        toAdd.addChildCity(ctoAdd);
                    }
                }

                allCities.add(new CityListParent(toAdd));
            }

        }

        mDownloadFrag = DownloadManFragment.newInstance();
        mCityListFrag = CityListFragment.newInstance();
        mAdapter = new CityListAdapter(getApplicationContext(), allCities, this);
        mCityListFrag.setAllCities(allCities);
        mCityListFrag.setmAdapter(mAdapter);

        mCityListFrag.setMKOfflineMap(mOfflineMap);

    }

    private void initListener() {

        mDownManBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                setDownPagerSel();
            }
        });

        mCityListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                setCityListPagerSel();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                switch (position) {

                    case 1:
                        setCityListPagerSel();
                        break;
                    case 0:
                        setDownPagerSel();
                        break;
                    default:
                        break;

                }
            }
        });

    }

    /**
     * 下载 离线地图的监听信息
     */
    @Override
    public void startDownload(CityDetail city) {
        Log.i(TAG, "Start Down " + city.getCityName());
        mOfflineMap.start(city.getCityId());
        if (mDownloadFrag != null) {
            mDownloadFrag.addDownloadCity(city);
        }
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
                    Log.i(TAG, "CityName: " + update.cityName + "Ratio " + update.ratio);

                    if (mDownloadFrag != null) {
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

    /**
     * view pager 的适配器
     */
    public class OfflineMapViewAdapter extends FragmentStatePagerAdapter {


        public OfflineMapViewAdapter(FragmentManager fm) {
            super(fm);
        }

        //http://blog.csdn.net/qq_28919337/article/details/50911103

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (mDownloadFrag != null)
                        return mDownloadFrag;
                case 1:
                    if (mCityListFrag != null)
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
