package com.getpoint.farminfomanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016-07-04.
 */
public class FarmInfoAppPref {

    private static final String DEFAULT_CONNECTION_TYPE = "1";
    private static final String DEFAULT_TCP_SERVER_IP = "192.168.1.10";
    private static final String DEFAULT_TCP_SERVER_PORT = "9750";
    private static final String DEFAULT_STATION_HEIGHT = "0";

    /*
     * Public for legacy usage
     */
    public SharedPreferences prefs;
    private Context context;

    public FarmInfoAppPref(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     *
     * @return 选择的连接方式
     */
    public int getConnectionParameterType() {
        return Integer.parseInt(prefs.getString(context.getString(R.string.
                pref_connection_type_key), DEFAULT_CONNECTION_TYPE));
    }

    public String getTcpServerIp(){
        return prefs.getString(context.getString(R.string.pref_server_ip_key),
                DEFAULT_TCP_SERVER_IP);
    }

    public int getTcpServerPort(){
        return Integer.parseInt(prefs.getString(context.getString(R.string.pref_server_port_key),
                DEFAULT_TCP_SERVER_PORT));
    }

    public float getMobileStationHeight() {
        return Float.parseFloat(prefs.getString(context.getString(R.string.pref_mobile_station_height_key),
                DEFAULT_STATION_HEIGHT));
    }

    public float getBaseStationHeight() {
        return Float.parseFloat(prefs.getString(context.getString(R.string.pref_base_station_height_key),
                DEFAULT_STATION_HEIGHT));
    }
}
