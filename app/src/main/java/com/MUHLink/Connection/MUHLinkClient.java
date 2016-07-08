package com.MUHLink.Connection;

import android.content.Context;
import android.util.Log;

import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.MUHLinkPacket;
import com.getpoint.farminfomanager.FarmInfoAppPref;
import com.getpoint.farminfomanager.GPSDeviceManager;

/**
 * Created by Gui Zhou on 2016/3/17.
 */
public class MUHLinkClient implements MUHLinkStream.MUHLinkOutputStream{

    private static final String TAG = MUHLinkClient.class.getSimpleName();

    private final MUHLinkConnectionListener mConnectionListener = new MUHLinkConnectionListener() {

        @Override
        public void onConnect() {
            listener.notifyConnected();
        }

        @Override
        public void onReceivePacket(GPSLinkPacket packet) {
            listener.notifyReceivedData(packet);
            //Log.i(TAG, "notify Data Recv");
        }

        @Override
        public void onDisconnect() {
            listener.notifyDisconnected();
        }
    };

    //protected DiygcsAPPPrefs mAppPrefs;
    protected FarmInfoAppPref farmInfoAppPref;
    private final MUHLinkStream.MuhLinkInputStream listener;
    private MUHLinkConnection droneConn;

    public MUHLinkClient(Context context, MUHLinkStream.MuhLinkInputStream listener, MUHLinkConnection droneConn) {

        this.listener = listener;
        this.droneConn = droneConn;

        farmInfoAppPref = new FarmInfoAppPref(context);
        //mAppPrefs = new DiygcsAPPPrefs(context);
    }

    @Override
    public void sendMuhPacket(MUHLinkPacket packet) {
        droneConn.sendMuhPacket(packet);
    }

    @Override
    public boolean isConnected() {
        if(droneConn != null)
            return droneConn.isConnected();
        else
            return false;
    }

    @Override
    public void openConnection(int connectionType) {

        if(connectionType == ConnectionType.TYPE_TCP) {
            Log.i(TAG, "CONN_VIA_TCP");
            droneConn = new TCPConnection(farmInfoAppPref.getTcpServerIp(), farmInfoAppPref.getTcpServerPort());
            //droneConn.connect();
        } else if(connectionType == ConnectionType.TYPE_UDP) {
            Log.i(TAG, "CONN_VIA_UDP");
            //droneConn = new UDPConnection(mAppPrefs.getUdpServerPort());
        }

        if(!droneConn.isConnected())
            droneConn.connect();

        droneConn.addMUHLinkConnectionListener(TAG, mConnectionListener);
    }

    @Override
    public void closeConnection() {
        droneConn.disconnect();
    }

}
