package com.example.yourassistant;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText edt_user, edt_password;
    Button btn_login;
    boolean success=false;
    private BroadcastReceiver myReceiver=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myReceiver = new MyReceiver();
        broadcastIntent();
        executeLogin();
    }

    private void executeLogin()
    {
        edt_user=(EditText) findViewById(R.id.editusername);
        edt_password=(EditText) findViewById(R.id.editpassword);
        btn_login=(Button) findViewById(R.id.loginbutton);

        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                String username = edt_user.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                if (username.equals("nvhoang191") && password.equals("12345678"))
                {
                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    success=true;
                    Intent mainScreen = new Intent(Login.this, VoiceControl.class);
                    startActivity(mainScreen);
                } else {
                    Toast.makeText(Login.this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                    success=false;
                }
            }
        });
    }

    public void broadcastIntent() {
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
