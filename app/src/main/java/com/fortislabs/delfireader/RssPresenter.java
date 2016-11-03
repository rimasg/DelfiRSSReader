package com.fortislabs.delfireader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fortislabs.delfireader.data.DatabaseManager;
import com.fortislabs.delfireader.items.Title;

/**
 * Created by SID on 2016-11-03.
 */

public class RssPresenter implements RssContract.Presenter {
    private final RssContract.View titleView;
    private final RssContract.View contentView;
    private final Context context;

    public RssPresenter(Context context, RssContract.View titleView, RssContract.View contentView) {
        this.context = context;
        this.titleView = titleView;
        this.titleView.setPresenter(this);
        this.contentView = contentView;
        this.contentView.setPresenter(this);
    }

    @Override
    public void loadTitles() {
        final Cursor cursor = DatabaseManager.inst(context).getAllTitles();
        titleView.showContent(cursor);
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
}
