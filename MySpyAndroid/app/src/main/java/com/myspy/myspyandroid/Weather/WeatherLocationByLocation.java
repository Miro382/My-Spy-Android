package com.myspy.myspyandroid.Weather;

import android.util.Log;

import com.google.gson.Gson;
import com.myspy.myspyandroid.functions.HTTPWork;
import com.myspy.myspyandroid.variables.LocationPoint;


/**
 * Created by Miroslav Murin on 06.01.2017.
 */

public class WeatherLocationByLocation implements WeatherLocation {


    private final String UrlGet = "http://nominatim.openstreetmap.org/reverse?format=json&zoom=10&addressdetails=0&lat=";

    public PlaceInfo placeinfo;
    private LocationPoint locationPoint = new LocationPoint(0,0);
    private String locationname;
    private boolean Dataavaible = false;

    /**
     * Get location. Add this function to another thread than main thread
     * @param Latitude Your Latitude
     * @param Longitude Your Longitude
     * @param Email Your email for security reason ( Nominatim )
     */
    public void GetLocation(float Latitude, float Longitude, String Email)
    {
        try {
            WorkWithData(HTTPWork.GET(UrlGet+Latitude+"&lon="+Longitude+"&email=" + Email));
        }catch (Exception ex){
            Log.w("Error","GetLocationByName: "+ex);
        }
    }


    /**
     * Is prepared? Return true if contains values
     * @return Boolean
     */
    @Override
    public boolean IsPrepared() {
        return Dataavaible;
    }

    /**
     * Get location point
     * @return LocationPoint
     */
    @Override
    public LocationPoint GetLocationPoint() {
        return locationPoint;
    }


    /**
     * Return location name
     * @return String
     */
    @Override
    public String GetLocationName() {
        return locationname;
    }

    private void  WorkWithData(String data)
    {
        Gson gson = new Gson();

        placeinfo = gson.fromJson(data,PlaceInfo.class);
        locationPoint.Longitude = Float.parseFloat( placeinfo.lon);
        locationPoint.Latitude = Float.parseFloat(placeinfo.lat);
        locationname = placeinfo.displayName;
        Dataavaible = true;
        Log.d("WeatherLocationByLocat","Successful acquired data");
    }

}
