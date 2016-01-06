package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.List;

/**
 * Created by DKotsopoulos on 03/09/2015.
 */
public class AccelerometerActivity extends ListActivity
{

    List<AccelerometerObject> NewXYZ;
    AccelerometerAdapter adapter;
    DBHelper dbHelper=new DBHelper(AccelerometerActivity.this);

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        NewXYZ=dbHelper.GetAllAcceleration();
        adapter=new AccelerometerAdapter(getApplicationContext(),R.layout.row_acceleration,NewXYZ);
        setListAdapter(adapter);
    }

}