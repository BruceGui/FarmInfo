package com.getpoint.farminfomanager.activity;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.getpoint.farminfomanager.fragment.MeasureFragment;

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
