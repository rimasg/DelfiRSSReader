package com.fortislabs.delfireader.items;

import android.database.Cursor;

import com.fortislabs.delfireader.data.RssDataContract;

/**
 * Created by SID on 2016-11-03.
 */

public class Content {
    public String title;
    public String description;
    public int pub_date;
    public String thumbnail;
    public int read;

    public Content(Cursor cursor) {
        title = cursor.getString(RssDataContract.ContentEntry.INDEX_TITLE);
        description = cursor.getString(RssDataContract.ContentEntry.INDEX_DESCRIPTION);
        pub_date = cursor.getInt(RssDataContract.ContentEntry.INDEX_PUB_DATE);
        thumbnail = cursor.getString(RssDataContract.ContentEntry.INDEX_THUMBNAIL);
        read = cursor.getInt(RssDataContract.ContentEntry.INDEX_READ);
    }
}
