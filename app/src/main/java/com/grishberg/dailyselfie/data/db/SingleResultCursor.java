package com.grishberg.dailyselfie.data.db;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.grishberg.dailyselfie.common.db.SingleResult;

/**
 * Created by grishberg on 30.04.16.
 */
public class SingleResultCursor<T extends CursorModel<T>> extends BaseResultCursor<T>
        implements SingleResult<T> {
    private static final String TAG = SingleResultCursor.class.getSimpleName();
    private int prevIndex;

    public SingleResultCursor(Context context,
                                Class<T> clazz,
                              @NonNull Uri uri,
                              @Nullable String[] projection,
                              @Nullable String selection,
                              @Nullable String[] selectionArgs,
                              @Nullable String sortOrder) {
        super(context, clazz, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public T getItem() {
        if (cursor == null || cursor.isClosed()) {
            return null;
        }
        if (cursor.moveToFirst()) {
            return getValueFromCursor();
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
            return result.getFromCursor(cursor);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
