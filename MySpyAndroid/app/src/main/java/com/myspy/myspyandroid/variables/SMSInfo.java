package com.myspy.myspyandroid.variables;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Miroslav Murin on 30.11.2016.
 */

public class SMSInfo {
    public String Body = "";
    public String From = "";
    public String Time = "";


    /**
     *
     * @param from Number from you received SMS
     * @param body Body of SMS
     * @param time Time of received SMS
     */
    public SMSInfo(String from, String body, String time)
    {
        Body = body;
        From = from;
        Time = time;
    }

    /**
     *
     * @param from Number from you received SMS
     * @param body  Body of SMS
     */
    public SMSInfo(String from, String body)
    {
        Body = body;
        From = from;
    }


    /**
     * Return current time in specific format : EEE MMM dd HH:mm:ss z yyyy
     * @return String
     */
    public static String GetCurrentTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
    }


}
