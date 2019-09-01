package com.myspy.myspyandroid;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.myspy.myspyandroid.variables.CallInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class TabFragment1 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        try {
            UpdateGraphSMS(view);
            UpdateCallLengthGraph(view);
        }catch (Exception ex)
        {
            Log.e("ErrorFragment1",""+ex);
        }
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void UpdateGraphSMS(View view) {
        BarChart mChart;
        mChart = (BarChart) view.findViewById(R.id.chartSMSInfo);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(60);

        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        //mChart.setValueFormatter(new YourFormatter());

        IAxisValueFormatter xAxisFormatter = new CustomFormatter();
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
         l.setExtra(ColorTemplate.MATERIAL_COLORS, new String[] {  getResources().getString(R.string.Received),
                 getResources().getString(R.string.ReceivedAverage), getResources().getString(R.string.Sent), getResources().getString(R.string.SentAverage)});


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(1, MySpyService.serviceVariables.SMSList.size()));
        yVals1.add(new BarEntry(2, MySpyService.specialVariables.SMSReceivedAvg/7));

        yVals1.add(new BarEntry(3, MySpyService.serviceVariables.SMSOutList.size()));
        yVals1.add(new BarEntry(4, MySpyService.specialVariables.SMSSentAvg/7));

        BarDataSet set1;


        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, getResources().getString(R.string.SMSStatistics));
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setValueFormatter(new CustomFormatter());

            mChart.setData(data);

        }
    }




    private void UpdateCallLengthGraph(View view) {

        int inlength = 0;
        int outlength =0;

        for (CallInfo info : MySpyService.serviceVariables.CallList) {
            if(!info.Missed) {

                if(info.OutGoingCall) {
                    outlength += info.CallLength;
                }
                else {
                    inlength += info.CallLength;
                }
            }
        }

        PieChart pieChart = (PieChart) view.findViewById(R.id.chartCallInfo);

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

        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);


        PieDataSet dataSet = new PieDataSet(null,getResources().getString(R.string.CallsLength));
        dataSet.addEntry(new PieEntry(outlength,getResources().getString(R.string.MadeLength)));
        dataSet.addEntry(new PieEntry(inlength,getResources().getString(R.string.ReceivedLength)));
        dataSet.addEntry(new PieEntry(MySpyService.specialVariables.CallTimeWeekIN,getResources().getString(R.string.ReceivedWeekLength)));
        dataSet.addEntry(new PieEntry(MySpyService.specialVariables.CallTimeWeekOUT,getResources().getString(R.string.MadeWeekLength)));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new TimeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        pieChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(10f);
    }



    private class TimeValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public TimeValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            int Minutes = (int) Math.floor(entry.getY()/60);
            int Seconds = ((int)entry.getY()-(Minutes*60));
            return String.format("%02d", Minutes)+":"+String.format("%02d", Seconds)+"  ( "+mFormat.format(value)+" % )"; // e.g. append a dollar-sign
        }
    }



    private class CustomFormatter implements IAxisValueFormatter, IValueFormatter
    {

        private DecimalFormat mFormat;

        public CustomFormatter() {
            mFormat = new DecimalFormat("###");
        }

        // YAxis
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if(value==1)
                return getResources().getString(R.string.Received);
            else if(value == 2)
                return getResources().getString(R.string.ReceivedAverage);
            else if(value == 3)
                return getResources().getString(R.string.Sent);
            else
                return getResources().getString(R.string.SentAverage);
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return ""+(int)value;
        }
    }



}
