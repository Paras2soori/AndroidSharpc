package com.example.jahan.uidemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class SecondPage extends AppCompatActivity {

    EditText password;
    EditText username;
    CheckBox showpassword;
    String user;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        Button btn3 = (Button) findViewById(R.id.btn3);

        username = (EditText) findViewById(R.id.et1);

        password = (EditText) findViewById(R.id.et2);

        showpassword = (CheckBox) findViewById(R.id.showpass);

        showpassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                String URL = "http://thingtalk.ir/channels/500/feed.json?key=J76MU8X3WS0U58HZ&results=100";
                HttpGetRequest ht = new HttpGetRequest();

                Log.e("SALAM","BYE");
                try {
                    String result = ht.execute(URL).get();

                    if(checkUserPass(result)){



                        Log.e("SALAM","BYE");
                        Toast.makeText(getApplicationContext(),"welcome", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SecondPage.this, sharPCmap.class);
                        Log.e("SALAM","BYE");
                        startActivity(intent);

                    }



                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;

            String inputLine;

            try {
                URL myUrl = new URL(stringUrl);


                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);


                connection.connect();

                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();


                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();


                result = stringBuilder.toString();
                System.out.print(result);
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }

            return result;


        }


        protected void onPostExecute(String result) {


            super.onPostExecute(result);


        }


    }

    public boolean checkUserPass(String json) {
        try {
            Log.e(":(", "dd");

            JSONObject jsonObj = new JSONObject(json);
            JSONArray array = jsonObj.getJSONArray("feeds");

            for (int i = 0; i < array.length(); i++) {
                JSONObject data = array.getJSONObject(i);

                user = data.getString("field5");
                pass = data.getString("field6");

                if (username.getText().toString().equals(user) && password.getText().toString().equals(pass)) {
                    return true;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

}

