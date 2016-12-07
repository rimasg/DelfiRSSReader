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

import com.fortislabs.delfireader.R;
import com.fortislabs.delfireader.RssPresenter;
import com.fortislabs.delfireader.annotations.ProgressMessageId;
import com.fortislabs.delfireader.annotations.ReplyMessageId;
import com.fortislabs.delfireader.data.RssDataContract;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        // TODO: 2016.11.05 it's better to implement Toast user notification
        try {
            downloadContent(downloadTitles(url));
        } catch (IOException e) {
            Log.e(TAG, getString(R.string.connection_error), e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, getString(R.string.xml_error), e);
        }
    }

    private List<RssPullParser.Entry> downloadTitles(String url) throws IOException, XmlPullParserException {
        InputStream is = null;
        final RssPullParser xmlParser = new RssPullParser();
        List<RssPullParser.Entry> entries = new ArrayList<>();
        try {
            is = downloadUrl(url);
            entries = xmlParser.parse(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }

        final ContentValues[] values = convertTitleEntriesToContentValues(entries);
        sendMessage(RssPresenter.TITLES_ID, values);
        return entries;
    }

    private void downloadContent(List<RssPullParser.Entry> titles) throws IOException, XmlPullParserException {
        setProgress(RssPresenter.PROGRESS_MAX, titles.size());
        int progress = 0;
        InputStream is = null;
        final RssPullParser xmlParser = new RssPullParser();
        final HashMap<String, List<RssPullParser.Entry>> titleContentMap = new HashMap<>();
        for (RssPullParser.Entry title : titles) {
            try {
                List<RssPullParser.Entry> entries = new ArrayList<>();
                is = downloadUrl(title.link);
                entries.addAll(xmlParser.parse(is));
                titleContentMap.put(title.title, entries);
                setProgress(RssPresenter.PROGRESS_STEP, ++progress);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        setProgress(RssPresenter.PROGRESS_STEP, 0);
        final ContentValues[] values = convertContentEntriesToContentValues(titleContentMap);
        sendMessage(RssPresenter.CONTENT_ID, values);
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        final URL url = new URL(urlString);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        final InputStream stream = conn.getInputStream();
        return stream;
    }

    private ContentValues[] convertTitleEntriesToContentValues(List<RssPullParser.Entry> entries) {
        final List<ContentValues> values = new ArrayList<>();
        for (RssPullParser.Entry entry : entries) {
            final ContentValues value = new ContentValues();
            value.put(RssDataContract.TitleEntry.COL_TITLE, entry.title);
            value.put(RssDataContract.TitleEntry.COL_DESCRIPTION, entry.description);
            value.put(RssDataContract.TitleEntry.COL_LINK, entry.link);
            values.add(value);
        }
        ContentValues[] valuesArray = new ContentValues[entries.size()];
        valuesArray = values.toArray(valuesArray);
        return valuesArray;
    }

    private ContentValues[] convertContentEntriesToContentValues(HashMap<String, List<RssPullParser.Entry>> titleContentMap) {
        final List<ContentValues> values = new ArrayList<>();
        for (Map.Entry<String, List<RssPullParser.Entry>> mapEntry : titleContentMap.entrySet()) {
            final String categoryTitle = mapEntry.getKey();
            for (RssPullParser.Entry entry : mapEntry.getValue()) {
                final ContentValues value = new ContentValues();
                value.put(RssDataContract.ContentEntry.COL_CATEGORY_TITLE, categoryTitle);
                value.put(RssDataContract.ContentEntry.COL_TITLE, entry.title);
                value.put(RssDataContract.ContentEntry.COL_DESCRIPTION, entry.description);
                value.put(RssDataContract.ContentEntry.COL_LINK, entry.link);
                value.put(RssDataContract.ContentEntry.COL_PUB_DATE, entry.pubDate);
                value.put(RssDataContract.ContentEntry.COL_THUMBNAIL_URL, entry.thumbnailUrl);
                values.add(value);
            }
        }
        ContentValues[] valuesArray = new ContentValues[values.size()];
        valuesArray = values.toArray(valuesArray);
        return valuesArray;
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

    private void setProgress(@ProgressMessageId int messageId, int value) {
        final Message message = Message.obtain();
        message.what = messageId;
        message.obj = value;
        try {
            replyMessenger.send(message);
        } catch (RemoteException e) {
            Log.e(getClass().getName(), "Exception while sending reply message back to RssPresenter.", e);
        }
    }
}