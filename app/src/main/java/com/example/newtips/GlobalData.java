package com.example.newtips;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalData {

//內部參數
    public static String FSM="IDLE";
    public static String receivedata="";
    public static Boolean connectstate=false;
    public static String macaddress_select="none";
//傳給server的資料
    public static String Login_user="";
    public static String Login_password="";

    public static String Deviceswitch1="0";
    public static String Deviceswitch2="0";

//server拿到的資料
    public static HashMap<String,String> datamap_getserver= new HashMap<String,String>()//正規化從server收到的資料
    {
        {
            put("Title","2");
            put("Volt1","0");
            put("Volt2","0");
            put("Current1","0");
            put("Current2","0");
            put("Watt1","0");
            put("Freq1","0");
            put("Freq2","0");
            put("Kwh1","0");
            put("Kwh2","0");
            put("Pf1","0");
            put("Pf2","0");
            put("Switch1","0");
            put("Switch2","0");
        }
    };





}
