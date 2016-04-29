package com.grishberg.dailyselfie.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.grishberg.dailyselfie.App;
import com.grishberg.dailyselfie.data.db.AppContentProvider;
import com.grishberg.dailyselfie.data.db.DbHelper;
import com.grishberg.dailyselfie.data.db.ListResult;
import com.grishberg.dailyselfie.data.db.ListResultCursor;
import com.grishberg.dailyselfie.data.model.Pictures;

import java.util.Date;

/**
 * Created by grishberg on 28.04.16.
 */
public class PictureDaoCursor implements PictureDao {
    private static final String TAG = PictureDaoCursor.class.getSimpleName();

    @Override
    public void storePicture(String path) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.PICTURES_PATH, path);
        values.put(DbHelper.PICTURES_LAST_UPDATE, new Date().getTime());
        App.getAppContext().getContentResolver()
                .insert(AppContentProvider.CONTENT_URI_PICTURES,
                        values);
    }

    @Override
    public ListResult<Pictures> getPictures() {
        // 1) retrive categories ID  from DB
        try (Cursor cursor = App.getAppContext().getContentResolver()
                .query(AppContentProvider.CONTENT_URI_PICTURES
                        , new String[]{DbHelper.COLUMN_ID}
                        , null
                        , null
                        , null)) {
            return new ListResultCursor<>(cursor, Pictures.class);
        }
    }
}
