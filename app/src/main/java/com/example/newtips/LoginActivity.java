package com.example.newtips;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    final Uri uri_register=Uri.parse("https://mnya.tw/cc/word/1477.html");//註冊網址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                        GlobalData.FSM = "Longin";
                    else
                        Toast.makeText(this, "須為連線狀態才能登入", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "帳號密碼不能為空", Toast.LENGTH_SHORT).show();
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
}
