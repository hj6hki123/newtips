package com.example.newtips.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newtips.GlobalData;
import com.example.newtips.R;
import com.example.newtips.adapter.ViewPagerAdapter;
import com.example.newtips.common.DepthPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public  static ViewPager2 viewPager2;
    SharedPreferences pref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref =getSharedPreferences("viewmode",MODE_MULTI_PROCESS);
        if(pref.getBoolean("darkmode",false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        //< get elements >
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.view_pager);
        //</ get elements >


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.tab1_name);
                                break;
                            case 1:
                                tab.setText(R.string.tab2_name);
                                break;
                            default:
                                tab.setText(R.string.tab3_name);
                                break;
                        }
                    }

                }
                ).attach();
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setPageTransformer(new DepthPageTransformer());

        //等待畫面創建完畢後初始化資料
        Context meContext=this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //todo:爛程式碼，易造成記憶體引用錯亂，欲改，暫放
                Toast.makeText(meContext,"正在初始化",Toast.LENGTH_SHORT).show();
                GlobalData.FSM="Inituserdata";
                Log.e("haha","work1");
            }
        },1000);

    }
    //返回鍵延遲
    private  Boolean isExit = false;
    private  Boolean hasTask = false;
    Timer timerExit = new Timer();
    TimerTask task = new TimerTask()
    {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isExit == false )
            {
                isExit = true; //記錄下一次要退出
                Toast.makeText(this, "再按一次Back登出", Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    timerExit.schedule(task, 2000);
                }
            }
            else
            {
                GlobalData.FSM="IDLE";
                finish(); // 離開程式
            }
        }
        return  false;

    }

    @Override
    protected void attachBaseContext(Context newBase) {  //使字體不受系統所影響
        super.attachBaseContext(newBase);
        Configuration config = new Configuration(newBase.getResources().getConfiguration());
        config.fontScale = 1.0f;
        applyOverrideConfiguration(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.item1).setChecked(pref.getBoolean("darkmode", false));
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    pref.edit().putBoolean("darkmode",false).apply();
                }else {
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    pref.edit().putBoolean("darkmode",true).apply();
                }
                recreate();


                break;
            case R.id.item2:
                final Uri uri=Uri.parse("http://120.114.68.132");//網址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);//開啟註冊網址
                break;
            case R.id.item3:
                break;
            case R.id.item4:
                Toast.makeText(this,"恢復資料中",Toast.LENGTH_SHORT).show();
                GlobalData.FSM="Inituserdata";
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
