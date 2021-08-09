package com.example.newtips;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class page1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Thread threadRecv;
    Thread threadSend;
    MyBroadcastReceiver mMyReceiver;
    Socket clientSocket;
    InetAddress serverIp;
    int serverPort;
    boolean CONEACCEPT_FLAG=false;
    boolean connectfrag;
    TextView Dlight,Dwet,Dwind,Dtemp,Dwatt,Dhottemp,Dco2,Dch2o,Dchemical,Dpm25,Dpm10,Dsensor,Ddrop,Dface,Dsound;
    InputStream is;
    BufferedInputStream bis;
    OutputStream os;
    BufferedOutputStream bos;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public page1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment page1.
     */
    // TODO: Rename and change types and number of parameters
    public static page1 newInstance(String param1, String param2) {
        page1 fragment = new page1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dlight=(TextView)getView().findViewById(R.id.data_light);
        Dwet=(TextView)getView().findViewById(R.id.data_wet);
        Dwind=(TextView)getView().findViewById(R.id.data_wind);
        Dtemp=(TextView)getView().findViewById(R.id.data_temp);
        Dwatt=(TextView)getView().findViewById(R.id.data_watt);
        Dhottemp=(TextView)getView().findViewById(R.id.data_hottemp);
        Dco2=(TextView)getView().findViewById(R.id.data_co2);
        Dch2o=(TextView)getView().findViewById(R.id.data_ch2o);
        Dchemical=(TextView)getView().findViewById(R.id.data_chemical);
        Dpm25=(TextView)getView().findViewById(R.id.data_pm25);
        Dpm10=(TextView)getView().findViewById(R.id.data_pm10);
        Dsensor=(TextView)getView().findViewById(R.id.data_sensor);
        Ddrop=(TextView)getView().findViewById(R.id.data_drop);
        Dface=(TextView)getView().findViewById(R.id.data_face);
        Dsound=(TextView)getView().findViewById(R.id.data_sound);

        allclear();//VIEW初始化
        threadRecv=new Thread(TCPconnect);
        threadRecv.start();
        threadSend=new Thread(TCPsender);
        threadSend.start();


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2); }
        Log.e("page1","created");


        IntentFilter itFilter = new IntentFilter("KEY");//KEY為廣播辨識碼
        mMyReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(mMyReceiver, itFilter); //註冊廣播接收器

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("created","page1");
        return inflater.inflate(R.layout.fragment_page1, container, false);
    }


    private Runnable TCPconnect=new Runnable() {
        @Override
        public void run() {

          while (true){
              connectfrag = CheckConnect();
            Log.e("thread", "while");

            if (connectfrag == false)
                continue;

            else if (connectfrag == true) {
                try {
                    Log.v("connectstate", "success");

                    is = clientSocket.getInputStream();
                    // Buffer the input stream
                    bis = new BufferedInputStream(is);
                    // Create a buffer in which to store the data
                    os = clientSocket.getOutputStream();
                    // Buffer the input stream
                    bos = new BufferedOutputStream(os);
                    // Create a buffer in which to store the data
                    byte[] buffer = new byte[1024];
                    int[] buffer_decode = new int[1024];//資料在這
                    while (clientSocket.isConnected()) {

                        int countBytesRead = bis.read(buffer, 0, 255);

                        for (int i = 0; i < buffer.length; i++)
                            buffer_decode[i] = (buffer[i] & 0xff);// byte java:-128~127 to 0~255


                        if (buffer_decode[0] == 0xFF && buffer_decode[1] == 0xA0)//收資料
                        {
                            try {

                                int dlight = (buffer_decode[2] * 256)
                                        + (buffer_decode[3]);
                                Dlight.setText(Integer.toString(dlight));

                                double dtemp = ((buffer_decode[4] * 256)
                                        + (buffer_decode[5]));
                                dtemp = dtemp / 10;
                                Dtemp.setText(Double.toString(dtemp));

                                double dwet = (buffer_decode[6] * 256)
                                        + (buffer_decode[7]);
                                dwet = dwet / 10;
                                Dwet.setText(Double.toString(dwet));

                                double dwatt = (buffer_decode[8] * 65536)
                                        + (buffer_decode[9] * 256)
                                        + (buffer_decode[10]);
                                dwatt = dwatt / 100;
                                Dwatt.setText(Double.toString(dwatt));////

                                double dwind = (buffer_decode[11] * 256)
                                        + (buffer_decode[12]);
                                dwind = dwind / 10;
                                Dwind.setText(Double.toString(dwind));

                                double dhottemp = (buffer_decode[13] * 256)
                                        + (buffer_decode[14]);
                                dhottemp = dhottemp / 10;
                                Dhottemp.setText(Double.toString(dhottemp));

                                double dco2 = (buffer_decode[15] * 256)
                                        + (buffer_decode[16]);
                                Dco2.setText(Double.toString(dco2));

                                double dch2o = (buffer_decode[17] * 256)
                                        + (buffer_decode[18]);
                                Dch2o.setText(Double.toString(dch2o));

                                double dchemical = (buffer_decode[19] * 256)
                                        + (buffer_decode[20]);
                                Dchemical.setText(Double.toString(dchemical));

                                double dpm25 = (buffer_decode[21] * 256)
                                        + (buffer_decode[22]);
                                Dpm25.setText(Double.toString(dpm25));

                                double dpm10 = (buffer_decode[23] * 256)
                                        + (buffer_decode[24]);
                                Dpm10.setText(Double.toString(dpm10));
                                //todo:光電感應  IF buffer_decode[25] ==1 有警報 ELSE 無警報
                                int dpmsensor = (buffer_decode[25]);
                                Dsensor.setText(dpmsensor == 1 ? "警報中" : "無警報");
                                //todo:雨滴  IF buffer_decode[26] ==1 有下雨 ELSE 沒下雨
                                int ddrop = (buffer_decode[26]);
                                Ddrop.setText(ddrop == 1 ? "下雨中" : "沒下雨");
                                //todo:人臉  IF buffer_decode[27] ==1 辨識成功 ELSE ""
                                int dfaca = (buffer_decode[27]);
                                Dface.setText(dfaca == 1 ? "辨識成功" : "");
                                //todo:語音辨識  IF buffer_decode[28] ==1 打開電燈 ELSE ""
                                int dsound = (buffer_decode[28]);
                                Dsound.setText(dsound == 1 ? "打開電燈" : "");

                            } catch (Exception e) {
                                Log.e("Exception", e.toString());
                            }
                        } else
                            allclear();

                    }

                }//try

                catch (IOException e) {
                    allclear();
                    clientSocket = null;
                    connectfrag = false;

                }


            }//else if(connectfrag==true)
        }

        }//run
    };

    private Runnable TCPsender=new Runnable() {
        @Override
        public void run()
        {
            byte[] S_buffer=new byte[5];

            while(true)
            {
                try
                {
                    while(clientSocket==null){}
                    while (clientSocket.isConnected())
                    {
                        switch(mMyReceiver.dataType)
                        {
                            case "light" :
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x01;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= mMyReceiver.datable2;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;
                            case "LED_light" :
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x02;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;
                            case "curtain" :
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x03;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;
                            case  "window":
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x04;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;
                            case  "dehumidifier":
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x05;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;
                            case  "conditioner":
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x06;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;
                            case  "fan":
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x07;
                                S_buffer[2]= mMyReceiver.datable1;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                                broadrecvClean();
                                break;

                            default:
                                S_buffer[0]= (byte) 0xFF;
                                S_buffer[1]= (byte) 0x20;
                                S_buffer[2]= (byte) 0x00;
                                S_buffer[3]= (byte) 0x00;
                                S_buffer[4]= (byte) 0xFF;
                        }

                        bos.write(S_buffer ,0 ,5);
                        bos.flush();
                        Log.d("state","sending");
                        Thread.sleep(200);
                    }
                }
                catch (Exception e)
                {
                    Log.e("TCPsenderCrack", e.toString());

                }

            }
        }
    };



    public boolean CheckConnect()
    {
        try {
            Log.e("llllllllllllllll", "1");


            serverIp = InetAddress.getByName("192.168.1.1");
            serverPort = Integer.valueOf("2005");

            Log.e("ADDR", serverIp.toString());
            Log.e("llllllllllllllll", "2");


            clientSocket=new Socket();
            InetSocketAddress isa = new InetSocketAddress(serverIp,serverPort);
            clientSocket.connect(isa,5000);

            Log.e("llllllllllllllll", "3");

            if (clientSocket != null) {
                CONEACCEPT_FLAG=true;
            }
            else
            {
                CONEACCEPT_FLAG=false;

                Log.e("llllllllllllllll", "4");

                //???????????????????製作跳至設定ip之fragment?????????????
            }
        } catch (IOException e) {

            Log.e("msg1", "Socket連線有問題 !");
            Log.e("msg1", "IOException :" + e.toString());
            //???????????????????製作跳至設定ip之fragment?????????????
            /*HomeFragment fm1= new HomeFragment();
            FragmentManager FM = getSupportFragmentManager();
            FM.beginTransaction().replace(R.id.nav_gallery,fm1).commit();*/
            return false;
        }

        if(CONEACCEPT_FLAG == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    private void allclear()
    {
        Dlight.setText("");
        Dtemp.setText("");
        Dwet.setText("");
        Dwatt.setText("");
        Dwind.setText("");
        Dhottemp.setText("");
        Dco2.setText("");
        Dch2o.setText("");
        Dchemical.setText("");
        Dpm25.setText("");
        Dpm10.setText("");
        Dsensor.setText("");
        Ddrop.setText("");
        Dface.setText("");
        Dsound.setText("");
    }
    private void broadrecvClean()
    {
        mMyReceiver.dataType="";
        mMyReceiver.datable1=(byte) 0x00;
        mMyReceiver.datable2=(byte) 0x00;
    }
    private void closeSocket(Socket socket) {
        try {
            if(socket!=null){
            if (!socket.isClosed()) {
                is.close();
                bis.close();
                os.close();
                bos.close();
                socket.close();

            }}
        } catch (IOException e) {
            //onError(new ProxyCacheException("Error closing socket", e));
        }
    }




}
