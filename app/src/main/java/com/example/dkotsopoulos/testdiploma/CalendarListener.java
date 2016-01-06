package com.example.dkotsopoulos.testdiploma;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class CalendarListener extends Service {

    String descriptions;
    String eventLocation;
    Cursor cursor = null;
    DBHelper dbHelper;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("CalendarListener", "Entered");
        dbHelper = new DBHelper(CalendarListener.this);
        Cursor cursor = CalendarListener.this.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[]{"calendar_id", "title", "description",
                        "dtstart", "dtend", "_id", "eventLocation"}, null,
                null, null);
        cursor.moveToFirst();

        // fetching calendars name
        while (cursor.isAfterLast() == false) {
            String nameOfEvent = cursor.getString(1);
            long startDates = Long.parseLong(cursor.getString(3));
            long endDates = Long.parseLong(cursor.getString(4));
            String eventid = cursor.getString(5);
            if (cursor.getString(2).isEmpty()) {
                descriptions = "Not Defined";
            } else {
                descriptions = cursor.getString(2);
            }
            if (cursor.getString(6).isEmpty()) {
                eventLocation = "Not Defined";
            } else {
                eventLocation = cursor.getString(6);
            }
            long currtime=System.currentTimeMillis();
            if (startDates>currtime)
            {
                dbHelper.InsertCalendarLogs(eventid, nameOfEvent, descriptions, eventLocation, startDates, endDates);
            }
                cursor.moveToNext();

        }

        cursor.close();
        dbHelper.close();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
