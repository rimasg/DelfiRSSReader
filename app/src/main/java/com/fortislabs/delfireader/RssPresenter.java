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

import com.bumptech.glide.Glide;
import com.fortislabs.delfireader.data.RssDataContract;
import com.fortislabs.delfireader.items.Content;
import com.fortislabs.delfireader.items.Title;
import com.fortislabs.delfireader.services.RssPullService;

/**
 * Created by SID on 2016-11-03.
 */

public class RssPresenter implements RssContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "RssPresenter";
    public static final int TITLES_ID = 717;
    public static final int CONTENT_ID = 780;
    public static final int PROGRESS_STEP = 838;
    public static final int PROGRESS_MAX = 916;

    @NonNull private final Context context;
    @NonNull private final LoaderManager loaderManager;
    @NonNull private final RssContract.View titleView;
    @NonNull private final RssContract.View contentView;
    private boolean isNetworkAvailable;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ContentValues[] values = null;
            switch (msg.what) {
                case TITLES_ID:
                    values = (ContentValues[]) msg.obj;
                    if (values != null) {
                        bulkInsertTitle(values);
                        loadTitleThumbnailsToCache();
                    }
                    break;
                case CONTENT_ID:
                    values = (ContentValues[]) msg.obj;
                    if (values != null) {
                        bulkInsertContent(values);
                        loadContentThumbnailsToCache();
                    }
                    break;
                case PROGRESS_STEP:
                    contentView.setProgress((Integer) msg.obj);
                    break;
                case PROGRESS_MAX:
                    contentView.setProgressMax((Integer) msg.obj);
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
        if (isNetworkAvailable) {
            RssPullService.startRssPullAction(context, handler, RssDataContract.RSS_TITLES_URL);
        } else {
            contentView.showToast(context.getString(R.string.network_not_available));
        }
    }

    @Override
    public void loadTitles() {
        final Cursor cursor = context.getContentResolver().query(RssDataContract.TitleEntry.CONTENT_URI, null, null, null, null);
        titleView.showContent(cursor);
    }

    @Override
    public void loadTitleThumbnailsToCache() {
        // no-op
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
            titleView.showTitle(title.title);
        }
    }

    @Override
    public void loadContentThumbnailsToCache() {
        final Cursor cursor = context.getContentResolver().query(RssDataContract.ContentEntry.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                final String thumbnailUrl = new Content(cursor).thumbnailUrl;
                Glide.with(context).load(thumbnailUrl).downloadOnly(125, 125);
            } while (cursor.moveToNext());
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
    public boolean getNetworkAvailable() {
        return isNetworkAvailable;
    }

    @Override
    public void setNetworkAvailable(boolean available) {
        isNetworkAvailable = available;
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
        contentView.showSnackbarNotification("News downloaded.");
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
