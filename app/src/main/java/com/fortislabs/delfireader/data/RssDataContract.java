package com.fortislabs.delfireader.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.fortislabs.delfireader.BuildConfig;

/**
 * Created by SID on 2016-11-02.
 */

public final class RssDataContract {
    private RssDataContract() { }

    public static final String RSS_TITLES_URL = "http://www.delfi.lt/rss/feeds/index.xml";

    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static class TitleEntry implements BaseColumns {
        public static final String TABLE_NAME = "titles";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_LINK = "link";

        public static final int INDEX_TITLE = 1;
        public static final int INDEX_DESCRIPTION = 2;
        public static final int INDEX_LINK = 3;

        public static final String[] PROJECTION = {
                _ID,
                COL_TITLE,
                COL_DESCRIPTION,
                COL_LINK
        };
    }

    public static class ContentEntry implements BaseColumns {
        public static final String TABLE_NAME = "content";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_LINK = "link";
        public static final String COL_PUB_DATE = "pub_date";
        public static final String COL_THUMBNAIL_URL = "thumbnail_url";
        public static final String COL_READ = "read"; // "TRUE" if article has been read

        public static final int INDEX_TITLE = 1;
        public static final int INDEX_DESCRIPTION = 2;
        public static final int INDEX_LINK = 3;
        public static final int INDEX_PUB_DATE = 4;
        public static final int INDEX_THUMBNAIL_URL = 5;
        public static final int INDEX_READ = 6;

        public static final String PROJECTION[] = {
                _ID,
                COL_TITLE,
                COL_DESCRIPTION,
                COL_LINK,
                COL_PUB_DATE,
                COL_THUMBNAIL_URL,
                COL_READ
        };
    }
}
