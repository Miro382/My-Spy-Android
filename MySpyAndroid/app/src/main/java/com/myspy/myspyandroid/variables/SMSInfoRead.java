package com.myspy.myspyandroid.variables;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Miroslav Murin on 21.12.2016.
 */

public class SMSInfoRead  extends SMSInfo{

    public int TimeVar = 0;
    public boolean Sent = false;
    public String TimeAction = "";


    /**
     *
     * @param info SMSInfo. Your SMS you want to read
     * @param sent Sent or received
     */
    public SMSInfoRead(SMSInfo info,boolean sent) {
        super(info.From,info.Body);

        Sent = sent;
        Time = info.Time;

        Calendar time = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            time.setTime(sdf.parse(info.Time));
        } catch (Exception ex) {
            Log.w("SMSinforead",""+ex);
        }

        TimeVar = (time.get(Calendar.HOUR_OF_DAY)*3600)+(time.get(Calendar.MINUTE)*60)+time.get(Calendar.SECOND);
        TimeAction = String.format(Locale.getDefault(),"%02d", time.get(Calendar.HOUR_OF_DAY))+":"+String.format(Locale.getDefault(),"%02d", time.get(Calendar.MINUTE))+":"+String.format(Locale.getDefault(),"%02d", time.get(Calendar.SECOND));
    }

}
