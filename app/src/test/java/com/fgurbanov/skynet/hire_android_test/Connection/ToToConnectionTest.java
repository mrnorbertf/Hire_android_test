package com.fgurbanov.skynet.hire_android_test.Connection;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by SkyNet on 18.12.2016.
 */
public class ToToConnectionTest {

    private static final String STATIONS_URL = "https://yadi.sk/i/vdGcOvnX33wDuu";

    @Test
    public void getUrlBytes() throws Exception {
        ToToConnection toToConnection = new ToToConnection();

        toToConnection.getUrlBytes(STATIONS_URL);

    }

    @Test
    public void getUrlString() throws Exception {

    }

    @Test
    public void getStationItems() throws Exception {

    }

    @Test
    public void parseItems() throws Exception {

    }

}