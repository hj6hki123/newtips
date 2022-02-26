package com.example.newtips;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, TimePickerDialog.OnClickListener {

    private static final String HOURS_ARG_KEY = "hours";
    private static final String MINUTES_ARG_KEY = "minutes";
    private static final String DEVICE_ARG_KEY = "deviceID";
    private static final String TARGET_ARG_KEY = "target";

    public static TimePickerFragment newInstance(int hours, int minutes,int deviceID, int targetResId) {
        TimePickerFragment tpf = new TimePickerFragment();
        Bundle args = new Bundle();

        //Setup the TimePickerFragment with some args if required.

        args.putInt(HOURS_ARG_KEY, hours);
        args.putInt(MINUTES_ARG_KEY, minutes);
        args.putInt(DEVICE_ARG_KEY,deviceID);
        args.putInt(TARGET_ARG_KEY, targetResId);
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
        tpd.setButton(DialogInterface.BUTTON_POSITIVE,"啟用",this);
        tpd.setButton(DialogInterface.BUTTON_NEUTRAL,"不啟用", this);

        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user


        int target = getArguments().getInt(TARGET_ARG_KEY,0);
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


    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_NEUTRAL) {
            int target = getArguments().getInt(TARGET_ARG_KEY,0);
            int deviceID=getArguments().getInt(DEVICE_ARG_KEY,0);
            if(deviceID==1)
            {
                Log.e("dialog","BUTTON_NEUTRAL1");
            }
            else if(deviceID==2)
            {
                Log.e("dialog","BUTTON_NEUTRAL2");
            }

        }
    }
}