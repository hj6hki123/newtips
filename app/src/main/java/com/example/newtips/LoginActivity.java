package com.example.newtips;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    final Uri uri_register=Uri.parse("http://120.114.68.132/sign-up/");//註冊網址
    MyBroadcast myBroadcast = new MyBroadcast();
    TextView textView_hint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //註冊廣播器
        IntentFilter intentFilter = new IntentFilter(SocketService.RECEIVE_SERVICE_ACTION);
        registerReceiver(myBroadcast, intentFilter);


        textView_hint=findViewById(R.id.text_socket_hint);

        //關閉歡迎頁面
        if(IntrodusActivity.introdusActivity!=null){
            IntrodusActivity.introdusActivity.finish();
        }

        SharedPreferences pref =getPreferences(Context.MODE_PRIVATE);


        EditText editText_user=findViewById(R.id.editextuser);
        EditText editText_password=findViewById(R.id.editTextPassword);
        editText_user.setText(pref.getString("Duser",""));
        editText_password.setText(pref.getString("Dpassword",""));

        Button button_login=findViewById(R.id.button_login);
        button_login.setOnClickListener((V)->
            {
                if(editText_user.getText().toString().length()>0 && editText_password.getText().toString().length()>0) {
                    pref.edit()
                            .putString("Duser", editText_user.getText().toString())
                            .putString("Dpassword", editText_password.getText().toString())
                            .commit();
                    GlobalData.Login_user=editText_user.getText().toString();
                    GlobalData.Login_password=editText_password.getText().toString();


                    if (GlobalData.connectstate && GlobalData.FSM.equals("IDLE"))
                    {
                        textView_hint.setText("");
                        GlobalData.FSM = "Longin";
                    }
                    else
                        textView_hint.setText("須為連線狀態才可登入");
                }
                else
                    textView_hint.setText("帳號密碼不能為空");
            }
        );

        TextView textView_register=findViewById(R.id.textView_register);
        textView_register.setOnClickListener((V)->
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri_register);
                startActivity(intent);//開啟註冊網址
            });




    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), SocketService.class);
        stopService(intent);
    }


    private class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            assert mAction != null;
            switch (mAction) {
                /**接收來自UDP回傳之訊息*/
                case SocketService.RECEIVE_SERVICE_ACTION:
                    String msg = intent.getStringExtra(SocketService.RECEIVE_SERVICE_STRING);
                    String msg_problem = intent.getStringExtra(SocketService.RECEIVE_SERVICE_PROBLEM);
                    if(msg.equals("false"))
                    {
                        textView_hint.setText(msg_problem+"");
                    }

                    break;
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) { //使字體不受系統所影響
        super.attachBaseContext(newBase);
        Configuration config = new Configuration(newBase.getResources().getConfiguration());
        config.fontScale = 1.0f;
        applyOverrideConfiguration(config);
    }
}
