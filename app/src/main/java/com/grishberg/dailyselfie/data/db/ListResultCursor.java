package com.grishberg.dailyselfie.data.db;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        if(index == 0 && cursor.moveToFirst()){
            prevIndex = index;
            return getValueFromCursor();
        }
        if (index - prevIndex == 1) {
            prevIndex = index;
            if (cursor.moveToNext()) {
                return getValueFromCursor();
            }
        } else if (cursor.move(index)) {
            prevIndex = index;
            return getValueFromCursor();
        }
        return null;
    }

    /**
     * extract values from cursor
     *
     * @return
     */
    private T getValueFromCursor() {
        if (cursor == null) {
            return null;
        }
        try {
            T result = clazz.newInstance();
            return result.getFromCursor(cursor);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }
}
