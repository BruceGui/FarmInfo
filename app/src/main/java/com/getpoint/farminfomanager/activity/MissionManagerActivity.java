package com.getpoint.farminfomanager.activity;

import android.support.v4.app.Fragment;

import com.getpoint.farminfomanager.fragment.MissionManagerFragment;

public class MissionManagerActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return MissionManagerFragment.newInstance();
    }

}
