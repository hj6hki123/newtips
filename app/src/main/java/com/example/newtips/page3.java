package com.example.newtips;

import static android.content.Context.BIND_AUTO_CREATE;


import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

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

public class page3 extends Fragment {


    private LineChart charts;
    private boolean isRunning = false;
    private LineChart chart;
    private Thread thread;


    private ServiceConnection sc;
    public SocketService socketService;


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
        bindSocketService();
        charts=view.findViewById(R.id.lineChart);
        initChart();
        startRun();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    protected void initChart()
    {
        charts.getDescription().setEnabled(false);
        LineData data=new LineData();
        data.setValueTextColor(Color.BLACK);
        charts.setData(data);


        //設置左下角標籤
        Legend l =  charts.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        l.setTextSize(20);
        l.setFormSize(20);
        XAxis x =  charts.getXAxis();
        x.setTextColor(Color.BLACK);
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
            //取亂數
            addData(Float.parseFloat((Objects.requireNonNull(GlobalData.datamap_getserver.get("Watt1")))),0,Color.GREEN,GlobalData.device_name_now[0]);
            addData(Float.parseFloat((Objects.requireNonNull(GlobalData.datamap_getserver.get("Watt2")))),1,Color.BLUE,GlobalData.device_name_now[1]);

        };
//            thread = new Thread(new Runnable()
//            {@Override public void run() {runOnUiThread(runnable);}});
        //簡略寫法
        thread =  new Thread(()->{
            while (isRunning) {
                getActivity().runOnUiThread(runnable);
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

}
