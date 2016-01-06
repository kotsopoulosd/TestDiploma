package com.example.dkotsopoulos.testdiploma;



import android.app.ListActivity;
import android.os.Bundle;
import java.util.List;

public class ApplicationLogActivity extends ListActivity {

    DBHelper dbHelper = new DBHelper(ApplicationLogActivity.this);
    List<ApplicationObject> Newapp;
    ApplicationAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        Newapp=dbHelper.GetAllApplications();
        adapter=new ApplicationAdapter(getApplicationContext(),R.layout.row_applications,Newapp);
        setListAdapter(adapter);
    }
}
