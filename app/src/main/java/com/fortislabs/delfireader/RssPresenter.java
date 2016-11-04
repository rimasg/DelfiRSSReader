package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.fortislabs.delfireader.data.RssDataContract;
import com.fortislabs.delfireader.items.Title;
import com.fortislabs.delfireader.services.RssPullService;

/**
 * Created by SID on 2016-11-03.
 */

public class RssPresenter implements RssContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "RssPresenter";
    public static final int TITLES_ID = 717;
    public static final int CONTENT_ID = 780;

    @NonNull private final Context context;
    @NonNull private final LoaderManager loaderManager;
    @NonNull private final RssContract.View titleView;
    @NonNull private final RssContract.View contentView;
    // TODO: 2016.11.04 implement RssPull service result handling
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ContentValues[] values = null;
            switch (msg.what) {
                case TITLES_ID:
                    Log.d(TAG, "handleMessage: Titles received");
                    values = (ContentValues[]) msg.obj;
                    if (values != null) {
                        bulkInsertTitle(values);
                    }
                    break;
                case CONTENT_ID:
                    Log.d(TAG, "handleMessage: Content received");
                    values = (ContentValues[]) msg.obj;
                    if (values != null) {
                        bulkInsertContent(values);
                    }
                    break;
            }
        }
    };

    public RssPresenter(Context context, LoaderManager loaderManager, RssContract.View titleView, RssContract.View contentView) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.titleView = titleView;
        this.titleView.setPresenter(this);
        this.contentView = contentView;
        this.contentView.setPresenter(this);
    }

    @Override
    public void initLoaders() {
        loaderManager.initLoader(TITLES_ID, null, this);
        loaderManager.initLoader(CONTENT_ID, null, this);
    }

    @Override
    public void initRssPullService() {
        RssPullService.startRssPullAction(context, handler, RssDataContract.RSS_TITLES_URL);
    }

    @Override
    public void loadTitles() {
        final Cursor cursor = context.getContentResolver().query(RssDataContract.TitleEntry.CONTENT_URI, null, null, null, null);
        titleView.showContent(cursor);
    }

    @Override
    public void loadContentByTitle(String title) {
        final Cursor cursor = context.getContentResolver().query(RssDataContract.ContentEntry.CONTENT_URI, null, null, new String[]{title}, null);
        contentView.showContent(cursor);
    }

    @Override
    public void loadContentByTitleId(int id) {
        final Cursor cursor = context.getContentResolver().query(RssDataContract.TitleEntry.CONTENT_URI, null, null, null, null);
        if (cursor.moveToPosition(id)) {
            final Title title = new Title(cursor);
            loadContentByTitle(title.title);
        }
    }

    @Override
    public void insertTitle(ContentValues values) {
        context.getContentResolver().insert(RssDataContract.TitleEntry.CONTENT_URI, values);
    }

    @Override
    public void bulkInsertTitle(ContentValues[] values) {
        context.getContentResolver().bulkInsert(RssDataContract.TitleEntry.CONTENT_URI, values);
    }

    @Override
    public void insertContent(ContentValues values) {
        context.getContentResolver().insert(RssDataContract.ContentEntry.CONTENT_URI, values);
    }

    @Override
    public void bulkInsertContent(ContentValues[] values) {
        context.getContentResolver().bulkInsert(RssDataContract.ContentEntry.CONTENT_URI, values);
    }

    @Override
    public void deleteAllRecords() {
        context.getContentResolver().delete(RssDataContract.TitleEntry.CONTENT_URI, null, null);
        context.getContentResolver().delete(RssDataContract.ContentEntry.CONTENT_URI, null, null);
    }

    @Override
    public void start() {
        initLoaders();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TITLES_ID:
                return new CursorLoader(context, RssDataContract.TitleEntry.CONTENT_URI, null, null, null, null);
            case CONTENT_ID:
                return new CursorLoader(context, RssDataContract.ContentEntry.CONTENT_URI, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader == null) return;
        switch (loader.getId()) {
            case TITLES_ID:
                titleView.showContent(data);
                break;
            case CONTENT_ID:
                contentView.showContent(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case TITLES_ID:
                titleView.showContent(null);
                break;
            case CONTENT_ID:
                contentView.showContent(null);
                break;
        }
    }
}
