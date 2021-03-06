package com.example.newtips;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.newtips.common.Aesencryption;
import com.example.newtips.common.Constants;
import com.example.newtips.common.EventMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class SocketService extends Service {



    public static final String RECEIVE_SERVICE_ACTION="TCPSOCKET_ACTION";
    public static final String RECEIVE_SERVICE_STRING="TCPSOCKET_ReceiveString";
    public static final String RECEIVE_SERVICE_PROBLEM="TCPSOCKET_ReceiveProblem";
    public static final String LoginAccess_ACTION="TCPSOCKET_LoginAccess";
    public static final String Loginvoke="Loginvoke";
    public static final String Init_ACTION="Init_ACTION";
    public static final String Init_DATA="Init_DATA";

    /*socket*/
    private Socket socket;
    /*連接線程*/
    private Thread connectThread;
    private Timer timer = new Timer();
    private OutputStream outputStream;
    private Aesencryption aesencryption;
    //private SocketBinder sockerBinder = new SocketBinder();

    private String ip;
    private String port;
    private TimerTask task;


    HashMap<String,String> datamap= new HashMap();
    /*默認重連*/
    private boolean isReConnect = true;

    private Handler handler = new Handler();


    @Override
    public IBinder onBind(Intent intent) {
        //return sockerBinder;
        return null;
    }


    public class SocketBinder extends Binder {
        /*返回SocketService 在需要的地方可以通過ServiceConnection獲取到SocketService */
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        aesencryption=new Aesencryption(this);

        /*拿到傳遞過來的ip和端口號*/
        ip = getString(R.string.TCP_IP).trim();
        port = getString(R.string.TCP_PORT).trim();

        /*初始化socket*/
        initSocket();

        //return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;//不重啟service

    }


    /*初始化socket*/
    private void initSocket() {
        if (socket == null && connectThread == null) {
            connectThread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {

                    socket = new Socket();
                    try {

                        /*超時時間為5秒*/
                        socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 5000);


                        if (socket.isConnected()) {

                            toastMsg("socket已連接");
                            GlobalData.connectstate=true;

                            EventMsg msg = new EventMsg();
                            msg.setTag(Constants.CONNET_SUCCESS);

                            InputStreamReader dis = new InputStreamReader(socket.getInputStream());
                            BufferedReader br = new BufferedReader(dis);


                            while(true)
                            {

                                Thread.sleep(300);


                                switch (GlobalData.FSM)
                                {
                                    case "IDLE":
                                        break;

                                    case "Longin":
                                        //傳資料
                                        HashMap<String,String> logingmap= new HashMap();

                                        logingmap.put("Title","1");
                                        logingmap.put("User",GlobalData.Login_user);
                                        logingmap.put("Password",GlobalData.Login_password);

                                        JSONObject login_json=new JSONObject(logingmap);
                                        sendOrder(aesencryption.startencode(login_json.toString()));
                                        //收資料
                                        Log.e("aes","3333333333333");
                                        String loginacess=br.readLine();
                                        Log.e("aes","444444444444444");
                                        loginacess=aesencryption.startdecode(loginacess);

                                        Log.e("get",loginacess+"");
                                        JSONObject login_acess=new JSONObject(loginacess);
                                        if(login_acess.getString("Title").equals("1"))
                                        {
                                            if(login_acess.getString("Loginaccess").equals("true"))
                                            {
                                                Intent intent = new Intent();
                                                intent.setAction(LoginAccess_ACTION);
                                                intent.putExtra(Loginvoke,true);
                                                sendBroadcast(intent);
                                            }
                                            else
                                            {
                                                GlobalData.FSM="IDLE";
                                                toastMsg("登入失敗請檢察帳號密碼");
                                            }

                                        }
                                        else//title!=1
                                        {
                                            GlobalData.FSM="IDLE";
                                            toastMsg("登入表頭錯誤");
                                        }

                                        break;

                                    case "Datatransport":
                                        if(!GlobalData.macaddress_select.equals("none"))
                                        {
                                            datamap.put("Title","2");
                                            datamap.put("MacAddress",GlobalData.macaddress_select);
                                            datamap.put("Switch1",GlobalData.Deviceswitch1);
                                            datamap.put("Switch2",GlobalData.Deviceswitch2);
                                            JSONObject data_json=new JSONObject(datamap);
                                            sendOrder(aesencryption.startencode(data_json.toString()) +"");
                                            if(data_json.getString("Switch1").equals("1"))
                                                GlobalData.Deviceswitch1="0";
                                            if(data_json.getString("Switch2").equals("1"))
                                                GlobalData.Deviceswitch2="0";

                                            String dataget=br.readLine();
                                            dataget = aesencryption.startdecode(dataget);
                                            Log.e("Datatransport",dataget+"");
                                            if(dataget!=null)
                                            {
                                                JSONObject jsondataget=new JSONObject(dataget);
                                                if(jsondataget.getString("Title").equals("2"))//如果表頭是2
                                                {
                                                    GlobalData.datamap_getserver=new Gson().fromJson(jsondataget.toString(),HashMap.class);
                                                }
                                                else if(jsondataget.getString("Title").equals("Error"))
                                                {

                                                }
                                            }
                                        }
                                        else
                                        {
                                            Log.e("Datatransport","nonemacaddr_select");
                                        }

                                        break;


                                    case "Deletmacaddress":
                                        JSONObject delet_mac=new JSONObject();
                                        delet_mac.put("Title","3");
                                        delet_mac.put("MacAddress",GlobalData.dlt_mac);
                                        sendOrder(aesencryption.startencode(delet_mac.toString()) +"");
                                        GlobalData.FSM="Datatransport";
                                        break;
                                    case "Changename":
                                        JSONObject change_name=new JSONObject();
                                        change_name.put("Title","4");
                                        change_name.put("MacAddress",GlobalData.macaddress_select);
                                        change_name.put("Name1",GlobalData.device_name_change[0]);
                                        change_name.put("Name2",GlobalData.device_name_change[1]);
                                        sendOrder(aesencryption.startencode(change_name.toString()) +"");
                                        GlobalData.FSM="Datatransport";
                                        Log.e("changename","s11111111111");
                                        break;
                                    case "Clockedit":
                                        JSONObject clockeditor=new JSONObject();
                                        clockeditor.put("Title","5");
                                        clockeditor.put("Device1Enable",GlobalData.Device1_Timeenable);
                                        clockeditor.put("Device2Enable",GlobalData.Device2_Timeenable);
                                        clockeditor.put("Device1ClockBegin",GlobalData.timeArray_clock.get(0));
                                        clockeditor.put("Device2ClockBegin",GlobalData.timeArray_clock.get(1));
                                        clockeditor.put("Device1ClockEnd",GlobalData.timeArray_clock.get(2));
                                        clockeditor.put("Device2ClockEnd",GlobalData.timeArray_clock.get(3));
                                        clockeditor.put("Macaddress",GlobalData.macaddress_select);
                                        sendOrder(aesencryption.startencode(clockeditor.toString()) +"");
                                        GlobalData.FSM="Datatransport";
                                        Log.e("ClockeditSender",clockeditor.toString()+"");
                                        break;
                                    case "Inituserdata":
                                        /*
                                        1.使用者註冊新帳號並第一次配對後，因macaddress未及時被WordListAdapter賦值導致page3拿到廣播後也無法及時改變狀態
                                        2.使用者在資料庫中以有配對插頭但手機本地端無資料情況下進行初始化，在page2拿到macaddress後與上述問題相同
                                        */
                                        JSONObject initdata=new JSONObject();
                                        initdata.put("Title","6");
                                        initdata.put("User",GlobalData.Login_user);
                                        sendOrder(aesencryption.startencode(initdata.toString()) +"");
                                        String dataget=br.readLine();
                                        dataget = aesencryption.startdecode(dataget);
                                        Log.e("Inituserdata",dataget+"");
                                        if(dataget!=null)
                                        {
                                            JSONObject jsondataget=new JSONObject(dataget);

                                            if(jsondataget.getString("Title").equals("6"))//如果表頭是6
                                            {

                                                JSONArray initdata_recv=jsondataget.getJSONArray("Initdata");
                                                Intent intent = new Intent();
                                                intent.setAction(Init_ACTION);
                                                intent.putExtra(Init_DATA,initdata_recv.toString());
                                                sendBroadcast(intent);

                                                toastMsg("資料恢復完成");
                                            }

                                        }
                                        GlobalData.FSM="Datatransport";
                                        break;
                                    default:
                                        break;
                                }


                            }
                        }


                    } catch (IOException | InterruptedException | JSONException e) {
                        e.printStackTrace();
                        GlobalData.connectstate=false;//若連線狀態為否，登入按鈕無法動作
                        String problem="";
                        if (e instanceof SocketTimeoutException) {
                            problem="連線超時!正在重新連線";
                            toastMsg(problem);
                            releaseSocket();

                        } else if (e instanceof NoRouteToHostException) {
                            problem="該地址錯誤!請重新檢查";
                            toastMsg(problem);
                            releaseSocket();

                        } else if (e instanceof ConnectException) {
                            problem="連接異常或被拒絕!請重新檢查";
                            toastMsg(problem);
                            releaseSocket();
                        }
                        else if(e instanceof SocketException)
                        {
                            problem="伺服器連接異常!請查看連線狀態";
                            toastMsg(problem);
                            releaseSocket();
                        }
                        else if(e instanceof InterruptedException)
                        {
                            problem="延時崩潰";
                            toastMsg(problem);
                            releaseSocket();
                        }
                        else if(e instanceof JSONException)
                        {
                            problem="JSON資料接收錯誤";
                            toastMsg(problem);
                            releaseSocket();
                        }
                        Intent intent = new Intent();
                        intent.setAction(RECEIVE_SERVICE_ACTION);
                        intent.putExtra(RECEIVE_SERVICE_STRING,"false");
                        intent.putExtra(RECEIVE_SERVICE_PROBLEM,problem);
                        sendBroadcast(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            /*啟動連接線程*/
            connectThread.start();

        }


    }

    /*因為Toast是要運行在主線程的   所以需要到主線程哪裡去顯示toast*/
    private void toastMsg(final String msg) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*發送數據*/
    public void sendOrder(final String order) {
        if (socket != null && socket.isConnected()) {
            /*發送指令*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        outputStream = socket.getOutputStream();
                        if (outputStream != null) {
                            outputStream.write((order).getBytes("UTF-8"));
                            outputStream.flush();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        if(e instanceof SocketException)
                        {
                            releaseSocket();
                        }
                        Log.e("sd",e.toString());
                    }

                }
            }).start();

        } else {
            toastMsg("socket連接錯誤,請重試");
        }
    }


    private void sendBeatData() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        outputStream = socket.getOutputStream();


                        outputStream.write(("test").getBytes("gbk"));
                        outputStream.flush();
                    } catch (Exception e) {

                        releaseSocket();
                        e.printStackTrace();


                    }
                }
            };
        }

        timer.schedule(task, 0, 2000);
    }


    public static String getData()
    {
        String data="";

        return data;
    }


    /*釋放資源*/
    private void releaseSocket() {

        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }

        if (socket != null) {
            try {
                socket.close();

            } catch (IOException e) {
            }
            socket = null;
        }

        if (connectThread != null) {
            connectThread = null;
        }

        /*重新初始化socket*/
        if (isReConnect) {
            initSocket();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SocketService", "onDestroy");
        isReConnect = false;
        releaseSocket();
    }
}