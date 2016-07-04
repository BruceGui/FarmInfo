package com.getpoint.farminfomanager.activity;

import android.support.v4.app.Fragment;

import com.getpoint.farminfomanager.fragment.NewMissionFragment;

/**
 * Created by lenovo on 2016/4/26.
 */
public class NewMissionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NewMissionFragment.newInstance();
    }
}
