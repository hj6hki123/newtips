package com.example.newtips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class IntrodusActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    ImageView chirtext;

    public static IntrodusActivity introdusActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }


                        if(deepLink != null)
                        {
                            String queryString = deepLink.getQuery();
                            String[] accountdata=queryString.split("&");
                            accountdata[1]=accountdata[1].replace("account=","");
                            accountdata[2]=accountdata[2].replace("password=","");
                            Log.e("URL",accountdata[1]);
                            Log.e("URL",accountdata[2]);
                            SharedPreferences pref =getSharedPreferences("login",MODE_MULTI_PROCESS);
                            pref.edit()
                                .putString("Duser", accountdata[1])
                                .putString("Dpassword", accountdata[2])
                                .apply();
                        }
                        else
                        {

                            Log.w("URLe", "deepLink=null");
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("URL", "getDynamicLink:onFailure", e);
                    }
                });


//        Intent ITn=getIntent();
//        String scheme = ITn.getScheme();
//        Uri getData_fromWeb=ITn.getData();
//        if(getData_fromWeb != null) {
//            String host = getData_fromWeb.getHost();
//            String dataString = ITn.getDataString();
//            String id = getData_fromWeb.getQueryParameter("d");
//            String path = getData_fromWeb.getPath();
//            String path1 = getData_fromWeb.getEncodedPath();
//            String queryString = getData_fromWeb.getQuery();
//            Log.e("URL","host:" + host+"dataString:" + dataString+"id:" + id+"path:" + path+"path1:" + path1+"queryString:" + queryString);
//            Log.e("URL",queryString);

//            String[] accountdata=queryString.split("&");
//            Log.e("URL",accountdata[0]);
//            Log.e("URL",accountdata[1]);
//            SharedPreferences pref =getSharedPreferences("login",MODE_MULTI_PROCESS);
//            pref.edit()
//                    .putString("Duser", accountdata[0])
//                    .putString("Dpassword", accountdata[1])
//                    .apply();
//
//        }
//        else
//            Log.e("URL" , "URL is null");



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



    }



}