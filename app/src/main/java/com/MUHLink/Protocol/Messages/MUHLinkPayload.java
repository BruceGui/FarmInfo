package com.MUHLink.Protocol.Messages;

/**
 * Created by ziwo5 on 2016-03-10.
 */

import com.MUHLink.Protocol.enums.MUH_MSG_ID;

import java.nio.ByteBuffer;

/**
 *  MUHLink 消息内容的载体，包含解包和打包的相关函数
 */

public class MUHLinkPayload {

    public static final int MAX_PAYLOAD_SIZE = MUH_MSG_ID.DATALINK_MAX_PAYLOAD_LEN;

    public ByteBuffer payload;
    public int index;

    public MUHLinkPayload() {
        payload = ByteBuffer.allocate(MAX_PAYLOAD_SIZE);
    }

    public ByteBuffer getData() {
        return payload;
    }

    public int size() {
        return payload.position();
    }

    public void add(byte c) {
        payload.put(c);
    }

    public void resetIndex() {
        index = 0;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte getByte() {
        byte result = 0;
        result |= (payload.get(index + 0) & 0xFF);
        index += 1;
        return result;
    }

    public short getShort() {
        short result = 0;
        result |= (payload.get(index + 1) & 0xFF) << 8;
        result |= (payload.get(index + 0) & 0xFF);
        index += 2;
        return result;
    }

    public int getInt() {
        int result = 0;
        result |= (payload.get(index + 3) & 0xFF) << 24;
        result |= (payload.get(index + 2) & 0xFF) << 16;
        result |= (payload.get(index + 1) & 0xFF) << 8;
        result |= (payload.get(index + 0) & 0xFF);
        index += 4;
        return result;
    }

    public int getIntReverse() {
        int result = 0;
        result |= (payload.get(index + 0) & 0xFF) << 24;
        result |= (payload.get(index + 1) & 0xFF) << 16;
        result |= (payload.get(index + 2) & 0xFF) << 8;
        result |= (payload.get(index + 3) & 0xFF);
        index += 4;
        return result;
    }

    public long getLong() {
        long result = 0;
        result |= (payload.get(index + 7) & (long)0xFF) << 56;
        result |= (payload.get(index + 6) & (long)0xFF) << 48;
        result |= (payload.get(index + 5) & (long)0xFF) << 40;
        result |= (payload.get(index + 4) & (long)0xFF) << 32;
        result |= (payload.get(index + 3) & (long)0xFF) << 24;
        result |= (payload.get(index + 2) & (long)0xFF) << 16;
        result |= (payload.get(index + 1) & (long)0xFF) << 8;
        result |= (payload.get(index + 0) & (long)0xFF);
        index += 8;
        return result;
    }


    public long getLongReverse() {
        long result = 0;
        result |= (payload.get(index + 0) & (long)0xFF) << 56;
        result |= (payload.get(index + 1) & (long)0xFF) << 48;
        result |= (payload.get(index + 2) & (long)0xFF) << 40;
        result |= (payload.get(index + 3) & (long)0xFF) << 32;
        result |= (payload.get(index + 4) & (long)0xFF) << 24;
        result |= (payload.get(index + 5) & (long)0xFF) << 16;
        result |= (payload.get(index + 6) & (long)0xFF) << 8;
        result |= (payload.get(index + 7) & (long)0xFF);
        index += 8;
        return result;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    public float getFloatReverse() {
        return Float.intBitsToFloat(getIntReverse());
    }

    public double getDoubleReverse() {
        return Double.longBitsToDouble(getLongReverse());
    }

    public void putByte(byte data) {
        add(data);
    }

    public void putShort(short data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
    }

    public void putInt(int data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
    }

    public void putLong(long data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
        add((byte) (data >> 32));
        add((byte) (data >> 40));
        add((byte) (data >> 48));
        add((byte) (data >> 56));
    }

    public void putFloat(float data) {
        putInt(Float.floatToIntBits(data));
    }

}
