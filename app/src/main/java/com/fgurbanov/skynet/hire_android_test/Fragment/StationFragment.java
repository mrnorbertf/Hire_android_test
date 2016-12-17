package com.fgurbanov.skynet.hire_android_test.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fgurbanov.skynet.hire_android_test.Data.Station;
import com.fgurbanov.skynet.hire_android_test.Data.StationLab;
import com.fgurbanov.skynet.hire_android_test.R;

/**
 * Created by SkyNet on 12.12.2016.
 */

public class StationFragment extends Fragment {

    private static final String ARG_STATION_TITLE = "station_title";

    private Station mStation;

    private TextView mStationTextView;
    private TextView mRegionTextView;
    private TextView mCityTextView;
    private TextView mDistrictTextView;
    private TextView mCountryTextView;
    private TextView mCoordinateTextView;

    public static StationFragment newInstance(String stationTitle) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATION_TITLE, stationTitle);

        StationFragment fragment = new StationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String stationTitle = (String) getArguments().getSerializable(ARG_STATION_TITLE);
        mStation = StationLab.get(getActivity()).getStation(stationTitle);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_station_detail, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station, container, false);

        Toolbar myToolbar = (Toolbar) v.findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        }

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mStationTextView = (TextView) v.findViewById(R.id.station_title_textView);
        mStationTextView.setText(mStation.getStationTitle());

        mCountryTextView = (TextView) v.findViewById(R.id.country_title_textView);
        mCountryTextView.setText(mStation.getCountryTitle());

        mRegionTextView = (TextView) v.findViewById(R.id.region_textView);
        mRegionTextView.setText(mStation.getRegionTitle());

        mCityTextView = (TextView) v.findViewById(R.id.city_title_textView);
        mCityTextView.setText(mStation.getCityTitle());

        mDistrictTextView = (TextView) v.findViewById(R.id.description_textView);
        mDistrictTextView.setText(mStation.getDistrictTitle());

        mCoordinateTextView = (TextView) v.findViewById(R.id.coordinate_textView);
        mCoordinateTextView.setText("Coordinates: lat: " + mStation.getLatitude() +
                " long: " + mStation.getLongitude());

        return v;
    }
}
