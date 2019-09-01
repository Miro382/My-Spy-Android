package com.myspy.myspyandroid.variables;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by Miroslav Murin on 03.02.2017.
 */

public class ApplicationInformation extends ApplicationPackage {

    public int Time = 0;
    public String LastTimeRunning = "";

    public ApplicationInformation(String packageName, String applicationName) {
        super(packageName, applicationName);
        Time = 0;
    }

    public ApplicationInformation(String packageName, String applicationName,int time)
    {
        super(packageName,applicationName);
        Time = time;
    }

    /**
     * Add time in seconds and automatically set Last Time Running
     * @param time seconds
     */
    public void AddTime(int time)
    {
        Time += time;
        LastTimeRunning = GetCurrentTime();
    }

    /**
     * Returns HH:MM:SS string of app running time
     * @return String HH:MM:SS
     */
    public String TimeFormat()
    {
        int Hours = (int) Math.floor(Time/3600);
        int Minutes = (int) Math.floor((Time-(Hours*3600))/60);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(Hours)+":"+decimalFormat.format(Minutes);
    }


    /**
     * Get current time in HH:mm:ss format
     * @return String time in HH:mm:ss format
     */
    public static String GetCurrentTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
    }


    /**
     * Check if arraylist contains package name
     * @param Applications ApplicationInformation ArrayList
     * @param PackageName PackageName to check
     * @return Boolean - true if contains
     */
    public static boolean ContainsPackageName(List<ApplicationInformation> Applications, String PackageName)
    {

        for(ApplicationInformation app : Applications){

            if(PackageName.equals(app.PackageName))
                return true;
        }

        return false;
    }



    /**
     * Get position in arraylist containing specific package name
     * @param Applications ApplicationInformation ArrayList
     * @param PackageName PackageName to check
     * @return int position
     */
    public static int GetPosition(List<ApplicationInformation> Applications, String PackageName)
    {
        for(int i=0;i<Applications.size();i++)
        {
            if(PackageName.equals(Applications.get(i).PackageName))
                return i;
        }

        return -1;
    }


}
