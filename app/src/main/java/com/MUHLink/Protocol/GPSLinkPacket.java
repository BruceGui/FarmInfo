package com.MUHLink.Protocol;

import com.MUHLink.Protocol.Messages.MUHLinkPayload;
import com.MUHLink.Protocol.common.GPSCRC;

import java.io.Serializable;

/**
 * Created by Gui Zhou on 2016-07-06.
 */
public class GPSLinkPacket implements Serializable {

    protected int magic1;
    protected int magic2;
    protected int magic3;

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
    //public int header[] = new int[28];

    public MUHLinkPayload payload;
    public GPSCRC crc;

    public GPSLinkPacket() {
        payload = new MUHLinkPayload();
    }

    /**
     * 检查消息是否满载
     * @return  是返回 true; 否返回 false.
     */
    public boolean payloadFilled() {
        if(payload.size() >= MUHLinkPayload.MAX_PAYLOAD_SIZE-1) {
            return true;
        }
        return (payload.size() == msg_len);
    }


    public void startCRC() {
        crc = new GPSCRC();
    }

    /**
     * 生成校验和
     */
    public void generateCRC() {

        crc = new GPSCRC();
    }

}
