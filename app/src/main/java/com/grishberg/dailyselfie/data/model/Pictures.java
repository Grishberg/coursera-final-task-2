package com.grishberg.dailyselfie.data.model;

import com.grishberg.dailyselfie.data.db.CursorModel;
import com.grishberg.dailyselfie.data.db.DbHelper;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Picture model
 */
public class Pictures extends CursorModel<Pictures> {
    private long id;
    private String path;
    private long lastupdate;

    public Pictures(long id
            , String path
            , long lastupdate) {
        this.id = id;
        this.path = path;
        this.lastupdate = lastupdate;
    }

    @Override
    public Pictures ftomCursor(Cursor cur) {
        return Pictures.fromCursor(cur);
    }

    public static Pictures fromCursor(Cursor c) {
        int idColId = c.getColumnIndex(DbHelper.COLUMN_ID);
        int pathId = c.getColumnIndex(DbHelper.PICTURES_PATH);
        int lastupdateId = c.getColumnIndex(DbHelper.PICTURES_LAST_UPDATE);
        return new Pictures(
                c.getLong(idColId)
                , c.getString(pathId)
                , c.getLong(lastupdateId));
    }

    public ContentValues buildContentValues() {
        ContentValues cv = new ContentValues();
        if (id >= 0) {
            cv.put(DbHelper.COLUMN_ID, id);
        }
        cv.put(DbHelper.COLUMN_ID, id);
        cv.put(DbHelper.PICTURES_PATH, path);
        cv.put(DbHelper.PICTURES_LAST_UPDATE, lastupdate);
        return cv;
    }

    //------------- getters ------------
    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public long getLastupdate() {
        return lastupdate;
    }
}
