package com.example.jahan.uidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Button btn1 = (Button) findViewById(R.id.btn2);
        Log.e("zzz2","mahdiyar");

        Intent noti = new Intent (this, notification.class);
            startService(noti);
        Log.e("zzz","bb");




    }



    public void sendMessage(View view) {
        Intent intent = new Intent(FirstPageActivity.this, SecondPage.class);
        startActivity(intent);

    }


    public void sendMessage1(View view) {
        Intent intent = new Intent(FirstPageActivity.this, Thirdpage.class);
        startActivity(intent);

    }
;}






