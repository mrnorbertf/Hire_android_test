package com.fgurbanov.skynet.hire_android_test.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SkyNet on 13.12.2016.
 * Для expandbleListView потребовалось разделить данные на 3 класса
 */

public class Country {
    private List<City> mCitiesList;

    private String mCountryTitle;

    public Country(String countryTitle) {
        mCountryTitle = countryTitle;
        mCitiesList = new ArrayList<>();
    }

    public List<City> getCitiesList() {
        return mCitiesList;
    }

    public String getCountryTitle() {
        return mCountryTitle;
    }

}
