package com.getpoint.flightmissionmanager;

public interface GPSInfo {
    void request();
    boolean getRequestResult();
    String getConectionType();
    String getGPSState();
    int getGPSSatlliteCounts();
    double getGPSLat();
    double getGPSLon();
    double getHeight();
    double getLDop();
    double getVDop();
    double getHDop();
    void setOnRequestResultListener(OnRequestResultListener onRequestResultListener);

    interface OnRequestResultListener {
        void onRequestResult();
    }
}
