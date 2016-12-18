package com.fgurbanov.skynet.hire_android_test;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fgurbanov.skynet.hire_android_test.Connection.ToToConnection;
import com.fgurbanov.skynet.hire_android_test.Data.City;
import com.fgurbanov.skynet.hire_android_test.Data.Country;
import com.fgurbanov.skynet.hire_android_test.Data.Station;
import com.fgurbanov.skynet.hire_android_test.Data.StationLab;
import com.fgurbanov.skynet.hire_android_test.Fragment.AboutFragment;
import com.fgurbanov.skynet.hire_android_test.Fragment.FragmentDrawer;
import com.fgurbanov.skynet.hire_android_test.Fragment.StationListFragment;

import java.util.ArrayList;
import java.util.List;

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


        new StationItemsTask().execute();


    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if (mTimetableFragment == null) {
                    mTimetableFragment = new StationListFragment().newInstance();
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

    private class StationItemsTask extends AsyncTask<Void,Void,List<Station>> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(DrawerActivity.this);
            mDialog.setMessage("Doing something, please wait.");
            mDialog.show();
        }

        @Override
        protected List<Station> doInBackground(Void... params) {
            return new ToToConnection().getStationItems();
        }

        @Override
        protected void onPostExecute(List<Station> stationList) {
            List<Country> mCountryList = refactorData(stationList);
            StationLab stationLab = StationLab.get(DrawerActivity.this);
            stationLab.setCountriesList(mCountryList);
            stationLab.setStationsList(stationList);

            //isDataReady = true;
            //setupData();

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            // display the first navigation drawer view on app launch
            displayView(0);
        }

    }

    private List<Country> refactorData(List<Station> stationList){
        List<Country> countryList = new ArrayList<>();

        for (Station station : stationList) {
            Country country = new Country(station.getCountryTitle());
            City city = new City(station.getCityTitle());

            int indexOfCountry  = indexOf(countryList,country);
            if (indexOfCountry != -1){

                int indexOfCity  = indexOf(countryList.get(indexOfCountry).getCitiesList(), city);
                //int indexOfCity  = country.getCitiesList().indexOf(city);
                if (indexOfCity != -1){
                    countryList.get(indexOfCountry).getCitiesList().get(indexOfCity).getStationList().add(station);
                } else {
                    countryList.get(indexOfCountry).getCitiesList().add(city);
                    indexOfCity  = indexOf(countryList.get(indexOfCountry).getCitiesList(), city);
                    countryList.get(indexOfCountry).getCitiesList().get(indexOfCity).getStationList().add(station);
                }
            } else {
                city.getStationList().add(station);
                country.getCitiesList().add(city);
                countryList.add(country);
            }
        }

        return countryList;
    }

    private int indexOf(List<Country> countryList, Country country){
        if (countryList.size() > 0){
            int i = 0;
            for (Country temp : countryList){
                if ( temp.getCountryTitle().equals( country.getCountryTitle() )) {
                    return i;
                } else {i++;}
            }
            return -1;
        } else {
            return -1;
        }
    }

    private int indexOf(List<City> cities, City city){
        if (cities.size() > 0){
            int i = 0;
            for (City temp : cities){
                if ( temp.getCityTitle().equals(city.getCityTitle()) ) {
                    return i;
                } else {i++;}
            }
            return -1;
        } else {
            return -1;
        }
    }

}
