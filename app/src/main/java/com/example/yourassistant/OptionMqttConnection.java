package com.example.yourassistant;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OptionMqttConnection extends AppCompatActivity {

    TextView edt_sub0, edt_sub1, edt_sub2, edt_sub3, edt_pub, edt_uri;
    Button btn_updateTopics;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.option_mqtt_connection);

        // mapping
        edt_uri = (TextView) findViewById(R.id.edt_server);
        edt_pub = (TextView) findViewById(R.id.edt_topic_pub);
        edt_sub0 = (TextView) findViewById(R.id.edt_topic_sub0);
        edt_sub1 = (TextView) findViewById(R.id.edt_topic_sub1);
        edt_sub2 = (TextView) findViewById(R.id.edt_topic_sub2);
        edt_sub3 = (TextView) findViewById(R.id.edt_topic_sub3);
        btn_updateTopics = (Button) findViewById(R.id.btn_update);

        // set default
        sharedPreferences = this.getSharedPreferences("topicConfig", MODE_PRIVATE);

        // shared preferences
        if (sharedPreferences != null) {
            String tmp_pub = sharedPreferences.getString("publish_topic", VoiceControl.publishTopic);
            String tmp_sub0 = sharedPreferences.getString("subscribe_topic0", VoiceControl.subscriptionTopic0);
            String tmp_sub1 = sharedPreferences.getString("subscribe_topic1", VoiceControl.subscriptionTopic1);
            String tmp_sub2 = sharedPreferences.getString("subscribe_topic2", VoiceControl.subscriptionTopic2);
            String tmp_sub3 = sharedPreferences.getString("subscribe_topic3", VoiceControl.subscriptionTopic3);
            String tmp_uri = sharedPreferences.getString("server_uri", VoiceControl.MQTTHOST);

            edt_pub.setText(tmp_pub);
            edt_sub0.setText(tmp_sub0);
            edt_sub1.setText(tmp_sub1);
            edt_sub2.setText(tmp_sub2);
            edt_sub3.setText(tmp_sub3);
            edt_uri.setText(tmp_uri);
        } else {
            edt_pub.setText(VoiceControl.publishTopic);
            edt_sub0.setText(VoiceControl.subscriptionTopic0);
            edt_sub1.setText(VoiceControl.subscriptionTopic1);
            edt_sub2.setText(VoiceControl.subscriptionTopic2);
            edt_sub3.setText(VoiceControl.subscriptionTopic3);
            edt_uri.setText(VoiceControl.MQTTHOST);
        }

        btn_updateTopics.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor edit_shared_pref = sharedPreferences.edit();
            @Override
            public void onClick(View v) {
                String publish_topic = edt_pub.getText().toString(),
                        sub_topic0 = edt_sub0.getText().toString(),
                        sub_topic1 = edt_sub1.getText().toString(),
                        sub_topic2 = edt_sub2.getText().toString(),
                        sub_topic3= edt_sub3.getText().toString(),
                        uri = edt_uri.getText().toString();
                
                // save 
                edit_shared_pref.putString("publish_topic", publish_topic);
                edit_shared_pref.putString("subscribe_topic0", sub_topic0);
                edit_shared_pref.putString("subscribe_topic1", sub_topic1);
                edit_shared_pref.putString("subscribe_topic2", sub_topic2);
                edit_shared_pref.putString("subscribe_topic3", sub_topic3);
                edit_shared_pref.putString("server_uri", uri);
                edit_shared_pref.commit();
                
                // set text
                edt_pub.setText(publish_topic);
                edt_sub0.setText(sub_topic0);
                edt_sub1.setText(sub_topic1);
                edt_sub2.setText(sub_topic2);
                edt_sub3.setText(sub_topic3);
                edt_uri.setText(uri);
                Toast.makeText(OptionMqttConnection.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            }
        });
    }
}