package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 10/10/2015.
 */import java.util.List;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


public class GeofenceIntentService extends IntentService {

    private final String TAG = this.getClass().getCanonicalName();

    public GeofenceIntentService() {

        super("GeofenceIntentService");
        Log.v(TAG, "Constructor.");

    }

    public void onCreate() {

        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
        Log.v(TAG, "onHandleIntent");
        if(!geofencingEvent.hasError()) {
            int transition = geofencingEvent.getGeofenceTransition();
            String notificationTitle;
            SharedPreferences CheckHome = PreferenceManager.getDefaultSharedPreferences(this);
            switch(transition) {

                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    notificationTitle = "Geofence Entered";
                    Log.v(TAG, "Geofence Entered");
                    AudioManager amEnter = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    for (int i = 0; i < geofences.size(); i++)
                    {
                        if (geofences.get(i).getRequestId().equals("Work"))
                        {

                            amEnter.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_ALLOW_RINGER_MODES);
                            amEnter.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            //For rule 2- midnight mute
                            SharedPreferences.Editor editor = CheckHome.edit();
                            editor.putString("CheckHome","0" ).commit();

                            /***********************************Here we implement the walking service**************************/
                            stopService(new Intent(this, WalkingService.class));
                            /***********************************End of the walking service**************************/

                        }
                        else if (geofences.get(i).getRequestId().equals("Home"))
                        {
                            amEnter.setStreamVolume(AudioManager.STREAM_RING, 2, AudioManager.FLAG_ALLOW_RINGER_MODES);
                            amEnter.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            //For rule 2- midnight mute
                            SharedPreferences.Editor editor = CheckHome.edit();
                            editor.putString("CheckHome","1" ).commit();
                            /***********************************Here we implement the walking service**************************/
                            stopService(new Intent(this, WalkingService.class));
                            /***********************************End of the walking service**************************/


                        }
                        else
                        {

                            amEnter.setStreamVolume(AudioManager.STREAM_RING, 5, AudioManager.FLAG_ALLOW_RINGER_MODES);
                            amEnter.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                           //For rule 2- midnight mute
                            SharedPreferences.Editor editor = CheckHome.edit();
                            editor.putString("CheckHome","0" ).commit();
                        }
                    }
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    notificationTitle = "Geofence Dwell";
                    Log.v(TAG, "Dwelling in Geofence");
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    notificationTitle = "Geofence Exit -Check city events!";
                    AudioManager amexit = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int streamMaxVolumeExit = amexit.getStreamMaxVolume(AudioManager.STREAM_RING);
                    amexit.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolumeExit, AudioManager.FLAG_ALLOW_RINGER_MODES);
                    amexit.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    /***********************************Here we implement the walking service**************************/

                        startService(new Intent(this, WalkingService.class));

                    /***********************************End of the walking service**************************/

                    break;
                default:
                    notificationTitle = "Geofence Unknown";
            }

            sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
        }
    }

    private void sendNotification(Context context, String notificationText,
                                  String notificationTitle) {

        if (notificationTitle.equals("Geofence Exit -Check city events!"))
        {

            Intent intent = new Intent(getApplicationContext(), EventfullActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.notification)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setContentIntent(contentIntent)
                    .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(false);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

        }
        else
        {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.notification)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(false);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

        }

    }

    private String getTriggeringGeofences(Intent intent) {
        GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = geofenceEvent
                .getTriggeringGeofences();

        String[] geofenceIds = new String[geofences.size()];

        for (int i = 0; i < geofences.size(); i++) {
            geofenceIds[i] = geofences.get(i).getRequestId();
        }

        return TextUtils.join(", ", geofenceIds);
    }
}