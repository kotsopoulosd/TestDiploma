package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;


public class CalendarActivity extends ListActivity {

    List<CalendarObject> NewCalendar =new ArrayList<CalendarObject>();
    CalendarAdapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(CalendarActivity.this);
        NewCalendar = dbHelper.GetAllCalendar();
        adapter = new CalendarAdapter(getApplicationContext(), R.layout.row_calendar, NewCalendar);
        setListAdapter(adapter);

    }
}