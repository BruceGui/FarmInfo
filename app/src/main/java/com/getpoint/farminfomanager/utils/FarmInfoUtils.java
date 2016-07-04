package com.getpoint.farminfomanager.utils;

import android.content.Context;

import java.io.File;

public class FarmInfoUtils {
    //private static Context mContext;

    public static String MISSION_FILE_PATH ;
    public static final String GPS_INFO_RESULT = "GPS_INFO_RESULT";
    public static final String GPS_INFO_RESULT_CODE = "GPS_INFO_RESULT_CODE";

    public static void init(Context conetxt) {
        MISSION_FILE_PATH = conetxt.getFilesDir() + "/missionFiles/";

        File missionFileDir = new File(FarmInfoUtils.MISSION_FILE_PATH);
        if (!missionFileDir.exists()) {
            missionFileDir.mkdir();
        }
    }

    public static File MissionFile(String fileName) {
        return new File(MISSION_FILE_PATH + fileName) ;
    }
}
