package com.myspy.myspyandroid.Reports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.myspy.myspyandroid.BuildConfig;
import com.myspy.myspyandroid.R;
import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.variables.LocationPoint;
import com.myspy.myspyandroid.variables.ServiceVariables;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MapPathReportActivity extends Activity {

    ServiceVariables srcvrb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_path_report);

        Intent intent = getIntent();
        String fileloc = intent.getStringExtra("File");

        ClassSaver classSaver = new ClassSaver();

        srcvrb = (ServiceVariables) classSaver.LoadClassFromSpecificFile(ServiceVariables.class,new File(fileloc));

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarmap);

        toolbar.setSubtitle(""+srcvrb.calendar.get(Calendar.DAY_OF_MONTH)+"."+(srcvrb.calendar.get(Calendar.MONTH)+1)+"."+srcvrb.calendar.get(Calendar.YEAR));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(9);

        ArrayList<GeoPoint> points = new ArrayList<>();

        for (LocationPoint locationPoint : srcvrb.Path)
        {
            points.add(locationPoint.ToGeoPoint());
        }

        if(!points.isEmpty())
        mapController.setCenter(points.get(0));
        else
            mapController.setCenter(new GeoPoint(0.0,0.0));

        Log.d("Points",""+points.size());

        Polyline polyline = new Polyline();
        polyline.setColor(Color.rgb(192, 57, 43));
        polyline.setPoints(points);

        map.getOverlays().add(polyline);
        map.invalidate();
    }
}
