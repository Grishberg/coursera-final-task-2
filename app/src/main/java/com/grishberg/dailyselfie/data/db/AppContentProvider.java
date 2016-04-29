package com.grishberg.dailyselfie.data.db;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

//generated by CP-generator
public class AppContentProvider extends ContentProvider {
	private static final String AUTHORITY = "com.grishberg.dailyselfie.content_provider";
	private static final String PATH_PICTURES = DbHelper.TABLE_PICTURES;

	public static final Uri CONTENT_URI_PICTURES = Uri.parse("content://" + AUTHORITY +"/" +PATH_PICTURES);

	private static final int CODE_PICTURES = 0;
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		URI_MATCHER.addURI(AUTHORITY, PATH_PICTURES, CODE_PICTURES);
	}
	private static DbHelper dbHelper;

	public synchronized static DbHelper getDbHelper(Context context) {
		if (null == dbHelper) {
			dbHelper = new DbHelper(context);
		}
		return dbHelper;
	}


	@Override
	public boolean onCreate() {
		getDbHelper(getContext());
		return true;
	}
	private String parseUri(Uri uri) {
		return parseUri(URI_MATCHER.match(uri));
	}

	private String parseUri(int match) {
		String table = null;
		switch (match) {
			case CODE_PICTURES:
				table = DbHelper.TABLE_PICTURES;
				break;
			default:
				throw new IllegalArgumentException("Invalid DB code: " + match);
		}
		return table;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection
			, String[] selectionArgs, String sortOrder) {
		int uriId = URI_MATCHER.match(uri);
		Cursor cursor = null;
		switch(uriId){
			case CODE_PICTURES:
				cursor = dbHelper.getReadableDatabase()
					.query(DbHelper.TABLE_PICTURES, projection, selection
					,selectionArgs, null, null, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Invalid DB code:" + uri);
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = -1;
		int uriId = URI_MATCHER.match(uri);
		String table = parseUri(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch(uriId){
			case CODE_PICTURES:
				id = db.insert(table, null, values);
				break;
			default:
				id = db.insert(table, null, values);
		}
		Uri resultUri = ContentUris.withAppendedId(uri, id);
		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int result = 0;
		//int uriId = URI_MATCHER.match(uri);
		String table = parseUri(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		result	= db.update(table, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return result;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = 0;
		//int uriId = URI_MATCHER.match(uri);
		String table = parseUri(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		result = db.delete(table, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return result;
	}

	private long insertOrUpdateById(SQLiteDatabase db, Uri uri, String table,
		ContentValues values, String column) throws SQLiteConstraintException{
		long result	= -1;
		try {
			result = db.insertOrThrow(table, null, values);
		} catch (SQLiteConstraintException e) {
			try(Cursor cursor = dbHelper.getReadableDatabase()
				.query(table, new String[]{DbHelper.COLUMN_ID}
					, column + "=?", null, null, null, null)) {
				if (cursor != null && cursor.moveToFirst()) {
					int idColId = cursor.getColumnIndex(DbHelper.COLUMN_ID);
					result = cursor.getLong(idColId);
				}
				int nRows = update(uri, values, column + "=?",
						new String[]{values.getAsString(column)});
				if (nRows == 0) {
					throw e;
				}
			}
		}
		return result;
	}

}
