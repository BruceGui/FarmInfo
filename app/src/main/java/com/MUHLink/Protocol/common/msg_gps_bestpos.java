package com.MUHLink.Protocol.common;

import android.util.Log;

import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.Messages.GPSMessage;
import com.MUHLink.Protocol.Messages.MUHLinkPayload;
import com.getpoint.farminfomanager.utils.GPSUTCTime;

import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.StringReader;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public class msg_gps_bestpos extends GPSMessage {

    private static final String TAG = "msg_bestpos";

    public GPSUTCTime gpsutcTime;
    public long POSState;

    public double lat;
    public double lon;
    public float alt;

    public float sol_age;
    public float diff_age;
    public float lon_SD;
    public float lat_SD;
    public float alt_SD;

    public short used;

    public msg_gps_bestpos(GPSLinkPacket packet) {

        this.header_len   = packet.header_len;
        this.msg_ID       = packet.msg_ID;
        this.msg_type     = packet.msg_type;
        this.portAddr     = packet.portAddr;
        this.msg_len      = packet.msg_len;
        this.sequence     = packet.sequence;
        this.idle_time    = packet.idle_time;
        this.time_status  = packet.time_status;
        this.week         = packet.week;
        this.ms           = packet.ms;
        this.receiverStatus    = packet.receiverStatus;
        this.reserved          = packet.reserved;
        this.receiverSWVersion = packet.receiverSWVersion;

        this.gpsutcTime = new GPSUTCTime(packet.week, packet.ms);

        unpack(packet.payload);

    }

    @Override
    public void unpack(MUHLinkPayload payload) {

        payload.setIndex(4);

        this.POSState = payload.getInt();
        Log.i(TAG, "POSState: " + this.POSState);

        this.lat = payload.getDouble();
        this.lon = payload.getDouble();

        Log.i(TAG, "Latitude: " + this.lat);
        Log.i(TAG, "Longitude: " + this.lon);

        this.alt = (float) payload.getDouble();
        this.lat_SD = payload.getFloat();
        this.lon_SD = payload.getFloat();
        this.alt_SD = payload.getFloat();

        payload.setIndex(56);

        this.diff_age = payload.getFloat();
        this.sol_age = payload.getFloat();

        payload.setIndex(65);

        this.used = payload.getByte();

        Log.i(TAG, "alt: " + this.alt);
        Log.i(TAG, "alt_SD: " + this.alt_SD);
        Log.i(TAG, "lat_SD: " + this.lat_SD);
        Log.i(TAG, "lon_SD: " + this.lon_SD);
        Log.i(TAG, "diff_age: " + this.diff_age);
        Log.i(TAG, "sol_age: " + this.sol_age);
        Log.i(TAG, "used: " + this.used);

        Log.i(TAG, gpsutcTime.year + "年" +
                gpsutcTime.month + "月" +
                gpsutcTime.day + "日");

        Log.i(TAG, gpsutcTime.hour +
                ":" + gpsutcTime.minute +
                ":" + gpsutcTime.second);


    }

    @Override
    public GPSLinkPacket pack() {
        return null;
    }
}
