package com.fortislabs.delfireader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private NetworkReceiver receiver;

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

        receiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
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
        // TODO: 2016.11.06 implement Settings and About
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

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                presenter.setNetworkAvailable(true);
            } else {
                presenter.setNetworkAvailable(false);
            }
        }
    }
}
