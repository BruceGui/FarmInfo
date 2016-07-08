package com.MUHLink.Protocol.common;

import android.util.Log;

/**
 * Created by Gui Zhou on 2016-07-07.
 */

/**
 *  校验时注意有符号数和无符号数
 */

public class GPSCRC {

    private static final String TAG = "GPSCRC";

    private static final long GPSCRC32_POLYNOMIAL = 0xEDB88320L;
    private static final long CRC_INIT_VALUE = 0x00000000L;
    private long CRCvalue;

    /**
     *
     * @param data
     */
    public void update_checksum(int data) {

        /*
        int tmp;
        data = data & 0xff;
        tmp = data ^ (CRCvalue & 0xff);
        tmp ^= (tmp << 4) & 0xff;
        CRCvalue = ((CRCvalue >> 8) & 0xff) ^ (tmp << 8) ^ (tmp << 3)
                ^ ((tmp >> 4) & 0xf);
                */

        long checkSumOld;
        long ulTemp1;
        long ulTemp2;

        data = data & 0xff;
        checkSumOld = CRCvalue;
        ulTemp1 = (checkSumOld >> 8) & 0x00FFFFFFL;
        checkSumOld = checkSumOld & 0xFFFF;
        ulTemp2 = GPSCRC32Value((checkSumOld ^ data) & 0xFF);

        CRCvalue = ulTemp1 ^ ulTemp2;

    }

    public long GPSCRC32Value(long value) {

        long ulCRC;
        ulCRC = value;

        for(int i = 8; i > 0; i --) {
            if((ulCRC & 1) == 1) {
                ulCRC = (ulCRC >> 1) ^ GPSCRC32_POLYNOMIAL;
                //Log.i(TAG, "One");
            } else {
                ulCRC >>= 1;
            }
        }

        return ulCRC;

    }

    public void start_checksum() {
        CRCvalue = CRC_INIT_VALUE;
    }

    /**
     *
     * @return 校验和的高高八位
     */
    public long getMMSB() {
        //return ((CRCvalue >> 24) &0xff);
        return ((CRCvalue & 0xff000000) >> 24);
    }

    /**
     *
     * @return  校验和的高低八位
     */
    public long getMLSB() {
        //return ((CRCvalue >> 16) &0xff);
        return ((CRCvalue & 0x00ff0000) >> 16);
    }

    /**
     *
     * @return 校验和的低高八位
     */
    public long getLMSB() {
        //return ((CRCvalue >> 8) & 0xff);
        return ((CRCvalue & 0x0000ff00) >> 8);
    }

    /**
     *
     * @return 校验和的低低八位
     */
    public long getLLSB() {
        //return (CRCvalue & 0xff);
        return (CRCvalue & 0x000000ff);
    }

    public GPSCRC() {
        start_checksum();
    }



}
