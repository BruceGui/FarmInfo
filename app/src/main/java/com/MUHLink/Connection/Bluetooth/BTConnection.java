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

    @Override
    protected void openConnection() throws IOException {

    }

    @Override
    protected void sendBuffer(byte[] buffer) throws IOException {
        if(out != null) {
            out.write(buffer);
        }
    }

    @Override
    protected int readDataBlock(byte[] buffer) throws IOException {
        return in.read(buffer);
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