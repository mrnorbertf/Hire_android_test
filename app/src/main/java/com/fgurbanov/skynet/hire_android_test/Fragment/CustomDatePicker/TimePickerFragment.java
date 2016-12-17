package com.fgurbanov.skynet.hire_android_test.Fragment.CustomDatePicker;

import java.util.Calendar;
import java.util.Date;

import com.fgurbanov.skynet.hire_android_test.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by SkyNet on 22.11.2016.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "time";

    public static final String EXTRA_TIME = "com.fgurbanov.skynet.hire_android_test.Fragment.CustomDatePicker.time";

    private TimePicker mTimePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setIs24HourView(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(min);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(min);
        }


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_piker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int hour;
                                int min;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hour = mTimePicker.getHour();
                                    min = mTimePicker.getMinute();
                                } else {

                                    hour = mTimePicker.getCurrentHour();
                                    min = mTimePicker.getCurrentMinute();
                                }
                                int[] time = {hour, min};
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }


    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode, int[] time) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
