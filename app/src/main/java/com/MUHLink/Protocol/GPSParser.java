package com.MUHLink.Protocol;

import com.MUHLink.Protocol.enums.MUH_MSG_ID;

/**
 * Created by Gui Zhou on 2016-07-06.
 */
public class GPSParser {

    private static final String TAG = "GPSParser";

    /**
     * 解包状态
     */
    enum Datalink_states {
        GPSLINK_PARSE_STATE_UNINIT,
        GPSLINK_PARSE_STATE_IDLE,
        GPSLINK_PARSE_STATE_GOT_STX1,
        GPSLINK_PARSE_STATE_GOT_STX2,
        GPSLINK_PARSE_STATE_GOT_STX3,
        GPSLINK_PARSE_STATE_GOT_HEADERLEN,
        GPSLINK_PARSE_STATE_GOT_MSGID0,
        GPSLINK_PARSE_STATE_GOT_MSGID1,
        GPSLINK_PARSE_STATE_GOT_MSGTYPE,
        GPSLINK_PARSE_STATE_GOT_PORTADDR,
        GPSLINK_PARSE_STATE_GOT_MSGLEN0,
        GPSLINK_PARSE_STATE_GOT_MSGLEN1,
        GPSLINK_PARSE_STATE_GOT_SEQUENCE0,
        GPSLINK_PARSE_STATE_GOT_SEQUENCE1,
        GPSLINK_PARSE_STATE_GOT_IDLETIME,
        GPSLINK_PARSE_STATE_GOT_TIMESTATUS,
        GPSLINK_PARSE_STATE_GOT_WEEK0,
        GPSLINK_PARSE_STATE_GOT_WEEK1,
        GPSLINK_PARSE_STATE_GOT_MS0,
        GPSLINK_PARSE_STATE_GOT_MS1,
        GPSLINK_PARSE_STATE_GOT_MS2,
        GPSLINK_PARSE_STATE_GOT_MS3,
        GPSLINK_PARSE_STATE_GOT_RECSTATUS0,
        GPSLINK_PARSE_STATE_GOT_RECSTATUS1,
        GPSLINK_PARSE_STATE_GOT_RECSTATUS2,
        GPSLINK_PARSE_STATE_GOT_RECSTATUS3,
        GPSLINK_PARSE_STATE_GOT_RESERVED0,
        GPSLINK_PARSE_STATE_GOT_RESERVED1,
        GPSLINK_PARSE_STATE_GOT_RECVERSION0,
        GPSLINK_PARSE_STATE_GOT_RECVERSION1,
        GPSLINK_PARSE_STATE_GOT_PAYLOAD,
        GPSLINK_PARSE_STATE_GOT_CK0,
        GPSLINK_PARSE_STATE_GOT_CK1,
        GPSLINK_PARSE_STATE_GOT_CK2
    }

    Datalink_states state = Datalink_states.GPSLINK_PARSE_STATE_UNINIT;

    static boolean msg_received;
    private GPSLinkPacket m;

    /**
     * 这是一个比较方便的解析数据的方法。一次解析一个字符，如果成功将返回
     * 解析的整个数据包，并记录相关失败的信息。
     *
     * @param c 要解析的字符
     * @return 成功返回解析的包
     */
    public GPSLinkPacket datalink_parser_char(int c) {

        msg_received = false;

        switch (state) {
            case GPSLINK_PARSE_STATE_UNINIT:
            case GPSLINK_PARSE_STATE_IDLE:
                if (c == MUH_MSG_ID.GPSLINK_STX1) {
                    m.magic1 = c;
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX1;
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_STX1:
                if (c == MUH_MSG_ID.GPSLINK_STX2) {
                    m.magic2 = c;
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX2;
                } else {
                    if (c == MUH_MSG_ID.GPSLINK_STX1) {
                        m.magic1 = c;
                        state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX1;
                    } else {
                        state = Datalink_states.GPSLINK_PARSE_STATE_IDLE;
                    }
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_STX2:
                if (c == MUH_MSG_ID.GPSLINK_STX3) {
                    m.magic3 = c;
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX3;
                } else {
                    if (c == MUH_MSG_ID.GPSLINK_STX1) {
                        m.magic1 = c;
                        state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX1;
                    } else {
                        state = Datalink_states.GPSLINK_PARSE_STATE_IDLE;
                    }
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_STX3:
                m.header_len = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_HEADERLEN;
                break;
            case GPSLINK_PARSE_STATE_GOT_HEADERLEN:
                m.msg_ID = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MSGID0;
                break;
            case GPSLINK_PARSE_STATE_GOT_MSGID0:
                m.msg_ID = c << 8 | m.msg_ID;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MSGID1;
                break;
            case GPSLINK_PARSE_STATE_GOT_MSGID1:
                m.msg_type = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MSGTYPE;
                break;
            case GPSLINK_PARSE_STATE_GOT_MSGTYPE:
                m.portAddr = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_PORTADDR;
                break;
            case GPSLINK_PARSE_STATE_GOT_PORTADDR:
                m.msg_len = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MSGLEN0;
                break;
            case GPSLINK_PARSE_STATE_GOT_MSGLEN0:
                m.msg_len = c << 8 | m.msg_len;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MSGLEN1;
                break;
            case GPSLINK_PARSE_STATE_GOT_MSGLEN1:
                m.sequence = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_SEQUENCE0;
                break;
            case GPSLINK_PARSE_STATE_GOT_SEQUENCE0:
                m.sequence = c << 8 | m.sequence;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_SEQUENCE1;
                break;
            case GPSLINK_PARSE_STATE_GOT_SEQUENCE1:
                m.idle_time = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_IDLETIME;
                break;
            case GPSLINK_PARSE_STATE_GOT_IDLETIME:
                m.time_status = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_TIMESTATUS;
                break;
            case GPSLINK_PARSE_STATE_GOT_TIMESTATUS:
                m.week = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_WEEK0;
                break;
            case GPSLINK_PARSE_STATE_GOT_WEEK0:
                m.week = c << 8 | m.week;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_WEEK1;
                break;
            case GPSLINK_PARSE_STATE_GOT_WEEK1:
                m.ms = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MS0;
                break;
            case GPSLINK_PARSE_STATE_GOT_MS0:
                m.ms = c << 8 | m.ms;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MS1;
                break;
            case GPSLINK_PARSE_STATE_GOT_MS1:
                m.ms = c << 16 | m.ms;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MS2;
                break;
            case GPSLINK_PARSE_STATE_GOT_MS2:
                m.ms = c << 24 | m.ms;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_MS3;
                break;
            case GPSLINK_PARSE_STATE_GOT_MS3:
                m.receiverStatus = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RECSTATUS0;
                break;
            case GPSLINK_PARSE_STATE_GOT_RECSTATUS0:
                m.receiverStatus = c << 8 | m.receiverStatus;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RECSTATUS1;
                break;
            case GPSLINK_PARSE_STATE_GOT_RECSTATUS1:
                m.receiverStatus = c << 16 | m.receiverStatus;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RECSTATUS2;
                break;
            case GPSLINK_PARSE_STATE_GOT_RECSTATUS2:
                m.receiverStatus = c << 24 | m.receiverStatus;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RECSTATUS3;
                break;
            case GPSLINK_PARSE_STATE_GOT_RECSTATUS3:
                m.reserved = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RESERVED0;
                break;
            case GPSLINK_PARSE_STATE_GOT_RESERVED0:
                m.reserved = c << 8 | m.reserved;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RESERVED1;
                break;
            case GPSLINK_PARSE_STATE_GOT_RESERVED1:
                m.receiverSWVersion = c;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RECVERSION0;
                break;
            case GPSLINK_PARSE_STATE_GOT_RECVERSION0:
                m.receiverSWVersion = c << 8 | m.receiverSWVersion;
                state = Datalink_states.GPSLINK_PARSE_STATE_GOT_RECVERSION1;
                break;
            case GPSLINK_PARSE_STATE_GOT_RECVERSION1:
                m.payload.add((byte) c);
                if (m.payloadFilled()) {
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_PAYLOAD;
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_PAYLOAD:
                if (c == m.crc.getLLSB()) {
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_CK0;
                } else {
                    msg_received = false;
                    state = Datalink_states.GPSLINK_PARSE_STATE_IDLE;

                    if (c == MUH_MSG_ID.GPSLINK_STX1) {
                        m.magic1 = c;
                        state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX1;
                    }
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_CK0:
                if (c == m.crc.getLMSB()) {
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_CK1;
                } else {
                    msg_received = false;
                    state = Datalink_states.GPSLINK_PARSE_STATE_IDLE;

                    if (c == MUH_MSG_ID.GPSLINK_STX1) {
                        m.magic1 = c;
                        state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX1;
                    }
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_CK1:
                if (c == m.crc.getMLSB()) {
                    state = Datalink_states.GPSLINK_PARSE_STATE_GOT_CK2;
                } else {
                    msg_received = false;
                    state = Datalink_states.GPSLINK_PARSE_STATE_IDLE;

                    if (c == MUH_MSG_ID.GPSLINK_STX1) {
                        m.magic1 = c;
                        state = Datalink_states.GPSLINK_PARSE_STATE_GOT_STX1;
                    }
                }
                break;
            case GPSLINK_PARSE_STATE_GOT_CK2:
                if (c == m.crc.getMMSB()) {
                    msg_received = true;
                    state = Datalink_states.GPSLINK_PARSE_STATE_UNINIT;
                } else {
                    msg_received = false;
                    state = Datalink_states.GPSLINK_PARSE_STATE_IDLE;
                }
                break;
        }

        if (msg_received) {
            return m;
        } else {
            return null;
        }
    }

}
