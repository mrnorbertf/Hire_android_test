package com.fgurbanov.skynet.hire_android_test;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.fgurbanov.skynet.hire_android_test.Fragment.StationFragment;

public class StationActivity extends SingleFragmentActivity {

    private static final String EXTRA_STATION_ID = "com.fgurbanov.skynet.hire_android_test";

    public static Intent newIntent(Context packageContext, String stationTitle) {
        Intent intent = new Intent(packageContext, StationActivity.class);
        intent.putExtra(EXTRA_STATION_ID, stationTitle);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String stationTitle = (String) getIntent()
                .getSerializableExtra(EXTRA_STATION_ID);
        return StationFragment.newInstance(stationTitle);
    }

    @Override
    public Intent getParentActivityIntent(){
        Intent upIntent = getIntent();
        return  upIntent;
    }
}
