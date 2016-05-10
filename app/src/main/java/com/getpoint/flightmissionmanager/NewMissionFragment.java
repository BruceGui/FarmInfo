package com.getpoint.flightmissionmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getpoint.flightmissionmanager.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class NewMissionFragment extends Fragment {

    private static final String LOG_TAG = "NewMissionFragment";

    private TextView mNameTextView;
    private TextView mSiteTextView;
    private TextView mTimeTextView;
    private TextView mCommentsTextView;

    private Button mAddButton;
    private Button mCancelButton;

    private String mNewMissionFileName;

    public static NewMissionFragment newInstance() {
        return new NewMissionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_missiom_fragment, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mNameTextView = (TextView) view.findViewById(R.id.id_new_mission_name);
        mTimeTextView = (TextView) view.findViewById(R.id.id_new_mission_time);
        mSiteTextView = (TextView) view.findViewById(R.id.id_new_mission_site);
        mCommentsTextView = (TextView) view.findViewById(R.id.id_new_mission_comments);

        mAddButton = (Button) view.findViewById(R.id.id_new_mission_add_button);
        mCancelButton = (Button) view.findViewById(R.id.id_new_mission_cancel_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createNewMissionFile()) {
                    startMeasureActivity();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private boolean createNewMissionFile() {
        mNewMissionFileName = "" + mNameTextView.getText() + mTimeTextView.getText()
                              + mSiteTextView.getText() + mCommentsTextView.getText();
        if (mNewMissionFileName.length() == 0) {
            Toast.makeText(getActivity(), "Please input the infomation", Toast.LENGTH_SHORT).show();
            return false;
        }

        mNewMissionFileName = mNewMissionFileName + ".txt";

        try {
            File file = new File(FlightMissionUtils.MISSION_FILE_PATH + mNewMissionFileName);
            Log.e(LOG_TAG, file.getAbsolutePath());

            if(file.exists() == false) {
                Log.d(LOG_TAG, "file does not exit");
                file.createNewFile();
                testCode(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void startMeasureActivity() {
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra(MeasureFragment.MISSION_FILE_NAME, mNewMissionFileName);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private void testCode(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (int i = 0 ; i < 10; i++) {
                double lat =  31.91059108 + 0.2 * (Math.random()-0.5);
                double lon =  118.822481 + 0.2 * (Math.random()-0.5);

                String s = "1 " + (i+1) + "    " + lon +  "    " + lat  + "   0"  + "   0" + "   0" + "    0";
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
