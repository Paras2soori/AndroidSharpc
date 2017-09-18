package com.example.jahan.uidemo;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHandler  {


    MqttAndroidClient mqttAndroidClient;
    private MqttService service;
    private final Context contex;
    String Client;
    String Topic;



   public MqttHandler(Context contex){
       this.contex=contex;
   }

    public  void Publish(){
        mqttAndroidClient = new MqttAndroidClient(contex, "tcp://thingtalk.ir:1883", Client);

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Connection Success!");
                    try {



                        String topic="reserve";

                        mqttAndroidClient.publish(topic, new MqttMessage());

                    } catch (MqttException ex) {

                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Connection Failure!");
                }
            });
        } catch (MqttException ex) {

        }

        mqttAndroidClient.setCallback(new MqttCallback() {
                                          @Override
                                          public void connectionLost(Throwable cause) {
                                              System.out.println("Connection was lost!");

                                          }

                                          @Override
                                          public void messageArrived(String topic, MqttMessage message) throws Exception {

                                              Log.e("message", "Message Arrived!: " + topic + ": " + new String(message.getPayload()));

                                          }

                                          @Override
                                          public void deliveryComplete(IMqttDeliveryToken token) {
                                              System.out.println("Delivery Complete!");
                                          }

                                      }
        );


    }}
















