package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fortislabs.delfireader.data.DatabaseManager;

/**
 * Created by SID on 2016-11-03.
 */

public class RssPresenter implements RssContract.Presenter {
    private final RssContract.View view;
    private final Context context;

    public RssPresenter(Context context, RssContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getAllTitles() {
        final Cursor cursor = DatabaseManager.inst(context).getAllTitles();
        view.showTitles(cursor);
    }

    @Override
    public void getContentByTitle(String title) {
        final Cursor cursor = DatabaseManager.inst(context).getContentByTitle(title);
        view.showContent(cursor);
    }

    @Override
    public void addTitles(ContentValues values) {
        DatabaseManager.inst(context).addTitles(values);
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

    }
}
