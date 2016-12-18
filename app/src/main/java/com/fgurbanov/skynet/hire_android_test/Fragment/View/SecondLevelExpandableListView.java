package com.fgurbanov.skynet.hire_android_test.Fragment.View;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by SkyNet on 18.12.2016.
 * Вспогательный класс для отображения вложенного списка
 */

public class SecondLevelExpandableListView extends ExpandableListView {
    public SecondLevelExpandableListView(Context context)
    {
        super(context);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(20000, MeasureSpec.AT_MOST);
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
