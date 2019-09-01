package com.myspy.myspyandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.functions.Encryption;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class WebLogin extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_web_login);


            final EditText email = (EditText)findViewById(R.id.editTextEmailWeb);
            final EditText password = (EditText)findViewById(R.id.editTextPasswordWeb);

            Button button =  (Button)findViewById(R.id.buttonLoginWeb);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!password.getText().toString().isEmpty() && !email.getText().toString().isEmpty()) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Login(email.getText().toString(),password.getText().toString());
                                } catch (Exception ex) {
                                    Log.w("ThreadError", "" + ex);
                                }

                            }
                        });
                        thread.start();

                    }else{
                        Toast.makeText(WebLogin.this, getResources().getString(R.string.EmptyForm), Toast.LENGTH_LONG).show();
                        Log.d("LoginActivity", "Passwords is empty or email is empty");
                    }


                }
            });


        }




        void Login(String Email, String Password)
        {
            try {
                URL url = new URL("http://myspy.diodegames.eu/Connection/ANCLogin.php");

                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("POST");
                client.setDoOutput(true);

                StringBuilder builder = new StringBuilder("");

                builder.append ("Token=blZEHudX");
                builder.append ("&Email="+Email);
                builder.append("&Pass="+ Password);

                OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                outputPost.write(builder.toString().getBytes());
                outputPost.flush();
                outputPost.close();


                String response = "";
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
                Log.d("WEB",response);


                if(!response.equals("Error")) {

                    response = response.replace("<tr>","");
                    Log.d("WEBReplace",response);

                    MySpyService.serviceSettings.ID = response;

                    ClassSaver classSaver =  new ClassSaver();
                    classSaver.SaveClassToFile(MySpyService.serviceSettings,"MySpy","Settings.dat",this);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WebLogin.this, getResources().getString(R.string.Success), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            Toast.makeText(WebLogin.this, getResources().getString(R.string.NoSuccess), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                client.disconnect();

            }catch (Exception ex)
            {
                Log.w("WebError",""+ex.toString());


                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(WebLogin.this, getResources().getString(R.string.NoSuccess), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }


    }

