package com.fgurbanov.skynet.hire_android_test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fgurbanov.skynet.hire_android_test.Fragment.AboutFragment;
import com.fgurbanov.skynet.hire_android_test.Fragment.StationFragment;

public class AboutActivity extends SingleFragmentActivity {

    private static final String EXTRA_STATION_ID = "com.fgurbanov.skynet.hire_android_test";

    public static Intent newIntent(Context packageContext, String stationTitle) {
        Intent intent = new Intent(packageContext, StationActivity.class);
        intent.putExtra(EXTRA_STATION_ID, stationTitle);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return AboutFragment.newInstance();
    }

    @Override
    public Intent getParentActivityIntent(){
        Intent upIntent = getIntent();
        return  upIntent;
    }
}
