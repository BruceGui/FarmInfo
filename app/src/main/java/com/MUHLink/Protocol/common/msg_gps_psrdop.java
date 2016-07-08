package com.MUHLink.Protocol.common;

import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.Messages.GPSMessage;
import com.MUHLink.Protocol.Messages.MUHLinkPayload;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public class msg_gps_psrdop extends GPSMessage {

    public float gdop;
    public float pdop;
    public float hdop;
    public float htdop;
    public float tdop;
    public float vdop;
    public float cutoff;

    public msg_gps_psrdop(GPSLinkPacket packet) {

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

        unpack(packet.payload);

    }

    @Override
    public void unpack(MUHLinkPayload payload) {

    }

    @Override
    public GPSLinkPacket pack() {
        return null;
    }

}
