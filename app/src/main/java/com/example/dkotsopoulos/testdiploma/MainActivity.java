package com.example.dkotsopoulos.testdiploma;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
    private static final String Tag="MainActivity";
    Button ButtonGPS;
    Button ContextManager;
    Button StopServiceButton;
    Button ButtonAccelometer;
    Button LightButton;
    Button GyroscopeButton;
    Button CalLoggerButton;
    Button SmsLogsButton;
    Button AppsButton;
    Button GCalendarButton;
    Integer counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startService(new Intent(this, UpdateService.class));
        //Intent for Application access
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
         //Intent for location GPS
        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        setContentView(R.layout.activity_main);

        //Initialization of buttons.
        ContextManager = (Button) findViewById(R.id.ContextManager);
        StopServiceButton = (Button) findViewById(R.id.StopServiceButton);
        ButtonGPS = (Button) findViewById(R.id.GPSButton);
        LightButton= (Button)findViewById(R.id.LightButton);
        ButtonAccelometer = (Button) findViewById(R.id.AccelerometerButton);
        GyroscopeButton = (Button) findViewById(R.id.GyroscopeButton);
        CalLoggerButton=(Button) findViewById(R.id.CalLoggerButton);
        SmsLogsButton=(Button) findViewById(R.id.SmsLogsButton);
        AppsButton=(Button) findViewById(R.id.AppsButton);
        GCalendarButton=(Button) findViewById(R.id.GCalendarButton);



        ContextManager.setOnClickListener(this);
        StopServiceButton.setOnClickListener(this);
        ButtonGPS.setOnClickListener(this);
        ButtonAccelometer.setOnClickListener(this);
        LightButton.setOnClickListener(this);
        GyroscopeButton.setOnClickListener(this);
        CalLoggerButton.setOnClickListener(this);
        SmsLogsButton.setOnClickListener(this);
        AppsButton.setOnClickListener(this);
        GCalendarButton.setOnClickListener(this);
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onPause() {
        counter=counter +1;
        super.onPause();
    }

    //Button Click choices
        public void onClick(View src) {
            /////////////////////
            Intent shortcut = new Intent(this, MainActivity.class);
            shortcut.setAction(Intent.ACTION_MAIN);
            ///////////////////////

        switch (src.getId()) {
            case R.id.ContextManager:

                SharedPreferences SetTimersON = PreferenceManager.getDefaultSharedPreferences(this);
                if (counter==1)
                {
                    SharedPreferences.Editor editor = SetTimersON.edit();
                    editor.putString("TimerOn", "True").commit();

                }
                else{
                    SharedPreferences.Editor editor = SetTimersON.edit();
                    editor.putString("TimerOn", "False").commit();
                }
                Log.d(Tag, "onClick: starting ContextManager");
                startActivity(new Intent(MainActivity.this, ContextManager.class));
                break;
            case R.id.StopServiceButton:
                Log.d(Tag, "onClick: stopping service");
                stopService(new Intent(this, UpdateService.class));
                break;
            case R.id.GPSButton:
                Log.d(Tag, "onClick: GPS activity");
                startActivity(new Intent(MainActivity.this, GpsActivity.class));
                break;
            case R.id.AccelerometerButton:
                Log.d(Tag, "onClick: Acceleration activity");
                startActivity(new Intent(MainActivity.this, AccelerometerActivity.class));
                break;
            case R.id.LightButton:
                Log.d(Tag, "onClick: Light activity");
                startActivity(new Intent(MainActivity.this, LightActivity.class));
                break;
            case R.id.GyroscopeButton:
                Log.d(Tag, "onClick: Gyroscope activity Testing!!");
                startActivity(new Intent(MainActivity.this, GyroscopeActivity.class));
                break;
            case R.id.CalLoggerButton:
                Log.d(Tag, "onClick: Call logs activity");
                startActivity(new Intent(MainActivity.this, CallActivity.class));
                break;
            case R.id.SmsLogsButton:
                Log.d(Tag, "onClick: Sms Logs activity");
                startActivity(new Intent(MainActivity.this, SmsActivity.class));
                break;
            case R.id.AppsButton:
                Log.d(Tag, "onClick: Application Logger Service Testing!!");
                startActivity(new Intent(MainActivity.this, ChooseApplicationStatus.class));
                break;
            case R.id.GCalendarButton:
                Log.d(Tag, "onClick:  Calendar activity");
                ///Calendar Service
                startService(new Intent(MainActivity.this, CalendarListener.class));
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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