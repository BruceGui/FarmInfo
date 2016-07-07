package com.MUHLink.Protocol;

import com.MUHLink.Protocol.Messages.MUHLinkPayload;
import com.MUHLink.Protocol.common.CRC;
import com.MUHLink.Protocol.common.GPSCRC;

import java.io.Serializable;

/**
 * Created by Gui Zhou on 2016-07-06.
 */
public class GPSLinkPacket implements Serializable {

    public int magic1;
    public int magic2;
    public int magic3;

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
    public int header[] = new int[28];

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

    /**
     * 生成校验和
     */
    public void generateCRC() {

        //TODO
        crc = new GPSCRC();

        crc.update_checksum(magic1);
        crc.update_checksum(magic2);
        crc.update_checksum(magic3);
        crc.update_checksum(header_len);
        crc.update_checksum(msg_ID);
        crc.update_checksum(msg_type);
        crc.update_checksum(portAddr);
        crc.update_checksum(msg_len);
        crc.update_checksum(sequence);
        crc.update_checksum(idle_time);
        crc.update_checksum(time_status);
        crc.update_checksum(week);
        crc.update_checksum(ms);
        crc.update_checksum(receiverStatus);
        crc.update_checksum(reserved);
        crc.update_checksum(receiverSWVersion);

        payload.resetIndex();
        for(int i = 0; i < payload.size(); i++) {
            crc.update_checksum(payload.getByte());
        }
    }

}
