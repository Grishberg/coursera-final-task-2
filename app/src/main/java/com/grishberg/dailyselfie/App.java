package com.grishberg.dailyselfie;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by grishberg on 28.04.16.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static Context sAppContext;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: DailySelfie");
        super.onCreate();
        sAppContext = getAppContext();
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
