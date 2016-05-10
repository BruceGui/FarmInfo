package com.getpoint.flightmissionmanager;

import android.support.v4.app.Fragment;
import android.util.Log;

public class MeasureActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String fileName = getIntent().getStringExtra(MeasureFragment.MISSION_FILE_NAME);
//        if(fileName == null || fileName.length() == 0) {
//            throw new Exception("Please start me with mission file name");
//        }
        Log.e("xuepeng", fileName);
        return MeasureFragment.newInstance(fileName);

    }


}
