package com.example.eunice.criminalintent;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment{
    public static final String EXTRA_TIME = "com.example.eunice.criminalintent.time";
    private Date mTime;

    public static TimePickerFragment newInstance(Date time){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void sendResult(int resultCode){
        if(getTargetFragment() == null) return;

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, mTime);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mTime = (Date) getArguments().getSerializable(EXTRA_TIME);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setHour(hour);
        timePicker.setMinute(minutes);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mTime = calendar.getTime();
                getArguments().putSerializable(EXTRA_TIME, mTime);
            }
        });
        return new AlertDialog.Builder(getActivity()).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
            }).create();

    }


    public TimePickerFragment() {
        // Required empty public constructor
    }




}
