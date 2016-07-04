package com.getpoint.farminfomanager.activity;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.fragment.SettingFragment;

/**
 * Created by Gui Zhou on 2016-07-03.
 */
public class SettingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new SettingFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
