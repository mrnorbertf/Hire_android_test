package com.fgurbanov.skynet.hire_android_test;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.fgurbanov.skynet.hire_android_test.Fragment.StationFragment;

/**
 * Не является фрагментом, чтобы добавить возможность backbutton в toolbar
 * для данной activity указан parentActivity.
 * данный элемент не имеет возможности сразу выйти в раздел about.
 * Также не предусматривается возможность вызова DrawerMenu
 */
public class StationActivity extends AppCompatActivity {

    private static final String EXTRA_STATION_ID = "com.fgurbanov.skynet.hire_android_test";

    public static Intent newIntent(Context packageContext, String stationTitle) {
        Intent intent = new Intent(packageContext, StationActivity.class);
        intent.putExtra(EXTRA_STATION_ID, stationTitle);
        return intent;
    }


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
