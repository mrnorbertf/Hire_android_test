package com.fgurbanov.skynet.hire_android_test.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SkyNet on 13.12.2016.
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

    public void setCitiesList(List<City> citiesList) {
        mCitiesList = citiesList;
    }

    public String getCountryTitle() {
        return mCountryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        mCountryTitle = countryTitle;
    }
}
