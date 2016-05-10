package com.getpoint.flightmissionmanager;

import android.support.v4.app.Fragment;

public class MissionManagerActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return MissionManagerFragment.newInstance();
    }

}
