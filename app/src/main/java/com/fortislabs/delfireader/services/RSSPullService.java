package com.fortislabs.delfireader.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;


public class RssPullService extends IntentService {

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
        // TODO: Handle action RssPull
        final long durationMillis = 3000L;
        try {
            Thread.sleep(durationMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Operation finished in " + (durationMillis / 1000) + "seconds", Toast.LENGTH_SHORT).show();
    }
}
