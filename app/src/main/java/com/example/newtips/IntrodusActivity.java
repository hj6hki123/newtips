package com.example.newtips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        String ip = getString(R.string.TCP_IP).trim();
        String port = getString(R.string.TCP_PORT).trim();
        Log.e("printIP&port",ip+port);
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            Toast.makeText(this , "ip和端口號不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this.getApplicationContext(), SocketService.class);
        intent.putExtra(Constants.INTENT_IP, ip);
        intent.putExtra(Constants.INTENT_PORT, port);
        this.startService(intent);

    }



}