package com.MUHLink.Protocol.common;

import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.Messages.GPSMessage;
import com.MUHLink.Protocol.Messages.MUHLinkPayload;
import com.getpoint.farminfomanager.utils.GPSUTCTime;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public class msg_gps_bestvel  extends GPSMessage{

    public GPSUTCTime gpsutcTime;

    public double Vd;
    public double track;
    public double Hdot;

    public msg_gps_bestvel(GPSLinkPacket packet) {

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

        this.gpsutcTime = new GPSUTCTime(this.week, this.ms);

        unpack(packet.payload);

    }

    @Override
    public void unpack(MUHLinkPayload payload) {

        payload.setIndex(16);

        this.Vd    = payload.getDouble();
        this.track = payload.getDouble();
        this.Hdot  = payload.getDouble();


    }

    @Override
    public GPSLinkPacket pack() {
        return null;
    }

}
