package com.grishberg.dailyselfie.data.files;

import android.os.Handler;
import android.util.Log;

/**
 * Created by grishberg on 29.04.16.
 */
public abstract class BaseRunnable implements Runnable {
    private static final String TAG = BaseRunnable.class.getSimpleName();
    protected final Handler handler;
    protected volatile Thread thread;

    public BaseRunnable(Handler handler) {
        this.handler = handler;
    }

    public void cancel() {
        if (thread != null) {
            Log.d(TAG, "cancel: ");
            thread.interrupt();
        }
    }
}
