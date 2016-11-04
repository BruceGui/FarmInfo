package com.getpoint.farminfomanager.updatemanager;

import android.content.Context;
import android.os.AsyncTask;

import static com.getpoint.farminfomanager.updatemanager.DeviceUtils.getLatestAppVersionXml;
import static com.getpoint.farminfomanager.updatemanager.DeviceUtils.isStringAVersion;

/**
 * Created by Gui Zhou on 2016/11/4.
 */

public class VersionCheckTask extends AsyncTask<Void, Void, Update> {

    private Context context;
    private Boolean fromUtils;
    private String xmlUrl;
    private UpdateManager.LibraryListener listener;

    public VersionCheckTask(Context context, String xmlUrl, UpdateManager.LibraryListener listener) {
        this.context = context;
        this.xmlUrl = xmlUrl;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Update doInBackground(Void... voids) {

        Update update = getLatestAppVersionXml(xmlUrl);
        if (update != null) {
            return update;
        } else {
            listener.onFailed();
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Update update) {
        super.onPostExecute(update);
        if (isStringAVersion(update.getLatestVersion())) {
            listener.onSuccess(update);
        } else {
            listener.onFailed();
        }
    }
}

