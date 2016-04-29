package com.grishberg.dailyselfie.data.db;

import android.database.Cursor;

import com.grishberg.dailyselfie.common.db.DataReceiveObserver;
import com.grishberg.dailyselfie.common.db.ListResult;

/**
 * Created by grishberg on 28.04.16.
 */
public class ListResultCursor<T extends CursorModel<T>> implements ListResult<T> {
    private static final String TAG = ListResultCursor.class.getSimpleName();
    private final Cursor cursor;
    private Class<T> clazz;
    private int prevIndex;

    public ListResultCursor(Cursor cursor, Class<T> clazz) {
        this.clazz = clazz;
        this.cursor = cursor;
    }

    @Override
    public T getItem(int index) {
        if (cursor.isClosed()) {
            return null;
        }
        if (prevIndex - index == 1) {
            prevIndex = index;
            if (cursor.moveToNext()) {
                getValueFromCursor();
            }
        } else if (cursor.move(index)) {
            prevIndex = index;
            getValueFromCursor();
        }
        return null;
    }

    /**
     * extract values from cursor
     * @return
     */
    private T getValueFromCursor() {
        try {
            T result = clazz.newInstance();
            return result.ftomCursor(cursor);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public void addDataReceiveObserver(DataReceiveObserver observer) {

    }

    @Override
    public void removeDataReceiveObserver(DataReceiveObserver observer) {

    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    public void release() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
