package com.fgurbanov.skynet.hire_android_test.Fragment.Adapter.ThreeLevelExpListAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgurbanov.skynet.hire_android_test.Data.City;
import com.fgurbanov.skynet.hire_android_test.Data.Country;
import com.fgurbanov.skynet.hire_android_test.Fragment.View.SecondLevelExpandableListView;
import com.fgurbanov.skynet.hire_android_test.R;
import com.fgurbanov.skynet.hire_android_test.StationActivity;

import java.util.List;

/**
 * Created by SkyNet on 17.12.2016.
 * стандартный класс для ExpandableList
 */

public class ParentLevelAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final List<Country> mCountryList;

    public ParentLevelAdapter(Context mContext, List<Country> mCountryList) {
        this.mContext = mContext;
        this.mCountryList = mCountryList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCountryList.get(groupPosition).getCitiesList();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //Child  в данном случае другой вложенный лист, поэтому создаем для него адаптер
    // также вешаем onClick на финальный элемент списка
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelExpListView = new SecondLevelExpandableListView(mContext);
        final List<City> cityList = mCountryList.get(groupPosition).getCitiesList();

        secondLevelExpListView.setAdapter(new SecondLevelAdapter(mContext, cityList));
        secondLevelExpListView.setGroupIndicator(null);
        secondLevelExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                String stationTitle = cityList.get(groupPosition).getStationList().get(childPosition).getStationTitle();
                Intent intent = StationActivity
                        .newIntent(mContext, stationTitle);
                mContext.startActivity(intent);
                return false;
            }
        });


        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return mCountryList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_first, parent, false);
        TextView text = (TextView) convertView.findViewById(R.id.textViewName);
        text.setText(mCountryList.get(groupPosition).getCountryTitle());

        ImageView imageFirstArrow = (ImageView) convertView.findViewById(R.id.imageFirstArrow);
        if (isExpanded){
            imageFirstArrow.setBackgroundResource(R.drawable.arw_down);
        } else {
            imageFirstArrow.setBackgroundResource(R.drawable.arw_up);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}