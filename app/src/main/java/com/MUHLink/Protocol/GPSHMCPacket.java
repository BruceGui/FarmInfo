package com.MUHLink.Protocol;

import com.MUHLink.Protocol.Messages.MUHLinkPayload;
import com.MUHLink.Protocol.common.CRC;
import com.MUHLink.Protocol.common.GPSCRC;

import java.io.Serializable;

/**
 * Created by Gui Zhou on 2016/11/2.
 */

public class GPSHMCPacket implements Serializable {

    protected int magic1;
    protected int magic2;

    public int len;
    public int seq;
    public int sysid;
    public int compid;
    public int msgid;

    public MUHLinkPayload payload;
    public CRC crc;

    public GPSHMCPacket() {
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
        return (payload.size() == len);
    }


    public void startCRC() {
        crc = new CRC();
    }

}
