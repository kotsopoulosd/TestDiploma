package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.List;

public class LightActivity extends ListActivity
{
    List<LightObject> NewLight;
    LightAdapter adapter;
    DBHelper dbHelper=new DBHelper(LightActivity.this);

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        NewLight=dbHelper.GetAllLightLogs();
        adapter=new LightAdapter(getApplicationContext(),R.layout.row_light,NewLight);
        setListAdapter(adapter);
    }

}
