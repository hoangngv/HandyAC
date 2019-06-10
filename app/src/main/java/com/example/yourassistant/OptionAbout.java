package com.example.yourassistant;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OptionAbout extends AppCompatActivity {
    TextView contact, version;
    Button btn_emailMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.option_about);

        contact = findViewById(R.id.contact);
        version = findViewById(R.id.version);
        btn_emailMe = (Button) findViewById(R.id.btn_email);
        btn_emailMe.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    protected void sendEmail() {
        String[] TO = {"nvhoang191@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[HandyAC] Phản hồi ứng dụng");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi phản hồi..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(OptionAbout.this, "Không tìm thấy email client.", Toast.LENGTH_SHORT).show();
        }
    }
}
