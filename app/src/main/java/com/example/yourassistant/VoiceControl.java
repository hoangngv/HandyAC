package com.example.yourassistant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static android.speech.RecognizerIntent.EXTRA_MAX_RESULTS;
import static android.speech.RecognizerIntent.EXTRA_PREFER_OFFLINE;

public class VoiceControl extends AppCompatActivity {
    //static String MQTTHOST = "tcp://iot.eclipse.org";
    static String MQTTHOST = "tcp://k61iotlab.duckdns.org:1883";
    static String USERNAME = "nvhoang";
    static String PASSWORD = "12345678";
    static String publishTopic  = "air_conditioner/command/";
    static String subscriptionTopic0 = "air_conditioner/data/";
    static String subscriptionTopic1 = "temperature_sensor/data/";
    static String subscriptionTopic2 = "humidity_sensor/data/";
    static String subscriptionTopic3 = "human_detection_sensor/data/";

    MqttAndroidClient client;

    TextView textView, appendWelcome, currentTemperature, roomTemperature, roomHumidity, humanDetection;
    ImageButton micButton;
    ImageView airConditionerIcon;
    int SPEECH_RECOGNITION_CODE = 199;
    String inputCommand = "";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.voice_control);
        final String lang_vietnamese = "vi-VN";

        // mapping
        textView = findViewById(R.id.mic);
        micButton = findViewById(R.id.micCommand);
        airConditionerIcon = findViewById(R.id.iconAC);
        appendWelcome = findViewById(R.id.wc);
        currentTemperature = findViewById(R.id.ac_temp_value);
        roomTemperature = findViewById(R.id.real_temp_value);
        roomHumidity = findViewById(R.id.real_humidity_value);
        humanDetection = findViewById(R.id.human_value);


        // processing;
        appendWelcome.append(", " + USERNAME);

        // shared preferences
        sharedPreferences = this.getSharedPreferences("roomData", MODE_PRIVATE);
        if (sharedPreferences != null) {
            String currentValue = sharedPreferences.getString("current_temperature", "null");
            String roomValue = sharedPreferences.getString("room_temperature", "null");
            String humidValue = sharedPreferences.getString("room_humidity", "null");
            String humanDetect = sharedPreferences.getString("human_detection", "null");

            currentTemperature.setText(currentValue);
            roomTemperature.setText(roomValue);
            roomHumidity.setText(humidValue);
            humanDetection.setText(humanDetect);
        } else {
            currentTemperature.setText("null");
            roomTemperature.setText("null");
            roomHumidity.setText("null");
            humanDetection.setText("null");
        }

        // MQTT connection
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(VoiceControl.this, "Kết nối MQTT thành công", Toast.LENGTH_SHORT).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(VoiceControl.this, "Kết nối MQTT thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        // set callback
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(VoiceControl.this, "Đã cập nhật dữ liệu", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor edt_shared_preferences = sharedPreferences.edit();

                // process 3 topics
                if (topic.equals(subscriptionTopic0)) {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String current_ac_temp = "null";
                    current_ac_temp = jsonObject.getString("current_temperature");
                    if (current_ac_temp.equals("OFF")) {
                        current_ac_temp = "Tắt";
                    } else {
                        current_ac_temp = current_ac_temp + "°C";
                    }
                    currentTemperature.setText(current_ac_temp);
                    edt_shared_preferences.putString("current_temperature", current_ac_temp);
                } else if (topic.equals(subscriptionTopic1)) {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String real_room_temp = "null";
                    real_room_temp = jsonObject.getString("room_temperature") + "°C";
                    roomTemperature.setText(real_room_temp);
                    edt_shared_preferences.putString("room_temperature", real_room_temp);
                } else if (topic.equals(subscriptionTopic2)) {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String real_room_humidity = "null";
                    real_room_humidity = jsonObject.getString("room_humidity") + "%";
                    roomHumidity.setText(real_room_humidity);
                    edt_shared_preferences.putString("room_humidity", real_room_humidity);
                } else if (topic.equals(subscriptionTopic3)) {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String human_state = "null";
                    if (jsonObject.getString("human_detection").equals("true")) {
                        human_state = "Có";
                    } else {
                        human_state = "Không";
                    }
                    humanDetection.setText(human_state);
                    edt_shared_preferences.putString("human_detection", human_state);
                }

                // save data
                edt_shared_preferences.commit();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        // when micro is triggered
        micButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang_vietnamese);
                intent.putExtra(EXTRA_MAX_RESULTS, 2);
                //intent.putExtra(EXTRA_PREFER_OFFLINE, true);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
                } else {
                    Toast.makeText(VoiceControl.this, "Thiết bị không tương thích", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // when AC icon is clicked
        airConditionerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInformationDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // process input
        if(requestCode == SPEECH_RECOGNITION_CODE && resultCode == RESULT_OK && data != null){
            ArrayList<String> resT = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputCommand = resT.get(0);

            if (inputCommand.contains("bật") || inputCommand.contains("Bật") || inputCommand.contains("tắt") || inputCommand.contains("Tắt")){
                confirmCommand(inputCommand);
            } else {
                Toast.makeText(VoiceControl.this, "Không rõ yêu cầu. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // confirm user command
    private void confirmCommand(final String inp)
    {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(VoiceControl.this, R.style.MyDialogTheme);

        // use JSONObject to send command
        final JSONObject command = new JSONObject();
        try {
            command.put("0xB3", "0x31"); // default: power off
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String processedInput = "";
        final String temp = extractNumber(inp);
        if (inp.contains("tắt") || inp.contains("Tắt")) {
            processedInput = "tắt điều hòa";
            try {
                command.remove("0xB3");
                command.put("0xB3", "0x31");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (inp.contains("bật") || inp.contains("Bật")) {
            String value = "";
            if (temp == null || temp.isEmpty()) {
                processedInput = "bật điều hòa với mức nhiệt mặc định 28°C";
                try {
                    command.remove("0xB3");
                    command.put("0xB3", "0x1C"); // default: 28oC
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                processedInput = "bật điều hòa ở mức " + temp + "°C";
                value = intToHexString(Integer.parseInt(temp)); // desired temperature in HEX string
                try {
                    command.remove("0xB3");
                    command.put("0xB3", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        sharedPreferences = this.getSharedPreferences("roomData", MODE_PRIVATE);
        final SharedPreferences.Editor edt_shared_pref = sharedPreferences.edit();

        confirmDialog.setTitle("Yêu cầu xác nhận");
        confirmDialog.setIcon(R.mipmap.ic_launcher);
        confirmDialog.setMessage("Có phải bạn ý bạn là: " + processedInput + "?");
        confirmDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                publishMessage(publishTopic, command.toString());
                if (inp.contains("tắt")) {
                    currentTemperature.setText("Tắt");
                    edt_shared_pref.putString("current_temperature", "Tắt");
                } else if (temp == null || temp.isEmpty()) {
                    currentTemperature.setText("28°C");
                    edt_shared_pref.putString("current_temperature", "28°C");
                } else {
                    currentTemperature.setText(temp + "°C");
                    edt_shared_pref.putString("current_temperature", temp + "°C");
                }
                edt_shared_pref.commit();
                Toast.makeText(VoiceControl.this, "Yêu cầu của bạn đã được thực hiện", Toast.LENGTH_SHORT).show();
            }
        });

        confirmDialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VoiceControl.this, "Đã hủy yêu cầu", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(VoiceControl.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
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

    public void publishMessage(String topic, String msg) {
        try {
            client.publish(topic, msg.getBytes(), 0, false);
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void setSubscription() {
        try {
            client.subscribe(subscriptionTopic0, 0);
            client.subscribe(subscriptionTopic1, 0);
            client.subscribe(subscriptionTopic2, 0);
            client.subscribe(subscriptionTopic3, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static String intToHexString(int n){
        return "0x" + Integer.toHexString(n);
    }

    public void openInformationDialog() {
        InformationDialog infoDialog = new InformationDialog();
        infoDialog.show(getSupportFragmentManager(), "MAC dialog");
    }
}