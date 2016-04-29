package com.grishberg.dailyselfie.data.files;

import android.graphics.Bitmap;

/**
 * Created by grishberg on 29.04.16.
 */
public class PictureTask {
    private static final String TAG = PictureTask.class.getSimpleName();
    private int state;
    private Bitmap bitmap;
    private String path;
    private PictureManager.DecodeCompleteListener decodeListener;
    private PictureManager.StoreCompleteListener storeListener;

    public void setPicture(Bitmap picture) {
        this.bitmap = picture;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDecodeListener(PictureManager.DecodeCompleteListener decodeListener) {
        this.decodeListener = decodeListener;
    }

    public PictureManager.DecodeCompleteListener getDecodeListener() {
        return decodeListener;
    }

    public PictureManager.StoreCompleteListener getStoreListener() {
        return storeListener;
    }

    public void setStoreListener(PictureManager.StoreCompleteListener storeListener) {
        this.storeListener = storeListener;
    }

    public int getState() {
        return state;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPath() {
        return path;
    }
}
