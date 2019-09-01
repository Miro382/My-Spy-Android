package com.myspy.myspyandroid.variables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Miroslav Murin on 30.11.2016.
 */

public class ServiceVariables {

    public Calendar calendar;

    public List<SMSInfo> SMSList = new ArrayList<SMSInfo>();
    public List<SMSInfo> SMSOutList = new ArrayList<SMSInfo>();
    public List<CallInfo> CallList = new ArrayList<CallInfo>();
    public List<LocationPoint> Path = new ArrayList<LocationPoint>();
    public List<ApplicationInformation> Appinfo = new ArrayList<ApplicationInformation>();


    public ServiceVariables()
    {
        calendar = GetCurrentTime();
    }


    private Calendar GetCurrentTime()
    {
        return Calendar.getInstance();
    }


}
