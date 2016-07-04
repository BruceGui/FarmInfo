package com.getpoint.flightmissionmanager.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.getpoint.flightmissionmanager.HomeFragment;
import com.getpoint.flightmissionmanager.SingleFragmentActivity;

public class HomeActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
       return HomeFragment.newInstance();
    }
}
