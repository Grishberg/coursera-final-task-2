package com.grishberg.dailyselfie.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.grishberg.dailyselfie.R;
import com.grishberg.dailyselfie.ui.activities.MainActivity;

/**
 * Created by grishberg on 30.04.16.
 */
public class NotificationHelper {
    private static final String TAG = NotificationHelper.class.getSimpleName();
    public static final int MY_NOTIFICATION_ID = 11151990;
    private static final long[] vibratePattern = { 0, 200, 200, 300 };


    public static void notify(Context context) {

        Intent notificationIntent = new Intent(context,
                MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        // Define the Notification's expanded message and Intent:
        String titleText = context.getString(R.string.selfy_time_title);
        String contentText = context.getString(R.string.selfy_time_content);
        Notification.Builder notificationBuilder = new Notification.Builder(
                context)
                .setTicker(context.getText(R.string.selfy_time))
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentTitle(titleText)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
//                .setContentIntent(contentIntent).setSound(soundURI)
                .setVibrate(vibratePattern);

        // Pass the Notification to the NotificationHelper:
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());
    }
}
