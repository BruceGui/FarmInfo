package com.MUHLink.Protocol.common;

/**
 * Created by Gui Zhou on 2016-07-07.
 */
public class GPSCRC {

    private static final int GPSCRC32_POLYNOMIAL = 0xEDB88320;
    private static final int CRC_INIT_VALUE = 0x00000000;
    private int CRCvalue;

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

        int checkSumOld;
        int ulTemp1;
        int ulTemp2;

        checkSumOld = CRCvalue;
        ulTemp1 = (checkSumOld >> 8) & 0x00FFFFFF;
        ulTemp2 = GPSCRC32Value((checkSumOld ^ data) & 0xFF);

        CRCvalue = ulTemp1 ^ ulTemp2;

    }

    public int GPSCRC32Value(int value) {

        int ulCRC;
        ulCRC = value;

        for(int i = 8; i > 0; i --) {
            if((ulCRC & 1) == 1) {
                ulCRC = (ulCRC >> 1) ^ GPSCRC32_POLYNOMIAL;
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
    public int getMMSB() {
        return ((CRCvalue >> 24) &0xff);
    }

    /**
     *
     * @return  校验和的高低八位
     */
    public int getMLSB() {
        return ((CRCvalue >> 16) &0xff);
    }

    /**
     *
     * @return 校验和的低高八位
     */
    public int getLMSB() {
        return ((CRCvalue >> 8) & 0xff);
    }

    /**
     *
     * @return 校验和的低低八位
     */
    public int getLLSB() {
        return (CRCvalue & 0xff);
    }

    public GPSCRC() {
        start_checksum();
    }

}
