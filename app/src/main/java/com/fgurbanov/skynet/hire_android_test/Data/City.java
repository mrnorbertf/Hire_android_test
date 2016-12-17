package com.fgurbanov.skynet.hire_android_test.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SkyNet on 13.12.2016.
 */

public class City {
    private List<Station> mStationList;

    private String mCityTitle;
    private int mCityId;

    public City(String cityTitle) {
        mCityTitle = cityTitle;
        mStationList = new ArrayList<>();
    }

    public List<Station> getStationList() {
        return mStationList;
    }

    public void setStationList(List<Station> stationList) {
        mStationList = stationList;
    }

    public String getCityTitle() {
        return mCityTitle;
    }

    public void setCityTitle(String cityTitle) {
        mCityTitle = cityTitle;
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        mCityId = cityId;
    }

}
