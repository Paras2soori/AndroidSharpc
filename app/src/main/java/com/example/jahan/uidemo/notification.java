package com.example.jahan.uidemo;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class notification extends Service {
    Resources mResources;
    String topic;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // your code
        final MqttAndroidClient client;
        client = new MqttAndroidClient(getApplicationContext(), "tcp://thingtalk.ir:1883", "android");
        Log.e("chera", "aaappp");
        try {
            client.connect(null, new IMqttActionListener() {

                @Override

                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        Log.e("chera", "ppp");
                        client.subscribe("SharpC", 0);
                        Log.e("first", "nn");
                        // client.subscribe("ParsIoT_alarm",0);
                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable cause) {
                            }

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                Log.e("hesam", topic + " " + new String(message.getPayload()));


                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {

                            }
                        });


                    } catch (MqttException e) {
                        Log.e("mgtt","ooo");
                        e.printStackTrace();
                    }
                }
            public  void publish(){
                try {

                    client.publish(topic, new MqttMessage(("off").getBytes()));


                } catch (MqttException ex) {

                }
            }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    exception.printStackTrace();
                    Log.e("second", "nn");
                }
            });
        } catch (MqttException e) {
            Log.e("third", "nn");
            e.printStackTrace();
        }

        return Service.START_REDELIVER_INTENT;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}