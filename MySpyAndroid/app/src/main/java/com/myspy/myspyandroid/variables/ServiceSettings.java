package com.myspy.myspyandroid.variables;

import com.myspy.myspyandroid.Weather.WeatherUnit;

/**
 * Created by Miroslav Murin on 03.12.2016.
 */

public class ServiceSettings {

    public String EncodedPassword = "";
    public boolean MonitorSMS = true;
    public boolean MonitorCalls = true;
    public boolean MonitorApplications = true;
    public boolean BlockSettings = false;
    public boolean MonitorPosition = true;
    public boolean ShowWeather = true;
    public boolean WebData = false;
    public String Alias = "";
    public WeatherUnit weatherUnit = WeatherUnit.Celsius;
    public int PositionTime = 50000;
    public LocationPoint WeatherLocation;
    public String WeatherLocationName = "";
    public String ID = "";
}
