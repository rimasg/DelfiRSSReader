package com.fortislabs.delfireader.data;

import android.provider.BaseColumns;

/**
 * Created by SID on 2016-11-02.
 */

public final class RssDataContract {
    private RssDataContract() { }

    public static class TitleEntry implements BaseColumns {
        public static final String TABLE_NAME = "titles";

        public static final String COL_TITLE = "title";
        public static final String COL_LINK = "link";

        public static final int INDEX_TITLE = 1;
        public static final int INDEX_LINK = 2;

        public static final String[] PROJECTION = {
                _ID,
                COL_TITLE,
                COL_LINK
        };
    }

    public static class ContentEntry implements BaseColumns {
        public static final String TABLE_NAME = "content";

        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_PUB_DATE = "pub_date";
        public static final String COL_THUMBNAIL = "thumbnail";
        public static final String COL_READ = "read"; // "TRUE" if article has been read

        public static final int INDEX_TITLE = 1;
        public static final int INDEX_DESCRIPTION = 2;
        public static final int INDEX_PUB_DATE = 3;
        public static final int INDEX_THUMBNAIL = 4;
        public static final int INDEX_READ = 5;

        public static final String PROJECTION[] = {
                _ID,
                COL_TITLE,
                COL_DESCRIPTION,
                COL_PUB_DATE,
                COL_THUMBNAIL,
                COL_READ
        };
    }
}
