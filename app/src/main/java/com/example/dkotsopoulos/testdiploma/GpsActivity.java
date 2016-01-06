package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;

public class GpsActivity extends ListActivity {

    List<GPSObject> NewGPS;
    GPSAdapter adapter;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(GpsActivity.this);
        NewGPS = dbHelper.GetAllGPS();
        if (NewGPS.size() != 0)
        {
            adapter = new GPSAdapter(getApplicationContext(), R.layout.row_gps, NewGPS);
            setListAdapter(adapter);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No available data is retrieved!",
                    Toast.LENGTH_LONG).show();
        }

    }

}
