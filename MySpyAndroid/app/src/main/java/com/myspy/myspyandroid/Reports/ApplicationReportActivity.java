
package com.myspy.myspyandroid.Reports;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.myspy.myspyandroid.R;
import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.variables.ApplicationInformation;
import com.myspy.myspyandroid.variables.CallInfo;
import com.myspy.myspyandroid.variables.ServiceVariables;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;

public class ApplicationReportActivity extends Activity {

    ServiceVariables srcvrb;

    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_report);

        Intent intent = getIntent();
        String fileloc = intent.getStringExtra("File");

        ClassSaver classSaver = new ClassSaver();

        srcvrb = (ServiceVariables) classSaver.LoadClassFromSpecificFile(ServiceVariables.class,new File(fileloc));

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarappreport);

        toolbar.setSubtitle(""+srcvrb.calendar.get(Calendar.DAY_OF_MONTH)+"."+(srcvrb.calendar.get(Calendar.MONTH)+1)+"."+srcvrb.calendar.get(Calendar.YEAR));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        drawTable();

    }


    void drawTable()
    {
        TableLayout table = (TableLayout) findViewById(R.id.tablelayoutApps);
        table.removeAllViewsInLayout();

        Log.d("APPCOUNT","Size: "+srcvrb.Appinfo.size());

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(10,5,10,5);


        TableRow rowinfo = new TableRow(this);

        rowinfo.setBackgroundColor(getResources().getColor(R.color.colorBox2));
        rowinfo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        TextView textViewhinticon = new TextView(this);
        textViewhinticon.setText("");
        textViewhinticon.setTextSize(20f);
        rowinfo.addView(textViewhinticon);

        TextView textViewhint = new TextView(this);
        textViewhint.setText(getResources().getString(R.string.ApplicationName));
        textViewhint.setGravity(Gravity.CENTER);
        textViewhint.setLayoutParams(params);
        textViewhint.setTextColor(Color.WHITE);
        textViewhint.setTextSize(15f);
        rowinfo.addView(textViewhint);


        TextView textViewhint2 = new TextView(this);
        textViewhint2.setText(getResources().getString(R.string.RunningTime));
        textViewhint2.setGravity(Gravity.CENTER);
        textViewhint2.setLayoutParams(params);
        textViewhint2.setTextColor(Color.WHITE);
        textViewhint2.setTextSize(15f);
        rowinfo.addView(textViewhint2);


        TextView textViewhint3 = new TextView(this);
        textViewhint3.setText(getResources().getString(R.string.LastTimeRunning));
        textViewhint3.setGravity(Gravity.CENTER);
        textViewhint3.setLayoutParams(params);
        textViewhint3.setTextColor(Color.WHITE);
        textViewhint3.setTextSize(15f);
        rowinfo.addView(textViewhint3);



        table.addView(rowinfo);


        for (ApplicationInformation info : srcvrb.Appinfo) {




            TableRow row = new TableRow(this);

            try {
            ImageView imageView = new ImageView(this);
            Drawable icon = null;
                icon = getPackageManager().getApplicationIcon(info.PackageName);
                imageView.setImageDrawable(icon);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(64, 64);
                layoutParams.setMargins(8,5,3,5);
                layoutParams.gravity = Gravity.LEFT;
                imageView.setLayoutParams(layoutParams);
                row.addView(imageView);
            } catch (Exception ex) {
                Log.w("Error",""+ex);
                Space space = new Space(this);
                row.addView(space);
            }


            TextView textView = new TextView(this);
            textView.setText(info.ApplicationName);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(16f);
            row.addView(textView);

            TextView textView2 = new TextView(this);
            textView2.setText(info.TimeFormat());
            textView2.setGravity(Gravity.CENTER);
            textView2.setTextColor( getResources().getColor(R.color.colorMarker));
            textView2.setLayoutParams(params);
            row.addView(textView2);


            TextView textView3 = new TextView(this);
            textView3.setText(info.LastTimeRunning);
            textView3.setGravity(Gravity.CENTER);
            textView3.setLayoutParams(params);
            row.addView(textView3);

            table.addView(row);
        }
    }


}
