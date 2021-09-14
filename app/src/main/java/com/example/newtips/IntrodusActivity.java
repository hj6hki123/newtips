package com.example.newtips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
        chirtext.animate().translationX(2000).setDuration(100).setStartDelay(1800);

        introdusActivity=this;
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        },2000); */

        String ip = "10.147.17.177".trim();
        String port = "9998".trim();

        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            Toast.makeText(this , "ip和端口号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this.getApplicationContext(), SocketService.class);
        intent.putExtra(Constants.INTENT_IP, ip);
        intent.putExtra(Constants.INTENT_PORT, port);
        this.startService(intent);

    }

}