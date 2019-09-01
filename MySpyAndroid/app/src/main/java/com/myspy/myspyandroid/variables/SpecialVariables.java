package com.myspy.myspyandroid.variables;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Miroslav Murin on 20.01.2017.
 */

public class SpecialVariables {
    public int SMSSentAvg = 0, SMSReceivedAvg = 0,CallTimeWeekIN = 0,CallTimeWeekOUT = 0;
    private boolean Reset = false;
    public String LastSentSMS = "";
    public ArrayList<ApplicationPackage> Blockedapps= new ArrayList<ApplicationPackage>();


    public void ResetAverage()
    {
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.DAY_OF_WEEK)== Calendar.MONDAY && !Reset) {
            SMSSentAvg = 0;
            SMSReceivedAvg = 0;
            CallTimeWeekIN = 0;
            CallTimeWeekOUT = 0;
            Reset = true;
        }

        if(calendar.get(Calendar.DAY_OF_WEEK)!= Calendar.MONDAY) {
            Reset = false;
        }

    }
}
