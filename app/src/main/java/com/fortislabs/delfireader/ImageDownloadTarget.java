package com.fortislabs.delfireader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by Okis on 2016.11.07.
 */

public class ImageDownloadTarget implements Target<File> {
    private static final String TAG = "ImageDownloadTarget";
    public static final String RSS_PICTURE_FOLDER = "rss_picture_folder";
    Context context;

    public ImageDownloadTarget(Context context) {
        this.context = context;
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
    }

    @Override
    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
/*
        final File outFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + RSS_PICTURE_FOLDER, resource.getName());
        if (!outFile.mkdirs()) {
            Log.e(TAG, "Directory not created.");
        }
*/
        // TODO: 2016.11.07 implement save images to private folder; this method is not called
        Log.d(TAG, "RSS file name: " + resource.getAbsolutePath());
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
    }

    @Override
    public void setRequest(Request request) {
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }
}
