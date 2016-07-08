package com.getpoint.farminfomanager.utils;

import com.MUHLink.Protocol.common.msg_gps_bestpos;
import com.MUHLink.Protocol.common.msg_gps_bestvel;
import com.MUHLink.Protocol.common.msg_gps_psrdop;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public class GPS {

    public double lon;
    public double lat;

    public float alt;
    public float Hdot;
    public float Vd;
    public float track;

    public long POSState;

    public float sol_age;
    public float diff_age;
    public float lon_SD;
    public float lat_SD;
    public float alt_SD;

    public short used;

    public int ANTX;
    public int ANTY;
    public int ANTZ;

    public float gdop;
    public float pdop;
    public float hdop;
    public float htdop;
    public float tdop;
    public float vdop;
    public float cutoff;

    public void getGPSBestPosMSG(msg_gps_bestpos msgGpsBestpos) {

    }

    public void getGPSBestVelMSG(msg_gps_bestvel msgGpsBestvel) {

    }

    public void getGPSPsrDopMSG(msg_gps_psrdop msgGpsPsrdop) {

    }

}
