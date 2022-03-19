package com.example.newtips.activitys;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newtips.GlobalData;
import com.example.newtips.R;
import com.example.newtips.SocketService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {
    private ServiceConnection sc;
    public SocketService socketService;

    final Uri uri_register=Uri.parse("http://120.114.68.132/sign-up/");//註冊網址
    MyBroadcast myBroadcast = new MyBroadcast();
    TextView textView_hint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // bindSocketService();
        setContentView(R.layout.activity_login);

        //註冊廣播器
        IntentFilter intentFilter = new IntentFilter(SocketService.RECEIVE_SERVICE_ACTION);
        registerReceiver(myBroadcast, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter(SocketService.LoginAccess_ACTION);
        registerReceiver(myBroadcast, intentFilter2);



        textView_hint=findViewById(R.id.text_socket_hint);

        //關閉歡迎頁面
        if(IntrodusActivity.introdusActivity!=null){
            IntrodusActivity.introdusActivity.finish();
        }

        SharedPreferences pref =getSharedPreferences("login",Context.MODE_PRIVATE);


        EditText editText_user=findViewById(R.id.editextuser);
        EditText editText_password=findViewById(R.id.editTextPassword);
        editText_user.setText(pref.getString("Duser",""));
        editText_password.setText(pref.getString("Dpassword",""));
        TextInputLayout textInputLayout_acc=findViewById(R.id.textInputLayout_acc);
        TextInputLayout  textInputLayout_password=findViewById(R.id.textInputLayout_password);

        Button button_login=findViewById(R.id.button_login);
        button_login.setOnClickListener((V)->
            {
                if(editText_user.getText().toString().length()>0 && editText_password.getText().toString().length()>0) {
                    pref.edit()
                            .putString("Duser", editText_user.getText().toString())
                            .putString("Dpassword", editText_password.getText().toString())
                            .apply();
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
                else if(editText_user.getText().toString().length()==0)
                    textInputLayout_acc.setError("帳號不可空白");
                else if(editText_password.getText().toString().length()==0)
                {
                    textInputLayout_password.setError("密碼不可空白");
                }


            }
        );



            editText_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout_acc.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout_password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





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
        //Intent intent = new Intent(getApplicationContext(), SocketService.class);
        //stopService(intent);
    }

    private void bindSocketService()
    {

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                SocketService.SocketBinder binder = (SocketService.SocketBinder) service;
                socketService = binder.getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(this, SocketService.class);
        this.bindService(intent, sc, BIND_AUTO_CREATE);

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
                case SocketService.LoginAccess_ACTION:
                    boolean la = intent.getBooleanExtra(SocketService.Loginvoke,false);
                    if(la)
                    {

                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        GlobalData.FSM="Datatransport";
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
