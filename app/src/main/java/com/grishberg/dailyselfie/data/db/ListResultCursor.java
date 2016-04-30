package com.grishberg.dailyselfie.data.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.grishberg.dailyselfie.common.db.DataReceiveObserver;
import com.grishberg.dailyselfie.common.db.ListResult;

/**
 * Created by grishberg on 28.04.16.
 */
public class ListResultCursor<T extends CursorModel<T>> extends BaseResultCursor<T>
        implements ListResult<T> {
    private static final String TAG = ListResultCursor.class.getSimpleName();
    private int prevIndex;

    public ListResultCursor(Context context,
                            Class<T> clazz,
                            @NonNull Uri uri,
                            @Nullable String[] projection,
                            @Nullable String selection,
                            @Nullable String[] selectionArgs,
                            @Nullable String sortOrder) {
        super(context, clazz, uri, projection, selection, selectionArgs, sortOrder);
    }
    @Override
    public T getItem(int index) {
        if (cursor == null || cursor.isClosed()) {
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

}
