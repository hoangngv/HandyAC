package com.example.yourassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceControl extends AppCompatActivity {
    TextView textView;
    TextView inpTextView;
    ImageButton micButton;
    int SPEECH_RECOGNITION_CODE = 199;
    String inputCommand="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.voice_control);
        final String VN_lang="vi-VN";
        textView = findViewById(R.id.txtView);
        micButton = findViewById(R.id.imageButton);
        inpTextView = findViewById(R.id.inputTextView);
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, VN_lang);
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
                    //System.out.println(inputCommand);
                } else {
                    Toast.makeText(VoiceControl.this, "Your device do not support speech input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SPEECH_RECOGNITION_CODE && resultCode==RESULT_OK && data!=null){
            ArrayList<String> resT = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //textView.setText(resT.get(0));
            inputCommand=resT.get(0);
            confirmCommand(inputCommand);
            //inpTextView.setText("Có phải ý bạn là: " + inputCommand + "?");
        }
    }

    private void confirmCommand(String inp)
    {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
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
}
