package com.fgurbanov.skynet.hire_android_test.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
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

import com.fgurbanov.skynet.hire_android_test.Connection.ConnectionToDataServer;
import com.fgurbanov.skynet.hire_android_test.Data.City;
import com.fgurbanov.skynet.hire_android_test.Data.Country;
import com.fgurbanov.skynet.hire_android_test.Data.Station;
import com.fgurbanov.skynet.hire_android_test.Data.StationLab;
import com.fgurbanov.skynet.hire_android_test.DrawerActivity;
import com.fgurbanov.skynet.hire_android_test.Fragment.Adapter.ThreeLevelExpListAdapter.ParentLevelAdapter;
import com.fgurbanov.skynet.hire_android_test.Fragment.CustomDatePicker.SwitchDataFragment;
import com.fgurbanov.skynet.hire_android_test.R;
import com.fgurbanov.skynet.hire_android_test.StationActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SkyNet on 12.12.2016.
 * Целевой фрагмент.
 * Весь интерфейс представлен на нем.
 */

public class StationListFragment extends Fragment {

    //Tag
    private static final String TAG = "StationListFragment";
    private static final String SEARCH_STATUS_FAILED = "StationListFragment.string_was_not_found";

    //ActivityResultRequest
    public static final int REQUEST_FULL_DATE = 2;
    public static final String DIALOG_SWITCH = "DialogSwitch";

    //Widget
    private SearchView mSearchView;
    private AutoCompleteTextView mStationFromACTextView;
    private AutoCompleteTextView mStationToACTextView;
    private Button mSwitchDataButton;
    private Button mRefreshConnectionButton;
    private ExpandableListView mExpandableListView;
    private CardView mListCardView;

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

        //Определяем виджеты
        mStationFromACTextView = (AutoCompleteTextView) view.findViewById(R.id.station_from_textView);
        mStationToACTextView = (AutoCompleteTextView) view.findViewById(R.id.station_to_textView);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView_Parent);
        ImageButton swapImageButton = (ImageButton) view.findViewById(R.id.swap_image_button);
        mSwitchDataButton = (Button) view.findViewById(R.id.setData_button);
        mRefreshConnectionButton = (Button) view.findViewById(R.id.refresh_datalist_button);
        mListCardView = (CardView) view.findViewById(R.id.list_data_card_view);

        //Заполняем данные
        setupData();

        //Обработчики нажатий
        //Поменять местави станцию отправления и прибытия
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

        //Установить время отправления
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

        //Если не получилось скачать данные с сервера и заполнить список,
        // то требуется предоставить возможность установить повторно соединение
        mRefreshConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupData();
            }
        });

        return view;
    }

    //Т.к. списки станциий отправление и прибытия не идентичны
    //требуется проверить вхождение содержимых при их смене местами
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

    //Добавил возможность поиска станций по кусочку названия
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

    //Загрузка данных и заполнение списка
    private void setupData() {
        StationLab stationLab = StationLab.get(getActivity());
        List<Station> stationList = stationLab.getStations();
        if (stationList.size() == 0){
            new StationItemsTask().execute();
        } else {
            if (isAdded()) {
                mListCardView.setVisibility(View.VISIBLE);
                mRefreshConnectionButton.setVisibility(View.INVISIBLE);
                createCustomExpandableListView();
                setAdapterRouteElementView();
            }
        }

    }

    //непосредственное заполнение многоуровнего списка
    private void createCustomExpandableListView() {
        StationLab stationLab = StationLab.get(getActivity());
        List<Country> countryList = stationLab.getCountriesList();
        if (mExpandableListView != null) {
            ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(getContext(), countryList);
            mExpandableListView.setAdapter(parentLevelAdapter);
            mExpandableListView.setGroupIndicator(null);
        }
    }

    //установка подсказок для полей "станция отправления" и "станция прибытия"
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

    //необходимо для получения времени отправления при закрытии фрагмента с Data и TimePicker
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

    //AsyncTask для скачивания и JSON со списком
    public class StationItemsTask extends AsyncTask<Void,Void,List<Station>> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Doing something, please wait.");
            mDialog.show();
        }

        @Override
        protected List<Station> doInBackground(Void... params) {
            return new ConnectionToDataServer().getStationItems();
        }

        @Override
        protected void onPostExecute(List<Station> stationList) {
            boolean isGetInformation = true;
            //если не удалось получить данные сервера, то
            //требуется уведомить пользователя и предоставить возможность повторить соединение
            //для этого отображаем кнопку повторного соединения
            if (stationList.size() == 0){
                Toast.makeText(getContext(), "No internet connection, try again later", Toast.LENGTH_SHORT).show();
                mRefreshConnectionButton.setVisibility(View.VISIBLE);
                mListCardView.setVisibility(View.INVISIBLE);
                isGetInformation  = false;
            }
            List<Country> mCountryList = refactorData(stationList);
            StationLab stationLab = StationLab.get(getActivity());
            stationLab.setCountriesList(mCountryList);
            stationLab.setStationsList(stationList);

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (isGetInformation) {
                setupData();
            }
        }

    }

    //вспомогательные методы для сортировки данных для синглета
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
