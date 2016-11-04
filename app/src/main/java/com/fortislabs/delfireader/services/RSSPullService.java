package com.fortislabs.delfireader.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class RssPullService extends IntentService {
    private static final String TAG = "RssPullService";

    public RssPullService() {
        super("RssPullService");
    }

    public static void startRssPullAction(Context context, String url) {
        Intent intent = new Intent(context, RssPullService.class);
        intent.setData(Uri.parse(url));
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String url = intent.getDataString();
            handleRssPullAction(url);
        }
    }

    private void handleRssPullAction(String url) {
        // TODO: Handle RssPull action
    }
}
