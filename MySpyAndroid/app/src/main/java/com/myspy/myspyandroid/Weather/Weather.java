package com.myspy.myspyandroid.Weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.myspy.myspyandroid.functions.HTTPWork;
import com.myspy.myspyandroid.variables.LocationPoint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Miroslav Murin on 11.01.2017.
 */

public class Weather {

    public WeatherInfo weatherInfo = new WeatherInfo();

    /**
     * Receive weather info
     * @param Latitude Latitude for weather info
     * @param Longitude Longitude for weather info
     * @return Boolean - true if successful
     */
    public boolean GetWeather(float Latitude, float Longitude)
    {
        try {
            GetData(Latitude, Longitude);
            return true;
        }catch (Exception ex){
            Log.w("Error","GetLocationByName: "+ex);
            return false;
        }
    }

    /**
     * Receive weather info
     * @param locationPoint Location for weather info
     * @return Boolean - true if successful
     */
    public boolean GetWeather(LocationPoint locationPoint)
    {
        try {
            GetData(locationPoint.Latitude, locationPoint.Longitude);
            return true;
        }catch (Exception ex){
            Log.w("Error","GetLocationByName: "+ex);
            return false;
        }
    }

    private void GetData(float lat, float lon)
    {
        Log.d("SendData","https://api.met.no/weatherapi/locationforecastlts/1.3/?lat="+lat+";lon="+lon);
        WorkWithData(HTTPWork.GET("https://api.met.no/weatherapi/locationforecastlts/1.3/?lat="+lat+";lon="+lon,"</time>",2));
    }


    private void WorkWithData(String data)
    {

        Log.d("Data","Size: "+data.length());
        data+="</product></weatherdata>";
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(data));
            Document doc = dBuilder.parse(is);


            weatherInfo.temperature.SetTemperature( Float.parseFloat( ((Element)doc.getElementsByTagName("temperature").item(0)).getAttribute("value")),WeatherUnit.Celsius);
            weatherInfo.humidity = ((Element)doc.getElementsByTagName("humidity").item(0)).getAttribute("value");
            weatherInfo.pressure = ((Element)doc.getElementsByTagName("pressure").item(0)).getAttribute("value");
            weatherInfo.cloudiness = ((Element)doc.getElementsByTagName("cloudiness").item(0)).getAttribute("percent");
            weatherInfo.windSpeed = ((Element)doc.getElementsByTagName("windSpeed").item(0)).getAttribute("mps");
            weatherInfo.windDirectionDeg = ((Element)doc.getElementsByTagName("windDirection").item(0)).getAttribute("deg");
            weatherInfo.windDirectionName =((Element)doc.getElementsByTagName("windDirection").item(0)).getAttribute("name");
            weatherInfo.precipitation = ((Element)doc.getElementsByTagName("precipitation").item(0)).getAttribute("value");
            weatherInfo.precipitationunit = ((Element)doc.getElementsByTagName("precipitation").item(0)).getAttribute("unit");
            weatherInfo.symbolID = ((Element)doc.getElementsByTagName("symbol").item(0)).getAttribute("id");
            weatherInfo.symbolNumber = ((Element)doc.getElementsByTagName("symbol").item(0)).getAttribute("number");
            weatherInfo.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            weatherInfo.hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        }catch (Exception ex)
        {
            Log.w("WeatherError","1-Weather: "+ex);
        }
    }


    /**
     * Returns weather icon
     * @param symbol Symbol code ( SymbolNumber in WeatherInfo )
     * @param night 1 - night  |  0 - Day
     * @return Bitmap
     */
    public Bitmap GetWeatherIcon(String symbol, int night)
    {
        try {
            URL url = new URL("https://api.met.no/weatherapi/weathericon/1.1/?symbol="+symbol+";is_night="+night+";content_type=image/png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception ex) {
            Log.w("WeatherError","2-WERR: "+ex);
            return null;
        }
    }

}
