package com.example.newtips;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, TimePickerDialog.OnClickListener {

    private static final String HOURS_ARG_KEY = "hours";
    private static final String MINUTES_ARG_KEY = "minutes";
    private static final String DEVICE_ARG_KEY = "deviceID";
    private static final String TARGET_ARG_KEY1 = "target1";
    private static final String TARGET_ARG_KEY2 = "target2";
    public static TimePickerFragment newInstance(int hours, int minutes,int deviceID, int targetResId1, int targetResId2) {
        TimePickerFragment tpf = new TimePickerFragment();
        Bundle args = new Bundle();

        //Setup the TimePickerFragment with some args if required.

        args.putInt(HOURS_ARG_KEY, hours);
        args.putInt(MINUTES_ARG_KEY, minutes);
        args.putInt(DEVICE_ARG_KEY,deviceID);
        args.putInt(TARGET_ARG_KEY1, targetResId1);
        args.putInt(TARGET_ARG_KEY2, targetResId2);
        tpf.setArguments(args);

        return tpf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker if we get nothing from
        // from the arguments.
        final Calendar c = Calendar.getInstance();

        int hour = getArguments().getInt(HOURS_ARG_KEY,c.get(Calendar.HOUR_OF_DAY));
        int minute = getArguments().getInt(MINUTES_ARG_KEY, c.get(Calendar.MINUTE));

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute, false);

        // And add the third button to clear the target field.
        tpd.setButton(DialogInterface.BUTTON_POSITIVE,"確定",this);


        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user


        int target1 = getArguments().getInt(TARGET_ARG_KEY1,0);
        int target2 = getArguments().getInt(TARGET_ARG_KEY2,0);
        int deviceID=getArguments().getInt(DEVICE_ARG_KEY,0);
        /*
        //Get reference of host activity (XML Layout File) TextView widget
        TextView txt = (TextView) getActivity().findViewById(target);
        //Format the hourOfDay and minute values, then display the user changed time on the TextView

         */
        if(deviceID==1)
        {
            Log.e("dialog","ok1");

        }
        else if(deviceID==2)
        {
            Log.e("dialog","ok2");
        }

        HashMap timedate=_24Hto12H(hourOfDay,minute);

        TextView txt1 = (TextView) getActivity().findViewById(target1);
        txt1.setText(timedate.get("_12HourTime").toString());
        TextView txt2 = (TextView) getActivity().findViewById(target2);
        txt2.setText(timedate.get("_am_pm").toString());

    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_NEUTRAL) {

        }
    }

    private HashMap<String,String> _24Hto12H(int hour, int min)
    {
        int currenthour = (hour>12) ? hour-12 : ((hour==0) ? 12 : hour);
        String _am_pm=(hour>=12) ? "下午" : "上午";
        String _12Hour=(String.valueOf(currenthour).length()<2)? "0"+String.valueOf(currenthour):String.valueOf(currenthour);
        String _12HourTime=_12Hour+":"+String.valueOf(min);
        HashMap<String,String> timedate= new HashMap()
        {
            {
                put("_am_pm",_am_pm);
                put("_12HourTime",_12HourTime);
            }
        };

        return timedate;
    }



}