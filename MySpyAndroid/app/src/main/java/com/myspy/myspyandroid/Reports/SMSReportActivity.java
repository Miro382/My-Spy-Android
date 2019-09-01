package com.myspy.myspyandroid.Reports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.myspy.myspyandroid.R;
import com.myspy.myspyandroid.functions.ChartMarker;
import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.functions.ContactName;
import com.myspy.myspyandroid.variables.SMSInfo;
import com.myspy.myspyandroid.variables.ServiceVariables;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SMSReportActivity extends Activity {

    ServiceVariables srcvrb;

    private Map<String,Integer> smsmap = new HashMap<String, Integer>();
    private Map<String,Integer> smsoutmap = new HashMap<String, Integer>();
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreport);


        Intent intent = getIntent();
        String fileloc = intent.getStringExtra("File");

        ClassSaver classSaver = new ClassSaver();

        srcvrb = (ServiceVariables) classSaver.LoadClassFromSpecificFile(ServiceVariables.class,new File(fileloc));

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbardate);
        ((TextView)findViewById(R.id.smsreceived)).setText(getResources().getString(R.string.SMSReceived)+": "+srcvrb.SMSList.size());
        ((TextView)findViewById(R.id.smssended)).setText(getResources().getString(R.string.SMSSended)+": "+srcvrb.SMSOutList.size());

        toolbar.setSubtitle(""+srcvrb.calendar.get(Calendar.DAY_OF_MONTH)+"."+(srcvrb.calendar.get(Calendar.MONTH)+1)+"."+srcvrb.calendar.get(Calendar.YEAR));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        int count = 0;
        for(SMSInfo inf : srcvrb.SMSList)
        {
            if(smsmap.containsKey(inf.From))
            {
                count = smsmap.get(inf.From);
                smsmap.remove(inf.From);
                smsmap.put(inf.From,count+1);
            }else
            {
                smsmap.put(inf.From,1);
            }
        }

        for(SMSInfo inf : srcvrb.SMSOutList)
        {
            if(smsoutmap.containsKey(inf.From))
            {
                count = smsoutmap.get(inf.From);
                smsoutmap.remove(inf.From);
                smsoutmap.put(inf.From,count+1);
            }else
            {
                smsoutmap.put(inf.From,1);
            }
        }

        SetSMSStatsChart();
        SetNumbersInfoChart();

    }



    void SetSMSStatsChart()
    {
        PieChart SMSStatschart = (PieChart) findViewById(R.id.chartSMSStats);

        SMSStatschart.setUsePercentValues(true);
        SMSStatschart.getDescription().setEnabled(false);
        SMSStatschart.setExtraOffsets(5, 10, 5, 5);

        SMSStatschart.setDragDecelerationFrictionCoef(0.95f);


        SMSStatschart.setDrawHoleEnabled(true);
        SMSStatschart.setHoleColor(Color.WHITE);

        SMSStatschart.setTransparentCircleColor(Color.WHITE);
        SMSStatschart.setTransparentCircleAlpha(110);

        SMSStatschart.setHoleRadius(58f);
        SMSStatschart.setTransparentCircleRadius(62f);

        SMSStatschart.setDrawCenterText(true);

        SMSStatschart.setRotationAngle(0);

        SMSStatschart.setRotationEnabled(true);
        SMSStatschart.setHighlightPerTapEnabled(true);




        PieDataSet dataSet = new PieDataSet(null,getResources().getString(R.string.SMSStatistics));
        dataSet.addEntry(new PieEntry(srcvrb.SMSList.size(),getResources().getString(R.string.SMSReceived)));
        dataSet.addEntry(new PieEntry(srcvrb.SMSOutList.size(),getResources().getString(R.string.SMSSended)));
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        SMSStatschart.setData(data);

        SMSStatschart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        Legend l = SMSStatschart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);







        SMSStatschart.setEntryLabelColor(Color.BLACK);

        SMSStatschart.setEntryLabelTextSize(10f);
    }




    void SetNumbersInfoChart()
    {

        BarChart mChart = (BarChart) findViewById(R.id.SMSInfochart);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);


        ChartMarker mv = new ChartMarker(this, R.layout.chartmarkerlayout);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart


        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setHighlightFullBarEnabled(false);

        mChart.getAxisLeft().setEnabled(false);
       // mChart.getAxisRight().setAxisMaximum(25f);
        mChart.getAxisRight().setAxisMinimum(0f);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setDrawZeroLine(true);
        mChart.getAxisRight().setLabelCount(7, false);
        mChart.getAxisRight().setValueFormatter(new CustomFormatter());
        mChart.getAxisRight().setTextSize(9f);

        mChart.animateY(1500);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();


        int kl=1;
        for(Map.Entry<String, Integer> entry : smsmap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            int valueout = 0;

            if(smsoutmap.containsKey(key))
            {
              valueout =  smsoutmap.get(key);
                smsoutmap.remove(key);
            }

            Log.d("ENTRY",key+": "+value+"    "+valueout);

            yValues.add(new BarEntry(kl, new float[]{ value,valueout },key));
            kl++;
        }


        for(Map.Entry<String, Integer> entry : smsoutmap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            Log.d("ENTRY",key+": "+value);


            yValues.add(new BarEntry(kl, new float[]{ 0,value },key));
            kl++;
        }





        BarDataSet set = new BarDataSet(yValues, getResources().getString(R.string.SMSMessaging));
        set.setValueFormatter(new CustomFormatter());
        set.setValueTextSize(7f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColors(new int[] {Color.rgb(67,67,72), Color.rgb(124,181,236)});
        set.setStackLabels(new String[]{
                getResources().getString(R.string.Received),getResources().getString(R.string.Sent)
        });


        BarData data = new BarData(set);

        mChart.setData(data);
        mChart.invalidate();

    }



    private class CustomFormatter implements IValueFormatter, IAxisValueFormatter
    {

        private DecimalFormat mFormat;

        public CustomFormatter() {
            mFormat = new DecimalFormat("###");
        }

        // data
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(Math.abs(value)) + " "+getResources().getString(R.string.SMS_Info_Number)+" "+ ContactName.GetContactName(context,entry.getData().toString());
        }

        // YAxis
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(Math.abs(value));
        }
    }



    public void ReadSMS(View view)
    {
        Intent intent = new Intent(SMSReportActivity.this, ReadSMSActivity.class);
        Gson gson = new Gson();
        intent.putExtra("Variables",""+gson.toJson(srcvrb));
        SMSReportActivity.this.startActivity(intent);
    }


}
