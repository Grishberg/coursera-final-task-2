package com.grishberg.dailyselfie;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.nostra13.universalimageloader.BuildConfig;

/**
 * Created by grishberg on 28.04.16.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static Context sAppContext;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: DailySelfie");
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
        sAppContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
