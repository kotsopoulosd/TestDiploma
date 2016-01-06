package com.example.dkotsopoulos.testdiploma;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Created by DKotsopoulos on 08/10/2015.
 */
public class MapActivity extends FragmentActivity {
    private static final String TAG_SERVICE = "GroupingLocations";
    private GoogleMap map;
    public static LatLng Thessaloniki = new LatLng(40.626340, 22.948351);
    LatLng Home = new LatLng(0, 0);
    LatLng Work = new LatLng(0, 0);
    String[]  newString= new String[2];
    String[] partshome = new String[2];
    String[] partswork = new String[2];

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
        } else {

            newString= extras.getStringArray("Geolocations");
            partshome = newString[0].split(",");
            partswork = newString[1].split(",");
            Home= new LatLng(Double.parseDouble(partshome[0]),Double.parseDouble(partshome[1]));
            Work= new LatLng(Double.parseDouble(partswork[0]),Double.parseDouble(partswork[1]));

        }

        setContentView(R.layout.activity_group_gps_locations);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.addMarker(new MarkerOptions().position(Work).title("Work"));
        map.addMarker(new MarkerOptions().position(Home).title("Home"));

        // Move the camera instantly to Thessaloniki with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Thessaloniki, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

    }
}

