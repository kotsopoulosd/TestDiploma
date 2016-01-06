package com.example.dkotsopoulos.testdiploma;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by DKotsopoulos on 19/10/2015.
 */
public class CalendarService extends Service {
    CallRejecterSmsSender CallerejectersmsSender;
    private static final String TAG = "CalendarService";
    List<CalendarObject> NewCalendar =new ArrayList<CalendarObject>();
    CalendarAdapter adapter;
    DBHelper dbHelper;


    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(CallerejectersmsSender);
        stopSelf();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dbHelper = new DBHelper(this);
        dbHelper.DeletePreviousEvents();
        NewCalendar = dbHelper.GetMeetings();
        for (int i=0; i<NewCalendar.size(); i++)
        {

            if(((NewCalendar.get(i).getstartDates()- System.currentTimeMillis())/1000L/60L) <=5
                    && NewCalendar.get(i).getendDates()>System.currentTimeMillis())
            {
                /***********************************Here we implement the walking service**************************/
                stopService(new Intent(this, WalkingService.class));
                /***********************************End of the walking service**************************/
                CallerejectersmsSender = new CallRejecterSmsSender();
                Log.d(TAG, NewCalendar.get(i).getnameOfEvent().toString());
                AudioManager amEnter = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                amEnter.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                amEnter.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                registerReceiver(CallerejectersmsSender, new IntentFilter("android.intent.action.PHONE_STATE"));

            }
            else
            {
                if (CallerejectersmsSender!=null)
                {
                    Log.d(TAG,"Unregister the Broadcast Receiver");
                    unregisterReceiver(CallerejectersmsSender);
                    CallerejectersmsSender = null;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
