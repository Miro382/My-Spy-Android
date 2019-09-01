package com.myspy.myspyandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myspy.myspyandroid.functions.Encryption;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class LoginActivity extends Activity {

    Encryption encryption = Encryption.getDefault("0GzyZdjKFMINl5vtC6rNjz5n9s", "vAlYXMwnrAJYlit5", new byte[16]);
    String decrypted = encryption.decryptOrNull(MySpyService.serviceSettings.EncodedPassword);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText password = (EditText)findViewById(R.id.editText);

        Button button =  (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString() != null && !password.getText().toString().isEmpty()) {

                    if(password.getText().toString().equals(decrypted)) {
                        Log.d("LoginActivity", "OK");

                        Intent myIntent = new Intent(LoginActivity.this, AdministratorActivity.class);

                        Calendar calendar = Calendar.getInstance();
                        myIntent.putExtra("Token", calendar.get(Calendar.DAY_OF_YEAR)+"-"+calendar.get(Calendar.HOUR));
                        LoginActivity.this.startActivity(myIntent);

                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.BadPassword), Toast.LENGTH_LONG).show();
                        Log.d("LoginActivity", "Passwords is bad");
                    }

                }else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.EmptyPassword), Toast.LENGTH_LONG).show();
                    Log.d("LoginActivity", "Passwords is empty");
                }


            }
        });


    }


}
