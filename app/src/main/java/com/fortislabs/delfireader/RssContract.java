package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by SID on 2016-11-03.
 */

public interface RssContract {
    interface View extends BaseView<Presenter> {
        void onLoadData();

        void showTitles(Cursor cursor);

        void showContent(Cursor cursor);
    }

    interface Presenter extends BasePresenter {
        void getAllTitles();

        void getContentByTitle(String title);

        void addTitles(ContentValues values);

        void addContent(ContentValues values);

        int deleteAllRecords(String tableName);
    }
}
