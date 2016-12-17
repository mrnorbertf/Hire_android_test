package com.fgurbanov.skynet.hire_android_test.Data;

import java.util.UUID;

/**
 * Created by SkyNet on 12.12.2016.
 */

public class Station {

    private int mStationId;
    private int mCityId;

    private String mStationTitle;
    private String mRegionTitle;
    private String mCityTitle;
    private String mDistrictTitle;
    private String mCountryTitle;

    private float mLongitude;
    private float mLatitude;

    public boolean isCitiesFrom() {
        return isCitiesFrom;
    }

    public void setCitiesFrom(boolean citiesFrom) {
        isCitiesFrom = citiesFrom;
    }

    public boolean isCitiesTo() {
        return isCitiesTo;
    }

    public void setCitiesTo(boolean citiesTo) {
        isCitiesTo = citiesTo;
    }

    private boolean isCitiesFrom;
    private boolean isCitiesTo;

    public int getStationId() {
        return mStationId;
    }

    public void setStationId(int stationId) {
        mStationId = stationId;
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        mCityId = cityId;
    }

    public String getStationTitle() {
        return mStationTitle;
    }

    public void setStationTitle(String stationTitle) {
        mStationTitle = stationTitle;
    }

    public String getRegionTitle() {
        return mRegionTitle;
    }

    public void setRegionTitle(String regionTitle) {
        mRegionTitle = regionTitle;
    }

    public String getCityTitle() {
        return mCityTitle;
    }

    public void setCityTitle(String cityTitle) {
        mCityTitle = cityTitle;
    }

    public String getDistrictTitle() {
        return mDistrictTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        mDistrictTitle = districtTitle;
    }

    public String getCountryTitle() {
        return mCountryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        mCountryTitle = countryTitle;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }
}
