package com.myspy.myspyandroid;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.myspy.myspyandroid.Weather.WeatherLocationByLocation;
import com.myspy.myspyandroid.Weather.WeatherLocationByName;
import com.myspy.myspyandroid.Weather.WeatherUnit;


public class TabFragment3 extends Fragment {

    boolean Loaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);


        if(!Loaded) {
            ((CheckBox) view.findViewById(R.id.checkBoxcalls)).setChecked(MySpyService.serviceSettings.MonitorCalls);
            ((CheckBox) view.findViewById(R.id.checkBoxsms)).setChecked(MySpyService.serviceSettings.MonitorSMS);
            ((CheckBox) view.findViewById(R.id.checkBoxlocation)).setChecked(MySpyService.serviceSettings.MonitorPosition);
            ((CheckBox) view.findViewById(R.id.checkBoxshoweather)).setChecked(MySpyService.serviceSettings.ShowWeather);
            ((CheckBox) view.findViewById(R.id.checkBoxmonitorapps)).setChecked(MySpyService.serviceSettings.MonitorApplications);
            ((CheckBox) view.findViewById(R.id.checkBoxblocksettings)).setChecked(MySpyService.serviceSettings.BlockSettings);
            ((CheckBox) view.findViewById(R.id.checkBoxWebSend)).setChecked(MySpyService.serviceSettings.WebData);
            ((TextView) view.findViewById(R.id.textViewWeatherNameSett)).setText(MySpyService.serviceSettings.WeatherLocationName);
            ((TextView) view.findViewById(R.id.editTextAlias)).setText(MySpyService.serviceSettings.Alias);

            if(!MySpyService.serviceSettings.ID.isEmpty() && !MySpyService.serviceSettings.ID.equals(""))
            ((CheckBox) view.findViewById(R.id.checkBoxWebLoggedin)).setChecked(true);
            else
                ((CheckBox) view.findViewById(R.id.checkBoxWebLoggedin)).setChecked(false);


            if(MySpyService.serviceSettings.weatherUnit == WeatherUnit.Celsius)
                ((RadioButton)view.findViewById(R.id.radioButtonCelsius)).setChecked(true);
            else
                ((RadioButton)view.findViewById(R.id.radioButtonFahrenheit)).setChecked(true);

            Log.d("Fragment 3", "Loaded");
            Loaded = true;
        }
        
        final TextView textaccuracy = (TextView) view.findViewById(R.id.textViewaccuracy) ;


        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBarAccuracy);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.d("SeekBar",""+progress);
                if(progress==0) {
                    textaccuracy.setText(getResources().getString(R.string.Low));
                    MySpyService.serviceSettings.PositionTime = 100000; //100 sekund
                }
                else if(progress==1) {
                    textaccuracy.setText(getResources().getString(R.string.Medium));
                    MySpyService.serviceSettings.PositionTime = 50000; //50 sekund
                }
                else {
                    textaccuracy.setText(getResources().getString(R.string.High));
                    MySpyService.serviceSettings.PositionTime = 30000; //30 sekund
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Activity activity = getActivity();

        (view.findViewById(R.id.buttongetbynameweat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {

                            WeatherLocationByName weatherLocationByName = new WeatherLocationByName();
                            weatherLocationByName.GetLocation(((EditText) view.findViewById(R.id.editTextLocationWeather)).getText().toString(), "miro382@centrum.sk");
                            MySpyService.serviceSettings.WeatherLocationName = weatherLocationByName.GetLocationName();
                            MySpyService.serviceSettings.WeatherLocation = weatherLocationByName.GetLocationPoint();

                            final String textl = weatherLocationByName.GetLocationName();

                            activity.runOnUiThread(new Runnable() {
                                public void run() {

                                    ((TextView)view.findViewById(R.id.textViewWeatherNameSett)).setText(textl);

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


        (view.findViewById(R.id.buttongetautomaticallyweat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                try {


                    WeatherLocationByLocation weatherLocationByLocation = new WeatherLocationByLocation();
                    LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);

                    Criteria criteria = new Criteria();
                    String bestProvider = locationManager.getBestProvider(criteria, false);
                    Location location = locationManager.getLastKnownLocation(bestProvider);
                    weatherLocationByLocation.GetLocation((float)location.getLatitude(),(float)location.getLongitude(),"miro382@centrum.sk");

                    MySpyService.serviceSettings.WeatherLocationName =  weatherLocationByLocation.GetLocationName();
                    MySpyService.serviceSettings.WeatherLocation =  weatherLocationByLocation.GetLocationPoint();

                    final String textl = weatherLocationByLocation.GetLocationName();

                    activity.runOnUiThread(new Runnable() {
                        public void run() {

                            ((TextView)view.findViewById(R.id.textViewWeatherNameSett)).setText(textl);

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

        /*
        if( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);

            if (Build.VERSION.SDK_INT >= 21) {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                List stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);

                if (stats == null || stats.isEmpty()) {
                    Intent intentr = new Intent();
                    intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    getActivity().startActivity(intent);
                }
            }
        }
        */

        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
