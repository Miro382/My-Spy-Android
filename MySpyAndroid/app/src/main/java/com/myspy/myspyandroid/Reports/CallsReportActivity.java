package com.myspy.myspyandroid.Reports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.myspy.myspyandroid.R;
import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.functions.ContactName;
import com.myspy.myspyandroid.variables.CallInfo;
import com.myspy.myspyandroid.variables.ServiceVariables;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

public class CallsReportActivity extends Activity {

    ServiceVariables srcvrb;
    TableLayout table;

    int out=0,in=0,missed=0;

    long inlength = 0, outlength = 0;
    DecimalFormat decimalFormat = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_report);

        Intent intent = getIntent();
        String fileloc = intent.getStringExtra("File");

        ClassSaver classSaver = new ClassSaver();

        srcvrb = (ServiceVariables) classSaver.LoadClassFromSpecificFile(ServiceVariables.class,new File(fileloc));

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarcalls);

        toolbar.setSubtitle(""+srcvrb.calendar.get(Calendar.DAY_OF_MONTH)+"."+(srcvrb.calendar.get(Calendar.MONTH)+1)+"."+srcvrb.calendar.get(Calendar.YEAR));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        table = (TableLayout) findViewById(R.id.tablelayoutCall);

        DrawLayout();
        MakeGraph();
        MakeGraphLength();

    }


    void DrawLayout()
    {
        table.removeAllViewsInLayout();

        missed=0;
        out=0;
        in=0;

        outlength = 0;
        inlength = 0;

        TableRow rowinfo = new TableRow(this);

        rowinfo.addView(new Space(this));

        TextView textViewa = new TextView(this);
        textViewa.setText(getResources().getString(R.string.Number));
        textViewa.setTextSize(15);
        textViewa.setTextColor(Color.BLACK);
        rowinfo.addView(textViewa);


        TextView textViewa2 = new TextView(this);
        textViewa2.setText(getResources().getString(R.string.Length));
        textViewa2.setTextSize(15);
        textViewa2.setTextColor(Color.rgb(22, 160, 133));
        textViewa2.setPadding(30,0,0,0);
        rowinfo.addView(textViewa2);

        TextView textViewa3 = new TextView(this);
        textViewa3.setText(getResources().getString(R.string.Time));
        textViewa3.setTextSize(15);
        textViewa3.setTextColor(Color.GRAY);
        textViewa3.setPadding(25,0,0,0);
        rowinfo.addView(textViewa3);

        rowinfo.setBackgroundColor(Color.rgb(189, 195, 199));

        table.addView(rowinfo);


        for (CallInfo info : srcvrb.CallList) {

            TableRow row = new TableRow(this);

            ImageView imageView = new ImageView(this);
            if(info.Missed) {
                imageView.setImageResource(R.drawable.callmissed);
                missed++;
            }
            else{
                if(info.OutGoingCall) {
                    imageView.setImageResource(R.drawable.callmade);
                    out++;
                    outlength += info.CallLength;
                }
                else {
                    imageView.setImageResource(R.drawable.callreceived);
                    in++;
                    inlength += info.CallLength;
                }

            }
            imageView.setPadding(6,10,10,0);
            row.addView(imageView);
            imageView.getLayoutParams().width = 42;
            imageView.getLayoutParams().height = 42;


            int Minutes = (int) Math.floor(info.CallLength/60);
            int Seconds = (info.CallLength-(Minutes*60));

            TextView textView = new TextView(this);
            textView.setText(ContactName.GetContactName(this ,info.Number));
            textView.setPadding(0,5,0,0);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            row.addView(textView);


            TextView textViewt = new TextView(this);
            if(!info.Missed)
            textViewt.setText(decimalFormat.format( Minutes)+":"+decimalFormat.format( Seconds));
            else
                textViewt.setText("---");

            textViewt.setPadding(30,0,0,0);
            textViewt.setTextSize(18);
            textViewt.setTextColor(Color.rgb(22, 160, 133));
            row.addView(textViewt);

            TextView textView2 = new TextView(this);
            textView2.setText(info.DateTimeOnly);
            textView2.setPadding(20,0,0,0);
            textView2.setTextSize(12);
            textView2.setTextColor(Color.GRAY);
            row.addView(textView2);

            table.addView(row);

        }

    }



    void MakeGraph()
    {

        PieChart pieChart = (PieChart) findViewById(R.id.chartcall);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(62f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);

        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);




        PieDataSet dataSet = new PieDataSet(null,getResources().getString(R.string.CallsCountStatistics));
        dataSet.addEntry(new PieEntry(in,getResources().getString(R.string.CallReceived)));
        dataSet.addEntry(new PieEntry(out,getResources().getString(R.string.CallMade)));
        dataSet.addEntry(new PieEntry(missed,getResources().getString(R.string.CallMissed)));
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new OnlyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(10f);
    }




    void MakeGraphLength()
    {

        PieChart pieChart = (PieChart) findViewById(R.id.chartcalllength);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(62f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);

        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);


        PieDataSet dataSet = new PieDataSet(null,getResources().getString(R.string.CallsLength));
        dataSet.addEntry(new PieEntry(outlength,getResources().getString(R.string.MadeLength)));
        dataSet.addEntry(new PieEntry(inlength,getResources().getString(R.string.ReceivedLength)));
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new TimeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(10f);
    }




    public class OnlyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        OnlyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int)entry.getY()+"  ( "+mFormat.format(value)+" % )";
        }
    }


    public class TimeValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        TimeValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            int Minutes = (int) Math.floor(entry.getY()/60);
            int Seconds = ((int)entry.getY()-(Minutes*60));
            return decimalFormat.format(Minutes)+":"+decimalFormat.format(Seconds)+"  ( "+mFormat.format(value)+" % )";
        }
    }


}
