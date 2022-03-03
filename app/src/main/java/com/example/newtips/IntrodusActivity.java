package com.example.newtips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.example.newtips.common.Constants;

public class IntrodusActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    ImageView chirtext;
    public static IntrodusActivity introdusActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introdus);
        chirtext=findViewById(R.id.imageView);
        chirtext.animate().translationX(2000).setDuration(100).setStartDelay(1500);

        introdusActivity=this;
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        },1800);


        Intent intent = new Intent(this.getApplicationContext(), SocketService.class);
        this.startService(intent);


        Intent ITn=getIntent();
        String scheme = intent.getScheme();
        Uri getData_fromWeb=ITn.getData();
        if(getData_fromWeb != null) {
            String host = getData_fromWeb.getHost();
            String dataString = ITn.getDataString();
            String id = getData_fromWeb.getQueryParameter("d");
            String path = getData_fromWeb.getPath();
            String path1 = getData_fromWeb.getEncodedPath();
            String queryString = getData_fromWeb.getQuery();
            Log.e("URL","host:" + host+"dataString:" + dataString+"id:" + id+"path:" + path+"path1:" + path1+"queryString:" + queryString);
            Log.e("URL",queryString);
        }
        else
            Log.e("URL" , "URL is null");

    }



}