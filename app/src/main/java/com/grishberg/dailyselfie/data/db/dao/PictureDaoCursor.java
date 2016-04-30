package com.grishberg.dailyselfie.data.db.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.grishberg.dailyselfie.common.db.DataStoreObserver;
import com.grishberg.dailyselfie.common.db.SingleResult;
import com.grishberg.dailyselfie.data.db.AppContentProvider;
import com.grishberg.dailyselfie.data.db.DbHelper;
import com.grishberg.dailyselfie.common.db.ListResult;
import com.grishberg.dailyselfie.data.db.ListResultCursor;
import com.grishberg.dailyselfie.data.db.SingleResultCursor;
import com.grishberg.dailyselfie.data.model.Pictures;

import java.util.Date;
import java.util.Locale;

/**
 * Created by grishberg on 28.04.16.
 */
public class PictureDaoCursor implements PictureDao {
    private static final String TAG = PictureDaoCursor.class.getSimpleName();
    private final Context context;

    public PictureDaoCursor(Context context) {
        this.context = context;
    }

    @Override
    public void storePicture(String path) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.PICTURES_PATH, path);
        values.put(DbHelper.PICTURES_LAST_UPDATE, new Date().getTime());
        context.getContentResolver()
                .insert(AppContentProvider.CONTENT_URI_PICTURES,
                        values);
    }

    @Override
    public void storePictureAsync(final String path, final DataStoreObserver callback) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... params) {
                ContentValues values = new ContentValues();
                values.put(DbHelper.PICTURES_PATH, path);
                values.put(DbHelper.PICTURES_LAST_UPDATE, new Date().getTime());
                Uri uri = context.getContentResolver()
                        .insert(AppContentProvider.CONTENT_URI_PICTURES,
                                values);
                return ContentUris.parseId(uri);
            }

            @Override
            protected void onPostExecute(Long id) {
                if (id != null && callback != null && !isCancelled()) {
                    callback.onDataStored(id);
                }
            }
        }.execute();
    }

    @Override
    public ListResult<Pictures> getPictures() {
        // 1) retrive categories ID  from DB
        return new ListResultCursor<>(context, Pictures.class,
                AppContentProvider.CONTENT_URI_PICTURES,
                null,
                null,
                null,
                DbHelper.PICTURES_LAST_UPDATE+" DESC ");
    }

    @Override
    public SingleResult<Pictures> getPicture(long id) {
        return new SingleResultCursor<>(context, Pictures.class,
                AppContentProvider.CONTENT_URI_PICTURES,
                null,
                String.format("%s = ?", DbHelper.COLUMN_ID),
                new String[]{String.format(Locale.US, "%d", id)},
                null);
    }
}
