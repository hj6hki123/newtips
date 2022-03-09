package com.example.newtips;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class CommendFun {
    @SuppressLint("DefaultLocale")
    public static String getLocalIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo info = wifiManager.getConnectionInfo();
        int ipAddress = info.getIpAddress();
        return String.format("%d.%d.%d.%d"
                , ipAddress & 0xff
                , ipAddress >> 8 & 0xff
                , ipAddress >> 16 & 0xff
                , ipAddress >> 24 & 0xff);
    }

    // 獲取有限網IP
    public static String getHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "0.0.0.0";
    }
    /**
     * 獲取外網ip地址（非本地區域網地址）的方法
     */
    public static String getOutNetIP() {
        String ipAddress = "";
        try {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML,like Gecko) Chrome/51.0.2704.7 Safari/537.36"); //設定瀏覽器ua 保證不出現503
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                // 將流轉化為字串
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                String tmpString;
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }
                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");
                Log.e("getOutNetIP","提示：" +retJSON.toString());
                if (code.equals("0")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    ipAddress = data.getString("ip")/* + "(" + data.getString("country")
              + data.getString("area") + "區"
              + data.getString("region") + data.getString("city")
              + data.getString("isp") + ")"*/;
                    Log.e("getOutNetIP","您的IP地址是：" + ipAddress);
                } else {
                    Log.e("getOutNetIP","IP介面異常，無法獲取IP地址！");
                }
            } else {
                Log.e("getOutNetIP","網路連線異常，無法獲取IP地址！");
            }
        } catch (Exception e) {
            Log.e("getOutNetIP","獲取IP地址時出現異常資訊是" + e.toString());
        }
        return ipAddress;
    }

    @SuppressLint("MissingPermission")
    public static String getIpAddress(Context context) {
        if (context == null) {
            return "";
        }
        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo info = conManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 3/4g網路
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return getHostIp();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
//          return getLocalIPAddress(context); // 區域網地址
                    return getLocalIP(context); // 外網地址
                } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    // 乙太網有限網路
                    return getHostIp();
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }


}
