package com.getpoint.farminfomanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MUHLink.Connection.ConnectionType;
import com.getpoint.farminfomanager.FarmInfoAppPref;
import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;

import java.util.HashSet;

/**
 * Created by Gui Zhou on 2016-07-03.
 */

public class SettingFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingFragment.class.getSimpleName();

    /**
     *  同步更新偏好设置
     */
    private final HashSet<String> mDefaultSummaryPrefs = new HashSet<String>();
    private FarmInfoManagerApp fmAPP;
    private FarmInfoAppPref fmAppPref;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fmAPP = (FarmInfoManagerApp) activity.getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        initSummaryPerPrefs();

        final Context context = getActivity().getApplicationContext();
        fmAppPref = new FarmInfoAppPref(context);
        final SharedPreferences sharedPref = fmAppPref.prefs;

        /*
        *   同步更新 mDefaultSummaryPrefs 哈希表中的偏好设置
        * */
        for(String prefKey : mDefaultSummaryPrefs) {
            final Preference pref = findPreference(prefKey);
            if(pref != null) {
                pref.setSummary(sharedPref.getString(prefKey, ""));
            }
        }

        /**
         *  设置偏好项点击的监听函数
         */
        setupConnectionPreferences();
    }

    private void setupConnectionPreferences() {
        ListPreference connectionTypePref = (ListPreference) findPreference(getString(R.string.pref_connection_type_key));
        if (connectionTypePref != null) {
            int defaultConnectionType = fmAppPref.getConnectionParameterType();
            updateConnectionPreferenceSummary(connectionTypePref, defaultConnectionType);
            connectionTypePref
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            int connectionType = Integer.parseInt((String) newValue);
                            updateConnectionPreferenceSummary(preference, connectionType);
                            return true;
                        }
                    });
        }
    }

    /**
     * 更新连接方式
     * @param preference
     * @param connectionType
     */
    private void updateConnectionPreferenceSummary(Preference preference, int connectionType) {
        String connectionName;
        switch (connectionType) {
            case ConnectionType.TYPE_TCP:
                connectionName = "TCP";
                break;
            case ConnectionType.TYPE_BT:
                connectionName = "BT";
                break;
            default:
                connectionName = null;
                break;
        }

        if (connectionName != null)
            preference.setSummary(connectionName);
    }

    /**
     *  偏好改变时调用
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final Preference preference = findPreference(key);

        if(preference == null) {
            return;
        }

        if(mDefaultSummaryPrefs.contains(key)) {
            preference.setSummary(sharedPreferences.getString(key, ""));
            Log.i(TAG, "Pref Change.");
        }
    }

    /**
     *  初始化要同步更新的偏好键值
     */
    private void initSummaryPerPrefs() {
        mDefaultSummaryPrefs.clear();

        mDefaultSummaryPrefs.add(getString(R.string.pref_server_ip_key));
        mDefaultSummaryPrefs.add(getString(R.string.pref_server_port_key));
        mDefaultSummaryPrefs.add(getString(R.string.pref_udp_server_port_key));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
