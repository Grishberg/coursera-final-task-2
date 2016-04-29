package com.grishberg.dailyselfie.data.files;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.grishberg.dailyselfie.App;
import com.grishberg.dailyselfie.data.db.dao.PictureDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by grishberg on 29.04.16.
 */
public class StoreRunnable extends BaseRunnable {
    private static final String TAG = StoreRunnable.class.getSimpleName();
    public static final String ANDROID_DATA = "/Android/data/";
    public static final String FILES = "/Files";
    private final PictureManager.StoreCompleteListener listener;
    private final Bitmap bitmap;
    private final PictureDao pictureDao;

    public StoreRunnable(Handler handler, Bitmap bitmap, PictureDao pictureDao, PictureManager.StoreCompleteListener listener) {
        super(handler);
        this.listener = listener;
        this.bitmap = bitmap;
        this.pictureDao = pictureDao;
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
        PictureTask pictureTask = new PictureTask();
        pictureTask.setStoreListener(listener);

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            sendMessage(PictureManager.STORE_FAIL, pictureTask);
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            if (!thread.isInterrupted()) {
                pictureDao.storePicture(pictureFile.getAbsolutePath());
                pictureTask.setPath(pictureFile.getAbsolutePath());
                pictureTask.setStoreListener(listener);
                sendMessage(PictureManager.STORE_COMPLETED, pictureTask);
            }
            return;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: ", e);
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: ", e);
        }
        sendMessage(PictureManager.STORE_FAIL, pictureTask);
        Message completeMessage =
                handler.obtainMessage();
        completeMessage.sendToTarget();
    }

    /**
     * Send msg to Pic manager
     *
     * @param state
     * @param task
     */
    private void sendMessage(int state, PictureTask task) {
        Message completeMessage =
                handler.obtainMessage(state, task);
        completeMessage.sendToTarget();
    }

    /**
     * Create a File for saving an image
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory()
                        + ANDROID_DATA
                        + App.getAppContext().getPackageName()
                        + FILES);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm", Locale.US)
                .format(new Date());
        File mediaFile;
        String imageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageName);
        return mediaFile;
    }
}
