package com.myspy.myspyandroid.variables;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Miroslav Murin on 23.12.2016.
 */

public class CallInfo {

    public String Number = "";
    public String DateTime = "", DateTimeOnly = "";
    public boolean OutGoingCall = false;
    public int CallLength = 0;
    public boolean Missed = false;

    /**
     * New CallInfo
     * @param number Number of call
     * @param outGoingCall Out going call?
     * @param length Length of call in seconds
     * @param time Time when call started
     */
    public CallInfo(String number, boolean outGoingCall,int length, String time)
    {
        Number = number;
        OutGoingCall = outGoingCall;
        CallLength = length;
        DateTime = time;
        Missed = false;
        DateTimeOnly = GetCurrentTimeOnly();
    }

    /**
     * New CallInfo
     * @param number Number of call
     * @param outGoingCall Out going call?
     * @param length Length of call in seconds
     */
    public CallInfo(String number, boolean outGoingCall,int length)
    {
        Number = number;
        OutGoingCall = outGoingCall;
        CallLength = length;
        Missed = false;
        DateTimeOnly = GetCurrentTimeOnly();
    }

    /**
     * New CallInfo
     * @param number Number of Call
     * @param time Time of Call
     */
    public CallInfo(String number, String time)
    {
        Number = number;
        OutGoingCall = false;
        CallLength = 0;
        DateTime = time;
        Missed = true;
        DateTimeOnly = GetCurrentTimeOnly();
    }


    /**
     * Return current time in specific format : HH:mm:ss
     * @return String
     */
    public static String GetCurrentTimeOnly()
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
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
