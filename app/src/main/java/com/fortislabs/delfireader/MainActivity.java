package com.fortislabs.delfireader;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fortislabs.delfireader.data.RssDataContract;
import com.fortislabs.delfireader.services.RssPullService;

public class MainActivity extends AppCompatActivity implements TitlesFragment.NavigationDrawerCallbacks{
    private RssContract.Presenter presenter;
    private CharSequence title;
    private TitlesFragment titlesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = getTitle();
        titlesFragment = (TitlesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        titlesFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        final ContentFragment contentFragment = ContentFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, contentFragment)
                .commit();

        presenter = new RssPresenter(getApplicationContext(), getSupportLoaderManager(), titlesFragment, contentFragment);
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
        if (!titlesFragment.isDrawerOpen()) {
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
                RssPullService.startRssPullAction(MainActivity.this, RssDataContract.RSS_TITLES_URL);
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
