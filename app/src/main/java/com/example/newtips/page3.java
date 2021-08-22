package com.example.newtips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.graphics.Color;

public class page3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button spelight_white,spelight_yellow,norlight_red,norlight_green,norlight_blue;
    SeekBar seekBar1,seekBar2;
    TextView trytext,trytext2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean spcflagW=false;
    private boolean spcflagY=false;


    private boolean LedflagR=false;
    private boolean LedflagG=false;
    private boolean LedflagB=false;

    public page3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment page3.
     */
    // TODO: Rename and change types and number of parameters
    public static page3 newInstance(String param1, String param2) {
        page3 fragment = new page3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spelight_white=(Button)getView().findViewById(R.id.SPClight_btn_white);
        spelight_yellow=(Button)getView().findViewById(R.id.SPClight_btn_yellow);
        norlight_red=(Button)getView().findViewById(R.id.NORlight_btn_red);
        norlight_green=(Button)getView().findViewById(R.id.NORlight_btn_green);
        norlight_blue=(Button)getView().findViewById(R.id.NORlight_btn_blue);
        seekBar1=(SeekBar) getView().findViewById(R.id.seekbar1);
        seekBar2=(SeekBar) getView().findViewById(R.id.seekbar2);

        spelight_white.setOnClickListener(Wspelight);
        spelight_yellow.setOnClickListener(Yspelight);
        norlight_red.setOnClickListener(Rnorlight);
        norlight_green.setOnClickListener(Gnorlight);
        norlight_blue.setOnClickListener(Bnorlight);
        seekBar1.setOnSeekBarChangeListener(seekBar_W);
        seekBar2.setOnSeekBarChangeListener(seekBar_Y);

        trytext=getView().findViewById(R.id.trytext);
        trytext2=getView().findViewById(R.id.trytext2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("created","page3");
        return inflater.inflate(R.layout.fragment_page3, container, false);
    }
//todo:-----------------------平板燈-----------------------------------------------------
    private int Wlight=60;
    private int Ylight=60;


    private final SeekBar.OnSeekBarChangeListener seekBar_W=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Wlight=i;

            if(i==0)
            {
                spelight_white.setBackgroundResource(R.drawable.buttonshape);
                spcflagW = false;
            }
            else
            {
                spelight_white.setBackgroundResource(R.drawable.buttonshape2);
                spcflagW = true;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            spelight_white.setBackgroundResource(R.drawable.buttonshape2);
            spcflagW = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.e("seekbar1",Integer.toString(Wlight));
            Intent it = new Intent("KEY"); //設定廣播識別碼
            it.putExtra("iuforon", "light");//設定廣播夾帶參數
            it.putExtra("stream3",(byte)(Wlight));
            it.putExtra("stream6",(byte)(Ylight));
            getActivity().sendBroadcast(it); //發送廣播訊息

        }
    };




    private final View.OnClickListener Wspelight =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(spcflagW)) {
                spcflagW = true;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(Wlight));
                it.putExtra("stream6",(byte)(Ylight));
                getActivity().sendBroadcast(it); //發送廣播訊息
                spelight_white.setBackgroundResource(R.drawable.buttonshape2);
            }
            else {
                spcflagW = false;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(0));
                it.putExtra("stream6",(byte)(Ylight));
                getActivity().sendBroadcast(it); //發送廣播訊息
                spelight_white.setBackgroundResource(R.drawable.buttonshape);
            }

        }
    };

    private final SeekBar.OnSeekBarChangeListener seekBar_Y=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Ylight=i;
            if(i==0)
            {
                spelight_yellow.setBackgroundResource(R.drawable.buttonshape);
                spcflagY = false;
            }
            else
            {
                spelight_yellow.setBackgroundResource(R.drawable.buttonshape2);
                spcflagY = true;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            spelight_yellow.setBackgroundResource(R.drawable.buttonshape2);
            spcflagY = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.e("seekbar2",Integer.toString(Ylight));
            Intent it = new Intent("KEY"); //設定廣播識別碼
            it.putExtra("iuforon", "light");//設定廣播夾帶參數
            it.putExtra("stream3",(byte)(Wlight));
            it.putExtra("stream6",(byte)(Ylight));
            getActivity().sendBroadcast(it); //發送廣播訊息

        }
    };
    private final View.OnClickListener Yspelight =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(spcflagY)) {
                spcflagY = true;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(Wlight));
                it.putExtra("stream6",(byte)(Ylight));
                getActivity().sendBroadcast(it); //發送廣播訊息
                spelight_yellow.setBackgroundResource(R.drawable.buttonshape2);
            }
            else {
                spcflagY = false;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(Wlight));
                it.putExtra("stream6",(byte)(0));
                getActivity().sendBroadcast(it); //發送廣播訊息
                spelight_yellow.setBackgroundResource(R.drawable.buttonshape);
            }



        }
    };
//todo:-----------------------平板燈-----------------------------------------------------

////todo:===================================led=================================
    private int ledR=0;
    private int ledG=0;
    private int ledB=0;
    private final View.OnClickListener Rnorlight =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(LedflagR)) {
                ledR=1;
                LedflagR = true;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "LED_light");//設定廣播夾帶參數
                it.putExtra("stream3", (byte) (ledR+ledG+ledB));
                getActivity().sendBroadcast(it); //發送廣播訊息
                norlight_red.setBackgroundResource(R.drawable.buttonshape2);
            }
            else {
                ledR=0;
                LedflagR = false;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "LED_light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(ledR+ledG+ledB));
                getActivity().sendBroadcast(it); //發送廣播訊息
                norlight_red.setBackgroundResource(R.drawable.buttonshape);
            }


        }
    };


    private final View.OnClickListener Gnorlight =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(LedflagG)) {
                ledG=2;
                LedflagG = true;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "LED_light");//設定廣播夾帶參數
                it.putExtra("stream3", (byte) (ledR+ledG+ledB));
                getActivity().sendBroadcast(it); //發送廣播訊息
                norlight_green.setBackgroundResource(R.drawable.buttonshape2);
            }
            else {
                ledG=0;
                LedflagG = false;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "LED_light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(ledR+ledG+ledB));
                getActivity().sendBroadcast(it); //發送廣播訊息
                norlight_green.setBackgroundResource(R.drawable.buttonshape);
            }

        }
    };


    private final View.OnClickListener Bnorlight =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(LedflagB)) {
                ledB=4;
                LedflagB = true;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "LED_light");//設定廣播夾帶參數
                it.putExtra("stream3", (byte) (ledR+ledG+ledB));
                getActivity().sendBroadcast(it); //發送廣播訊息
                norlight_blue.setBackgroundResource(R.drawable.buttonshape2);
            }
            else {
                ledB=0;
                LedflagB = false;
                Intent it = new Intent("KEY"); //設定廣播識別碼
                it.putExtra("iuforon", "LED_light");//設定廣播夾帶參數
                it.putExtra("stream3",(byte)(ledR+ledG+ledB));
                getActivity().sendBroadcast(it); //發送廣播訊息
                norlight_blue.setBackgroundResource(R.drawable.buttonshape);
            }

        }
    };
////todo:===================================led=================================


}
