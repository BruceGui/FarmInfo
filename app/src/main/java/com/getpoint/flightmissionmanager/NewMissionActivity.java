package com.getpoint.flightmissionmanager;

import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * Created by lenovo on 2016/4/26.
 */
public class NewMissionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NewMissionFragment.newInstance();
    }
}
