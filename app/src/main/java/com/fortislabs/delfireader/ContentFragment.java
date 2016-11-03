package com.fortislabs.delfireader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fortislabs.delfireader.data.RssDataContract;

/**
 * Created by SID on 2016-11-03.
 */

public class ContentFragment extends Fragment implements RssContract.View {
    private static final String[] FROM_CONTENT = {
            RssDataContract.ContentEntry.COL_TITLE,
            RssDataContract.ContentEntry.COL_DESCRIPTION
    };
    private static final int[] TO_CONTENT = {
            R.id.rss_title,
            R.id.rss_content
    };

    private SimpleCursorAdapter adapter;
    private RssContract.Presenter presenter;

    public static ContentFragment newInstance() {
        ContentFragment fragment = new ContentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ListView contentListView = (ListView) inflater.inflate(R.layout.fragment_content_list, container, false);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.content_list_item, null, FROM_CONTENT, TO_CONTENT, 0);
        contentListView.setAdapter(adapter);
        return contentListView;
    }

    @Override
    public void showContent(Cursor cursor) {
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(RssContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
