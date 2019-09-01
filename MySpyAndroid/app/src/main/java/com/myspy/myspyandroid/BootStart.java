package com.myspy.myspyandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Miroslav Murin on 25.11.2016.
 */

public class BootStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MySpyService.class);
        context.startService(myIntent);
    }
}
