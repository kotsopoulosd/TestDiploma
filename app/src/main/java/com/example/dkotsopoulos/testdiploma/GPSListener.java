package com.example.dkotsopoulos.testdiploma;

import java.text.SimpleDateFormat;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by DKotsopoulos on 01/09/2015.
 */

final class GPSListener implements LocationListener {
    private Context mContext;
    TestDiplomaApp diplomaapp;
    DBHelper  dbHelper;
    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    double altitude;//altitude
    double speed;//speed
    float direction;//direction
    long timestamp;//timestamp

    protected LocationManager locationManager;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 50 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 900000; // 15 minute

    public GPSListener(Context context) {
        this.mContext = context;
        dbHelper = new DBHelper(mContext);
        diplomaapp=((TestDiplomaApp)mContext.getApplicationContext());
        getLocation();
    }

    public GPSListener() {}

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v("isGPSEnabled", "=" + isGPSEnabled);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
            if (isGPSEnabled == false && isNetworkEnabled == false)
            {
                // no network provider is enabled
            } else
            {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location = null;

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    location = null;
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();


                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void StopGettingLocation(LocationListener locationListener) {
        locationManager.removeUpdates(locationListener);
    }

    public void clearLocationManager() {
        locationManager = null;
    }


    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {

        if (location != null) {

            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    /**
     * Function to get altitude
     */
    public double getAltitude() {
        if (location != null) {
            altitude = location.getAltitude();
        }

        // return altitude
        return altitude;
    }

    /**
     * Function to get speed
     */
    public double getSpeed() {
        if (location != null) {
            speed = location.getSpeed();
        }

        // return speed
        return speed;
    }

    /**
     * Function to get Direction
     */
    public float getDirection() {
        if (location != null) {
            direction = location.getBearing();
        }

        // return direction
        return direction;
    }

    /**
     * Function to get Timestamp
     */
    public long getTimestamp() {
        if (location != null) {
            timestamp = location.getTime();
        }

        // return Timestamp
        return timestamp;
    }

    @Override

    public void onLocationChanged(Location locFromGps)  {
        this.location=locFromGps;
        Location mycoordinates;
        if(diplomaapp.islocationServiceStart())
            {
            mycoordinates = new Location(locFromGps);
                if ( mycoordinates.getAccuracy()<70)
                {
                    dbHelper.InsertGPS(mycoordinates.getLatitude(), mycoordinates.getLongitude(), mycoordinates.getAccuracy(), mycoordinates.getTime());
                }
            }
        Log.d("New Changed location:",locFromGps.toString());
        dbHelper.close();

    }

    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}