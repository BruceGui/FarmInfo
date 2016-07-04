package com.getpoint.farminfomanager.activity;

import android.support.v4.app.Fragment;

import com.getpoint.farminfomanager.fragment.HomeFragment;

public class HomeActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
       return HomeFragment.newInstance();
    }
}
