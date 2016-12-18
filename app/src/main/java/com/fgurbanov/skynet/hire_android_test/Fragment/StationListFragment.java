package com.fgurbanov.skynet.hire_android_test.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fgurbanov.skynet.hire_android_test.Data.Country;
import com.fgurbanov.skynet.hire_android_test.Data.StationLab;
import com.fgurbanov.skynet.hire_android_test.Fragment.Adapter.ThreeLevelExpListAdapter.ParentLevelAdapter;
import com.fgurbanov.skynet.hire_android_test.Fragment.CustomDatePicker.SwitchDataFragment;
import com.fgurbanov.skynet.hire_android_test.R;
import com.fgurbanov.skynet.hire_android_test.StationActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by SkyNet on 12.12.2016.
 */

public class StationListFragment extends Fragment {

    //Tag
    private static final String TAG = "StationListFragment";
    private static final String SEARCH_STATUS_FAILED = "StationListFragment.string_was_not_found";

    //ActivityResultRequest
    public static final int REQUEST_FULL_DATE = 2;
    public static final String DIALOG_SWITCH = "DialogSwitch";

    //Widget
    private LinearLayout mLinearListView;
    private SearchView mSearchView;
    private AutoCompleteTextView mStationFromACTextView;
    private AutoCompleteTextView mStationToACTextView;
    private Button mSwitchDataButton;
    private ExpandableListView mExpandableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_stations, container, false);

        mLinearListView = (LinearLayout) view.findViewById(R.id.linear_ListView);
        mStationFromACTextView = (AutoCompleteTextView) view.findViewById(R.id.station_from_textView);
        mStationToACTextView = (AutoCompleteTextView) view.findViewById(R.id.station_to_textView);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView_Parent);

        setupData();

        ImageButton swapImageButton = (ImageButton) view.findViewById(R.id.swap_image_button);
        swapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stationFrom = mStationFromACTextView.getText().toString();
                String stationTo = mStationToACTextView.getText().toString();
                if (checkStationEntry(stationFrom, stationTo)) {
                    mStationFromACTextView.setText(stationTo);
                    mStationToACTextView.setText(stationFrom);

                    mStationFromACTextView.clearFocus();
                    mStationToACTextView.clearFocus();
                    mStationFromACTextView.dismissDropDown();
                    mStationToACTextView.dismissDropDown();

                    // Check if no view has focus:
                    View v = getActivity().getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }

            }
        });

        mSwitchDataButton = (Button) view.findViewById(R.id.setData_button);
        mSwitchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                //Date date = new Date();
                SwitchDataFragment dialog = new SwitchDataFragment().newInstance(new Date());
                dialog.setTargetFragment(StationListFragment.this, REQUEST_FULL_DATE);
                dialog.show(manager, DIALOG_SWITCH);
            }
        });


        return view;
    }

    private boolean checkStationEntry(String stationFrom, String stationTo) {
        StationLab stationLab = StationLab.get(getActivity());
        List<String> mStationFromTitleList = stationLab.getStationFromTitleArrayList();
        List<String> mStationToTitleList = stationLab.getStationToTitleArrayList();
        boolean inFromArray = false;
        boolean inToArray = false;

        stationFrom = stationFrom.toLowerCase();
        stationTo = stationTo.toLowerCase();

        int i = 0;
        while ( (!inFromArray) && (i < mStationFromTitleList.size())) {
            if (mStationFromTitleList.get(i).toLowerCase().contains(stationTo)) {
                inFromArray = true;
            }
            i++;
        }
        i = 0;
        while ( (!inToArray) && (i < mStationToTitleList.size())){
            if (mStationToTitleList.get(i).toLowerCase().contains(stationFrom)) {
                inToArray = true;
            }
            i++;
        }

        if (inFromArray && inToArray){
            return true;
        } else {
            if (inFromArray){
                Toast.makeText(getContext(), "This station(" + stationFrom + ") is not contained in the arrival station list", Toast.LENGTH_SHORT).show();
            } else if (inToArray){
                Toast.makeText(getContext(), "This station(" + stationTo +") is not contained in the departure station list", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No information about this stations", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_station_list, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView =
                (SearchView) searchItem.getActionView();

        mSearchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        String result = isStationFind(s);
                        mSearchView.clearFocus();

                        if (!result.contains(SEARCH_STATUS_FAILED)){
                            searchItem.collapseActionView();
                            Intent intent = StationActivity
                                    .newIntent(getActivity(), result);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), s + " was not found", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "QueryTextChange: " + s);
                        return false;
                    }
                });
    }

    private String isStationFind(String title){
        StationLab stationLab = StationLab.get(getActivity());
        List<String> mStationTitleList = stationLab.getStationTitleArrayList();

        for (String temp : mStationTitleList){
            if (temp.toLowerCase().contains(title.toLowerCase())) {
                return temp;
            }
        }
        return SEARCH_STATUS_FAILED;
    }

    private void setupData() {
        if (isAdded()) {

            createCustomExpandableListView();

            //createMultiLevelListView();
            setAdapterRouteElementView();
        }

    }

    private void createCustomExpandableListView() {
        StationLab stationLab = StationLab.get(getActivity());
        List<Country> countryList = stationLab.getCountriesList();
        if (mExpandableListView != null) {
            ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(getContext(), countryList);
            mExpandableListView.setAdapter(parentLevelAdapter);
            mExpandableListView.setGroupIndicator(null);
        }
    }


    private void setAdapterRouteElementView() {
        StationLab stationLab = StationLab.get(getActivity());
        List<String> mStationFromTitleList = stationLab.getStationFromTitleArrayList();
        List<String> mStationToTitleList = stationLab.getStationToTitleArrayList();

        mStationFromACTextView.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                mStationFromTitleList
                ));
        mStationToACTextView.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                mStationToTitleList
        ));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_FULL_DATE) {
            Date date = (Date) data.getSerializableExtra(SwitchDataFragment.EXTRA_FULL_DATE);
            mSwitchDataButton.setText(DateFormat.format(
                    "EEEE, MMM dd, yyyy kk:mm", date).toString());
        }
    }

}
