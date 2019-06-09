package com.example.yourassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.RecognizerIntent.EXTRA_PREFER_OFFLINE;

public class VoiceControl extends AppCompatActivity {
    TextView textView, appendWelcome;
    ImageButton micButton;
    int SPEECH_RECOGNITION_CODE = 199;
    String inputCommand="";
    Login loginGetter = new Login();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.voice_control);
        final String lang_vietnamese="vi-VN";

        // mapping
        textView = findViewById(R.id.mic);
        micButton = findViewById(R.id.micCommand);
        appendWelcome = findViewById(R.id.wc);

        // processing;
        appendWelcome.append(", " + loginGetter.getUserName());

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang_vietnamese);
                intent.putExtra(EXTRA_PREFER_OFFLINE, true);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
                } else {
                    Toast.makeText(VoiceControl.this, "Your device does not support speech input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPEECH_RECOGNITION_CODE && resultCode == RESULT_OK && data != null){
            ArrayList<String> resT = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputCommand = resT.get(0);
            String processedInput = "", temp = "";
            if (inputCommand.contains("tắt") || inputCommand.contains("Tắt")) {
                processedInput = "tắt điều hòa";
            } else if (inputCommand.contains("bật") || inputCommand.contains("Bật")) {
                temp = extractNumber(inputCommand);
                if (temp == null || temp.isEmpty()) {
                    processedInput = "bật điều hòa";
                } else {
                    processedInput = "bật điều hòa ở mức " + temp + " độ C";
                }
            }
            confirmCommand(processedInput);
        }
    }


    // dialog confirming user command
    private void confirmCommand(String inp)
    {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(VoiceControl.this, R.style.MyDialogTheme);
        confirmDialog.setTitle("Yêu cầu xác nhận");
        confirmDialog.setIcon(R.mipmap.ic_launcher);
        confirmDialog.setMessage("Có phải bạn ý bạn là: " + inp + "?");
        confirmDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.opt0:
                Intent login_page = new Intent(VoiceControl.this, Login.class);
                startActivity(login_page);
                finish();
                break;
            case R.id.opt1:
                Intent option_mqtt = new Intent(VoiceControl.this, OptionMqttConnection.class);
                startActivity(option_mqtt);
                break;
            case R.id.opt2:
                Intent option_about = new Intent(VoiceControl.this, OptionAbout.class);
                startActivity(option_about);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }
}
