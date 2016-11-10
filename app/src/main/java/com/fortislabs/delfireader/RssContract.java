package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by SID on 2016-11-03.
 */

public interface RssContract {
    interface View extends BaseView<Presenter> {
        void showContent(Cursor cursor);

        void showToast(String message);

        void showSnackbarNotification(String message);

        void showTitle(String title);
    }

    interface Presenter extends BasePresenter {
        void initLoaders();

        void initRssPullService();

        void loadTitles();

        void loadTitleThumbnailsToCache();

        void loadContentByTitle(String title);

        void loadContentByTitleId(int id);

        void loadContentThumbnailsToCache();

        void insertTitle(ContentValues values);

        void bulkInsertTitle(ContentValues[] values);

        void insertContent(ContentValues values);

        void bulkInsertContent(ContentValues[] values);

        void deleteAllRecords();

        boolean getNetworkAvailable();

        void setNetworkAvailable(boolean available);
    }
}
