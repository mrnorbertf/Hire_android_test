package com.fgurbanov.skynet.hire_android_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fgurbanov.skynet.hire_android_test.Fragment.FragmentDrawer;
import com.fgurbanov.skynet.hire_android_test.Fragment.StationListFragment;


/**
 * Created by SkyNet on 12.12.2016.
 */

public class StationListActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {
        //return new StationListFragment();
        return new StationListFragment().newInstance();
    }

}
