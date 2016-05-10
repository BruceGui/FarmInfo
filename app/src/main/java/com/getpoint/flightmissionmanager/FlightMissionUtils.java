package com.getpoint.flightmissionmanager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.jar.Attributes;

public class FlightMissionUtils {
    //private static Context mContext;

    public static String MISSION_FILE_PATH ;
    public static final String GPS_INFO_RESULT = "GPS_INFO_RESULT";
    public static final String GPS_INFO_RESULT_CODE = "GPS_INFO_RESULT_CODE";

    public static void init(Context conetxt) {
        MISSION_FILE_PATH = conetxt.getFilesDir() + "/missionFiles/";

        File missionFileDir = new File(FlightMissionUtils.MISSION_FILE_PATH);
        if (missionFileDir.exists() == false) {
            missionFileDir.mkdir();
        }
    }

    public static File MissionFile(String fileName) {
        return new File(MISSION_FILE_PATH + fileName) ;
    }
}
