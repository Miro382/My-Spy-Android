package com.myspy.myspyandroid.Reports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myspy.myspyandroid.R;
import com.myspy.myspyandroid.functions.ContactName;
import com.myspy.myspyandroid.variables.SMSInfo;
import com.myspy.myspyandroid.variables.SMSInfoRead;
import com.myspy.myspyandroid.variables.ServiceVariables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReadSMSActivity extends Activity {

    final float Bodysize = 16;
    final float Timesize = 12;
    final int DateColor = Color.rgb(127, 140, 141);

    Spinner spinner,spinnerfilter;
    List<String> categories = new ArrayList<String>();
    ServiceVariables srcvrb;
    LinearLayout smslayout;

    List<SMSInfoRead> SMSinfolist = new ArrayList<SMSInfoRead>();

    int lastf = -5 , last = -5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sms);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarreadsms);


        Intent intent = getIntent();
        String var = intent.getStringExtra("Variables");

        Gson gson = new Gson();
        srcvrb = gson.fromJson(var,ServiceVariables.class);

        categories.add(getResources().getString(R.string.All));


        for(SMSInfo info : srcvrb.SMSList)
        {
            if(!categories.contains(info.From))
            {
                categories.add(info.From);
            }

            SMSinfolist.add(new SMSInfoRead(info,false));
        }


        for(SMSInfo info : srcvrb.SMSOutList)
        {
            if(!categories.contains(info.From))
            {
                categories.add(info.From);
            }

            SMSinfolist.add(new SMSInfoRead(info,true));
        }



        for (int i=0;i< SMSinfolist.size();i++)
        {
            for (int j=i+1;j< SMSinfolist.size();j++)
            {
                if(SMSinfolist.get(i).TimeVar>SMSinfolist.get(j).TimeVar)
                {
                    SMSInfoRead temp = SMSinfolist.get(i);
                    SMSinfolist.set(i,SMSinfolist.get(j));
                    SMSinfolist.set(j,temp);
                }

            }
        }


        spinner = (Spinner) findViewById(R.id.spinnerSMS);
        spinnerfilter = (Spinner) findViewById(R.id.spinnerFilter);

        smslayout = (LinearLayout) findViewById(R.id.SMSReadLayout);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,categories);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Selected",""+position+"   "+categories.get(position));
                AddSMSToView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<String> filters = new ArrayList<String>();
        filters.add(getResources().getString(R.string.All));
        filters.add(getResources().getString(R.string.Received));
        filters.add(getResources().getString(R.string.Sent));


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,filters);

        dataAdapter2.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);


        spinnerfilter.setAdapter(dataAdapter2);

        spinnerfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               AddSMSToView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        toolbar.setSubtitle(getResources().getString(R.string.SMSList)+": "+srcvrb.calendar.get(Calendar.DAY_OF_MONTH)+"."+(srcvrb.calendar.get(Calendar.MONTH)+1)+"."+srcvrb.calendar.get(Calendar.YEAR));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        AddSMSToView();
    }


    void AddSMSToView()
    {
        Log.d("SPINNER",""+spinner.getSelectedItem()+"    POS:"+spinner.getSelectedItemPosition());

        if(last != spinner.getSelectedItemPosition() ||  lastf != spinnerfilter.getSelectedItemPosition()) {

            lastf = spinnerfilter.getSelectedItemPosition();
            last = spinner.getSelectedItemPosition();

            smslayout.removeAllViewsInLayout();


            for (SMSInfoRead info : SMSinfolist) {

                if(spinner.getSelectedItemPosition() == 0 || spinner.getSelectedItem().toString().equals(info.From)) {

                    short FilterI = (short) ((info.Sent) ? 2 : 1);
                    if(FilterI == spinnerfilter.getSelectedItemPosition() || spinnerfilter.getSelectedItemPosition()==0) {

                        TextView txt = new TextView(this);
                        txt.setText(info.Body);
                        txt.setTextSize(Bodysize);
                        if (!info.Sent)
                            txt.setTextColor(Color.BLACK);
                        else
                            txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                        TextView txt2 = new TextView(this);
                        txt2.setText(ContactName.GetContactName(this ,info.From) + "   " + info.TimeAction);
                        txt2.setTextSize(Timesize);
                        txt2.setTextColor(DateColor);
                        smslayout.addView(txt);
                        smslayout.addView(txt2);
                    }

                }

            }


        }


    }


}
