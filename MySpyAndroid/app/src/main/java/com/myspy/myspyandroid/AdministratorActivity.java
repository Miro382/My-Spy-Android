package com.myspy.myspyandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.myspy.myspyandroid.Reports.ChooseReportActivity;
import com.myspy.myspyandroid.Weather.WeatherUnit;
import com.myspy.myspyandroid.functions.ClassSaver;

import java.util.Calendar;

public class AdministratorActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);


        Intent intent = getIntent();
        String value = intent.getStringExtra("Token");
        Log.d("Value",value);


        if(!value.equals(calendar.get(Calendar.DAY_OF_YEAR)+"-"+calendar.get(Calendar.HOUR))) {
            Toast.makeText(this, getResources().getString(R.string.Error),Toast.LENGTH_LONG).show();
            finish();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public void ToSMSReport(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, ChooseReportActivity.class);
        intent.putExtra("Report",1);
        AdministratorActivity.this.startActivity(intent);
    }


    public void ToCallsReport(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, ChooseReportActivity.class);
        intent.putExtra("Report",2);
        AdministratorActivity.this.startActivity(intent);
    }


    public void ToMapReport(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, ChooseReportActivity.class);
        intent.putExtra("Report",3);
        AdministratorActivity.this.startActivity(intent);
    }

    public void ToAppReport(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, ChooseReportActivity.class);
        intent.putExtra("Report",5);
        AdministratorActivity.this.startActivity(intent);
    }

    public void ToRemoveReport(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, ChooseReportActivity.class);
        intent.putExtra("Report",4);
        AdministratorActivity.this.startActivity(intent);
    }

    public void ToChangePassword(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, ChangePasswordActivity.class);
        AdministratorActivity.this.startActivity(intent);
    }


    public void ToWebLogin(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, WebLogin.class);
        AdministratorActivity.this.startActivity(intent);
    }


    public void SaveSettings(View view)
    {
        MySpyService.serviceSettings.MonitorCalls = ((CheckBox)findViewById(R.id.checkBoxcalls)).isChecked();
        MySpyService.serviceSettings.MonitorSMS = ((CheckBox)findViewById(R.id.checkBoxsms)).isChecked();
        MySpyService.serviceSettings.MonitorPosition = ((CheckBox)findViewById(R.id.checkBoxlocation)).isChecked();
        MySpyService.serviceSettings.ShowWeather = ((CheckBox)findViewById(R.id.checkBoxshoweather)).isChecked();
        MySpyService.serviceSettings.MonitorApplications = ((CheckBox)findViewById(R.id.checkBoxmonitorapps)).isChecked();
        MySpyService.serviceSettings.BlockSettings = ((CheckBox)findViewById(R.id.checkBoxblocksettings)).isChecked();
        MySpyService.serviceSettings.WebData = ((CheckBox)findViewById(R.id.checkBoxWebSend)).isChecked();
        MySpyService.serviceSettings.Alias = ((EditText)findViewById(R.id.editTextAlias)).getText().toString();

        if(((RadioButton)findViewById(R.id.radioButtonCelsius)).isChecked())
        MySpyService.serviceSettings.weatherUnit = WeatherUnit.Celsius;
        else
            MySpyService.serviceSettings.weatherUnit = WeatherUnit.Fahrenheit;

        ClassSaver classSaver =  new ClassSaver();
        classSaver.SaveClassToFile(MySpyService.serviceSettings,"MySpy","Settings.dat",this);
    }



    public void ToBlockApplications(View view)
    {
        Intent intent = new Intent(AdministratorActivity.this, BlockAppsActivity.class);
        AdministratorActivity.this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_administrator, menu);
        return true;
    }













    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new TabFragment1();
                case 1:
                    return new TabFragment2();
                case 2:
                    return new TabFragment3();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.Tab1);
                case 1:
                    return getResources().getString(R.string.Tab2);
                case 2:
                    return getResources().getString(R.string.Tab3);
            }
            return null;
        }
    }
}
