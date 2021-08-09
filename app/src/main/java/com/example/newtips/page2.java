package com.example.newtips;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class page2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean conditionerFlag = false;
    private boolean dehumidifierFlag = false;
    MyBroadcastReceiver mMyReceiver;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public page2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment page2.
     */
    // TODO: Rename and change types and number of parameters
    public static page2 newInstance(String param1, String param2) {
        page2 fragment = new page2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //---------------------------------------------------------------------------
        Button curtain_on=(Button)getView().findViewById(R.id.angry1_btn1);
        Button curtain_off=(Button)getView().findViewById(R.id.angry1_btn2);
        Button curtain_stop=(Button)getView().findViewById(R.id.angry1_btn3);
        curtain_on.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "curtain");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x01);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        curtain_off.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "curtain");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x02);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        curtain_stop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "curtain");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x00);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        //---------------------------------------------------------------------------
        //---------------------------------------------------------------------------
        Button window_on=(Button)getView().findViewById(R.id.angry2_btn1);
        Button window_off=(Button)getView().findViewById(R.id.angry2_btn2);
        Button window_stop=(Button)getView().findViewById(R.id.angry2_btn3);
        window_on.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "window");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x01);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        window_off.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "window");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x02);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        window_stop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "window");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x00);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        //---------------------------------------------------------------------------
        //---------------------------------------------------------------------------
        Button fan_on=(Button)getView().findViewById(R.id.angry3_btn1);
        Button fan_off=(Button)getView().findViewById(R.id.angry3_btn2);
        fan_on.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "fan");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x01);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });
        fan_off.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "fan");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)0x00);
                getActivity().sendBroadcast(it); //發送廣播訊息
            }
        });

        //---------------------------------------------------------------------------
        //---------------------------------------------------------------------------
        Button conditioner_switch=(Button)getView().findViewById(R.id.angry4_btn1);
        Button dehumidifier_switch=(Button)getView().findViewById(R.id.angry4_btn2);

        conditioner_switch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(conditionerFlag)) {
                    conditionerFlag = true;
                    Intent it = new Intent("KEY"); //設定廣播識別碼
                    it.putExtra("iuforon", "conditioner");//設定廣播夾帶參數
                    it.putExtra("stream3",(byte)0x01);
                    getActivity().sendBroadcast(it); //發送廣播訊息
                    conditioner_switch.setBackgroundResource(R.drawable.buttonshape2);
                } else {
                    conditionerFlag = false;
                    Intent it = new Intent("KEY"); //設定廣播識別碼
                    it.putExtra("iuforon", "conditioner");//設定廣播夾帶參數
                    it.putExtra("stream3",(byte)0x00);
                    getActivity().sendBroadcast(it); //發送廣播訊息
                    conditioner_switch.setBackgroundResource(R.drawable.buttonshape);
                }


            }
        });
        dehumidifier_switch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(dehumidifierFlag)) {
                    dehumidifierFlag = true;
                    Intent it = new Intent("KEY"); //設定廣播識別碼
                    it.putExtra("iuforon", "dehumidifier");//設定廣播夾帶參數
                    it.putExtra("stream3",(byte)0x01);
                    getActivity().sendBroadcast(it); //發送廣播訊息
                    dehumidifier_switch.setBackgroundResource(R.drawable.buttonshape2);
                } else {
                    dehumidifierFlag = false;
                    Intent it = new Intent("KEY"); //設定廣播識別碼
                    it.putExtra("iuforon", "dehumidifier");//設定廣播夾帶參數
                    it.putExtra("stream3",(byte)0x00);
                    getActivity().sendBroadcast(it); //發送廣播訊息
                    dehumidifier_switch.setBackgroundResource(R.drawable.buttonshape);
                }

            }
        });

        //---------------------------------------------------------------------------
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        IntentFilter itFilter = new IntentFilter("KEY");//KEY為廣播辨識碼
        mMyReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(mMyReceiver, itFilter); //註冊廣播接收器
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("created","page2");
        return inflater.inflate(R.layout.fragment_page2, container, false);
    }
}
