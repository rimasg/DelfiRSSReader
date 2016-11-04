package com.fortislabs.delfireader.annotations;

import android.support.annotation.IntDef;

import com.fortislabs.delfireader.RssPresenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Okis on 2016.11.04.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        RssPresenter.TITLES_ID,
        RssPresenter.CONTENT_ID
})
public @interface ReplyMessageId {}
