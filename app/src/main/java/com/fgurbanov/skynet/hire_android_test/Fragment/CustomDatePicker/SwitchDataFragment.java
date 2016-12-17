package com.fgurbanov.skynet.hire_android_test.Fragment.CustomDatePicker;

import java.util.Calendar;
import java.util.Date;

import com.fgurbanov.skynet.hire_android_test.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SkyNet on 24.11.2016.
 */

public class SwitchDataFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    public static final String DIALOG_DATE = "DialogDate";
    public static final String DIALOG_TIME = "DialogTime";

    public static final String EXTRA_FULL_DATE =
            "com.fgurbanov.skynet.hire_android_test.Fragment.CustomDatePicker.switchdate";

    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;

    private Button mSetTime;
    private Button mSetData;
    private TextView mTempText;

    private Date mDate;

    public static SwitchDataFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        SwitchDataFragment fragment = new SwitchDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(ARG_DATE);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_custom_data, null);

        mSetTime = (Button) view.findViewById(R.id.setTime);
        mSetTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();

                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mDate);
                dialog.setTargetFragment(SwitchDataFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        mSetData = (Button) view.findViewById(R.id.setData);
        mSetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mDate);
                dialog.setTargetFragment(SwitchDataFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });


        mTempText = (TextView) view.findViewById(R.id.temp_text_view);

        updateDate();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_piker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, mDate);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();

    }



    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FULL_DATE, date);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            //TODO: Change all Util.Date to Calendar

            Date tempDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(mDate);
            int hour = calendar2.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar2.get(Calendar.MINUTE);

            calendar.set(year, month, day, hour, minutes);
            mDate = calendar.getTime();

            updateDate();
        }
        if (requestCode == REQUEST_TIME) {
            int[] time = (int[]) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            mDate.setHours(time[0]);
            mDate.setMinutes(time[1]);

        }
        updateDate();
    }

    private void updateDate() {

        mTempText.setText(DateFormat.format(
                //        "EEEE, MMM dd, yyyy", mDate).toString();
                "EEEE, MMM dd, yyyy kk:mm", mDate).toString());

    }

}