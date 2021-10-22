package com.example.newtips;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.newtips.R;
import com.example.newtips.common.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Color;

public class page2 extends Fragment {
    private LinkedList<String> Alist=new LinkedList<String>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    MyBroadcast myBroadcast = new MyBroadcast();
    SharedPreferences pref ;
    Set<String> sett_mask;
    boolean udp_flag=false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer = new Timer();
    private TimerTask task;
    ExecutorService exec = Executors.newCachedThreadPool();
    UDP udpServer;

    public page2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DATAinit();
        // Get a handle to the RecyclerView.
        mRecyclerView = view.findViewById(R.id.recycleview);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(getActivity(), Alist);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button btn_udpstart=view.findViewById(R.id.btn_udpstart);
        btn_udpstart.setOnClickListener( (view1 ->
        {
            if(!udp_flag)
            {
                start_UDP();
                udp_flag=true;
            }
            else
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exec.shutdown();
                        udp_flag=false;
                    }
                },1800);
            }

        }));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter(UDP.RECEIVE_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter(WordListAdapter.RECEIVE_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter2);


        super.onCreate(savedInstanceState);


    }
    private void DATAinit()
    {
        pref =getActivity().getPreferences(Context.MODE_PRIVATE);
        sett_mask=new HashSet<String>(pref.getStringSet("Macaddress", new HashSet<>()));//!!new一個set防止sett引用sp變數
        Alist.addAll(sett_mask);
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
                    if(msg.length()>0)
                    {
                        boolean samelist=false;
                        for(String s:sett_mask)//檢查有無重複
                        {
                            if (msg.equals(s)) {
                                samelist = true;
                                break;

                            }
                        }
                        if(!samelist)
                        {

                            sett_mask.add(msg);
                            pref.edit().putStringSet("Macaddress",sett_mask)
                                    .commit();
                            Alist.add(msg);
                            mAdapter.notifyDataSetChanged();

                        }

                    }
                    break;
                case WordListAdapter.RECEIVE_ACTION:
                    String delete_str = intent.getStringExtra(WordListAdapter.RECEIVE_STRING);
                    sett_mask.remove(delete_str);
                    Alist.remove(delete_str);
                    mAdapter.notifyDataSetChanged();
                    pref.edit().remove("Macaddress").putStringSet("Macaddress",sett_mask).commit();
                    GlobalData.macaddress_select="none";

                    break;



            }
        }
    }

    private void start_UDP()
    {
        //UDP_setting
        udpServer = new UDP(CommendFun.getLocalIP(getActivity()),getActivity());
        udpServer.setPort(31999);
        udpServer.changeServerStatus(true);
        exec.execute(udpServer);


    }


}
