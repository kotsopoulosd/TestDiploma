package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class CallActivity extends ListActivity {
    DBHelper dbHelper;
    List<CallObject> NewCalls =new ArrayList<CallObject>();
    CallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(CallActivity.this);
        NewCalls = dbHelper.GetAllCalls();
        if (NewCalls != null) {
            adapter = new CallAdapter(getApplicationContext(), R.layout.row_caller, NewCalls);
            setListAdapter(adapter);
        }
    }
}
