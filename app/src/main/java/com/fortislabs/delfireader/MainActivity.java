package com.fortislabs.delfireader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fortislabs.delfireader.data.RssDataContract;
import com.fortislabs.delfireader.items.Title;

public class MainActivity extends AppCompatActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks, RssContract.View{

    private RssContract.Presenter presenter;
    private NavigationDrawerFragment navigationDrawerFragment;
    private CharSequence title;
    private SimpleCursorAdapter titlesAdapter;
    private SimpleCursorAdapter contentAdapter;

    private static final String[] FROM_TITLE = {RssDataContract.ContentEntry.COL_TITLE};
    private static final int[] TO_TITLE = {R.id.rss_title};

    private static final String[] FROM_CONTENT = {
            RssDataContract.ContentEntry.COL_TITLE,
            RssDataContract.ContentEntry.COL_DESCRIPTION
    };
    private static final int[] TO_CONTENT = {
            R.id.rss_title,
            R.id.rss_content
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        title = getTitle();
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        final ContentFragment contentFragment = ContentFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, contentFragment)
                .commit();

        titlesAdapter = new SimpleCursorAdapter(this, R.layout.drawer_list_item, null, FROM_TITLE, TO_TITLE, 0);
        contentAdapter = new SimpleCursorAdapter(this, R.layout.content_list_item, null, FROM_CONTENT, TO_CONTENT, 0);

        new RssPresenter(getApplicationContext(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Item selected: " + position, Toast.LENGTH_SHORT).show();
        final Cursor cursor = titlesAdapter.getCursor();
        if (cursor.moveToPosition(position)) {
            final Title title = new Title(cursor);
            presenter.getContentByTitle(title.title);
        }
    }

    @Override
    public void onLoadData() {

    }

    @Override
    public void showTitles(Cursor cursor) {
        titlesAdapter.swapCursor(cursor);
    }

    @Override
    public void showContent(Cursor cursor) {
        contentAdapter.swapCursor(cursor);
    }

    @Override
    public void setPresenter(RssContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
