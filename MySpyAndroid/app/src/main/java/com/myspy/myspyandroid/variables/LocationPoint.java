package com.myspy.myspyandroid.variables;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Miroslav Murin on 29.12.2016.
 */

public class LocationPoint {

    public float Latitude = 0; //sirka
    public float Longitude = 0; //dlzka

    /**
     * New LocationPoint from latitude, longitude
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public LocationPoint(float latitude, float longitude)
    {
        Latitude = latitude;
        Longitude = longitude;
    }

    /**
     * New LocationPoint from Location
     * @param location Location
     */
    public LocationPoint(Location location)
    {
        Latitude = (float)location.getLatitude();
        Longitude = (float)location.getLongitude();
    }

    /**
     * LocationPoint to GeoPoint
     * @return GeoPoint
     */
    public GeoPoint ToGeoPoint()
    {
        return new GeoPoint(Latitude,Longitude);
    }

}
