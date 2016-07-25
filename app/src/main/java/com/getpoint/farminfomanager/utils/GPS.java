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
    public double Hdot;
    public double Vd;
    public double track;

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

    public GPSUTCTime gpsutcTime;

    public void getGPSBestPosMSG(msg_gps_bestpos msgGpsBestpos) {

        this.lon = msgGpsBestpos.lon;
        this.lat = msgGpsBestpos.lat;
        this.alt = msgGpsBestpos.alt;

        this.lon_SD = msgGpsBestpos.lon_SD;
        this.lat_SD = msgGpsBestpos.lat_SD;
        this.alt_SD = msgGpsBestpos.alt_SD;

        this.diff_age = msgGpsBestpos.diff_age;
        this.sol_age = msgGpsBestpos.sol_age;

        this.POSState = msgGpsBestpos.POSState;
        this.used = msgGpsBestpos.used;

        this.gpsutcTime = msgGpsBestpos.gpsutcTime;

    }

    public void getGPSBestVelMSG(msg_gps_bestvel msgGpsBestvel) {

        this.Vd = msgGpsBestvel.Vd;
        this.track = msgGpsBestvel.track;
        this.Hdot = msgGpsBestvel.Hdot;

        this.gpsutcTime = msgGpsBestvel.gpsutcTime;

    }

    public void getGPSPsrDopMSG(msg_gps_psrdop msgGpsPsrdop) {

        this.gdop = msgGpsPsrdop.gdop;
        this.hdop = msgGpsPsrdop.hdop;
        this.pdop = msgGpsPsrdop.pdop;
        this.vdop = msgGpsPsrdop.vdop;
        this.tdop = msgGpsPsrdop.tdop;
        this.htdop = msgGpsPsrdop.htdop;
        this.cutoff = msgGpsPsrdop.cutoff;

        this.gpsutcTime = msgGpsPsrdop.gpsutcTime;
    }

}
