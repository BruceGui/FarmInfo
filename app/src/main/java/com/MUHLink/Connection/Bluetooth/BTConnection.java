package com.MUHLink.Connection.Bluetooth;

import com.MUHLink.Connection.MUHLinkConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.*;
import android.util.Log;

/**
 * Created by Home on 29/05/2016.
 */

public class BTConnection extends MUHLinkConnection {

    private static final String TAG = "BTConnection";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket  bluetoothSocket;
    private OutputStream out;
    private InputStream  in;

    @Override
    protected void closeConnectoin() throws IOException {
        resetConnection();
        Log.i(TAG, "BT Close!");
    }

    //TODO BT 蓝牙获取链接
    @Override
    protected void openConnection() throws IOException {
        Log.i(TAG, "GET-BT-CONN");
    }

    @Override
    protected void sendBuffer(byte[] buffer) throws IOException {
        if(out != null) {
            out.write(buffer);
        }
    }

    @Override
    protected int readDataBlock(byte[] buffer) throws IOException {
        if(in != null) {
            return in.read(buffer);
        }
        return 0;
    }

    private void resetConnection() throws IOException {
        if(in != null) {
            in.close();
            in = null;
        }

        if(out != null) {
            out.close();
            out = null;
        }

        if(bluetoothSocket != null) {
            bluetoothSocket.close();
            bluetoothSocket = null;
        }
    }
}
