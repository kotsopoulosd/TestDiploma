package com.example.dkotsopoulos.testdiploma;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by DKotsopoulos on 04/09/2015.
 */

public class UpdateService extends Service {
    private static final String TAG_SERVICE = "MyService";
    AccelerometerListener myaccelerationlistener;
    LightListener mylightListener;
    GyroscopeListener myGyroscopeListener;
    GPSListener MyfirstLocation;
    Context context;
    CallListener h;
    SMSListener smsobserver;
    Uri uri = Uri.parse("content://sms");
    TestDiplomaApp diplomaapp;

    @Override
    public void onCreate() {

        context = this;
        diplomaapp=((TestDiplomaApp)context.getApplicationContext());

        //sms initialization
        smsobserver=new SMSListener(new Handler(), context);

        //Call initialization
        h=new CallListener(new Handler(), context);

        //Intent for location GPS
        MyfirstLocation = new GPSListener(context);

        if (MyfirstLocation.getLocation()==null) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        //Acceleration Service
        myaccelerationlistener = new AccelerometerListener(context);

        //Light Service
        mylightListener= new LightListener(context);

        //Gyroscope Service
        myGyroscopeListener = new GyroscopeListener(context);

        ///CallLogs Service
        context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, h);

        ///SMS Service
        context.getContentResolver().registerContentObserver(uri,true,smsobserver);

        Log.d(TAG_SERVICE, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG_SERVICE, "onStarted");
        //GPS service for checking if gps is open.
        diplomaapp.setLocationServiceStart(true);
        //Gps Listener
        MyfirstLocation = new GPSListener(context);
        //Acceleration Listener
        myaccelerationlistener.onResumeAccelerometer();
        //Light Listener
        mylightListener.onResumeLightListener();
        //Gyroscope Listener
        myGyroscopeListener.onResumeGyroscopeListener();
        //CallLogs Service
        context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, h);
        //SMS Service
        context.getContentResolver().registerContentObserver(uri, true, smsobserver);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {

        // initialize the locationservice to false
        diplomaapp.setLocationServiceStart(false);
        MyfirstLocation.clearLocationManager();

        //Light destroyer
        mylightListener.onPauseLightListener();
        //Acceleration Destroyer
        myaccelerationlistener.onPauseAccelerometer();
        //Gyroscope Destroyer
         myGyroscopeListener.onPauseGyroscopeListener();
        //Call log destroyer
        context.getContentResolver().unregisterContentObserver(h);
        //Sms log destroyer
        context.getContentResolver().unregisterContentObserver(smsobserver);
        //Application log destroyer
        stopService(new Intent(this, ApplicationListener.class));
        //Calendar log destroyer
        stopService(new Intent(this, CalendarListener.class));
        //Geofencing destroyer
        stopService(new Intent(this, GeofenceIntentService.class));
        //Calendar events destroyer
        stopService(new Intent(this, CalendarService.class));
        stopSelf();
        Log.d(TAG_SERVICE, "Is Killed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}