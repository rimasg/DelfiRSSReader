package com.fortislabs.delfireader.items;

import android.database.Cursor;

import com.fortislabs.delfireader.data.RssDataContract;

/**
 * Created by SID on 2016-11-03.
 */

public class Content {
    public String title;
    public String description;
    public String link;
    public int pub_date;
    public String thumbnailUrl;
    public int read;

    public Content(Cursor cursor) {
        title = cursor.getString(RssDataContract.ContentEntry.INDEX_TITLE);
        description = cursor.getString(RssDataContract.ContentEntry.INDEX_DESCRIPTION);
        link = cursor.getString(RssDataContract.ContentEntry.INDEX_LINK);
        pub_date = cursor.getInt(RssDataContract.ContentEntry.INDEX_PUB_DATE);
        thumbnailUrl = cursor.getString(RssDataContract.ContentEntry.INDEX_THUMBNAIL_URL);
        read = cursor.getInt(RssDataContract.ContentEntry.INDEX_READ);
    }
}
