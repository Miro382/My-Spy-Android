package com.myspy.myspyandroid;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myspy.myspyandroid.variables.ApplicationPackage;

import java.util.ArrayList;
import java.util.List;

//Miroslav Murin

public class BlockAppsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_apps);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e)
        {
            Log.w("Error",""+e);
        }

        final PackageManager pm = getPackageManager();

        ListView listViewapp = (ListView) findViewById(R.id.ListApps);
        final ListView listViewblockedapp = (ListView) findViewById(R.id.ListBlockApps);

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        final ArrayList<ApplicationPackage> installedapps = new ArrayList<ApplicationPackage>();


        for (ApplicationInfo packageInfo : packages) {

            if(!packageInfo.packageName.equals(getApplicationContext().getPackageName())) {
                installedapps.add(new ApplicationPackage( packageInfo.packageName+"",packageInfo.loadLabel(pm)+""));
            }
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, ApplicationPackage.ApplicationName(installedapps));

        listViewapp.setAdapter(arrayAdapter);



        final ArrayAdapter<String> arraybAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, ApplicationPackage.ApplicationName(MySpyService.specialVariables.Blockedapps));

        listViewblockedapp.setAdapter(arraybAdapter);

        final Context context = this;

        listViewapp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("ClickedOn",installedapps.get(position).PackageName);
                if(!MySpyService.specialVariables.Blockedapps.contains(new ApplicationPackage(installedapps.get(position).PackageName,installedapps.get(position).ApplicationName)))
                MySpyService.specialVariables.Blockedapps.add(new ApplicationPackage(installedapps.get(position).PackageName,installedapps.get(position).ApplicationName));

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_list_item_1, ApplicationPackage.ApplicationName(MySpyService.specialVariables.Blockedapps));

                listViewblockedapp.setAdapter(arrayAdapter);

            }
        });


        listViewblockedapp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("ClickedOn",MySpyService.specialVariables.Blockedapps.get(position).PackageName);
                MySpyService.specialVariables.Blockedapps.remove(position);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_list_item_1, ApplicationPackage.ApplicationName(MySpyService.specialVariables.Blockedapps));

                listViewblockedapp.setAdapter(arrayAdapter);

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
