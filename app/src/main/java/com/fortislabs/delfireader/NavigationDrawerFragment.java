package com.fortislabs.delfireader;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private NavigationDrawerCallbacks callbacks;
    private ActionBarDrawerToggle drawerToggle;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private View fragmentContainerView;

    private int currentSelectedPosition = 0;
    private boolean fromSavedInstancestate;
    private boolean userLearnedDrawer;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            fromSavedInstancestate = true;
        }

        selectItem(currentSelectedPosition);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String[] RssTitles =  getResources().getStringArray(R.array.rss_titles);

        drawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        drawerListView.setAdapter(new ArrayAdapter<>(getActionBar().getThemedContext(),
                R.layout.drawer_list_item, R.id.rss_title, RssTitles));
        return drawerListView;
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout) {
        fragmentContainerView = getActivity().findViewById(fragmentID);
        this.drawerLayout = drawerLayout;
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                this.drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (!userLearnedDrawer) {
                    userLearnedDrawer = true;
                    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu();
            }
        };
        if (!userLearnedDrawer && !fromSavedInstancestate) {
            drawerLayout.openDrawer(fragmentContainerView);
        }

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        drawerLayout.addDrawerListener(drawerToggle);
    }

    public void selectItem(int position) {
        currentSelectedPosition = position;
        if (drawerListView != null) {
            drawerListView.setItemChecked(position, true);
        }
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(fragmentContainerView);
        }
        if (callbacks != null) {
            callbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callbacks = (NavigationDrawerCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (drawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}
