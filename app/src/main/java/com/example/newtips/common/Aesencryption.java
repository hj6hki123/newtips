package com.example.newtips.common;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.security.spec.KeySpec;
import android.util.Base64;
import android.util.Xml;

import com.example.newtips.R;

import org.w3c.dom.Text;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public  class Aesencryption  {
    private final String sKey;
    private final String ivParameter;
    public Aesencryption(Context context)//新增建構子並獲取Resourse資料
    {
        sKey=context.getString(R.string.key);
        ivParameter=context.getString(R.string.iv);
    }

    // 加密  方法不能為靜態類，KEY&IV 不能為靜態變數
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        byte[] encrypted_after=  (Base64.encode(encrypted, Base64.DEFAULT));//此处使用BASE64做转码。

        return new String(encrypted_after,"UTF-8");
    }

    // 解密
    @RequiresApi(api = Build.VERSION_CODES.O)
    private   String decrypt(String sSrc) {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 =  Base64.decode(sSrc,Base64.DEFAULT);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String startencode(String str)
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
