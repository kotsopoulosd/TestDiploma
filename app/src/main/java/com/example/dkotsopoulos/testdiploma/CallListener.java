package com.example.dkotsopoulos.testdiploma;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class CallListener extends ContentObserver {

    Context context;
    Handler h;
    DBHelper dbHelper;
    public CallListener(Handler h, Context x) {

        super(h);
        this.h=h;
        this.context=x;
    }
    public void onChange(boolean selfChange) {
        Log.d("CallListener", "Entered");
        Cursor mcursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
        int number = mcursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = mcursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = mcursor.getColumnIndex(CallLog.Calls.DURATION);
        int name = mcursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = mcursor.getColumnIndex(CallLog.Calls.TYPE);
        int Geolocation = mcursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION);
        mcursor.moveToFirst();
        while (mcursor.isAfterLast() == false) {
            String phnumber = mcursor.getString(number);
            String callduration = mcursor.getString(duration);
            String callname = mcursor.getString(name);
            if (callname == null) {
                callname = "Unknown";
            }
            String calltype = mcursor.getString(type);
            String callGeo = mcursor.getString(Geolocation);
            long calldate = mcursor.getLong(date);
            String callTypeStr = "";
            switch (Integer.parseInt(calltype)) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callTypeStr = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callTypeStr = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callTypeStr = "Missed";
                    break;
            }
            dbHelper= new DBHelper(context);
            dbHelper.InsertCallLogs(phnumber, callduration, callname, callTypeStr, callGeo, calldate);
            mcursor.moveToNext();
        }
        super.onChange(selfChange);
        dbHelper.close();
        mcursor.close();
    }




    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

}
