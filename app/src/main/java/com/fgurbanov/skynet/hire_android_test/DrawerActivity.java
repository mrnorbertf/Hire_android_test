package com.fgurbanov.skynet.hire_android_test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fgurbanov.skynet.hire_android_test.Fragment.AboutFragment;
import com.fgurbanov.skynet.hire_android_test.Fragment.FragmentDrawer;
import com.fgurbanov.skynet.hire_android_test.Fragment.StationListFragment;

/**
 * Главная Activity
 * управляеет фрагментами
 */
public class DrawerActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer mDrawerFragment;
    private Fragment mTimetableFragment;
    private Fragment mAboutFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_fragment);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        mDrawerFragment.setDrawerListener(this);

        displayView(0);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    //чтобы не создавать фрагменты каждый раз сохраняем их
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if (mTimetableFragment == null) {
                    mTimetableFragment = new StationListFragment();
                }
                fragment= mTimetableFragment;
                title = getString(R.string.nav_item_timetable);
                break;
            case 1:
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                }
                fragment = mAboutFragment;
                title = getString(R.string.nav_item_about);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


}
