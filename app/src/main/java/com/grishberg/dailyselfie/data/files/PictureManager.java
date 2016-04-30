package com.grishberg.dailyselfie.data.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.grishberg.dailyselfie.data.db.dao.PictureDao;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by grishberg on 28.04.16.
 */
public class PictureManager implements Runnable {
    private static final String TAG = PictureManager.class.getSimpleName();
    public static final int DECODE_COMPLETED = 1;
    public static final int DECODE_FAIL = 2;
    public static final int STORE_COMPLETED = 3;
    public static final int STORE_FAIL = 4;
    /*
    * Gets the number of available cores
    * (not always the same as the maximum number of cores)
    */
    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    // A queue of Runnables
    private final BlockingQueue<Runnable> decodeWorkQueue;
    private final ThreadPoolExecutor decodeThreadPool;
    private final Handler handler;

    private final LruCache<String, Bitmap> bitmapCache;
    private static PictureManager sInstance;

    public static PictureManager getInstance(){
        if(sInstance == null){
            sInstance = new PictureManager();
        }
        return sInstance;
    }
    private PictureManager() {
        int cacheSize = 50 * 1024 * 1024; // 4MiB
        bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        // Instantiates the queue of Runnables as a LinkedBlockingQueue
        decodeWorkQueue = new LinkedBlockingQueue<>();
        // Creates a thread pool manager
        decodeThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                decodeWorkQueue);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                PictureTask pictureTask = (PictureTask) inputMessage.obj;
                DecodeCompleteListener listener = pictureTask.getDecodeListener();
                StoreCompleteListener storeCompleteListener = pictureTask.getStoreListener();

                switch (inputMessage.what) {
                    case DECODE_COMPLETED:
                        Log.d(TAG, "handleMessage: DECODE_COMPLETED");
                        // Gets the image task from the incoming Message object.
                        bitmapCache.put(pictureTask.getPath(), pictureTask.getBitmap());
                        if (listener != null) {
                            listener.onCompleted(pictureTask.getBitmap(), pictureTask.getPath());
                        }
                        break;
                    case STORE_COMPLETED:
                        Log.d(TAG, "handleMessage: STORE_COMPLETED");
                        storeCompleteListener = pictureTask.getStoreListener();
                        if (storeCompleteListener != null) {
                            storeCompleteListener.onCompleted(pictureTask.getPath());
                        }
                        break;
                    case STORE_FAIL:
                        Log.e(TAG, String.format("handleMessage: STORE_FAIL msg = %s",
                                pictureTask.getErrorMessage()));
                        if (storeCompleteListener != null) {
                            storeCompleteListener.onFail(pictureTask.getErrorMessage());
                        }
                        break;
                }
            }
        };
    }

    /**
     * Store picture to file system and db
     *
     * @param src
     */
    public void storePicture(Context context, Bitmap src, StoreCompleteListener listener) {
        Log.d(TAG, "storePicture: ");
        StoreRunnable runnable = new StoreRunnable(context, handler, src, listener);
        decodeThreadPool.execute(runnable);
    }

    /**
     * decode bitmap from file
     *
     * @param path
     * @param listener
     */
    public void loadPicture(String path, DecodeCompleteListener listener) {
        if (path == null) return;
        Bitmap dst = bitmapCache.get(path);
        if (dst != null) {
            Log.d(TAG, String.format("loadPicture: load from memory: path=%s", path));
            listener.onCompleted(dst, path);
            return;
        }
        DecodeRunnable runnable = new DecodeRunnable(handler, path, listener);
        decodeThreadPool.execute(runnable);
    }

    @Override
    public void run() {

    }

    public void cancelAll() {
        /*
         * Creates an array of Runnables that's the same size as the
         * thread pool work queue
         */
        Runnable[] runnableArray = new Runnable[decodeWorkQueue.size()];
        // Populates the array with the Runnables in the queue
        decodeWorkQueue.toArray(runnableArray);
        // Stores the array length in order to iterate over the array
        int len = runnableArray.length;
        /*
         * Iterates over the array of Runnables and interrupts each one's Thread.
         */
        synchronized (decodeWorkQueue) {
            // Iterates over the array of tasks
            for (int runnableIndex = 0; runnableIndex < len; runnableIndex++) {
                // Gets the current thread
                BaseRunnable thread = (BaseRunnable) runnableArray[runnableIndex];
                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.cancel();
                }
            }
        }
    }

    public interface DecodeCompleteListener {
        void onCompleted(Bitmap bitmap, String path);
        void onFail(String path);
    }

    public interface StoreCompleteListener {
        void onCompleted(String path);

        void onFail(String errorMessage);
    }
}
