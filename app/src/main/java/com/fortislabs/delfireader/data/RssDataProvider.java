package com.fortislabs.delfireader.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

public class RssDataProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TITLE_CODE = 100;
    private static final int CONTENT_CODE = 101;

    static {
        uriMatcher.addURI(RssDataContract.CONTENT_AUTHORITY, RssDataContract.TitleEntry.TABLE_NAME, TITLE_CODE);
        uriMatcher.addURI(RssDataContract.CONTENT_AUTHORITY, RssDataContract.ContentEntry.TABLE_NAME, CONTENT_CODE);
    }

    public RssDataProvider() {
    }

    @Override
    public boolean onCreate() {
        // We're using DatabaseManager singleton, so not creating anything here
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case TITLE_CODE:
                cursor = DatabaseManager.inst(getContext()).getAllTitles();
                break;
            case CONTENT_CODE:
                if (selectionArgs == null) {
                    cursor = DatabaseManager.inst(getContext()).getAllContent();
                } else {
                    cursor = DatabaseManager.inst(getContext()).getContentByTitle(selectionArgs[0]);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case TITLE_CODE:
                id = DatabaseManager.inst(getContext()).insertTitle(values);
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Insert error " + uri);
                }
            case CONTENT_CODE:
                id = DatabaseManager.inst(getContext()).insertTitle(values);
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Insert error " + uri);
                }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int id = -1;
        switch (uriMatcher.match(uri)) {
            case TITLE_CODE:
                id = DatabaseManager.inst(getContext()).bulkInsertTitles(values);
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return id;
                } else {
                    throw new SQLiteException("Insert error");
                }
            case CONTENT_CODE:
                id = DatabaseManager.inst(getContext()).bulkInsertContent(values);
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return id;
                } else {
                    throw new SQLiteException("Insert error");
                }
            default:
                throw new IllegalArgumentException("Bulk insert -- Invalid uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = -1;
        switch (uriMatcher.match(uri)) {
            case TITLE_CODE:
                id = DatabaseManager.inst(getContext()).deleteAllRecords(RssDataContract.TitleEntry.TABLE_NAME);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case CONTENT_CODE:
                id = DatabaseManager.inst(getContext()).deleteAllRecords(RssDataContract.ContentEntry.TABLE_NAME);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
