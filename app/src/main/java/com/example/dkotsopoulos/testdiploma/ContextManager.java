package com.example.dkotsopoulos.testdiploma;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ContextManager extends Activity {
    private static final String TAG_SERVICE = "GroupingLocations";
    private static final String TAG = "InstallShortcuts";
    private static double threshold = 300; // Distance between two points in METERS
    public static List<GroupPointObject> points = new ArrayList<GroupPointObject>();
    DBHelper dbHelper;
    ArrayList<Geofence> mGeofences;
    ArrayList<LatLng> mGeofenceCoordinates;
    ArrayList<Integer> mGeofenceRadius;
    String[] geocoordinate = new String[2];
    GeofenceStore mGeofenceStore;
    List<ApplicationObject> Newapp;
    CalendarAdapter adapter;
    Button mapbutton,ShowEvents,Directions,StopAlarm,add,remove;
    Timer timer;
    Timer timerservices;
    Timer timerservicesShortcuts;
    TimerTask timerTask;
    TimerTask timerTaskservices;
    TimerTask timerTaskservicesShortcuts;
    final Handler handler = new Handler();
    final Handler handlerservices = new Handler();
    final Handler handlerservicesShortcuts = new Handler();
    String TimersAreRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SharedPreferences location = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_manager);
        mapbutton = (Button) findViewById(R.id.mapbutton);
        ShowEvents=(Button) findViewById(R.id.ShowEvents);
        Directions = (Button) findViewById(R.id.directionsbutton);
        StopAlarm = (Button) findViewById(R.id.stopalarmServicesbutton);



///////////////////////////////////////////////////////////////////////////////////////////////

/*Here we create the clustering for Work centroid and Home centroid*/

        dbHelper = new DBHelper(this);
        points = dbHelper.GetGroupedGPS();
        Log.d(TAG_SERVICE, String.valueOf(points.size()));
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); ) {
                GroupPointObject pointHere = points.get(i);
                GroupPointObject pointThere = points.get(j);
                double length = distance(pointHere.getY(), pointThere.getY(), pointHere.getX(), pointThere.getX(), 0.0, 0.0);

                if (length <= threshold) {
                    points.get(i).xCluster = (points.get(i).xCluster + points.get(j).getX());
                    points.get(i).yCluster = (points.get(i).yCluster + points.get(j).getY());
                    points.get(i).cluster_volume++;
                    try {
                        if (checktime(points.get(j).getdatetime()).equals("Work")) {
                            points.get(i).Work++;}
                        else {points.get(i).Home++;}
                    } catch (ParseException e) {e.printStackTrace();}
                    points.remove(j);
                } else {j += 1;}
            }
        }
        LatLng Home = new LatLng(0, 0);
        LatLng Work = new LatLng(0, 0);
        Collections.sort(points);

        location.edit().clear();
        if (points.size() >= 2) {
            if (points.get(0).Work < points.get(0).Home) {
                //Here i should create the markers for map
                Log.d("Home: ", points.get(0).toString());
                Home = new LatLng(points.get(0).getX(), points.get(0).getY());

            } else {
                //Here i should create the markers for map
                Log.d("Work: ", points.get(0).toString());
                Work = new LatLng(points.get(0).getX(), points.get(0).getY());
            }
            if (points.get(1).Work < points.get(1).Home) {
                //Here i should create the markers for map
                Log.d("Home: ", points.get(1).toString());
                Home = new LatLng(points.get(1).getX(), points.get(1).getY());
            } else {
                //Here i should create the markers for map
                Log.d("Work: ", points.get(1).toString());
                Work = new LatLng(points.get(1).getX(), points.get(1).getY());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Less than two clusters!", Toast.LENGTH_LONG).show();
        }


        // Writing data a variable and pass them to the map activity for displaying them
        geocoordinate[0] = String.valueOf(Home.latitude) + "," + String.valueOf(Home.longitude);
        geocoordinate[1] = String.valueOf(Work.latitude) + "," + String.valueOf(Work.longitude);
        SharedPreferences.Editor editor = location.edit();
        editor.putString("LatHome", String.valueOf(Home.latitude)).commit();
        editor.putString("LogHome", String.valueOf(Home.longitude)).commit();
        editor.putString("LatWork", String.valueOf(Work.latitude)).commit();
        editor.putString("LogWork", String.valueOf(Work.longitude)).commit();
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG_SERVICE, "onClick: starting ContextManager");
                startActivity(new Intent(ContextManager.this, MapActivity.class).putExtra("Geolocations", geocoordinate));
            }
        });
/*******************************************End of clustering creation ***********************/

///////////////////////////////////////////////////////////////////////////////////////////////

/*********************Here we implement the geofencing*****************************************/
        // Initializing variables
        mGeofences = new ArrayList<Geofence>();
        mGeofenceCoordinates = new ArrayList<LatLng>();
        mGeofenceRadius = new ArrayList<Integer>();
        mGeofenceRadius.add(100); //Radious in meters
        mGeofenceCoordinates.add(Work);
        mGeofenceCoordinates.add(Home);
        // Home, the coordinates of the center of the geofence and the radius in meters.
        mGeofences.add(new Geofence.Builder().setRequestId("Home").setCircularRegion(mGeofenceCoordinates.get(1).latitude, mGeofenceCoordinates.get(1).longitude,
                mGeofenceRadius.get(0).intValue())
                .setExpirationDuration(Geofence.NEVER_EXPIRE).setLoiteringDelay(3600000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        // Work, the coordinates of the center of the geofence and the radius in meters.
        mGeofences.add(new Geofence.Builder().setRequestId("Work")
                .setCircularRegion(mGeofenceCoordinates.get(0).latitude, mGeofenceCoordinates.get(0).longitude, mGeofenceRadius.get(0).intValue())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        // Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
                .setLoiteringDelay(3600000)// 60minutes inside the geofence an alert will be sent
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        // Add the geofences to the GeofenceStore object.
        mGeofenceStore = new GeofenceStore(this, mGeofences);

/***************End of geofencing************************************************************** */

///////////////////////////////////////////////////////////////////////////////////////////////

/*************************Here we implement mute after 23:59 event*********************************/
        SharedPreferences shareref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TimersAreRunning= shareref.getString("TimerOn", "");
        if (TimersAreRunning.equals("True"))
        {
            try {
                startTimerServices();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**************************End  mute after 23:59 event*********************************************/

            /*************************Here we implement shortcuts rule*********************************/
            // 3 most used apps
            try {
                startTimer();
                //whatsapp, hangouts, top three callers
                startTimerShortcuts();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


/**************************End  shortcuts *********************************************/
///////////////////////////////////////////////////////////////////////////////////////////////

/***********************Here we implement the selection of shortcuts manually******************/


    add = (Button) findViewById(R.id.buttonAddShortcut);
    add.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //Add shortcut on Home screen
            addShortcut();
            AddContactShortcut();
            AddWhatsShortcuts();
            AddSMSShortcuts();

        }
    });

    //Add listener to remove shortcut button
    remove = (Button) findViewById(R.id.buttonRemoveShortcut);
    remove.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            //Remove shortcut from Home screen
            removeShortcut();
            RemoveContactShortcut();
            RemoveSMSShortcuts();
            RemoveWhatsShortcuts();
        }
    });


/*********************************End selection of the shortcuts*************************/

///////////////////////////////////////////////////////////////////////////////////////////////


/* *****************************Show all the events in your area*****************************************/
        ShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContextManager.this, EventfullActivity.class));
            }
        });

/**********************End of showing all the events in your area*****************************************/

///////////////////////////////////////////////////////////////////////////////////////////////


/* *****************************Show Direction for mobility*****************************************/
        Directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ContextManager.this, ChooseTransportMode.class));
            }
        });

/**********************End of showing Direction for mobility*****************************************/

///////////////////////////////////////////////////////////////////////////////////////////////

/* *****************************Stop Timers!*****************************************/

        StopAlarm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               TimersAreRunning="false";
               stoptimertask();
               stoptimertaskServices();
               stoptimerShortcuts();


           }
       });
/**********************End of Timer for weekly most used applications module*****************************************/
    }



/*******************************************************End OnCreate *******************************************************/


///*******************************************************Start OnPause *******************************************************/
//    @Override
//    protected void onPause() {
//
//        super.onPause();
//    }
///*******************************************************End OnPause *******************************************************/
//
///*******************************************************Start OnResume *******************************************************/
//    @Override
//    protected void onResume() {

//        super.onResume();
//    }
//
///*******************************************************End OnResume *******************************************************/



    /************************************Assisting functions****************************/



//////////////////////Here we are checking the time////////////////////////////////
    public static String checktime(Long time) throws ParseException {
        //Checking whether the timestamp is on home or work based on fixed values. (We exclude Weekends because they affect the reliability of the sample)
        //Start Time
        Date inTime = new SimpleDateFormat("HH:mm:ss").parse("09:00:00");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(inTime);

        //End Time
        Date finTime = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(finTime);
        //Current Time
        String timeLog2 = new SimpleDateFormat("EEEE").format(time);
        String timeLog = new SimpleDateFormat("HH:mm:ss").format(time);
        Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(timeLog);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(checkTime);
        Date actualTime = calendar3.getTime();
        if (timeLog2.matches("Saturday|Sunday")) {
//            Log.d(TAG_SERVICE, "Nothing");
            return "Nothing";
        } else {

            if (actualTime.after(calendar1.getTime()) && actualTime.before(calendar2.getTime())) {
//                Log.d(TAG_SERVICE, "Work");
                return "Work";
            } else {
//                Log.d(TAG_SERVICE, "Home");
                return "Home";
            }

        }
    }

//////////////////////Here we are checking the distance between two points/////////////////////////
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth
        //Wg84 and this is a function that calculates coordinates to meters
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }

//////////////////////////////Add delete shortcut applications//////////////////////////////
    public void addShortcut()
    {
        Log.d(TAG, "Adding Shorcuts");
        //Adding shortcut for MainActivity
        //on Home screen

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getPackageManager();
        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
        Newapp=dbHelper.GetAllApplications();
    if(!Newapp.equals(null))
    {
        for (ResolveInfo temp : appList) {
            for (int i = 0; i < 3; i++) {
                if (temp.activityInfo.packageName.equals(Newapp.get(i).getApplicationPackageName())) {

                    Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

                    // Shortcut name
                    shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, temp.loadLabel(pm).toString());
                    shortcut.putExtra("duplicate", true);  // Just create once

                    // Setup current activity shoud be shortcut object
                    ComponentName comp = new ComponentName(temp.activityInfo.packageName, temp.activityInfo.name);
                    shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
                    Drawable d = temp.loadIcon(pm);
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
                    sendBroadcast(shortcut);
                }
            }

        }
    }
    }
    public  void removeShortcut() {

        Log.d(TAG, "Deleting Shorcuts");
        //Deleting shortcut for MainActivity on Home screen
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getPackageManager();
        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
        Newapp = dbHelper.GetAllApplications();
        if (!Newapp.equals(null)) {
            for (ResolveInfo temp : appList) {
                for (int i = 0; i < 3; i++) {
                    if (temp.activityInfo.packageName.equals(Newapp.get(i).getApplicationPackageName())) {


                        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
                        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, temp.loadLabel(pm).toString());
                        String appClass = temp.activityInfo.packageName + "." + temp.activityInfo.name;
                        ComponentName comp = new ComponentName(temp.activityInfo.packageName, appClass);
                        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
                        sendBroadcast(shortcut);


                    }
                }
            }
        }
    }

//////////////////////////////// Add delete contacts shortuts//////////////////////////////
    private void AddContactShortcut() {
        Log.d(TAG, "Adding Contact Shorcuts");
        DBHelper dbHelper= new DBHelper(this);

        List<CallObject> tester= new ArrayList<CallObject>();
        String checker= null;
        try {
            checker = checktime(System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (checker.equals("Work"))
        {
            tester= dbHelper.GetTopThreeCallers();
        }
        else{
            tester= dbHelper.GetTopThreeCallersAfterWork();
        }

        for (int i=0; i<tester.size();i++) {

            if (!tester.get(i).getcallname().equals("Unknown")) {

                Log.d(TAG, tester.get(i).getcallname());
                Log.d(TAG, tester.get(i).getphnumber().toString());
                Intent shortCutInt = new  Intent(Intent.ACTION_DIAL);
                shortCutInt.setData(Uri.parse("tel:" + tester.get(i).getphnumber().toString()));
                shortCutInt.putExtra("duplicate", true);  // Just create once
                Intent addInt = new Intent();
                addInt.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutInt);
                addInt.putExtra(Intent.EXTRA_SHORTCUT_NAME, tester.get(i).getcallname().toString());
                Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), tester.get(i).getphnumber().toString());
                Drawable myDrawable = getResources().getDrawable(R.drawable.phone);
                Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                if (bitmap == null) {
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                            Intent.ShortcutIconResource.fromContext(this, R.drawable.phone));
                } else {
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, overlay(myLogo,bitmap));
                }
                addInt.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                sendBroadcast(addInt);

            }
        }
    }
    private void RemoveContactShortcut(){
        DBHelper dbHelper= new DBHelper(this);
        List<CallObject> tester= new ArrayList<CallObject>();
        tester= dbHelper.GetTopThreeCallers();
        for (int i=0; i<tester.size();i++) {

            if (!tester.get(i).getcallname().equals("Unknown"))
            {
                Intent shortCutInt = new  Intent(Intent.ACTION_DIAL);
                shortCutInt.setData(Uri.parse("tel:" + tester.get(i).getphnumber().toString()));
                Intent removeshortcut = new Intent();
                removeshortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutInt);
                removeshortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, tester.get(i).getcallname().toString());
                removeshortcut.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                sendBroadcast(removeshortcut);

            }
        }

    }

//////////////////////////////// Add delete whatup shortuts//////////////////////////////
    private void AddWhatsShortcuts(){
        Log.d(TAG, "Adding WhatsApp Shorcuts");
        DBHelper dbHelper= new DBHelper(this);
        List<CallObject> tester= new ArrayList<CallObject>();
        tester= dbHelper.GetTopThreeCallers();
        Drawable AppIcon = null;
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            try {
                AppIcon = getPackageManager().getApplicationIcon("com.whatsapp");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap Appbitmap = ((BitmapDrawable) AppIcon).getBitmap();
            for (int i = 0; i < tester.size(); i++) {

                if (!tester.get(i).getcallname().equals("Unknown")) {
                    Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            new String[]{ContactsContract.Contacts.Data._ID}, ContactsContract.Data.DATA1 + "=?",
                            new String[]{tester.get(i).getphnumber() + "@s.whatsapp.net"}, null);
                    c.moveToFirst();
                    if (c.getCount() != 0) {
                        Intent whatupsintent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));
                        Intent addInt = new Intent();
                        addInt.putExtra("duplicate", false);
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_INTENT, whatupsintent);
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_NAME, tester.get(i).getcallname().toString());
                        Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), tester.get(i).getphnumber().toString());
                        if (bitmap == null) {
                            addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, Appbitmap);
                        } else {
                            addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, overlay(Appbitmap, bitmap));
                        }
                        addInt.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                        sendBroadcast(addInt);

                    }
                    c.close();
                }
            }
        }
    }
    private void RemoveWhatsShortcuts(){

        DBHelper dbHelper= new DBHelper(this);
        List<CallObject> tester= new ArrayList<CallObject>();
        tester= dbHelper.GetAllCalls();
        Drawable AppIcon = null;
        try {
            AppIcon = getPackageManager().getApplicationIcon("com.whatsapp");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap Appbitmap = ((BitmapDrawable)AppIcon).getBitmap();
        for (int i=0; i<tester.size();i++) {

            if (!tester.get(i).getcallname().equals("Unknown"))
            {
                Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.Contacts.Data._ID}, ContactsContract.Data.DATA1 + "=?",
                        new String[]{tester.get(i).getphnumber() + "@s.whatsapp.net"}, null);
                c.moveToFirst();
                if (c.getCount()!=0)
                {
                    Intent whatupsintent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));
                    Intent addInt = new Intent();
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_INTENT, whatupsintent);
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_NAME, tester.get(i).getcallname().toString());
                    Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), tester.get(i).getphnumber().toString());
                    if (bitmap == null) {
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, Appbitmap);
                    } else {
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, overlay(Appbitmap,bitmap));
                    }
                    addInt.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                    sendBroadcast(addInt);

                }
                c.close();
            }
        }
    }

//////////////////////////////// Add delete Hangouts shortuts//////////////////////////////
    private void AddSMSShortcuts() {
        Log.d(TAG, "Adding SMS Shorcuts");
        DBHelper dbHelper = new DBHelper(this);
        List<SmsObject> tester = new ArrayList<SmsObject>();
        tester = dbHelper.GetMostThreeSMS();
        Drawable AppIcon = null;
        boolean installed = appInstalledOrNot("com.google.android.talk");
        if (installed) {
            try {
                AppIcon = getPackageManager().getApplicationIcon("com.google.android.talk");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap Appbitmap = ((BitmapDrawable) AppIcon).getBitmap();

            for (int i = 0; i < tester.size(); i++) {

                if (!tester.get(i).getPerson().equals("Unknown")) {

                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.setData(Uri.parse("sms:" + tester.get(i).getaddress()));
                    Intent addInt = new Intent();
                    addInt.putExtra("duplicate", true);
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_INTENT, smsIntent);
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_NAME, tester.get(i).getPerson().toString());
                    Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), tester.get(i).getaddress().toString());
                    if (bitmap == null) {
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, Appbitmap);
                    } else {
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, overlay(Appbitmap, bitmap));
                    }
                    addInt.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    sendBroadcast(addInt);

                }
            }
        }
    }
    private void RemoveSMSShortcuts() {

        DBHelper dbHelper= new DBHelper(this);
        List<CallObject> tester= new ArrayList<CallObject>();
        tester= dbHelper.GetTopThreeCallers();
        Drawable AppIcon = null;
        boolean installed = appInstalledOrNot("com.google.android.talk");
        if (installed) {
            try {
                AppIcon = getPackageManager().getApplicationIcon("com.google.android.talk");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap Appbitmap = ((BitmapDrawable) AppIcon).getBitmap();

            for (int i = 0; i < tester.size(); i++) {

                if (!tester.get(i).getcallname().equals("Unknown")) {

                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.setData(Uri.parse("sms:" + tester.get(i).getphnumber()));
                    Intent addInt = new Intent();
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_INTENT, smsIntent);
                    addInt.putExtra(Intent.EXTRA_SHORTCUT_NAME, tester.get(i).getcallname().toString());
                    Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), tester.get(i).getphnumber().toString());
                    if (bitmap == null) {
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, Appbitmap);
                    } else {
                        addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON, overlay(Appbitmap, bitmap));
                    }
                    addInt.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                    sendBroadcast(addInt);

                }
            }
        }
    }


////////////////////////////////Find the contact photo from the call number/////////////////////////
    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                assert inputStream != null;
                inputStream.close();
            }else
            {
                photo=null;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photo!=null) {
            Bitmap circleBitmap = photo.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);

            BitmapShader shader = new BitmapShader(photo, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(photo.getWidth() / 2, photo.getHeight() / 2, photo.getWidth() / 2, paint);
            return circleBitmap;



        }
        else
            return photo;
    }

    // Merge the application icon with the contact photo
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

////////Timer for weekly repeated applications' shortcuts installment.//////////////////////////////
    public void startTimer() throws ParseException {
        // get start of this week in milliseconds
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Log.d("Start of this week:       ", cal.getTime().toString());
        Date finaldate = cal.getTime();
        int interval = 1000 * 60 *10080;//1440;// 24 hour
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 10sec the TimerTask will run every 24 hour
        timer.schedule(timerTask, finaldate, interval); //
    }

    public void stoptimertask() {

        //stop the timer, if it's not already null
        Log.d(TAG, "Stopping timer task");
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }

    }

    public void initializeTimerTask() {
        Log.d(TAG, "initializeTimerTask");
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp

                handler.post(new Runnable() {

                    public void run()
                    {
                        removeShortcut();
                        addShortcut();

                    }

                });

            }

        };

    }

//////////////////////////////////////////////////////////////////////////////////////////////////
   // Check if user is home and the time is 23:57 and mute phone

    public void startTimerServices()
    {
    //set a new Timer
        timerservices = new Timer();
        //initialize the TimerTask's job
        initializeTimerTaskServices();
        //schedule the timer, after the first 10sec the TimerTask will run every 2,5min
        timerservices.schedule(timerTaskservices, 10000,150000); //

    }
    public  void stoptimertaskServices()
    {

        Log.d(TAG, "Stopping timer task Services");
        //stop the timer, if it's not already null
        if (timerservices != null)
        {
            timerservices.cancel();
            timerservices = null;
        }

    }
    public void initializeTimerTaskServices() {

        timerTaskservices = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp

                handlerservices.post(new Runnable() {

                    public void run()
                    {
                        //Calendar Service
                        startService(new Intent(ContextManager.this, CalendarListener.class));

                        //CalendarEvents Service
                        startService(new Intent(ContextManager.this, CalendarService.class));
                        ///Application Service
                        startService(new Intent(ContextManager.this, ApplicationListener.class));
                        /**********************Rule number 2- Check if user is home and midnight*****************************************/

                        SharedPreferences check = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String Checker= check.getString("CheckHome", "");
                        Calendar currTime = Calendar.getInstance();
                        Log.d("ContextManager Outside",Checker);
                        int hour = currTime.get(Calendar.HOUR_OF_DAY);
                        int minute = currTime.get(Calendar.MINUTE);
                        // 0 ==work ,1== home
                        if ((Checker.equals("1")) && (hour==23) && (minute>57))
                        {
                            /***********************************Here we implement the walking service**************************/
                            stopService(new Intent(ContextManager.this, WalkingService.class));
                            /***********************************End of the walking service**************************/

                            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                            Log.d("ContextManager Inside",Checker);
                        }

                        /**********************End of Rule number 2- Check if user is home and midnight*****************************************/

                    }

                });

            }

        };

    }
//////////////////////////////////////////////////////////////////////////////////////////////////
// For adding or removing contact shortcuts


    public void startTimerShortcuts() throws ParseException {
        Log.d(TAG, "Entering timer task Shortcuts");
        //set a new Timer
        timerservicesShortcuts = new Timer();
        //initialize the TimerTask's job
        initializeTimerShortcuts();
        //schedule the timer, after the first 10sec the TimerTask will run every 2,5min
        timerservicesShortcuts.schedule(timerTaskservicesShortcuts, 10000,150000); //
}

    public void stoptimerShortcuts() {

        //stop the timer, if it's not already null
        if (timerservicesShortcuts != null)
        {
            timerservicesShortcuts.cancel();
            timerservicesShortcuts = null;
        }

    }

    public void initializeTimerShortcuts() {

        timerTaskservicesShortcuts = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                Log.d(TAG, "initializeTimerShortcuts");
                handlerservicesShortcuts.post(new Runnable() {

                    public void run()
                    {
                        try {
                            int checker=checkcurrenttime(System.currentTimeMillis());
                            if (checker==1) {
                                AddContactShortcut();
                                AddSMSShortcuts();
                                AddWhatsShortcuts();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                });

            }

        };

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////

    public static int checkcurrenttime(Long time) throws ParseException {

        //End Time
        Date inTime = new SimpleDateFormat("HH:mm:ss").parse("17:15:00");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(inTime);

        //Current Time
        String timeLog2 = new SimpleDateFormat("EEEE").format(time);
        String timeLog = new SimpleDateFormat("HH:mm:ss").format(time);
        Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(timeLog);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(checkTime);
        Date actualTime = calendar3.getTime();
        if (actualTime.after(calendar1.getTime()) ) {return 1;} else {return 0;}
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_context_manager, menu);
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
