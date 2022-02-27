package com.example.newtips;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.newtips.R;
import com.example.newtips.common.Constants;
import com.ntt.customgaugeview.library.GaugeView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Color;

//demo版

public class page1 extends Fragment {
    SharedPreferences pref ;

    private ServiceConnection sc;
    public SocketService socketService;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer = new Timer();
    private TimerTask task;
    ExecutorService exec = Executors.newCachedThreadPool();
    UDP udpServer;
    StringBuffer stringBuffer = new StringBuffer();
    ArrayAdapter<String> spinnerAdapter;



    Spinner spinner_guage1,spinner_guage2;
    GaugeView gaugeView_Vupp,gaugeView_Vdown,gaugeView_Iupp,gaugeView_Idown;
    TextView textView_VOLT1,textView_CURRENT1,textView_WATT1,textView_FREQ1,textView_KHW1,textView_PF1;
    TextView textView_VOLT2,textView_CURRENT2,textView_WATT2,textView_FREQ2,textView_KHW2,textView_PF2;
    Button switch1,switch2;
    TextView devicename1,devicename2;
    ImageView editdevucename1,editdevucename2;

    public page1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page1, container, false);
    }

    protected void initUI(View root){
        gaugeView_Vupp = (GaugeView) root.findViewById(R.id.gauge_view_Vupp);
        gaugeView_Vupp.setShowRangeValues(true);
        gaugeView_Vupp.setTargetValue(0);
        gaugeView_Vupp.setAlpha((float)1);


        gaugeView_Iupp = (GaugeView) root.findViewById(R.id.gauge_view_Iupp);
        gaugeView_Iupp.setShowRangeValues(true);
        gaugeView_Iupp.setTargetValue(0);
        gaugeView_Iupp.setAlpha((float)0);

        gaugeView_Vdown = (GaugeView) root.findViewById(R.id.gauge_view_Vdown);
        gaugeView_Vdown.setShowRangeValues(true);
        gaugeView_Vdown.setTargetValue(0);
        gaugeView_Vdown.setAlpha((float)1);

        gaugeView_Idown = (GaugeView) root.findViewById(R.id.gauge_view_Idown);
        gaugeView_Idown.setShowRangeValues(true);
        gaugeView_Idown.setTargetValue(0);
        gaugeView_Idown.setAlpha((float)0);

        textView_VOLT1 = (TextView) root.findViewById(R.id.textViewVOLT1);
        textView_CURRENT1 = (TextView)root.findViewById(R.id.textViewCURRENT1);
        textView_WATT1 = (TextView)root.findViewById(R.id.textViewWATT1);
        textView_FREQ1 = (TextView)root.findViewById(R.id.textViewFREQ1);
        textView_KHW1 = (TextView)root.findViewById(R.id.textViewKWH1);
        textView_PF1 = (TextView)root.findViewById(R.id.textViewPF1);

        textView_VOLT2 = (TextView)root.findViewById(R.id.textViewVOLT2);
        textView_CURRENT2 = (TextView)root.findViewById(R.id.textViewCURRENT2);
        textView_WATT2 = (TextView)root.findViewById(R.id.textViewWATT2);
        textView_FREQ2 = (TextView)root.findViewById(R.id.textViewFREQ2);
        textView_KHW2 = (TextView)root.findViewById(R.id.textViewKWH2);
        textView_PF2 = (TextView)root.findViewById(R.id.textViewPF2);


        spinner_guage1=(Spinner) root.findViewById(R.id.spinner_gauge1);
        spinner_guage2=(Spinner) root.findViewById(R.id.spinner_gauge2);

        devicename1=(TextView) root.findViewById(R.id.devicename1);
        devicename2=(TextView) root.findViewById(R.id.devicename2);

        editdevucename1=(ImageView) root.findViewById(R.id.editdevucename1);
        editdevucename2=(ImageView) root.findViewById(R.id.editdevucename2);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref =getActivity().getPreferences(Context.MODE_PRIVATE);
        initUI(view);//UI初始化

        //guage_spinner初始化與事件
        ArrayAdapter<CharSequence> adapter_guage =
                ArrayAdapter.createFromResource(getActivity(),    //對應的Context
                        R.array.gauge_array,                             //資料選項內容
                        android.R.layout.simple_spinner_item);  //預設Spinner未展開時的View(預設及選取後樣式)
        adapter_guage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_guage1.setAdapter(adapter_guage);
        spinner_guage2.setAdapter(adapter_guage);
        spinner_guage1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String sPos=String.valueOf(position);
                String sInfo=parent.getItemAtPosition(position).toString();
                switch (sInfo)
                {
                    case "電壓表":
                        gaugeView_Vupp.setAlpha((float)1);
                        gaugeView_Iupp.setAlpha((float)0);
                        break;
                    case"電流表":
                        gaugeView_Vupp.setAlpha((float)0);
                        gaugeView_Iupp.setAlpha((float)1);
                        break;
                    default:
                        break;
                }
                //Toast.makeText(getActivity(),sInfo,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_guage2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sPos=String.valueOf(position);
                String sInfo=parent.getItemAtPosition(position).toString();
                switch (sInfo)
                {
                    case "電壓表":
                        gaugeView_Vdown.setAlpha((float)1);
                        gaugeView_Idown.setAlpha((float)0);
                        break;
                    case"電流表":
                        gaugeView_Vdown.setAlpha((float)0);
                        gaugeView_Idown.setAlpha((float)1);
                        break;
                    default:
                        break;
                }
                //Toast.makeText(getActivity(),sInfo,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*devicename1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalData.FSM.equals("Datatransport") && !GlobalData.macaddress_select.equals("none"))
                    setEdittextCustomDialog(0);
                else
                    Toast.makeText(getActivity(),"需有設備連線",Toast.LENGTH_SHORT).show();
            }
        });
        devicename2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalData.FSM.equals("Datatransport") && !GlobalData.macaddress_select.equals("none"))
                    setEdittextCustomDialog(1);
                else
                    Toast.makeText(getActivity(),"需有device連線",Toast.LENGTH_SHORT).show();
            }
        });*/
        editdevucename1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalData.FSM.equals("Datatransport") && !GlobalData.macaddress_select.equals("none"))
                    setEdittextCustomDialog(0);
                else
                    Toast.makeText(getActivity(),"需有設備連線",Toast.LENGTH_SHORT).show();
            }
        });
        editdevucename2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalData.FSM.equals("Datatransport") && !GlobalData.macaddress_select.equals("none"))
                    setEdittextCustomDialog(0);
                else
                    Toast.makeText(getActivity(),"需有設備連線",Toast.LENGTH_SHORT).show();
            }
        });


        switch1=view.findViewById(R.id.switch1);
        switch2=view.findViewById(R.id.switch2);

        switch1.setOnClickListener((V)->GlobalData.Deviceswitch1="1");
        switch2.setOnClickListener((V)->GlobalData.Deviceswitch2="1");



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bindSocketService();
        timerUI();



    }

    private void start_UDP()
    {
        //UDP_setting
        udpServer = new UDP(CommendFun.getLocalIP(getActivity()),getActivity());
        udpServer.setPort(31999);
        udpServer.changeServerStatus(true);
        exec.execute(udpServer);


    }


    private void timerUI()//每過0.5秒更新一次UI
    {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //TODO:updata UI
                                if(Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Volt1")))<=230 && Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Volt2")))<=230 && Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Current1")))<=16&& Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Current2")))<=16)
                                {
                                    gaugeView_Vupp.setTargetValue(Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Volt1"))));
                                    gaugeView_Iupp.setTargetValue(Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Current1"))));
                                    gaugeView_Vdown.setTargetValue(Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Volt2"))));
                                    gaugeView_Idown.setTargetValue(Float.parseFloat(Objects.requireNonNull(GlobalData.datamap_getserver.get("Current2"))));
                                }
                                textView_VOLT1.setText(GlobalData.datamap_getserver.get("Volt1"));
                                textView_CURRENT1.setText(GlobalData.datamap_getserver.get("Current1"));
                                textView_WATT1.setText(GlobalData.datamap_getserver.get("Watt1"));
                                textView_FREQ1.setText(GlobalData.datamap_getserver.get("Freq1"));
                                textView_KHW1.setText(GlobalData.datamap_getserver.get("Kwh1"));
                                textView_PF1.setText(GlobalData.datamap_getserver.get("Pf1"));

                                textView_VOLT2.setText(GlobalData.datamap_getserver.get("Volt2"));
                                textView_CURRENT2.setText(GlobalData.datamap_getserver.get("Current2"));
                                textView_WATT2.setText(GlobalData.datamap_getserver.get("Watt2"));
                                textView_FREQ2.setText(GlobalData.datamap_getserver.get("Freq2"));
                                textView_KHW2.setText(GlobalData.datamap_getserver.get("Kwh2"));
                                textView_PF2.setText(GlobalData.datamap_getserver.get("Pf2"));

                                if(GlobalData.datamap_getserver.get("Name1").equals(""))
                                    devicename1.setText("null");
                                else
                                {
                                    devicename1.setText(GlobalData.datamap_getserver.get("Name1"));
                                    GlobalData.device_name_now[0]=GlobalData.datamap_getserver.get("Name1");
                                }

                                if(GlobalData.datamap_getserver.get("Name2").equals(""))
                                    devicename2.setText("null");
                                else
                                {
                                    devicename2.setText(GlobalData.datamap_getserver.get("Name2"));
                                    GlobalData.device_name_now[1]=GlobalData.datamap_getserver.get("Name2");
                                }

                                if(GlobalData.datamap_getserver.get("Switch1").equals("0"))
                                {
                                    switch1.setText("OFF");
                                    switch1.setBackgroundResource(R.drawable.buttonshape);
                                }
                                else if (GlobalData.datamap_getserver.get("Switch1").equals("1"))
                                {
                                    switch1.setText("ON");
                                    switch1.setBackgroundResource(R.drawable.buttonshap3);
                                }

                                if(GlobalData.datamap_getserver.get("Switch2").equals("0"))
                                {
                                    switch2.setText("OFF");
                                    switch2.setBackgroundResource(R.drawable.buttonshape);
                                }
                                else if (GlobalData.datamap_getserver.get("Switch2").equals("1"))
                                {
                                    switch2.setText("ON");
                                    switch2.setBackgroundResource(R.drawable.buttonshap3);
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        timer.schedule(task, 0, 300);

    }


    private void bindSocketService()
    {

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                SocketService.SocketBinder binder = (SocketService.SocketBinder) service;
                socketService = binder.getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(getActivity(), SocketService.class);
        getActivity().bindService(intent, sc, BIND_AUTO_CREATE);

    }

    private void setEdittextCustomDialog(final int pos){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v =  inflater.inflate(R.layout.alert_edittext,null);
        EditText editText = v.findViewById(R.id.editext_alert);
        alertDialog.setTitle("設定名稱");
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                GlobalData.FSM="Changename";
                GlobalData.device_name_change[pos]=editText.getText().toString();

            }
        });
        alertDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.setView(v);
        alertDialog.show();


    }









}