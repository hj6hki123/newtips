package com.example.newtips;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GlobalData {


    public static String FSM="IDLE";
    public static String receivedata="";
    public static Boolean connectstate=false;
    static HashMap<String,String> logingmap= new HashMap();
    static HashMap<String,String> datamap= new HashMap();
    static Set<String> macaddr=new HashSet<>();


    public static String Login_user="";
    public static String Login_password="";




}
