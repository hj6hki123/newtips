package com.example.newtips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //< get elements >
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        //</ get elements >


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("感測器");
                                break;
                            case 1:
                                tab.setText("電器");
                                break;
                            default:
                                tab.setText("感光");
                                break;
                        }

                    }
                }).attach();
    }

    private static Boolean isExit = false;
    private static Boolean hasTask = false;
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
                Toast.makeText(this, "再按一次Back退出APP", Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    timerExit.schedule(task, 2000);
                }
            }
            else
                {
                    finish(); // 離開程式
                    System.exit(0);
                }


            }
            return  false;

    }
}
