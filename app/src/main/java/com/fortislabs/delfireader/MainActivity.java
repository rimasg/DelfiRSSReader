package com.fortislabs.delfireader;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RssTitlesFragment.NavigationDrawerCallbacks{
    private RssContract.Presenter presenter;
    private CharSequence title;
    private RssTitlesFragment rssTitlesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = getTitle();
        rssTitlesFragment = (RssTitlesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        rssTitlesFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        final RssContentFragment rssContentFragment = RssContentFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, rssContentFragment)
                .commit();

        presenter = new RssPresenter(getApplicationContext(), getSupportLoaderManager(), rssTitlesFragment, rssContentFragment);
        // TODO: 2016.11.03 testing only, remove this code
/*
        for (int i = 0; i < 10; i++) {
            final ContentValues titleValues = new ContentValues();
            final ContentValues contentValues1 = new ContentValues();
            final ContentValues contentValues2 = new ContentValues();
            final ContentValues contentValues3 = new ContentValues();
            titleValues.put(RssDataContract.TitleEntry.COL_TITLE, "Title " + i);
            titleValues.put(RssDataContract.TitleEntry.COL_LINK, "url");
            contentValues1.put(RssDataContract.ContentEntry.COL_TITLE, "Title " + i);
            contentValues1.put(RssDataContract.ContentEntry.COL_DESCRIPTION, "Desc 1");
            contentValues2.put(RssDataContract.ContentEntry.COL_TITLE, "Title " + i);
            contentValues2.put(RssDataContract.ContentEntry.COL_DESCRIPTION, "Desc 2");
            contentValues3.put(RssDataContract.ContentEntry.COL_TITLE, "Title " + i);
            contentValues3.put(RssDataContract.ContentEntry.COL_DESCRIPTION, "Desc 3");
            presenter.insertTitle(titleValues);
            presenter.insertContent(contentValues1);
            presenter.insertContent(contentValues2);
            presenter.insertContent(contentValues3);
        }
*/
//        presenter.loadTitles();
//        presenter.deleteAllRecords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (presenter != null) {
            presenter.loadContentByTitleId(position);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!rssTitlesFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                presenter.initRssPullService();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about:
                Toast.makeText(this, "About App", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
