package com.fortislabs.delfireader.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.fortislabs.delfireader.RssPresenter;
import com.fortislabs.delfireader.annotations.ReplyMessageId;


public class RssPullService extends IntentService {
    private static final String TAG = "RssPullService";
    private static final String KEY_REQUEST_MESSAGE = "REQUEST_MESSAGE";
    private Messenger replyMessenger;

    public RssPullService() {
        super("RssPullService");
    }

    public static void startRssPullAction(Context context, Handler handler, String url) {
        Intent intent = new Intent(context, RssPullService.class);
        intent.setData(Uri.parse(url));
        intent.putExtra(KEY_REQUEST_MESSAGE, new Messenger(handler));
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String url = intent.getDataString();
            replyMessenger = (Messenger) intent.getParcelableExtra(KEY_REQUEST_MESSAGE);
            handleRssPullAction(url);
        }
    }

    private void handleRssPullAction(String url) {
        // TODO: Handle RssPull action
        sendMessage(RssPresenter.TITLES_ID, null);
        sendMessage(RssPresenter.CONTENT_ID, null);
    }

    private void sendMessage(@ReplyMessageId int messageId, ContentValues[] values) {
        final Message message = Message.obtain();
        message.what = messageId;
        message.obj = values;
        try {
            replyMessenger.send(message);
        } catch (RemoteException e) {
            Log.e(getClass().getName(), "Exception while sending reply message back to RssPresenter.", e);
        }
    }
}