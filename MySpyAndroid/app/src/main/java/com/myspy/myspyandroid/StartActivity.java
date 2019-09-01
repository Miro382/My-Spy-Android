package com.myspy.myspyandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myspy.myspyandroid.Weather.WeatherLocationByLocation;
import com.myspy.myspyandroid.Weather.WeatherLocationByName;
import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.functions.Encryption;

import java.util.List;

public class StartActivity extends Activity {

    Context tcontext = this;

    Activity activity = this;


    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final EditText password = (EditText) findViewById(R.id.editText2);
        final EditText passwordrepeat = (EditText) findViewById(R.id.editText3);


        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password.getText().toString() != null && !password.getText().toString().isEmpty()) {

                    if (password.getText().toString().equals(passwordrepeat.getText().toString())) {

                        Encryption encryption = Encryption.getDefault("0GzyZdjKFMINl5vtC6rNjz5n9s", "vAlYXMwnrAJYlit5", new byte[16]);
                        String encrypted = encryption.encryptOrNull(password.getText().toString());

                        MySpyService.serviceSettings.EncodedPassword = encrypted;
                        ClassSaver classSaver = new ClassSaver();
                        classSaver.SaveClassToFile(MySpyService.serviceSettings, "MySpy", "Settings.dat", tcontext);
                        Log.d("ClassSaver", "Saved");
                        Toast.makeText(StartActivity.this, "OK", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(StartActivity.this, getResources().getString(R.string.NotEquals), Toast.LENGTH_LONG).show();
                        Log.d("StartActivity", "Passwords not equals");
                    }
                } else {
                    Toast.makeText(StartActivity.this, getResources().getString(R.string.EmptyPassword), Toast.LENGTH_LONG).show();
                    Log.d("StartActivity", "Passwords is empty");
                }


            }
        });


        final TextView textweather = (TextView) findViewById(R.id.textViewWeatherLoc);

        final Activity activity = this;

        (findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {

                            WeatherLocationByName weatherLocationByName = new WeatherLocationByName();
                            weatherLocationByName.GetLocation(((EditText) findViewById(R.id.editTextLocationName)).getText().toString(), "miro382@centrum.sk");
                            MySpyService.serviceSettings.WeatherLocationName = weatherLocationByName.GetLocationName();
                            MySpyService.serviceSettings.WeatherLocation = weatherLocationByName.GetLocationPoint();

                            final String textl = weatherLocationByName.GetLocationName();

                            activity.runOnUiThread(new Runnable() {
                                public void run() {

                                    textweather.setText(textl);

                                }
                            });

                        } catch (Exception ex) {
                            Log.w("Error", "" + ex);
                        }


                    }
                });
                thread.start();

            }
        });


        (findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {

                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);

                            WeatherLocationByLocation weatherLocationByLocation = new WeatherLocationByLocation();
                            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                            Criteria criteria = new Criteria();
                            String bestProvider = locationManager.getBestProvider(criteria, false);
                            if (ActivityCompat.checkSelfPermission(tcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(tcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
                            }

                            Location location = getLastKnownLocation();
                            weatherLocationByLocation.GetLocation((float) location.getLatitude(), (float) location.getLongitude(), "miro382@centrum.sk");

                            MySpyService.serviceSettings.WeatherLocationName = weatherLocationByLocation.GetLocationName();
                            MySpyService.serviceSettings.WeatherLocation = weatherLocationByLocation.GetLocationPoint();

                            final String textl = weatherLocationByLocation.GetLocationName();

                            activity.runOnUiThread(new Runnable() {
                                public void run() {

                                    textweather.setText(textl);

                                }
                            });

                        }catch (Exception ex)
                        {
                            Log.w("Error",""+ex);
                        }


                    }
                });
                thread.start();

            }
        });

    }


    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)tcontext.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(tcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(tcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }

            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @Override
    public void onBackPressed() {

    }


}
