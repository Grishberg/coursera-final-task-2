package com.grishberg.dailyselfie.data.files;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * Created by grishberg on 29.04.16.
 */
public class DecodeRunnable extends BaseRunnable {
    private static final String TAG = DecodeRunnable.class.getSimpleName();
    private final String path;
    private final PictureManager.DecodeCompleteListener listener;

    public DecodeRunnable(Handler handler, String path, PictureManager.DecodeCompleteListener listener) {
        super(handler);
        this.path = path;
        this.listener = listener;
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
        Bitmap src = BitmapFactory.decodeFile(path);
        if(! thread.isInterrupted()){
            PictureTask pictureTask = new PictureTask();
            pictureTask.setPicture(src);
            pictureTask.setPath(path);
            pictureTask.setDecodeListener(listener);
            Message completeMessage =
                    handler.obtainMessage(PictureManager.DECODE_COMPLETED, pictureTask);
            completeMessage.sendToTarget();
        }
    }
}
