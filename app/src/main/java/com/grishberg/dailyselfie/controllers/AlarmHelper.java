package com.grishberg.dailyselfie.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by grishberg on 30.04.16.
 */
public class AlarmHelper {
    private static final String TAG = AlarmHelper.class.getSimpleName();
    private static final long INITIAL_ALARM_DELAY = 1000 * 60;

    public static void makeAlarm(Context context , long delay){
        // Get the AlarmManager Service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        Intent notificationReceiverIntent = new Intent(context,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        PendingIntent notificationReceiverPendingIntent = PendingIntent.getBroadcast(
                context, 0, notificationReceiverIntent, 0);

        // Set single alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                notificationReceiverPendingIntent);

    }
}
