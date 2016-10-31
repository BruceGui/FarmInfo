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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.fragment.offlinemap.CityListFragment;
import com.getpoint.farminfomanager.fragment.offlinemap.DownloadManFragment;

/**
 * Created by Gui Zhou on 2016/10/28.
 */

/**
 * 利用 viewpager 把页面分成两部分
 * 一部分显示 所有可用的地图，另一部分显示下载管理列表
 * <p>
 * 利用 两个 Fragment 来显示
 */

public class GetOfflineMapActivity extends AppCompatActivity {

    private static final String TAG = "GetOfflineMap";

    private static final int PAGER_NUM = 2;

    private MKOfflineMap mOfflineMap;

    private Button mDownManBtn;
    private Button mCityListBtn;

    private ViewPager mViewPager;

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

        mOfflineMap = new MKOfflineMap();
        //mOfflineMap.init();

        mViewPager = (ViewPager) findViewById(R.id.id_offline_map_vp);
        mViewPager.setAdapter(new OfflineMapViewAdapter(getSupportFragmentManager()));

        mCityListBtn = (Button) findViewById(R.id.id_city_list_btn);
        mDownManBtn = (Button) findViewById(R.id.id_down_manager_btn);

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
     * view pager 的适配器
     */
    public static class OfflineMapViewAdapter extends FragmentStatePagerAdapter {


        public OfflineMapViewAdapter(FragmentManager fm) {
            super(fm);
        }

        //http://blog.csdn.net/qq_28919337/article/details/50911103

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return DownloadManFragment.newInstance();
                case 1:
                    return CityListFragment.newInstance();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
