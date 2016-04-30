package com.grishberg.dailyselfie.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by grishberg on 30.04.16.
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmNotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Alarm");
        NotificationHelper.notify(context);
    }
}
