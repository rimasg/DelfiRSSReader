package com.fortislabs.delfireader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SID on 2016-11-02.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "rss.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String BOOLEAN_TYPE = " BOOLEAN NOT NULL DEFAULT 0";
    public static final String COMMA_SEP = ",";

    private final String SQL_TITLE_CREATE = "CREATE TABLE " + RssDataContract.TitleEntry.TABLE_NAME + " (" +
            RssDataContract.TitleEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            RssDataContract.TitleEntry.COL_TITLE + TEXT_TYPE + COMMA_SEP +
            RssDataContract.TitleEntry.COL_LINK + TEXT_TYPE + ")";
    private final String SQL_CONTENT_CREATE = "CREATE TABLE " + RssDataContract.ContentEntry.TABLE_NAME + " (" +
            RssDataContract.ContentEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            RssDataContract.ContentEntry.COL_TITLE + TEXT_TYPE + COMMA_SEP +
            RssDataContract.ContentEntry.COL_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            RssDataContract.ContentEntry.COL_PUB_DATE + INTEGER_TYPE + COMMA_SEP +
            RssDataContract.ContentEntry.COL_THUMBNAIL + TEXT_TYPE + COMMA_SEP +
            RssDataContract.ContentEntry.COL_READ + BOOLEAN_TYPE + ")";
    private final String SQL_TITLE_DELETE = "DROP TABLE IF EXISTS " + RssDataContract.TitleEntry.TABLE_NAME;
    private final String SQL_CONTENT_DELETE = "DROP TABLE IF EXISTS " + RssDataContract.ContentEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TITLE_CREATE);
        db.execSQL(SQL_CONTENT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_TITLE_DELETE);
        db.execSQL(SQL_CONTENT_DELETE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
