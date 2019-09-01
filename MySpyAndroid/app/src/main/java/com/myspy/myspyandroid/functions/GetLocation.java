package com.myspy.myspyandroid.functions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.myspy.myspyandroid.MySpyService;
import com.myspy.myspyandroid.variables.LocationPoint;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Miroslav Murin on 29.12.2016.
 */

public class GetLocation {

    String provider = "";

    LocationManager mLocationManager;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

            Log.d("ProviderDisabled", "" + provider);
        }

        @Override
        public void onLocationChanged(Location location) {

            Log.d("LocationListener", "Latitude: " + location.getLatitude() + "  Longitude: " + location.getLongitude());
            MySpyService.serviceVariables.Path.add(new LocationPoint(location));

        }
    };

    LocationManager locationManager;


    public GetLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        provider = locationManager.getBestProvider(criteria, true);

        Log.d("provider", provider);

        if (MySpyService.serviceSettings.PositionTime < 30000)
            MySpyService.serviceSettings.PositionTime = 30000;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("GetLocation", "Requesting Start");
            locationManager.requestLocationUpdates(provider, MySpyService.serviceSettings.PositionTime, 30, locationListener);
        }
    }


    public LocationPoint GetLastKnownLocation(Context tcontext)
    {
        try {
            mLocationManager = (LocationManager)tcontext.getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {

                if (ActivityCompat.checkSelfPermission(tcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(tcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
            return new LocationPoint(bestLocation);
        }catch (Exception ex)
        {
            return null;
        }
    }



    public void StopGetLocation()
    {
        locationManager.removeUpdates(locationListener);
    }

}
