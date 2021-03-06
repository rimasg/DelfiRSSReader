package com.fortislabs.delfireader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fortislabs.delfireader.data.RssDataContract;

/**
 * Created by SID on 2016-11-03.
 */

public class RssContentFragment extends Fragment implements RssContract.View {
    private static final String[] FROM_CONTENT = {
            RssDataContract.ContentEntry.COL_TITLE,
            RssDataContract.ContentEntry.COL_DESCRIPTION
    };
    private static final int[] TO_CONTENT = {
            R.id.rss_title,
            R.id.rss_content
    };

    private RssAdapter rssAdapter;
    private RssContract.Presenter presenter;
    private ProgressBar progressBar;
    private ListView contentListView;

    public static RssContentFragment newInstance() {
        RssContentFragment fragment = new RssContentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content_list, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        contentListView = (ListView) view.findViewById(R.id.rss_content);
        rssAdapter = new RssAdapter(getActivity(), null, 0);
        contentListView.setAdapter(rssAdapter);
        return view;
    }

    @Override
    public void showContent(Cursor cursor) {
        rssAdapter.swapCursor(cursor);
        rssAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackbarNotification(String message) {
        Snackbar.make(contentListView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTitle(String title) {
        // no-op
    }

    @Override
    public void setProgress(int maxValue) {
        progressBar.setProgress(maxValue);
    }

    @Override
    public void setProgressMax(int maxValue) {
        progressBar.setMax(maxValue);
    }

    @Override
    public void setPresenter(RssContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
