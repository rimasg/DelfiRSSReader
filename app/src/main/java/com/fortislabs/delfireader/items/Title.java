package com.fortislabs.delfireader.items;

import android.database.Cursor;

import com.fortislabs.delfireader.data.RssDataContract;

/**
 * Created by SID on 2016-11-03.
 */

public class Title {
    public String title;
    public String link;

    public Title(Cursor cursor) {
        this.title = cursor.getString(RssDataContract.TitleEntry.INDEX_TITLE);
        this.link = cursor.getString(RssDataContract.TitleEntry.INDEX_LINK);
    }
}
