package com.grishberg.dailyselfie;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.grishberg.dailyselfie.controllers.AlarmHelper;
import com.grishberg.dailyselfie.controllers.AlarmNotificationReceiver;
import com.grishberg.dailyselfie.ui.activities.MainActivity;
import com.robotium.solo.Solo;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Created by grishberg on 30.04.16.
 */
public class NotificationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String TAG = NotificationTest.class.getSimpleName();

    private Solo solo;

    public NotificationTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation());
        //To make mockito work
        System.setProperty("dexmaker.dexcache",
                getActivity().getCacheDir().toString());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


    public void testAlarmReceive() throws Exception {
        AlarmNotificationReceiver receiver = new AlarmNotificationReceiver();
        AlarmHelper.makeAlarm(getActivity().getApplicationContext(),200);
        assertTrue("Alarm not received", solo.waitForLogMessage("onReceive: Alarm", 500));
        assertNull(receiver.getResultData());
    }

}
