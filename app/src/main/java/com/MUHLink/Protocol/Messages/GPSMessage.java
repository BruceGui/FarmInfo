package com.MUHLink.Protocol.Messages;

import com.MUHLink.Protocol.GPSLinkPacket;

import java.io.Serializable;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public abstract class GPSMessage implements Serializable {

    protected int header_len;
    protected int msg_ID;
    protected int msg_type;
    protected int portAddr;
    protected int msg_len;
    protected int sequence;
    protected int idle_time;
    protected int time_status;
    protected int week;
    public int ms;
    protected int receiverStatus;
    public int reserved;
    protected int receiverSWVersion;
    
    public abstract GPSLinkPacket pack();
    public abstract void unpack(MUHLinkPayload payload);
    
}
