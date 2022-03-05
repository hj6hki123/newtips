package com.example.newtips;

import android.os.Build;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String encrypt(String secretKey, String salt, String value) throws Exception {
        Cipher cipher = initCipher(secretKey, salt, Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String decrypt(String secretKey, String salt, String encrypted) throws Exception {
        Cipher cipher = initCipher(secretKey, salt, Cipher.DECRYPT_MODE);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }

    private static Cipher initCipher(String secretKey, String salt, int mode) throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec skeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(mode, skeySpec, new IvParameterSpec(new byte[16]));
        return cipher;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startencode() throws Exception {
        String secretKey = "Secret";
        String fSalt = "tJHnN5b1i6wvXMwzYMRk";
        String plainText = "England";

        String cipherText = encrypt(secretKey, fSalt, plainText);
        System.out.println("Cipher: " + cipherText);
//      cipherText = "6peDTxE1xgLE4hTGg0PKTnuuhFC1Vftsd7NH9DF/7WM="; // Cipher from python
        String dcrCipherText = decrypt(secretKey, fSalt, cipherText);
        System.out.println(dcrCipherText);

    }
}
