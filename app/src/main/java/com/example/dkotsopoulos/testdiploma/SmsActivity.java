package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class SmsActivity extends ListActivity
{

    List<SmsObject> NewSms =new ArrayList<SmsObject>();
    SmsAdapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(SmsActivity.this);
        NewSms = dbHelper.GetAllSms();
        adapter = new SmsAdapter(getApplicationContext(), R.layout.row_sms, NewSms);
        setListAdapter(adapter);

    }
}
