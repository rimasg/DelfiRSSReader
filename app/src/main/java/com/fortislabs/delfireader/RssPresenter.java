package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.fortislabs.delfireader.data.DatabaseManager;
import com.fortislabs.delfireader.items.Title;

/**
 * Created by SID on 2016-11-03.
 */

public class RssPresenter implements RssContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TITLES_LOADER_ID = 717;
    private static final int CONTENT_LOADER_ID = 780;

    private final Context context;
    @NonNull
    private final LoaderManager loaderManager;
    private final RssContract.View titleView;
    private final RssContract.View contentView;

    public RssPresenter(Context context, LoaderManager loaderManager, RssContract.View titleView, RssContract.View contentView) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.titleView = titleView;
        this.titleView.setPresenter(this);
        this.contentView = contentView;
        this.contentView.setPresenter(this);
    }

    @Override
    public void loadTitles() {
        final Cursor cursor = DatabaseManager.inst(context).getAllTitles();
        titleView.showContent(cursor);
        loaderManager.initLoader(TITLES_LOADER_ID, null, this);
        loaderManager.initLoader(CONTENT_LOADER_ID, null, this);
    }

    @Override
    public void loadContentByTitle(String title) {
        final Cursor cursor = DatabaseManager.inst(context).getContentByTitle(title);
        contentView.showContent(cursor);
    }

    @Override
    public void loadContentByTitleId(int id) {
        final Cursor cursor = DatabaseManager.inst(context).getAllTitles();
        if (cursor.moveToPosition(id)) {
            final Title title = new Title(cursor);
            loadContentByTitle(title.title);
        }
    }

    @Override
    public void addTitle(ContentValues values) {
        DatabaseManager.inst(context).addTitle(values);
    }

    @Override
    public void addContent(ContentValues values) {
        DatabaseManager.inst(context).addContent(values);
    }

    @Override
    public int deleteAllRecords(String tableName) {
        return DatabaseManager.inst(context).deleteAllRecords(tableName);
    }

    @Override
    public void start() {
        loadTitles();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TITLES_LOADER_ID:
                break;
            case CONTENT_LOADER_ID:
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case TITLES_LOADER_ID:
                titleView.showContent(data);
                break;
            case CONTENT_LOADER_ID:
                contentView.showContent(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case TITLES_LOADER_ID:
                titleView.showContent(null);
                break;
            case CONTENT_LOADER_ID:
                contentView.showContent(null);
                break;
        }
    }
}
