package com.fgurbanov.skynet.hire_android_test.Connection;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.fgurbanov.skynet.hire_android_test.Data.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SkyNet on 12.12.2016.
 */

public class ConnectionToDataServer {

    private static final String TAG = "ConnectionToDataServer";

    private static final String STATIONS_URL = "https://raw.githubusercontent.com/tutu-ru/hire_android_test/master/allStations.json";


    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Station> getStationItems() {
        List<Station> stationList = new ArrayList<>();
        try {
            String url = Uri.parse(STATIONS_URL)
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();
            String jsonString = getUrlString(url);
            //Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(stationList, jsonBody);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return stationList;
    }

    private void parseItems(List<Station> stationList, JSONObject jsonBody)
            throws IOException, JSONException{

        //Log.e(TAG, jsonBody.toString());
        //JSONObject JsonInitialObject = jsonBody.getJSONObject("");
        JSONArray citiesFromJsonArray = jsonBody.getJSONArray("citiesFrom");

        for (int i = 0; i < citiesFromJsonArray.length(); i++){
            JSONObject cityJsonObject = citiesFromJsonArray.getJSONObject(i);
            JSONArray stationInCityJsonArray = cityJsonObject.getJSONArray("stations");

            for (int j = 0; j < stationInCityJsonArray.length(); j++) {
                JSONObject StationJsonObject = stationInCityJsonArray.getJSONObject(j);

                Station station = getStationFromJson(StationJsonObject);
                station.setCitiesFrom(true);
                stationList.add(station);
            }
        }

        JSONArray citiesToJsonArray = jsonBody.getJSONArray("citiesTo");

        for (int i = 0; i < citiesToJsonArray.length(); i++){
            JSONObject cityJsonObject = citiesToJsonArray.getJSONObject(i);
            JSONArray stationInCityJsonArray = cityJsonObject.getJSONArray("stations");

            for (int j = 0; j < stationInCityJsonArray.length(); j++) {
                JSONObject StationJsonObject = stationInCityJsonArray.getJSONObject(j);

                Station station = getStationFromJson(StationJsonObject);
                //for (int k = 0; k < stationList.size()-1; k++){
                int k = indexOf(stationList, station);
                if (k != -1) {
                    stationList.get(k).setCitiesTo(true);
                } else {
                    station.setCitiesTo(true);
                    stationList.add(station);
                }

            }
        }
    }

    private int indexOf(List<Station> stationList, Station station){
        if (stationList.size() > 0){
            int i = 0;
            for (Station temp : stationList){
                if ( temp.getStationId() == station.getStationId() ) {
                    return i;
                } else {i++;}
            }
            return -1;
        } else {
            return -1;
        }
    }

    private Station getStationFromJson(JSONObject StationJsonObject)
            throws IOException, JSONException{
        Station station = new Station();

        station.setCountryTitle(StationJsonObject.getString("countryTitle"));

        JSONObject StationPointJsonObject = StationJsonObject.getJSONObject("point");

        station.setLongitude(StationPointJsonObject.getInt("longitude"));
        station.setLatitude(StationPointJsonObject.getInt("latitude"));

        station.setCityId(StationJsonObject.getInt("cityId"));
        station.setCityTitle(StationJsonObject.getString("cityTitle"));
        station.setStationId(StationJsonObject.getInt("stationId"));
        station.setStationTitle(StationJsonObject.getString("stationTitle"));

        return station;
    }
}
