// MainActivity.java
package com.example.test;

import android.os.CountDownTimer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import android.os.CountDownTimer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;
    private static Device user1 = new Device("kaitoud","aio_seUr87ZhGBlQcox7zdajzMlXNrgm","Thiết bị 1");
    private static Device user2 = new Device("","","Thiết bị 2");
    private static Device [] users = new Device[]{user1,user2};
    private int usermode = 1;

    private  TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private  long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    private static final String USERNAME = "kaitoud";
    private static final String IO_KEY = "aio_seUr87ZhGBlQcox7zdajzMlXNrgm";

    private Switch pumpButton,auto_watering;
    private ExtendedFloatingActionButton floatingActionButton;
    private TextView txtTemp, txtHumidity, txtLastWater,txtLight;
    private EditText edttxtTemp1,edttxtTemp2,edttxtHumid1,edttxtHumid2;
    private RelativeLayout tempLayout, humidLayout,lightLayout;
    private Double tempThreshold1,tempThreshold2;
    private Integer humidThreshold1,humidThreshold2;
    private boolean coldFlag, hotFlag, dryFlag, wetFlag, pumpFlag, water_auto;
    private BottomNavigationView navigation;
    private SharedPreferences sharedPreferences;
    private MqttAndroidClient client;
    private DataBaseHelper dataBaseHelper;
    private ActionBar toolbar;
    private Button update_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById((R.id.button_reset));
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetTimer();
            }
        });


        // Perform logout of current user and close the client

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        sharedPreferences = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);

        //Temp and Humidity Threshold
        tempThreshold1 = 15.0;
        tempThreshold2 = 40.0;
        humidThreshold1 = 20;
        humidThreshold2 = 80;
//        tempThreshold1 = sharedPreferences.getFloat("temp1", 20.5);
//        tempThreshold2 = sharedPreferences.getFloat("temp2", 30.5);
//        humidThreshold1 = sharedPreferences.getInt("humid1", 20);
//        humidThreshold2 = sharedPreferences.getInt("humid2", 60);
        water_auto = sharedPreferences.getBoolean("water_auto", true);

        coldFlag = sharedPreferences.getBoolean("coldFlag", false);
        hotFlag = sharedPreferences.getBoolean("hotFlag", false);
        dryFlag = sharedPreferences.getBoolean("dryFlag", false);
        wetFlag = sharedPreferences.getBoolean("wetFlag", false);
        pumpFlag = sharedPreferences.getBoolean("pumpFlag", false);


        //Temp and Humidity Text and Last Watering time
        txtTemp = findViewById(R.id.temp_information);
        txtHumidity = findViewById(R.id.humid_information);
        txtLight = findViewById(R.id.light_information);
        txtLastWater = findViewById(R.id.last_watering_information);

        edttxtTemp1 = findViewById(R.id.temperature_input1);
        edttxtTemp2 = findViewById(R.id.temperature_input2);
        edttxtHumid1 = findViewById(R.id.humidity_input1);
        edttxtHumid2 = findViewById(R.id.humidity_input2);

        //View & Icon
        tempLayout = findViewById(R.id.temperature_board);
        humidLayout = findViewById(R.id.humidity_board);
        lightLayout = findViewById(R.id.light_board);
//        tempImg = findViewById(R.id.tempImg);
//        humidImg = findViewById(R.id.humidImg);

        //Create Notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "SmartFarmChannel";
            String description = "Channel for my SmartFarm app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("My Notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //MQTT Connect
        mqttConnect();

        initialBound();
        // Call API for current temp & humidity & last watering
        apiCall();



        // main fragment
        toolbar = getSupportActionBar();
        auto_watering = (Switch) findViewById(R.id.auto_watering_switch);
        auto_watering.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (auto_watering.isChecked()){
                            turnAutoOn();
                        }
                        else {
                            turnAutoOff();
                        }
                    }
                });
        pumpButton = (Switch) findViewById(R.id.watering_button);
        pumpButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (pumpButton.isChecked()){
                            pumpWater();
                        }
                        else {
                            stopPumpWater();
                        }
                    }
                });

        update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String temp1 = edttxtTemp1.getText().toString();
                String temp2 = edttxtTemp2.getText().toString();
                String humid1 = edttxtHumid1.getText().toString();
                String humid2 = edttxtHumid2.getText().toString();
                System.out.println(temp1 + '\n' + temp2 + '\n' + humid1 +'\n' + humid2);
                updateThreshold(temp1,temp2,humid1,humid2);
            }
        });

        navigation= (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.navigation_home:
//                        break;
//                    case R.id.navigation_chart:
//                        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
//                        startActivity(intent);
//                        break;
////                    case R.id.history:
////                        Intent intent1 = new Intent(MainActivity.this, HistoryActivity.class);
////                        startActivity(intent1);
////                        break;
//                    case R.id.navigation_setting:
//                        Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
//                        startActivity(intent2);
//                        break;
//                }
//                return false;
//            }
//        });

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Trang chủ");
        loadFragment(new HomeFragment());

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                apiCall();
                System.out.println("debug: API");
            }
        },0,5000);
    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("start");
        mButtonReset.setVisibility((View.VISIBLE));
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Trang chủ");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_chart:
                    toolbar.setTitle("Dữ liệu");
                    fragment = new ChartFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_alarm:
                    toolbar.setTitle("Báo động");
                    fragment = new AlarmFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_setting:
                    toolbar.setTitle("Thiết lập");
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void mqttConnect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setUserName(USERNAME);
        options.setPassword(IO_KEY.toCharArray());

        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(getApplicationContext(), "tcp://io.adafruit.com:1883",
                        clientId, Ack.AUTO_ACK);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("mqtt", "Connection lost");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("mqtt", topic + " " + message);
                if(topic.equals("kaitoud/feeds/temperature/json"))
                {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    Double temp = jsonObject.getDouble("last_value");

                    Temp tempObj = new Temp(-1, temp);
                    boolean success = dataBaseHelper.addOne(tempObj);
                    Log.d("mqtt", "Add Temp Success = " + success);

                }
                else if(topic.equals("kaitoud/feeds/humidity/json"))
                {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    Integer humid = jsonObject.getInt("last_value");

                    Humid humidObj = new Humid(-1, humid);
                    boolean success = dataBaseHelper.addOne(humidObj);
                    Log.d("mqtt", "Add Humid Success = " + success);
                }
                else if(topic.equals("kaitoud/feeds/button/json"))
                {
                    Log.d("mqtt", "Pump" );
                    JSONObject jsonObject = new JSONObject(message.toString());
                    Integer pump = jsonObject.getInt("last_value");

                    if(pump == 1)
                    {
                        addNewHisory();
                    }
                }
                else if (topic.equals("kaitoud/feeds/light/json"))
                {
                    Log.d("mqtt","Light");
                    JSONObject jsonObject = new JSONObject(message.toString());
                    Double light = jsonObject.getDouble("last_value");

                    Light lightObj = new Light(-1, light);
                    boolean success = dataBaseHelper.addOne(lightObj);
                    Log.d("mqtt", "Add Light Success = " + success);
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("mqtt", "Delivery Complete");
            }
        });


        IMqttToken token = client.connect(options);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // We are connected
                Log.d("mqtt", "Connect MQTT on Success");
                subscribeTemp();
                subscribeHumid();
                subscribePump();
                subscribeLight();
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.d("mqtt Error", asyncActionToken.toString());
                Log.d("mqtt Error", exception.toString());
            }
        });
    }


    private void addNewHisory() {
        String url = "https://io.adafruit.com/api/v2/kaitoud/feeds";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        Double temp = -99.0;
                        Integer humidity = -1;
                        try {
                            JSONObject tempInfo = response.getJSONObject(5);
                            temp = tempInfo.getDouble("last_value");

                            JSONObject humidInfo = response.getJSONObject(1);
                            humidity = humidInfo.getInt("last_value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LocalDateTime localDateTime = LocalDateTime.now();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");

                        History history = new History(-1, dateTimeFormatter1.format(localDateTime), dateTimeFormatter.format(localDateTime), temp, humidity);

                        boolean success = dataBaseHelper.addOne(history);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
    }

    private void subscribePump() {
        String topic = "kaitoud/feeds/button/json";
        int qos = 1;
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // The message was published
                Log.d("mqtt", "Subscribe to Pump success");
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Log.d("mqtt", "Subscribe to Pump failed");
            }
        });
    }

    private void subscribeHumid() {
        String topic = "kaitoud/feeds/humidity/json";
        int qos = 1;
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // The message was published
                Log.d("mqtt", "Subscribe to Humid success");
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Log.d("mqtt", "Subscribe to Humid failed");
            }
        });
    }

    private void subscribeLight() {
        String topic = "kaitoud/feeds/light/json";
        int qos = 1;
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // The message was published
                Log.d("mqtt", "Subscribe to Light success");
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Log.d("mqtt", "Subscribe to Light failed");
            }
        });
    }

    private void subscribeTemp() {
        String topic = "kaitoud/feeds/temperature/json";
        int qos = 1;
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // The message was published
                Log.d("mqtt", "Subscribe to Temp success");
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Log.d("mqtt", "Subscribe to Temp failed");
            }
        });
    }

    private void publishAPI(String topic, String payload) throws UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = payload.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        client.publish(topic, message);
    }

    private void initialBound(){
        String url = "https://io.adafruit.com/api/v2/kaitoud/feeds";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Integer humid_lower_bound = -1;
                        Integer humid_upper_bound = -1;
                        Double temp_lower_bound = -1.0;
                        Double temp_upper_bound = -1.0;
                        Integer watering_mode = -1;

                        try {
                            JSONObject humidLower = response.getJSONObject(4);
                            humid_lower_bound = humidLower.getInt("last_value");

                            JSONObject humidUpper = response.getJSONObject(5);
                            humid_upper_bound = humidUpper.getInt("last_value");

                            JSONObject tempLower = response.getJSONObject(6);
                            temp_lower_bound = tempLower.getDouble("last_value");

                            JSONObject tempUpper = response.getJSONObject(7);
                            temp_upper_bound = tempUpper.getDouble("last_value");

                            JSONObject watering = response.getJSONObject(9);
                            watering_mode = watering.getInt("last_value");

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        humidThreshold1 = humid_lower_bound;
                        humidThreshold2 = humid_upper_bound;
                        tempThreshold1 = temp_lower_bound;
                        tempThreshold2 = temp_upper_bound;

                        edttxtHumid1.setText(humidThreshold1.toString());
                        edttxtHumid2.setText(humidThreshold2.toString());
                        edttxtTemp1.setText(tempThreshold1.toString());
                        edttxtTemp2.setText(tempThreshold2.toString());

                        if (watering_mode == 1){
                            water_auto = true;
                            if (auto_watering.isChecked() == false){
                                auto_watering.setChecked(true);
                            }
                        }
                        else if (watering_mode == 0) {
                            water_auto = false;
                            if (auto_watering.isChecked()) {
                                auto_watering.setChecked(false);
                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
        }

    private void apiCall(){
        String url = "https://io.adafruit.com/api/v2/kaitoud/feeds";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Double temp = -99.0;
                        Integer humidity = -1;
                        Double light = -1.0;
//                        Integer button = -1;


                        Date date = new Date();
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        DateFormat localFormat = new SimpleDateFormat("dd MMMM HH:mm");
                        localFormat.setTimeZone(TimeZone.getDefault());

                        try {
                            JSONObject tempInfo = response.getJSONObject(0);
                            temp = tempInfo.getDouble("last_value");

                            JSONObject humidInfo = response.getJSONObject(1);
                            humidity = humidInfo.getInt("last_value");

                            JSONObject lightInfo = response.getJSONObject(2);
                            light = lightInfo.getDouble("last_value");

//                            JSONObject buttonInfo = response.getJSONObject(3);
//                            button = buttonInfo.getInt("last_value");

                            JSONObject lastWatering = response.getJSONObject(8);
                            date = utcFormat.parse(lastWatering.getString("last_value_at"));



                            Date time = new Date();

                            if (time.getTime() - date.getTime() > 10000) {
                                pumpFlag = false;
                            }
                            if (pumpFlag && water_auto == true){
                                stopPumpWater();
                                pumpButton.setChecked(false);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }

                        if (temp > tempThreshold2 | temp < tempThreshold1) {
                            tempLayout.setBackground(getDrawable(R.drawable.warning_board));
                            if (!hotFlag) {
                                hotFlag = true;
                            }
                            System.out.println("debug: " +pumpFlag +", " + water_auto);
                            if (!pumpFlag && water_auto){
                                pumpFlag = true;
                                pumpWater();
                                pumpButton.setChecked(true);
                            }
                        } else if (temp > tempThreshold1 && temp <= tempThreshold2) {
                            tempLayout.setBackground(getDrawable(R.drawable.action_board_outline));
                            if (hotFlag) {
                                hotFlag = false;
                            }
                            if (coldFlag) {
                                coldFlag = false;
                            }
                        } else {
                            if (temp != -99.0) {
                                tempLayout.setBackground(getDrawable(R.drawable.warning_board));
                                if (!coldFlag) {
                                    coldFlag = true;
                                }
                            }
                        }
                        if (humidity > humidThreshold2 | humidity < humidThreshold1) {
                            humidLayout.setBackground(getDrawable(R.drawable.warning_board));
                            if (!wetFlag) {
                                wetFlag = true;
                            }
                        } else if (humidity > humidThreshold1 && humidity <= humidThreshold2) {
                            humidLayout.setBackground(getDrawable(R.drawable.action_board_outline));
                            if (dryFlag) {
                                dryFlag = false;
                            }
                            if (wetFlag) {
                                wetFlag = false;
                            }
                        } else {
                            if (humidity != -99.0) {
                                humidLayout.setBackground(getDrawable(R.drawable.warning_board));
                                if (!dryFlag) {
                                    dryFlag = true;
                                }
                                if (!pumpFlag && water_auto) {
                                    pumpWater();
                                }
                            }
                        }

                        txtLastWater.setText(localFormat.format(date));
                        txtTemp.setText(temp.toString() + "\u00B0C");
                        txtHumidity.setText(humidity.toString() + "%");
                        txtLight.setText(light.toString() + " lux");



                    SharedPreferences.Editor editor = getSharedPreferences("preferences", MODE_PRIVATE).edit();

                        editor.putBoolean("coldFlag", coldFlag);
                        editor.putBoolean("hotFlag", hotFlag);
                        editor.putBoolean("dryFlag", dryFlag);
                        editor.putBoolean("wetFlag", wetFlag);
                        editor.putBoolean("pumpFlag", pumpFlag);
                        editor.commit();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
    }

    private void pumpWater() {
        try {
            publishAPI("kaitoud/feeds/button/json", "1");
            publishAPI("kaitoud/feeds/last_watering/json", "1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void pumpInTime(Integer time){
        try {
            pumpWater();
            Thread.sleep(time);
            stopPumpWater();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopPumpWater(){
        try {
            publishAPI("kaitoud/feeds/button/json", "0");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void turnAutoOn(){
        try {
            publishAPI("kaitoud/feeds/auto_watering/json", "1");
            water_auto = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void turnAutoOff(){
        try {
            publishAPI("kaitoud/feeds/auto_watering/json", "0");
            water_auto = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void updateThreshold(String temp1, String temp2, String humid1, String humid2){
        try {
            publishAPI("kaitoud/feeds/temperature_lower_bound/json", temp1);
            publishAPI("kaitoud/feeds/temperature_upper_bound/json", temp2);
            publishAPI("kaitoud/feeds/humidity_lower_bound/json", humid1);
            publishAPI("kaitoud/feeds/humidity_upper_bound/json", humid2);
            tempThreshold1 = Double.parseDouble(temp1);
            tempThreshold2 = Double.parseDouble(temp2);
            humidThreshold1 = Integer.parseInt(humid1);
            humidThreshold2 = Integer.parseInt(humid2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}

