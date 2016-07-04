package com.getpoint.flightmissionmanager.activity;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.getpoint.flightmissionmanager.NewMissionFragment;
import com.getpoint.flightmissionmanager.SingleFragmentActivity;

/**
 * Created by lenovo on 2016/4/26.
 */
public class NewMissionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NewMissionFragment.newInstance();
    }
}
