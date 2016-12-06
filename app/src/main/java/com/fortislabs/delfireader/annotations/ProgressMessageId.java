package com.fortislabs.delfireader.annotations;

import android.support.annotation.IntDef;

import com.fortislabs.delfireader.RssPresenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Okis on 2016.12.06.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        RssPresenter.PROGRESS_STEP,
        RssPresenter.PROGRESS_MAX
})
public @interface ProgressMessageId {}
