package com.example.newtips;

import static android.content.Context.BIND_AUTO_CREATE;


import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.newtips.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.timepicker.MaterialTimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class page3 extends Fragment {


    private LineChart charts;
    private boolean isRunning = false;
    private Thread thread;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer = new Timer();
    private TimerTask task;
    MyBroadcast myBroadcast = new MyBroadcast();
    SharedPreferences pref ;

    LinearLayout clock_LeftTop,clock_LeftBotton,clock_RightTop,clock_RightBotton;
    TextView    time1_begin,time2_begin,time1_end,time2_end;
    TextView    time1_begin_12H,time2_begin_12H,time1_end_12H,time2_end_12H;
    SwitchCompat device1_enable,device2_enable;
    TextView user_text,ip_text,mac_text,state_text;
    MaterialCardView card_view;
    public page3() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        initpref();
        charts=view.findViewById(R.id.lineChart);
        initChart();
        startRun();
        timerUI();
        randomcolor(card_view);
        clock_LeftTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker(v,1);
            }
        });
        clock_LeftBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker(v,2);
            }
        });
        clock_RightTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker(v,3);
            }
        });
        clock_RightBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker(v,4);
            }
        });

        device1_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean("device1_enable",isChecked).apply();
                getdataFrompref();
            }
        });
        device2_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean("device2_enable",isChecked).apply();
                getdataFrompref();
            }
        });
        card_view.setOnClickListener(v -> randomcolor(card_view));

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter3 = new IntentFilter(SocketService.Init_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter3);

    }

private void initpref(){
    pref =getActivity().getPreferences(Context.MODE_PRIVATE);

    boolean S1 =pref.getBoolean("device1_enable",false);
    boolean S2 =pref.getBoolean("device2_enable",false);
    device1_enable.setChecked(S1);
    device2_enable.setChecked(S2);

    String t1 =pref.getString("time1_begin","00:01");
    String t2 =pref.getString("time2_begin","06:02");
    String t3 =pref.getString("time1_end","12:03");
    String t4 =pref.getString("time2_end","23:04");

    String[] currenttime= _24Hto12H(t1,t2,t3,t4);//轉換為12小時制 ex(23:04 > pm--11:04)
    time1_begin_12H.setText((currenttime[0].split("--"))[0]);
    time1_begin.setText((currenttime[0].split("--"))[1]);
    time2_begin_12H.setText((currenttime[1].split("--"))[0]);
    time2_begin.setText((currenttime[1].split("--"))[1]);
    time1_end_12H.setText((currenttime[2].split("--"))[0]);
    time1_end.setText((currenttime[2].split("--"))[1]);
    time2_end_12H.setText((currenttime[3].split("--"))[0]);
    time2_end.setText((currenttime[3].split("--"))[1]);
}



    private void initUI(View root)
    {
        clock_LeftTop=(LinearLayout) root.findViewById(R.id.device1_timeBegin);
        clock_LeftBotton=(LinearLayout) root.findViewById(R.id.device2_timeBegin);
        clock_RightTop=(LinearLayout) root.findViewById(R.id.device1_timeEnd);
        clock_RightBotton=(LinearLayout) root.findViewById(R.id.device2_timeEnd);
        time1_begin=(TextView) root.findViewById(R.id.time1_Begin);
        time2_begin=(TextView) root.findViewById(R.id.time2_Begin);
        time1_end=(TextView) root.findViewById(R.id.time1_End);
        time2_end=(TextView) root.findViewById(R.id.time2_End);
        time1_begin_12H=(TextView) root.findViewById(R.id.time1_Begin_12H);
        time2_begin_12H=(TextView) root.findViewById(R.id.time2_Begin_12H);
        time1_end_12H=(TextView) root.findViewById(R.id.time1_End_12H);
        time2_end_12H=(TextView) root.findViewById(R.id.time2_End_12H);
        device1_enable=(SwitchCompat) root.findViewById(R.id.device1_enable);
        device2_enable=(SwitchCompat) root.findViewById(R.id.device2_enable);


        user_text=(TextView) root.findViewById(R.id.user_text);
        user_text.setText(GlobalData.Login_user);
        ip_text =(TextView) root.findViewById(R.id.ip_text);
        mac_text=(TextView) root.findViewById(R.id.mac_text);
        state_text=(TextView) root.findViewById(R.id.state_text);

        card_view=(MaterialCardView) root.findViewById(R.id.card_view);
    }
    private  void timepickManager(int deviceID,int targetResld1,int targetResld2)
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerFragment tpf=TimePickerFragment.newInstance(hour,minute,deviceID,targetResld1,targetResld2);
        tpf.show(getActivity().getFragmentManager(),"timePicker");
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

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                //TODO:updata UI
                                if(!GlobalData.datamap_getserver.get("Status").equals("Offline") && !GlobalData.macaddress_select.equals("none"))
                                {
                                    state_text.setTextColor(Color.GREEN);
                                    state_text.setText("Online");
                                    device1_enable.setClickable(true);
                                    device2_enable.setClickable(true);
                                    clock_LeftBotton.setClickable(true);
                                    clock_LeftTop.setClickable(true);
                                    clock_RightBotton.setClickable(true);
                                    clock_RightTop.setClickable(true);
                                    device1_enable.setText(GlobalData.device_name_now[0]);
                                    device2_enable.setText(GlobalData.device_name_now[1]);
                                }
                                else
                                {
                                    state_text.setTextColor(Color.RED);
                                    state_text.setText("Offline");
                                    device1_enable.setClickable(false);
                                    device2_enable.setClickable(false);
                                    clock_LeftBotton.setClickable(false);
                                    clock_LeftTop.setClickable(false);
                                    clock_RightBotton.setClickable(false);
                                    clock_RightTop.setClickable(false);
                                }
                                ip_text.setText(CommendFun.getIpAddress(getActivity()));
                                mac_text.setText(GlobalData.macaddress_select);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        timer.schedule(task, 0, 2000);

    }

    protected void initChart()
    {
        charts.getDescription().setEnabled(false);
        charts.setTouchEnabled(false);//設置不可觸碰
        charts.setDragEnabled(false);//設置不可互動
        LineData data=new LineData();
        data.setValueTextColor(isNightmode()?Color.WHITE:Color.BLACK);
        charts.setData(data);


        //設置左下角標籤
        Legend l =  charts.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        l.setTextSize(20);
        l.setFormSize(20);
        l.setTextColor(isNightmode()?Color.WHITE:Color.BLACK);
        XAxis x =  charts.getXAxis();
        x.setTextColor(isNightmode()?Color.WHITE:Color.BLACK);
        x.setDrawGridLines(true);//畫X軸線
        x.setPosition(XAxis.XAxisPosition.BOTTOM);//把標籤放底部
        x.setLabelCount(5,true);//設置顯示5個標籤
        //設置X軸標籤內容物
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "No. "+Math.round(value);
            }
        });

        //
        YAxis y = charts.getAxisLeft();
        y.setTextColor(Color.BLACK);
        y.setDrawGridLines(true);
        y.setAxisMaximum(100);//最高100
        y.setAxisMinimum(0);//最低0
        y.setTextColor(isNightmode()?Color.WHITE:Color.BLACK);
        charts.getAxisRight().setEnabled(false);//右邊Y軸不可視
        charts.setVisibleXRange(0,50);//設置顯示範圍


    }
    /**設置數據線的樣式*/
    private LineDataSet createSet(int color,String name) {
        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setLineWidth(2);
        set.setDrawCircles(false);
        set.setFillColor(color);
        set.setFillAlpha(20);
        set.setDrawFilled(true);
        set.setValueTextColor(Color.BLACK);
        set.setDrawValues(false);
        return set;
    }
    private void addData(float inputData,int index,int color,String text){
        LineData data =  charts.getData();//取得原數據
        ILineDataSet set = data.getDataSetByIndex(index);//取得曲線(因為只有一條，故為0，若有多條則需指定)
        if (set == null){
            set = createSet(color,text);
            data.addDataSet(set);//如果是第一次跑則需要載入數據
        }
        set.setLabel(text);
        data.addEntry(new Entry(set.getEntryCount(),inputData),index);//新增數據點
        //更新圖表
        data.notifyDataChanged();
        charts.notifyDataSetChanged();
        charts.setVisibleXRange(0,50);//設置可見範圍
        charts.moveViewToX(data.getEntryCount());//將可視焦點放在最新一個數據，使圖表可移動

    }

    private void startRun(){
        if (isRunning)return;
        if (thread != null) thread.interrupt();
//            Runnable runnable = new Runnable() {@Override public void run() {}};
        //簡略寫法
        isRunning = true;
        Runnable runnable  = ()->{
            if(!GlobalData.macaddress_select.equals("none") && !GlobalData.datamap_getserver.get("Status").equals("Offline"))
            {
                addData(Float.parseFloat((Objects.requireNonNull(GlobalData.datamap_getserver.get("Watt1")))),0,Color.GREEN,GlobalData.device_name_now[0]);
                addData(Float.parseFloat((Objects.requireNonNull(GlobalData.datamap_getserver.get("Watt2")))),1,Color.BLUE,GlobalData.device_name_now[1]);
            }

        };
//            thread = new Thread(new Runnable()
//            {@Override public void run() {runOnUiThread(runnable);}});
        //簡略寫法
        thread =  new Thread(()->{
            while (isRunning) {
                try {
                    getActivity().runOnUiThread(runnable);
                }
                catch (NullPointerException e)
                {
                    Log.e("NullPointerException",Log.getStackTraceString(e));
                   isRunning=false;
                }
                if (!isRunning)break;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void timePicker(View v,int DeviceID) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        switch (DeviceID)
        {
            case 1:
                String t1 =pref.getString("time1_begin","00:01");
                hourOfDay=Integer.parseInt(t1.split(":")[0]);
                minute=Integer.parseInt(t1.split(":")[1]);
                break;
            case 2:
                String t2 =pref.getString("time2_begin","06:02");
                hourOfDay=Integer.parseInt(t2.split(":")[0]);
                minute=Integer.parseInt(t2.split(":")[1]);
                break;
            case 3:
                String t3 =pref.getString("time1_end","12:03");
                hourOfDay=Integer.parseInt(t3.split(":")[0]);
                minute=Integer.parseInt(t3.split(":")[1]);
                break;
            case 4:
                String t4 =pref.getString("time2_end","23:04");
                hourOfDay=Integer.parseInt(t4.split(":")[0]);
                minute=Integer.parseInt(t4.split(":")[1]);
                break;
        }


        new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                int currenthour = (hour>12) ? hour-12 : ((hour==0) ? 12 : hour);
                String _am_pm=(hour>=12) ? "下午" : "上午";
                String _12Hour=String.format("%02d",currenthour);
                String _12minute=String.format("%02d",minute);
                String _12HourTime=_12Hour+":"+_12minute;
                switch (DeviceID)
                {
                    case 1:
                        time1_begin.setText(_12HourTime);
                        time1_begin_12H.setText(_am_pm);
                        pref.edit().putString("time1_begin",String.valueOf(hour)+":"+minute).commit();
                        break;
                    case 2:
                        time2_begin.setText(_12HourTime);
                        time2_begin_12H.setText(_am_pm);
                        pref.edit().putString("time2_begin",String.valueOf(hour)+":"+minute).commit();
                        break;
                    case 3:
                        time1_end.setText(_12HourTime);
                        time1_end_12H.setText(_am_pm);
                        pref.edit().putString("time1_end",String.valueOf(hour)+":"+minute).commit();
                        break;
                    case 4:
                        time2_end.setText(_12HourTime);
                        time2_end_12H.setText(_am_pm);
                        pref.edit().putString("time2_end",String.valueOf(hour)+":"+minute).commit();
                        break;


                }
                getdataFrompref();

            }
        }, hourOfDay, minute,false).show();
    }

    public void MaterialTimePicker(View v,int DeviceID) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        switch (DeviceID)
        {
            case 1:
                String t1 =pref.getString("time1_begin","00:01");
                hourOfDay=Integer.parseInt(t1.split(":")[0]);
                minute=Integer.parseInt(t1.split(":")[1]);
                break;
            case 2:
                String t2 =pref.getString("time2_begin","06:02");
                hourOfDay=Integer.parseInt(t2.split(":")[0]);
                minute=Integer.parseInt(t2.split(":")[1]);
                break;
            case 3:
                String t3 =pref.getString("time1_end","12:03");
                hourOfDay=Integer.parseInt(t3.split(":")[0]);
                minute=Integer.parseInt(t3.split(":")[1]);
                break;
            case 4:
                String t4 =pref.getString("time2_end","23:04");
                hourOfDay=Integer.parseInt(t4.split(":")[0]);
                minute=Integer.parseInt(t4.split(":")[1]);
                break;
        }
        MaterialTimePicker picker
                =new MaterialTimePicker.Builder()
                .setHour(hourOfDay)
                .setMinute(minute)
                .setTitleText("Select Appointment time")
                .build();


        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour=picker.getHour();
                int minute=picker.getMinute();

                int currenthour = (hour>12) ? hour-12 : ((hour==0) ? 12 : hour);
                String _am_pm=(hour>=12) ? "下午" : "上午";
                String _12Hour=String.format("%02d",currenthour);
                String _12minute=String.format("%02d",minute);
                String _12HourTime=_12Hour+":"+_12minute;
                switch (DeviceID)
                {
                    case 1:
                        time1_begin.setText(_12HourTime);
                        time1_begin_12H.setText(_am_pm);
                        pref.edit().putString("time1_begin",String.valueOf(hour)+":"+minute).commit();
                        break;
                    case 2:
                        time2_begin.setText(_12HourTime);
                        time2_begin_12H.setText(_am_pm);
                        pref.edit().putString("time2_begin",String.valueOf(hour)+":"+minute).commit();
                        break;
                    case 3:
                        time1_end.setText(_12HourTime);
                        time1_end_12H.setText(_am_pm);
                        pref.edit().putString("time1_end",String.valueOf(hour)+":"+minute).commit();
                        break;
                    case 4:
                        time2_end.setText(_12HourTime);
                        time2_end_12H.setText(_am_pm);
                        pref.edit().putString("time2_end",String.valueOf(hour)+":"+minute).commit();
                        break;


                }
                getdataFrompref();
            }
        });


        picker.show(getActivity().getSupportFragmentManager(),"pi");


    }
    private String[] _24Hto12H(String...time)
    {
        try {
            for(int i=0;i<time.length;i++) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");//宣告時間格式為"HH:mm"
                Date dateObj = sdf.parse(time[i]);
                time[i] = new SimpleDateFormat("aa--hh:mm").format(dateObj);
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    private void getdataFrompref()
    {
        GlobalData.timeArray_clock.set(0,pref.getString("time1_begin","00:01"));
        GlobalData.timeArray_clock.set(1,pref.getString("time2_begin","00:02"));
        GlobalData.timeArray_clock.set(2,pref.getString("time1_end","00:03"));
        GlobalData.timeArray_clock.set(3, pref.getString("time2_end","00:04"));
        GlobalData.Device1_Timeenable=pref.getBoolean("device1_enable",false) ? "1":"0";
        GlobalData.Device2_Timeenable=pref.getBoolean("device2_enable",false) ? "1":"0";
        GlobalData.FSM="Clockedit";
    }

    private void randomcolor(MaterialCardView card)
    {
        Random random = new Random();
        int base=0;
        base=isNightmode()?100:200;

        int r = random.nextInt(55)+base;
        int g = random.nextInt(55)+base;
        int b = random.nextInt(55)+base;
        card.setCardBackgroundColor(Color.rgb(r,g,b));
    }

    private boolean isNightmode()
    {
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_YES) {
            return true;
        } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
            return false;
        }

        return false;
    }

    private class MyBroadcast extends BroadcastReceiver {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            assert mAction != null;
            switch (mAction) {
                /**接收來自UDP回傳之訊息*/
                case SocketService.Init_ACTION:
                    String init_DATA = intent.getStringExtra(SocketService.Init_DATA);


                    try {
                        JSONArray init_DATA_array = new JSONArray(init_DATA);
//                        if(init_DATA_array.length()>0)
//                        {
//                            JSONObject modFamily = init_DATA_array.getJSONObject(GlobalData.worldlist_pos);
//                            GlobalData.macaddress_select=modFamily.getString("Macaddress");
//                            String S_time1_begin=modFamily.getString("clockbegin1");
//                                    String S_time1_end=modFamily.getString("clockend1");
//                                    String S_time2_begin=modFamily.getString("clockbegin2");
//                                    String S_time2_end=modFamily.getString("clockend2");
//                            pref.edit()
//                                    .putBoolean("device1_enable", modFamily.getInt("clockenable1") == 1)
//                                    .putBoolean("device2_enable", modFamily.getInt("clockenable2") == 1)
//                                    .putString("time1_begin",S_time1_begin)
//                                    .putString("time1_end",S_time1_end)
//                                    .putString("time2_begin",S_time2_begin)
//                                    .putString("time2_end",S_time2_end)
//                                    .apply();
//
//                            String[] time_12h=_24Hto12H(S_time1_begin,S_time1_end,S_time2_begin,S_time2_end);
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        device1_enable.setChecked(modFamily.getInt("clockenable1") == 1);
//                                        device2_enable.setChecked(modFamily.getInt("clockenable2") == 1);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    time1_begin_12H.setText((time_12h[0].split("--"))[0]);
//                                    time1_begin.setText((time_12h[0].split("--"))[1]);
//                                    time1_end_12H.setText((time_12h[1].split("--"))[0]);
//                                    time1_end.setText((time_12h[1].split("--"))[1]);
//                                    time2_begin_12H.setText((time_12h[2].split("--"))[0]);
//                                    time2_begin.setText((time_12h[2].split("--"))[1]);
//                                    time2_end_12H.setText((time_12h[3].split("--"))[0]);
//                                    time2_end.setText((time_12h[3].split("--"))[1]);
//
//                                }
//                            });
//                        }
//                        else {
//                            pref.edit()
//                                    .putBoolean("device1_enable", false)
//                                    .putBoolean("device2_enable", false)
//                                    .putString("time1_begin","00:00")
//                                    .putString("time1_end","00:00")
//                                    .putString("time2_begin","00:00")
//                                    .putString("time2_end","00:00")
//                                    .apply();
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    device1_enable.setChecked(false);
//                                    device2_enable.setChecked(false);
//                                    time1_begin_12H.setText("上午");
//                                    time1_begin.setText("12:00");
//                                    time1_end_12H.setText("上午");
//                                    time1_end.setText("12:00");
//                                    time2_begin_12H.setText("上午");
//                                    time2_begin.setText("12:00");
//                                    time2_end_12H.setText("上午");
//                                    time2_end.setText("12:00");
//
//                                }
//                            });
//                        }
                        for(int i = 0;i < init_DATA_array.length(); i++){
                            //取出JSON物件
                            JSONObject modFamily = init_DATA_array.getJSONObject(i);

                                if(GlobalData.macaddress_select.equals(modFamily.getString("Macaddress")))
                                {
                                    String S_time1_begin=modFamily.getString("clockbegin1");
                                    String S_time1_end=modFamily.getString("clockend1");
                                    String S_time2_begin=modFamily.getString("clockbegin2");
                                    String S_time2_end=modFamily.getString("clockend2");
                                    pref.edit()
                                            .putBoolean("device1_enable", modFamily.getInt("clockenable1") == 1)
                                            .putBoolean("device2_enable", modFamily.getInt("clockenable2") == 1)
                                            .putString("time1_begin",S_time1_begin)
                                            .putString("time1_end",S_time1_end)
                                            .putString("time2_begin",S_time2_begin)
                                            .putString("time2_end",S_time2_end)
                                            .apply();

                                    String[] time_12h=_24Hto12H(S_time1_begin,S_time1_end,S_time2_begin,S_time2_end);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                device1_enable.setChecked(modFamily.getInt("clockenable1") == 1);
                                                device2_enable.setChecked(modFamily.getInt("clockenable2") == 1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            time1_begin_12H.setText((time_12h[0].split("--"))[0]);
                                            time1_begin.setText((time_12h[0].split("--"))[1]);
                                            time1_end_12H.setText((time_12h[1].split("--"))[0]);
                                            time1_end.setText((time_12h[1].split("--"))[1]);
                                            time2_begin_12H.setText((time_12h[2].split("--"))[0]);
                                            time2_begin.setText((time_12h[2].split("--"))[1]);
                                            time2_end_12H.setText((time_12h[3].split("--"))[0]);
                                            time2_end.setText((time_12h[3].split("--"))[1]);

                                        }
                                    });


                                    break;
                                }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }






}
