package com.myspy.myspyandroid.Reports;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.myspy.myspyandroid.R;

import java.io.File;

public class ChooseReportActivity extends AppCompatActivity {

    int ReportCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_report);

        LinearLayout reports =  (LinearLayout) findViewById(R.id.DayReports);


        Intent intent = getIntent();

        ReportCode = intent.getIntExtra("Report",0);




        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e)
        {
            Log.w("Error",""+e);
        }

        File dirn  = new File( this.getDir("MySpy", Context.MODE_PRIVATE).getPath()+"/Monitoring");
        File[] files = dirn.listFiles();


        int k=1;
        Log.d("Files",""+files.length);
        for(File f : files)
        {

            Log.d("SMS FILE",""+f.getPath());
            Button button = new Button(this);
            button.setText(f.getName().replace(".dat","").replace("_",".").replace("Day.",""));
            button.setTag(f.getPath());
            button.setOnClickListener(onClickListener);
            button.setTextColor(Color.WHITE);
            if((k%2)==0)
            button.setBackgroundResource(R.drawable.buttonstyle);
            else
                button.setBackgroundResource(R.drawable.buttonstylelight);

            reports.addView(button);
            k++;

        }


    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
           Log.d("Report",""+v.getTag());
            Intent intent = null;
            boolean newActivity = true;

                if (ReportCode==1)
                 intent = new Intent(ChooseReportActivity.this, SMSReportActivity.class);
                else if (ReportCode==2)
                 intent = new Intent(ChooseReportActivity.this, CallsReportActivity.class);
                else if (ReportCode==3)
                    intent = new Intent(ChooseReportActivity.this, MapPathReportActivity.class);
                else if (ReportCode==4) {
                    newActivity = false;
                    Remove(v.getTag().toString(),((Button)v).getText().toString());
                }
                else if (ReportCode==5) {
                    intent = new Intent(ChooseReportActivity.this, ApplicationReportActivity.class);
                }
                else
                    intent = new Intent(ChooseReportActivity.this, SMSReportActivity.class);

                if(newActivity) {
                    intent.putExtra("File", "" + v.getTag());
                    ChooseReportActivity.this.startActivity(intent);
                }
        };
    };



    void Remove(String filelocation, final String name)
    {

        final File file = new File(filelocation);
        final Context cont = this;

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.Removereport))
                .setMessage(getResources().getString(R.string.Removereporttext)+ " "+ name+" ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(file.delete())
                        {
                            Log.d("FileDeleted","Deleted "+file.getAbsolutePath());
                            Toast.makeText(cont,getResources().getString(R.string.Removed),Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Log.d("FailedDeleting","Failed "+file.getAbsolutePath());
                            Toast.makeText(cont,getResources().getString(R.string.FailedRemoveReport),Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }



}
