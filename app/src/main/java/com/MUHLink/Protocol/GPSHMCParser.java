package com.MUHLink.Protocol;

import android.util.Log;

import static com.MUHLink.Protocol.enums.MUH_MSG_ID.GPSHMC_STX1;
import static com.MUHLink.Protocol.enums.MUH_MSG_ID.GPSHMC_STX2;

/**
 * Created by Gui Zhou on 2016/11/2.
 */

public class GPSHMCParser {

    private static final String TAG = "GPSHMCParser";

    private enum GPSHMC_parser_state {
        GPSHMC_PARSE_STATE_UNINIT ,
        GPSHMC_PARSE_STATE_IDLE,
        GPSHMC_PARSE_STATE_GOT_STX1,
        GPSHMC_PARSE_STATE_GOT_STX2,
        GPSHMC_PARSE_STATE_GOT_LENGTH,
        GPSHMC_PARSE_STATE_GOT_SEQ,
        GPSHMC_PARSE_STATE_GOT_SYSID,
        GPSHMC_PARSE_STATE_GOT_COMPID,
        GPSHMC_PARSE_STATE_GOT_MSGID,
        GPSHMC_PARSE_STATE_GOT_PAYLOAD,
        GPSHMC_PARSE_STATE_GOT_CRC1
    }

    private GPSHMC_parser_state state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_UNINIT;

    private static boolean msg_received;
    private GPSHMCPacket m;

    /**
     * 这是一个比较方便的解析数据的方法。一次解析一个字符，如果成功将返回
     * 解析的整个数据包，并记录相关失败的信息。
     *
     * @param c 要解析的字符
     * @return 成功返回解析的包
     */
    public GPSHMCPacket gpshmc_parser_char(int c) {

        msg_received = false;

        switch (state) {
            case GPSHMC_PARSE_STATE_UNINIT:
            case GPSHMC_PARSE_STATE_IDLE:
                m = new GPSHMCPacket();
                m.startCRC();
                if ( c == GPSHMC_STX1) {
                    Log.i(TAG, "GOT_STX1: " + c);
                    m.magic1 = c;
                    state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_STX1;
                }
                break;
            case GPSHMC_PARSE_STATE_GOT_STX1:
                if(c == GPSHMC_STX2) {
                    Log.i(TAG, "GOT_STX2: " + c);
                    m.magic2 = c;
                    state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_STX2;
                } else {
                    if ( c == GPSHMC_STX1) {
                        m.magic1 = c;
                        state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_STX1;
                    } else {
                        state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_IDLE;
                    }
                }
                break;
            case GPSHMC_PARSE_STATE_GOT_STX2:
                Log.i(TAG, "LEN: " + c);
                m.len = c;
                m.crc.update_checksum(c);
                state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_LENGTH;
                break;
            case GPSHMC_PARSE_STATE_GOT_LENGTH:
                Log.i(TAG, "SEQ: " + c);
                m.seq = c;
                m.crc.update_checksum(c);
                state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_SEQ;
                break;
            case GPSHMC_PARSE_STATE_GOT_SEQ:
                Log.i(TAG, "SYSID: " + c);
                m.sysid = c;
                m.crc.update_checksum(c);
                state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_SYSID;
                break;
            case GPSHMC_PARSE_STATE_GOT_SYSID:
                Log.i(TAG, "COMPID: " + c);
                m.compid = c;
                m.crc.update_checksum(c);
                state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_COMPID;
                break;
            case GPSHMC_PARSE_STATE_GOT_COMPID:
                Log.i(TAG, "MSGID: " + c);
                m.msgid = c;
                m.crc.update_checksum(c);
                state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_MSGID;
                break;
            case GPSHMC_PARSE_STATE_GOT_MSGID:
                m.payload.add((byte) c);
                m.crc.update_checksum(c);
                if(m.payloadFilled()) {
                    state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_PAYLOAD;
                }
                break;
            case GPSHMC_PARSE_STATE_GOT_PAYLOAD:
                if(c == m.crc.getLSB()) {
                    state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_CRC1;
                } else {
                    msg_received = false;
                    Log.i(TAG, "GOT_FAILURE");
                    if ( c == GPSHMC_STX1) {
                        m.magic1 = c;
                        state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_STX1;
                    }
                }
                break;
            case GPSHMC_PARSE_STATE_GOT_CRC1:
                if(c == m.crc.getMSB()) {
                    msg_received = true;
                    state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_UNINIT;
                } else {
                    msg_received = false;
                    if ( c == GPSHMC_STX1) {
                        m.magic1 = c;
                        state = GPSHMC_parser_state.GPSHMC_PARSE_STATE_GOT_STX1;
                    }
                }
                break;

        }

        if(msg_received) {
            return m;
        } else {
            return null;
        }

    }
}
