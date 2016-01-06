package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.List;

public class InstalledApplicationActivity extends ListActivity {
    DBHelper dbHelper = new DBHelper(InstalledApplicationActivity.this);
    InstalledAppAdapter adapter;
    List<String> InstalledApps;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        InstalledApps=dbHelper.GetInstalledApps();
        adapter=new InstalledAppAdapter(getApplicationContext(),R.layout.row_installed_app,InstalledApps);
        setListAdapter(adapter);

    }
}
