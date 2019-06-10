package com.example.yourassistant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OptionMqttConnection extends AppCompatActivity {

    TextView topicToSubscribe, topicToPublish, serverURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.option_mqtt_connection);

        // mapping
        topicToPublish = (TextView) findViewById(R.id.edt_topic_pub);
        topicToSubscribe = (TextView) findViewById(R.id.edt_topic_sub);
        serverURI = (TextView) findViewById(R.id.edt_server);

        // set text
        topicToPublish.setText(VoiceControl.publishTopic);
        topicToSubscribe.setText(VoiceControl.subscriptionTopic);
        serverURI.setText(VoiceControl.MQTTHOST);

    }
}