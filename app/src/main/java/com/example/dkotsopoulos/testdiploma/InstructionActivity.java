package com.example.dkotsopoulos.testdiploma;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class InstructionActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        ArrayList<String> newString= extras.getStringArrayList("Instructions");
        Log.d("Instruction", extras.toString());
        setContentView(R.layout.activity_instruction);
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.row_instructions, R.id.Itemname,newString));
        super.onCreate(savedInstanceState);

    }


}
