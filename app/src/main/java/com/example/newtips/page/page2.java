package com.example.newtips.page;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.newtips.common.CommendFun;
import com.example.newtips.GlobalData;
import com.example.newtips.R;
import com.example.newtips.SocketService;
import com.example.newtips.common.UDP;
import com.example.newtips.adapter.WordListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class page2 extends Fragment {
    private LinkedList<String> Alist=new LinkedList<String>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    MyBroadcast myBroadcast = new MyBroadcast();
    SharedPreferences pref ;
    Set<String> sett_mask;


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

        /*Button btn_udpstart=view.findViewById(R.id.btn_udpstart);
        btn_udpstart.setOnClickListener( (view1 ->
        {

            //開啟UDP 、關閉按鈕點擊
            start_UDP();
            btn_udpstart.setClickable(false);
            btn_udpstart.setText("正在搜尋裝置...");

            //關閉UDP 、 並開啟按鈕點擊
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    udpServer.changeServerStatus(false);
                    btn_udpstart.setText("搜尋裝置");
                    btn_udpstart.setClickable(true);
                }
            },15000);
        }));*/


        LottieAnimationView lottie_addBtn=view.findViewById(R.id.lottie_addbutton);
        lottie_addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //開啟UDP 、關閉按鈕點擊
                start_UDP();
                lottie_addBtn.playAnimation();
                lottie_addBtn.setClickable(false);
                Toast.makeText(getActivity(),"請與15秒內與裝置配對",Toast.LENGTH_LONG).show();



                //關閉UDP 、 並開啟按鈕點擊
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        udpServer.changeServerStatus(false);
                        lottie_addBtn.setClickable(true);
                        lottie_addBtn.pauseAnimation();
                    }
                },15000);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter(UDP.RECEIVE_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter(WordListAdapter.RECEIVE_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter2);
        IntentFilter intentFilter3 = new IntentFilter(SocketService.Init_ACTION);
        getActivity().registerReceiver(myBroadcast, intentFilter3);
        //todo:爛程式碼，易造成記憶體引用錯亂，欲改，暫放
        if(GlobalData.macaddress_select.equals("none"))
        {
            Toast.makeText(getActivity(),"檢查到無設備連接，正在初始化",Toast.LENGTH_SHORT).show();
            GlobalData.FSM="Inituserdata";
        }
        super.onCreate(savedInstanceState);


    }
    private void DATAinit()
    {
        pref =getActivity().getSharedPreferences("pref_macaddress",Context.MODE_PRIVATE);
        sett_mask=new HashSet<String>(pref.getStringSet("Macaddress", new HashSet<>()));//!!new一個set防止sett引用sp變數
        Alist.addAll(sett_mask);
    }
    private class MyBroadcast extends BroadcastReceiver {
        @SuppressLint("NotifyDataSetChanged")
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
                        Log.e("UDP","gotmsg");
                        boolean samelist=false;
                        for(String s:sett_mask)//檢查有無重複
                        {
                            if (msg.equals(s)) {
                                samelist = true;
                                break;
                            }
                        }
                        if(!samelist)//若無重複將資料添加到sett_mask和Alist中，並通知mAdapter更新資料
                        {
                            sett_mask.add(msg);
                            pref.edit().putStringSet("Macaddress",sett_mask)
                                    .apply();
                            Alist.add(msg);
                            mAdapter.notifyDataSetChanged();
                            GlobalData.FSM="Inituserdata";
                        }

                    }
                    break;
                case WordListAdapter.RECEIVE_ACTION:
                    String delete_str = intent.getStringExtra(WordListAdapter.RECEIVE_STRING);
                    sett_mask.remove(delete_str);
                    Alist.remove(delete_str);
                    mAdapter.notifyDataSetChanged();
                    pref.edit().remove("Macaddress").putStringSet("Macaddress",sett_mask).apply();
                    GlobalData.dlt_mac=delete_str;
                    GlobalData.FSM="Deletmacaddress";
                    GlobalData.macaddress_select="none";
                    break;
                case SocketService.Init_ACTION:
                    String init_DATA = intent.getStringExtra(SocketService.Init_DATA);
                    try {
                        JSONArray init_DATA_array = new JSONArray(init_DATA);
                        for(int i = 0;i < init_DATA_array.length(); i++){
                            //取出JSON物件
                            JSONObject modFamily = init_DATA_array.getJSONObject(i);
                            //取得物件內資料
                            sett_mask.add(modFamily.getString("Macaddress"));
                        }
                        pref.edit().putStringSet("Macaddress",sett_mask)
                                .apply();
                        Alist.clear();  //清除
                        Alist.addAll(sett_mask);
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
