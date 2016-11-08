package com.fortislabs.delfireader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

    public static RssContentFragment newInstance() {
        RssContentFragment fragment = new RssContentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ListView contentListView = (ListView) inflater.inflate(R.layout.fragment_content_list, container, false);
        rssAdapter = new RssAdapter(getActivity(), null, 0);
        contentListView.setAdapter(rssAdapter);
        return contentListView;
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
    public void showTitle(String title) {
        // no-op
    }

    @Override
    public void setPresenter(RssContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
