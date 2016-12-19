package com.fgurbanov.skynet.hire_android_test.Fragment.Adapter.ThreeLevelExpListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgurbanov.skynet.hire_android_test.Data.City;
import com.fgurbanov.skynet.hire_android_test.R;

import java.util.List;
/**
 * Created by SkyNet on 18.12.2016.
 */

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<City> mCityList;

    public SecondLevelAdapter(Context context, List<City> cityList) {
        this.mContext = context;
        this.mCityList = cityList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCityList.get(groupPosition).getStationList().get(childPosition).getStationTitle();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_third, null);
        TextView text = (TextView) convertView.findViewById(R.id.textViewItemName);
        text.setText((String) getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCityList.get(groupPosition).getStationList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return mCityList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_second, parent, false);
        TextView text = (TextView) convertView.findViewById(R.id.textViewTitle);
        text.setText(mCityList.get(groupPosition).getCityTitle());

        ImageView imageSecondArrow = (ImageView) convertView.findViewById(R.id.imageSecondArrow);
        //imageSecondArrow.setImageResource(R.drawable.arw_down);


        if (isExpanded){
            imageSecondArrow.setBackgroundResource(R.drawable.arw_down);
        } else {
            imageSecondArrow.setBackgroundResource(R.drawable.arw_up);
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
