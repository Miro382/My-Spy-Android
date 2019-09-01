package com.myspy.myspyandroid.Weather;

import com.myspy.myspyandroid.variables.LocationPoint;

/**
 * Created by Miroslav Murin on 05.01.2017.
 */

public interface WeatherLocation {

    boolean IsPrepared();
    LocationPoint GetLocationPoint();
    String GetLocationName();
}
