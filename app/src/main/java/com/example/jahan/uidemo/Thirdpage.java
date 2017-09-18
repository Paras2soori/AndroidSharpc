package com.example.jahan.uidemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Thirdpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdpage);
        Button btn4 = (Button) findViewById(R.id.btn4);



        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //connect two activity
                 Intent intent = new Intent(Thirdpage.this, Forthpage.class);
                 startActivity(intent);

                EditText etx3 = (EditText) findViewById(R.id.et3);
                String s3 = etx3.getText().toString();
                EditText etx4 = (EditText) findViewById(R.id.et4);
                String s4 = etx4.getText().toString();
                EditText etx5 = (EditText) findViewById(R.id.et5);
                String s5 = etx5.getText().toString();
                EditText etx6 = (EditText) findViewById(R.id.et6);
                String s6 = etx6.getText().toString();
                EditText etx7 = (EditText) findViewById(R.id.et7);
                String s7 = etx7.getText().toString();
                EditText etx8 = (EditText) findViewById(R.id.et8);
                String s8 = etx8.getText().toString();

                String URL = "http://thingtalk.ir/update?key=J76MU8X3WS0U58HZ&field1=" + s3 + "&field2=" + s4 + "&field3=" + s5 + "&field4=" + s6 + "&field5=" + s7 + "&field6=" + s8;
                HttpPostRequest ht = new HttpPostRequest();
                try {
                    String result = ht.execute(URL).get();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.e("url", "http://thingtalk.ir/update?key=J76MU8X3WS0U58HZ&field1=" + s3 + "&field2=" + s4 + "&field3=" + s5 + "&field4=" + s6 + "&field5=" + s7 + "&field6=" + s8);
            }

        } )
        ;





    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params){


            String stringUrl = params[0];
            String result;
            String inputLine;

            try {

                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);



                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);


                //Connect to our url
                connection.connect();


                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());


                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();


                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }


                reader.close();
                streamReader.close();

                result = stringBuilder.toString();
                System.out.print(result);
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }

            return result;

        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }


    }
}

