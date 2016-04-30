package com.grishberg.dailyselfie.data.db;

import android.database.Cursor;

/**
 * Created by grishberg on 28.04.16.
 */
public abstract class CursorModel<T> {
    private static final String TAG = CursorModel.class.getSimpleName();
    public abstract T getFromCursor(Cursor cur);
}
