package com.myspy.myspyandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myspy.myspyandroid.Weather.Weather;
import com.myspy.myspyandroid.Weather.WeatherInfo;
import com.myspy.myspyandroid.Weather.WeatherLocationByLocation;
import com.myspy.myspyandroid.Weather.WeatherUnit;
import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.variables.CallInfo;
import com.myspy.myspyandroid.variables.LocationPoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Permission;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    ClassSaver classSaver = new ClassSaver();


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    Activity activity = this;
    Context context = this;

    LocationManager mLocationManager;


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permission))
                return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_CALL_LOG))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.PROCESS_OUTGOING_CALLS))
            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.KILL_BACKGROUND_PROCESSES))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }


        try {
            if (!isAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }catch (Exception ex)
        {
            Log.e("USAGE ACCESS SETTINGS","API 21 Required");
        }


        if(!isMyServiceRunning(MySpyService.class))
            startService(new Intent(this, MySpyService.class));

        Log.d("POINT", "1");

        Typeface type = Typeface.createFromAsset(getAssets(),"jockeyone.ttf");
        ((TextView)findViewById(R.id.textViewMySpy)).setTypeface(type);


        File dir = this.getDir("MySpy", Context.MODE_PRIVATE);
        File dirn  = new File(dir.getPath()+"/Monitoring");
        dirn.mkdir();


        if(classSaver.FileExist("MySpy","Settings.dat",this)) {
            Log.d("Settings","Settings Loaded");
        }else{
            Log.d("Settings","Not exists. Starting Start Activity");

            Intent myIntent = new Intent(MainActivity.this, StartActivity.class);
            MainActivity.this.startActivity(myIntent);
        }


        final Activity activity = this;
        final Context context = this;

        Log.d("POINT", "2");



        UpdateInformations();


        if(MySpyService.serviceSettings.ShowWeather) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (classSaver.FileExist("MySpy", "Weatherdata.dat", context)) {

                            Log.d("CurrentWeather", "Weather exist checking");
                            //Log.d("GetWeather", "Location Name: " + MySpyService.serviceSettings.WeatherLocationName + "   Location Position:  LAT: " + MySpyService.serviceSettings.WeatherLocation.Latitude + "  LON: " + MySpyService.serviceSettings.WeatherLocation.Longitude);
                            final WeatherInfo weatherInfo = (WeatherInfo) classSaver.LoadClassFromSpecificFile(WeatherInfo.class, classSaver.GetFile("MySpy", "Weatherdata.dat", context)); // classSaver.LoadClassFromFile(WeatherInfo.class,"MySpy","Weatherdata.dat",context);
                            Calendar calendar = Calendar.getInstance();

                            if (weatherInfo != null && weatherInfo.day == (calendar.get(Calendar.DAY_OF_MONTH)) && weatherInfo.hour < (calendar.get(Calendar.HOUR_OF_DAY) + 3)) {
                                Log.d("CurrentWeather", "Weather exist and still valid:  Hour: " + weatherInfo.hour + "   Day: " + weatherInfo.day);
                                final LinearLayout weatherlayout = (LinearLayout) findViewById(R.id.WeatherLayout);

                                FileInputStream is = new FileInputStream(classSaver.GetFile("MySpy", "Weatherimage.dat", context));
                                //activity.openFileInput(classSaver.GetFile("MySpy","Weatherimage.dat",context).getAbsolutePath());
                                final Bitmap weatherbitmap = BitmapFactory.decodeStream(is);

                                Log.d("Weather temp:",""+weatherInfo.temperature.GetTemperature(MySpyService.serviceSettings.weatherUnit));

                                if (weatherInfo.temperature.GetTemperature(WeatherUnit.Celsius) > -272) {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {

                                            ((TextView) findViewById(R.id.textViewlocationweather)).setText(MySpyService.serviceSettings.WeatherLocationName);
                                            ((TextView) findViewById(R.id.textViewtemperature)).setText((int) weatherInfo.temperature.GetTemperature(MySpyService.serviceSettings.weatherUnit) + " " + weatherInfo.temperature.GetUnitSymbol(MySpyService.serviceSettings.weatherUnit));
                                            ((TextView) findViewById(R.id.textViewhumidity)).setText(FloatStringToInt(weatherInfo.humidity) + " %");
                                            ((TextView) findViewById(R.id.textViewprecipitation)).setText(weatherInfo.precipitation + " " + weatherInfo.precipitationunit);
                                            ((TextView) findViewById(R.id.textViewcloudiness)).setText(FloatStringToInt(weatherInfo.cloudiness) + " %");
                                            ((TextView) findViewById(R.id.textViewDate)).setText(weatherInfo.Date);
                                            ((ImageView) findViewById(R.id.imageViewWeather)).setImageBitmap(weatherbitmap);
                                            weatherlayout.setVisibility(View.VISIBLE);


                                        }
                                    });
                                } else {
                                    weatherlayout.setVisibility(View.GONE);
                                }

                                Log.d("CurrentWeather", "Weather loaded from file");

                            } else {
                                Log.d("New WEATHER", "New Weather 1");
                                NewWeather();
                            }

                        } else {

                            Log.d("New WEATHER","New Weather 2");
                            NewWeather();

                        }


                    } catch (Exception ex) {
                        Log.w("Error", "Weather: " + ex);
                        try {

                            File file = classSaver.GetFile("MySpy", "Weatherdata.dat", context);
                            if (file.exists())
                                file.delete();

                        } catch (Exception exe) {
                            Log.w("Error", "Weather Remove file: " + exe);
                        }
                    }
                }
            });
            thread.start();
        }//showweather

    }



    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    void NewWeather()
    {
        final Activity activity = this;
        Log.d("Weather","***** New Weather *****");

        if (MySpyService.serviceSettings.WeatherLocation != null && MySpyService.serviceSettings.WeatherLocationName != null && !MySpyService.serviceSettings.WeatherLocationName.isEmpty()) {
            final Weather weather = new Weather();
            boolean weathersuccess = weather.GetWeather(MySpyService.serviceSettings.WeatherLocation);

            Log.d("Temperature", weather.weatherInfo.temperature.GetTemperatureWithUnit(WeatherUnit.Celsius));

            final LinearLayout weatherlayout = (LinearLayout) findViewById(R.id.WeatherLayout);
            final Bitmap weatherbitmap;
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) > 21 && calendar.get(Calendar.HOUR_OF_DAY) < 6)
                weatherbitmap = weather.GetWeatherIcon(weather.weatherInfo.symbolNumber, 1);
            else
                weatherbitmap = weather.GetWeatherIcon(weather.weatherInfo.symbolNumber, 0);


            weather.weatherInfo.day = calendar.get(Calendar.DAY_OF_MONTH);
            weather.weatherInfo.hour = calendar.get(Calendar.HOUR_OF_DAY);
            NumberFormat nf = new DecimalFormat("00");
            weather.weatherInfo.Date = (nf.format(calendar.get(Calendar.DAY_OF_MONTH)))+"."+ nf.format((calendar.get(Calendar.MONTH)+1))+"  "+nf.format(calendar.get(Calendar.HOUR_OF_DAY))+":00";
            final WeatherInfo weatherInfo = weather.weatherInfo;

            Log.d("Weather temperature",""+weatherInfo.temperature.GetTemperature(WeatherUnit.Celsius)+"    Success: "+weathersuccess);
            if(weathersuccess && weatherInfo.temperature.GetTemperature(WeatherUnit.Celsius)>-272) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        ((TextView) findViewById(R.id.textViewlocationweather)).setText(MySpyService.serviceSettings.WeatherLocationName);
                        ((TextView) findViewById(R.id.textViewtemperature)).setText((int) weatherInfo.temperature.GetTemperature(MySpyService.serviceSettings.weatherUnit) + " " + weatherInfo.temperature.GetUnitSymbol(MySpyService.serviceSettings.weatherUnit));
                        ((TextView) findViewById(R.id.textViewhumidity)).setText(FloatStringToInt(weatherInfo.humidity) + " %");
                        ((TextView) findViewById(R.id.textViewprecipitation)).setText(weatherInfo.precipitation + " " + weatherInfo.precipitationunit);
                        ((TextView) findViewById(R.id.textViewcloudiness)).setText(FloatStringToInt(weatherInfo.cloudiness) + " %");
                        ((TextView) findViewById(R.id.textViewDate)).setText(weatherInfo.Date);
                        ((ImageView) findViewById(R.id.imageViewWeather)).setImageBitmap(weatherbitmap);
                        weatherlayout.setVisibility(View.VISIBLE);
                    }
                });
            }else{
                weatherlayout.setVisibility(View.GONE);
            }

            try {
                FileOutputStream fos = new FileOutputStream(classSaver.GetFile("MySpy","Weatherimage.dat",this));
                weatherbitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            }catch (Exception ex)
            {
                Log.d("Error",""+ex);
            }

            classSaver.SaveClassToSpecificFile(weatherInfo,classSaver.GetFile("MySpy","Weatherdata.dat",this));
            Log.d("Weather","Saved weather data");
        }else{
            Log.d("Weather GET","Location data not exist");
        }

    }


    private String FloatStringToInt(String text)
    {
        try{
            return ((int)Float.parseFloat(text))+"";
        }catch (Exception ex)
        {
            Log.w("FloatStringToInt",""+ex);
            return text;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int FLoc = 0;
        int i=0;
        for (String t: permissions) {

            if(t.equals(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                FLoc = i;
                Log.d("Find Location","FLOC: "+FLoc+" Sp: "+permissions[FLoc]+"   IR: "+grantResults[FLoc]);

                stopService(new Intent(this, MySpyService.class));
                startService(new Intent(this, MySpyService.class));
                break;
            }
            i++;

        }
    }


    private void UpdateInformations()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    int inlength = 0,in = 0;
                    int outlength =0, out = 0;

                    for (CallInfo info : MySpyService.serviceVariables.CallList) {
                        if(!info.Missed) {

                            if(info.OutGoingCall) {
                                outlength += info.CallLength;
                                out++;
                            }
                            else {
                                inlength += info.CallLength;
                                in++;
                            }
                        }
                    }


                    final int inlengthf = inlength,inf = in;
                    final int outlengthf = outlength, outf = out;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                ((TextView) findViewById(R.id.textViewsmsreceivedcount)).setText(MySpyService.serviceVariables.SMSList.size()+"");
                                ((TextView) findViewById(R.id.textViewsmssentcount)).setText(MySpyService.serviceVariables.SMSOutList.size()+"");
                                ((TextView) findViewById(R.id.textViewmadecallscount)).setText(outf + "");
                                ((TextView) findViewById(R.id.textViewreceivedcallscount)).setText(inf + "");
                                ((TextView) findViewById(R.id.textViewmadecallslength)).setText(IntToTime(outlengthf));
                                ((TextView) findViewById(R.id.textViewreceivedcallslength)).setText(IntToTime(inlengthf));

                            }catch (Exception ex)
                            {
                                Log.w("Error",""+ex);
                            }


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


    private String IntToTime(int Time)
    {
        int Minutes = (int) Math.floor(Time/60);
        int Seconds = (Time-(Minutes*60));
        return String.format("%02d", Minutes)+":"+String.format("%02d", Seconds);
    }





    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void ToAdminLogin(View view)
    {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }




}
