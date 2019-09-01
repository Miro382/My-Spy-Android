package com.myspy.myspyandroid.Weather;

import android.util.Log;

import com.google.gson.Gson;
import com.myspy.myspyandroid.functions.HTTPWork;
import com.myspy.myspyandroid.variables.LocationPoint;

/**
 * Created by Miroslav Murin on 05.01.2017.
 */

public class WeatherLocationByName implements WeatherLocation{

    private final String UrlGet = "http://nominatim.openstreetmap.org/search?city=";
    public PlaceInfo placeinfo;
    private LocationPoint locationPoint = new LocationPoint(0,0);
    private String locationname;
    private boolean Dataavaible = false;

    /**
     * Get location. Add this function to another thread than main thread
     * @param Cityname Location name
     * @param Email Your email for security reason ( Nominatim )
     */
    public void GetLocation(final String Cityname, final String Email) {

        try {
            WorkWithData(HTTPWork.GET(UrlGet+Cityname+"&addressdetails=0&format=json&email=" + Email));
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


    private void WorkWithData(String data)
    {
        Gson gson = new Gson();
        PlaceInfo[] placeInfos = gson.fromJson(data,PlaceInfo[].class);
        placeinfo = placeInfos[0];
        locationPoint.Longitude = Float.parseFloat( placeinfo.lon);
        locationPoint.Latitude = Float.parseFloat(placeinfo.lat);
        locationname = placeinfo.displayName;
        Dataavaible = true;
        Log.d("WeatherLocationByName","Successful acquired data");
    }


}
