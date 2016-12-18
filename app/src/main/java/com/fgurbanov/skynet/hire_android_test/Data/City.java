package com.fgurbanov.skynet.hire_android_test.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SkyNet on 13.12.2016.
 * Для expandbleListView потребовалось разделить данные на 3 класса
 */

public class City {
    private List<Station> mStationList;

    private String mCityTitle;

    public City(String cityTitle) {
        mCityTitle = cityTitle;
        mStationList = new ArrayList<>();
    }

    public List<Station> getStationList() {
        return mStationList;
    }

    public String getCityTitle() {
        return mCityTitle;
    }

}
