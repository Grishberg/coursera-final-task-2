package com.grishberg.dailyselfie.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pictures";
    private static final int DB_VERSION = 1;
    // pictures
    public static final String TABLE_PICTURES = "pictures";
    public static final String COLUMN_ID = "_id";
    public static final String PICTURES_PATH = "path";
    public static final String PICTURES_LAST_UPDATE = "lastUpdate";

    // pictures
    private static final String CREATE_TABLE_PICTURES =
            "CREATE TABLE " + TABLE_PICTURES + "(" +
                    COLUMN_ID + " integer primary key," +
                    PICTURES_PATH + " TEXT ," +
                    PICTURES_LAST_UPDATE + " INTEGER " +
                    ");";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] CREATES = {
                CREATE_TABLE_PICTURES
        };
        for (String table : CREATES) {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] TABLES = {
                TABLE_PICTURES
        };
        for (final String table : TABLES) {
            db.execSQL("DROP TABLE IF EXISTS" + table);
        }
        onCreate(db);
    }
}
