package com.MUHLink.Protocol.Messages;

import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.MUHLinkPacket;

import java.io.Serializable;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public abstract class GPSMessage implements Serializable {

    public int header_len;
    public int msg_ID;
    public int msg_type;
    public int portAddr;
    public int msg_len;
    public int sequence;
    public int idle_time;
    public int time_status;
    public int week;
    public int ms;
    public int receiverStatus;
    public int reserved;
    public int receiverSWVersion;
    
    public abstract GPSLinkPacket pack();
    public abstract void unpack(MUHLinkPayload payload);
    
}
