package com.fortislabs.delfireader;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TitlesFragment.NavigationDrawerCallbacks{
    private RssContract.Presenter presenter;
    private CharSequence title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = getTitle();
        final TitlesFragment titlesFragment = (TitlesFragment) getSupportFragmentManager()
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
}
