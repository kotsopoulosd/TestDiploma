package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

/**
 * Created by DKotsopoulos on 03/09/2015.
 */
public class GyroscopeActivity extends ListActivity
{

    List<GyroscopeObject> NewXGYRO;
    GyroscopeAdapter adapter;
    DBHelper dbHelper=new DBHelper(GyroscopeActivity.this);

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        NewXGYRO=dbHelper.GetAllGyroscope();

        if( NewXGYRO.size()!= 0) {

            adapter = new GyroscopeAdapter(getApplicationContext(), R.layout.row_gyroscope, NewXGYRO);
            setListAdapter(adapter);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No available data is retrieved!",
                    Toast.LENGTH_LONG).show();
        }
    }

}