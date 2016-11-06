package com.fortislabs.delfireader.annotations;

import android.support.annotation.StringDef;

import com.fortislabs.delfireader.data.RssDataContract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Okis on 2016.11.04.
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        RssDataContract.TitleEntry.TABLE_NAME,
        RssDataContract.ContentEntry.TABLE_NAME
})
public @interface TableName {}
