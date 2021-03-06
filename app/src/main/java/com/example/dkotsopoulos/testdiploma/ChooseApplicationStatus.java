package com.example.dkotsopoulos.testdiploma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ChooseApplicationStatus extends Activity implements View.OnClickListener {
    private static final String Tag="ChooseApplicationStatus";

    Button InstalledApps;
    Button ActiveApps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_application_status);
        InstalledApps = (Button) findViewById(R.id.InstalledApps);
        ActiveApps = (Button) findViewById(R.id.activeApps);
        InstalledApps.setOnClickListener(this);
        ActiveApps.setOnClickListener(this);
    }
    public void onClick(View src) {

        switch (src.getId()) {
            case R.id.InstalledApps:
                //InstalledApps
                startActivity(new Intent(this, InstalledApplicationActivity.class));
                break;
            case R.id.activeApps:
                //LactiveApps
                ///Application Service

                startService(new Intent(this, ApplicationListener.class));
                startActivity(new Intent(this, ApplicationLogActivity.class));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_application_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
