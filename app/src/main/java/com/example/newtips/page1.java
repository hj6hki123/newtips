package com.example.newtips;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Color;



public class page1 extends Fragment {
    private ServiceConnection sc;
    public SocketService socketService;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer = new Timer();
    private TimerTask task;
    ExecutorService exec = Executors.newCachedThreadPool();
    UDP udpServer;
    MyBroadcast myBroadcast = new MyBroadcast();
    StringBuffer stringBuffer = new StringBuffer();
    ArrayAdapter<String> spinnerAdapter;
    Set<String> sett=new HashSet<>();

//TODO:UI 宣告
    Spinner spinner;
    GaugeView gaugeView_Vupp,gaugeView_Vdown,gaugeView_Iupp,gaugeView_Idown;


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
        gaugeView_Iupp.setAlpha((float)0.1);

        gaugeView_Vdown = (GaugeView) root.findViewById(R.id.gauge_view_Vdown);
        gaugeView_Vdown.setShowRangeValues(true);
        gaugeView_Vdown.setTargetValue(0);
        gaugeView_Vdown.setAlpha((float)1);

        gaugeView_Idown = (GaugeView) root.findViewById(R.id.gauge_view_Idown);
        gaugeView_Idown.setShowRangeValues(true);
        gaugeView_Idown.setTargetValue(0);
        gaugeView_Idown.setAlpha((float)0.1);

        spinner=root.findViewById(R.id.spinner2);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);//UI初始化


        spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sPos=String.valueOf(position);
                String sInfo=parent.getItemAtPosition(position).toString();

                Toast.makeText(getActivity(),sInfo,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindSocketService();
        timerUI();

        //註冊廣播器，使回傳能夠從其他類別內傳回此Activity
        IntentFilter intentFilter = new IntentFilter(UDP.RECEIVE_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter);
        //UDP_setting
        udpServer = new UDP(CommendFun.getLocalIP(getActivity()),getActivity());
        udpServer.setPort(8888);
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

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        timer.schedule(task, 0, 500);

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


    private class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            assert mAction != null;
            switch (mAction) {
                /**接收來自UDP回傳之訊息*/
                case UDP.RECEIVE_ACTION:
                    String msg = intent.getStringExtra(UDP.RECEIVE_STRING);
                    byte[] bytes = intent.getByteArrayExtra(UDP.RECEIVE_BYTES);
                    stringBuffer.append("收到： ").append(msg).append("\n");
                    if(msg.length()>0)
                    {
                        spinnerAdapter.clear();
                        sett.add(msg);
                        spinnerAdapter.addAll(sett);
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = new Intent(getActivity().getApplicationContext(), SocketService.class);
        getActivity().stopService(intent);
    }
}