package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by SID on 2016-11-03.
 */

public interface RssContract {
    interface View extends BaseView<Presenter> {
        void showContent(Cursor cursor);
    }

    interface Presenter extends BasePresenter {
        void initLoaders();

        void loadTitles();

        void loadContentByTitle(String title);

        void loadContentByTitleId(int id);

        void insertTitle(ContentValues values);

        void bulkInsertTitle(ContentValues[] values);

        void insertContent(ContentValues values);

        void bulkInsertContent(ContentValues[] values);

        void deleteAllRecords();
    }
}
