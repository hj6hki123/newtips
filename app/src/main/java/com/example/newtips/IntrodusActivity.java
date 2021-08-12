package com.example.newtips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class IntrodusActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    ImageView chirtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introdus);
        lottieAnimationView=findViewById(R.id.lottie);
        chirtext=findViewById(R.id.imageView);


        chirtext.animate().translationX(-2000).setDuration(500).setStartDelay(1700);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();//銷毀目前activity

            }
        },2300);
    }

}