package com.grishberg.dailyselfie.data.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.grishberg.dailyselfie.common.db.BaseResult;
import com.grishberg.dailyselfie.common.db.DataReceiveObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by grishberg on 30.04.16.
 */
public class BaseResultCursor<T extends CursorModel<T>> implements BaseResult {
    private final List<DataReceiveObserver> observers;
    protected final Context context;
    protected Cursor cursor;
    private boolean isLoaded;
    protected final Class<T> clazz;
    private final Uri uri;
    private final String[] projection;
    private final String[] selectionArgs;
    private final String selection;
    private final String sortOrder;

    public BaseResultCursor(Context context,
                            Class<T> clazz,
                            @NonNull Uri uri,
                            @Nullable String[] projection,
                            @Nullable String selection,
                            @Nullable String[] selectionArgs,
                            @Nullable String sortOrder) {
        this.context = context;
        this.uri = uri;
        this.clazz = clazz;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;

        observers = new LinkedList<>();
        isLoaded = false;
        doRequest();
    }

    private void doRequest() {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                Cursor cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, sortOrder);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                BaseResultCursor.this.cursor = cursor;
                if (cursor != null) {
                    isLoaded = true;
                    // notify recepients
                    for (DataReceiveObserver observer : observers) {
                        observer.onDataReceived();
                    }
                }
            }
        }.execute();
    }

    /**
     * Добавить слушателя
     *
     * @param observer
     */
    @Override
    public void addDataReceiveObserver(DataReceiveObserver observer) {
        observers.add(observer);
    }

    /**
     * удалить слушателя
     *
     * @param observer
     */
    @Override
    public void removeDataReceiveObserver(DataReceiveObserver observer) {
        observers.remove(observer);
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    public void release() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
