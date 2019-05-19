package com.example.yourassistant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class OptionAbout extends AppCompatActivity {
    TextView contact, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        contact = findViewById(R.id.contact);
        version = findViewById(R.id.version);
        setContentView(R.layout.option_about);
    }
}
