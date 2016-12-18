package com.fgurbanov.skynet.hire_android_test.Data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by SkyNet on 12.12.2016.
 * Синглет для хранения данных.
 * Удобен для хранения информации, поскольку держит всю информацию
 * пока приложение остается в памяти.
 */

public class StationLab {
    private static StationLab sStationLab;

    private List<Station> mStations;
    private List<Country> mCountriesList;

    public static StationLab get(Context context) {
        if (sStationLab == null) {
            sStationLab = new StationLab(context);
        }
        return sStationLab;
    }

    private StationLab(Context Context) {
        mStations = new ArrayList<>();
    }

    public List<Station> getStations() {
        return mStations;
    }

    public Station getStation(String stationTitle) {
        for (Station station : mStations) {
            if (station.getStationTitle().equals(stationTitle)) {
                return station;
            }
        }
        return null;
    }

    public void setStationsList(List<Station> mStations) {
        this.mStations = mStations;
    }

    //Все данные о станциях в конечном итоге представляются как список стран.
    public void setCountriesList(List<Country> mCountriesList) {
        this.mCountriesList = mCountriesList;
    }

    public List<Country> getCountriesList() {
        return mCountriesList;
    }

    //Для заполнения списка нам нужны только названия станций
    //поэтому было решено вынести их в отдельный список для удобства
    public List<String> getStationTitleArrayList(){
        List<String> stationTitleArrayList = new ArrayList<>();
        for (Station temp : mStations){
            stationTitleArrayList.add(temp.getStationTitle());
        }
        return stationTitleArrayList;
    }

    //Т.к. в приложении есть разница между станциями отправления и прибытия,
    //то мной они были разбиты на два списка.
    //В данном виде они будут удобны для автозаполнения полей
    public List<String> getStationFromTitleArrayList(){
        List<String> stationFromTitleArrayList = new ArrayList<>();
        for (Station temp : mStations){
            if (temp.isCitiesFrom()) {
                stationFromTitleArrayList.add(temp.getStationTitle());
            }
        }
        return stationFromTitleArrayList;
    }
    public List<String> getStationToTitleArrayList(){
        List<String> stationToTitleArrayList = new ArrayList<>();
        for (Station temp : mStations){
            if (temp.isCitiesTo()) {
                stationToTitleArrayList.add(temp.getStationTitle());
            }
        }
        return stationToTitleArrayList;
    }
}
