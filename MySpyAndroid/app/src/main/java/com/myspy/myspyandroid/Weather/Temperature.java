package com.myspy.myspyandroid.Weather;

/**
 * Created by Miroslav Murin on 11.01.2017.
 */


//Class for receive temperature in specific units... Celsius, Fahrenheit, Kelvin
public class Temperature {

    private float temperature = 0;

    /**
     * Set temperature value
     * @param Temperature Your temperature value (float)
     * @param Unit Your Temperature Unit (WeatherUnit)
     */
    public void SetTemperature(float Temperature, WeatherUnit Unit)
    {
        if(Unit == WeatherUnit.Kelvin)
            temperature = Temperature;
        else if(Unit == WeatherUnit.Fahrenheit)
           temperature = ConvertFromFahrenheit(Temperature);
        else
          temperature = ConvertFromCelsius(Temperature);
    }

    /**
     * Get temperature value
     * This function will convert value to your unit.
     * @param Unit Your Temperature Unit (WeatherUnit)
     * @return value (Float)
     */
    public float GetTemperature(WeatherUnit Unit)
    {

        if(Unit == WeatherUnit.Kelvin)
            return temperature;
        else if(Unit == WeatherUnit.Fahrenheit)
           return ConvertToFahrenheit(temperature);
        else
            return ConvertToCelsius(temperature);
    }

    /**
     * Get temperature value with symbol
     * @param Unit Temperature Unit in you want to get your value(WeatherUnit)
     * @return [Value] [Unit]  -  280 K (String)
     */
    public String GetTemperatureWithUnit(WeatherUnit Unit)
    {
        if(Unit == WeatherUnit.Kelvin)
            return temperature  + " "+ GetUnitSymbol(Unit);
        else if(Unit == WeatherUnit.Fahrenheit)
            return ConvertToFahrenheit(temperature)  + " "+ GetUnitSymbol(Unit);
        else
            return ConvertToCelsius(temperature)  + " "+ GetUnitSymbol(Unit);
    }



    private float ConvertFromFahrenheit(float far)
    {
        float kel = (5/9 * (far - 32) + 273.15f);
        return kel;
    }

    private float ConvertFromCelsius(float cel)
    {
        return (cel+273.15f);
    }


    private float ConvertToFahrenheit(float kel)
    {
        return ((kel - 273.15f) * 9/5) + 32;
    }

    private float ConvertToCelsius(float kel)
    {
        return  kel - 273.15f;
    }


    /**
     * Get unit symbol
     * @param UNIT Unit for your symbol
     * @return symbol char (String)
     */
    public String GetUnitSymbol(WeatherUnit UNIT)
    {
        if(UNIT == WeatherUnit.Celsius)
            return "°C";
        if(UNIT == WeatherUnit.Fahrenheit)
            return "°F";
        else
            return "K";
    }


}
