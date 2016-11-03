package com.fortislabs.delfireader.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
                cursor = DatabaseManager.inst(getContext()).getContentByTitle(selectionArgs[0]);
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
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
