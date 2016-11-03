package com.fortislabs.delfireader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Okis on 2016.11.03.
 */

public final class DatabaseManager {
    private static DatabaseManager inst;
    private DbHelper dbHelper;

    private DatabaseManager(Context context) {
        dbHelper = new DbHelper(context.getApplicationContext());
    }

    public static synchronized DatabaseManager inst(Context context) {
        if (inst == null) {
            inst = new DatabaseManager(context);
        }
        return inst;
    }

    public Cursor getAllTitles() {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.query(
                RssDataContract.TitleEntry.TABLE_NAME,
                RssDataContract.TitleEntry.PROJECTION,
                null, null, null, null, null);
        return cursor;
    }

    public Cursor getContentByTitle(String title) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.query(
                RssDataContract.ContentEntry.TABLE_NAME,
                RssDataContract.ContentEntry.PROJECTION,
                RssDataContract.ContentEntry.COL_TITLE + " = ?",
                new String[]{title}, null, null, null);
        return cursor;
    }

    public long addTitles(ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final long rowId = db.insert(RssDataContract.TitleEntry.TABLE_NAME, null, values);
        return rowId;
    }

    public long addContent(ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final long rowId = db.insert(RssDataContract.ContentEntry.TABLE_NAME, null, values);
        return rowId;
    }

    public int deleteAllRecords(String tableName) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(tableName, "1", null);
    }
}
