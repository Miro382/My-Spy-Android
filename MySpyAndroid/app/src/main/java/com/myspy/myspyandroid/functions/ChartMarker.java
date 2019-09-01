package com.myspy.myspyandroid.functions;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.myspy.myspyandroid.R;

/**
 * Created by Miroslav Murin on 17.12.2016.
 */

public class ChartMarker extends MarkerView {

    private TextView textchart;
    final private String Number = getResources().getString(R.string.Number),
    SMS = getResources().getString(R.string.OverallSMS);

    public ChartMarker (Context context, int layoutResource) {
        super(context, layoutResource);

        textchart = (TextView) findViewById(R.id.MarkerText);
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        textchart.setText(Number+": " + e.getData()+"   "+SMS+": "+(int)e.getY());

        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}