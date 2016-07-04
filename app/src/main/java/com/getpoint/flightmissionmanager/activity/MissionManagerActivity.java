package com.getpoint.flightmissionmanager.activity;

import android.support.v4.app.Fragment;

import com.getpoint.flightmissionmanager.MissionManagerFragment;
import com.getpoint.flightmissionmanager.SingleFragmentActivity;

public class MissionManagerActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return MissionManagerFragment.newInstance();
    }

}
