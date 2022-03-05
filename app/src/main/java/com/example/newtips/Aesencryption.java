package com.example.newtips;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public  class Aesencryption {

    private static String sKey = "0123456789012345";
    private static String ivParameter = "0123456789012345";

    // 加密
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return  Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码。
    }

    // 解密
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String sSrc) {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 =  Base64.getDecoder().decode(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String startencode(String str)
    {
        String output="";
        try {
            output = encrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("aes",output);
        return output;
    }


}
