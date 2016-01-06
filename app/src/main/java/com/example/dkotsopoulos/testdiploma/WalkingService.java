package com.example.dkotsopoulos.testdiploma;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.example.dkotsopoulos.testdiploma.LegMovementDetector.ILegMovementListener;
import com.example.dkotsopoulos.testdiploma.PocketDetector.IInPocketListener;

/**
 * Created by DKotsopoulos on 31/10/2015.
 */

public class WalkingService extends Service {

    private static final int NOTIFICATION 	= 1;
    private static final String WAKELOCK 	= "WL_TAG";

    private final IBinder mBinder = new RobotBinder();
    private NotificationManager mNotificationManager;
    private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private LegMovementDetector mLegMovementDetector;
    private PocketDetector mPocket;
    LightListener mylightListener;
    final Handler handler = new Handler();
    public class RobotBinder extends Binder {
        WalkingService getService() {
            return WalkingService.this;
        }
    }

    /**
     * Used for receiving notifications from the LegMovementDetector when leg state have changed
     */
    private  ILegMovementListener mLegMovementListener = new ILegMovementListener() {
        @Override
        public void onLegActivity(int activity) {}
    };

    /********************* Service *************************************/

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mylightListener= new LightListener(getApplicationContext());
        // initialize class fields
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mPowerManager = (PowerManager) getSystemService(this.POWER_SERVICE);

        // initialize wakelock
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK);
        mWakeLock.acquire();

        // initialize movement detector
        mLegMovementDetector = new LegMovementDetector(mSensorManager);
        mLegMovementDetector.addListener(mLegMovementListener);

        // initialize pocket detector
        mPocket = new PocketDetector((SensorManager) getSystemService(SENSOR_SERVICE));
        mPocket.registerListener(mPocketDetectorListener);

        mPocket.start();

        showNotification(NOTIFICATION,"Walking Service");
    }

    @Override
    public void onDestroy() {
        mLegMovementDetector.stopDetector();
        mNotificationManager.cancel(NOTIFICATION);
        mPocket.release();
        mWakeLock.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Light Service
        mPocket.start();
        return START_STICKY;
    }

    /******************* Working with Pocket detector *****************/

    private IInPocketListener mPocketDetectorListener = new IInPocketListener() {

        /**
         * Called when you put the phone in pocket
         */
        public void phoneInPocket() {
            if (mLegMovementDetector != null) { // just to be on safe side


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        int callState = tm.getCallState();
                        // Do something after 3s = 3000ms
                        mLegMovementDetector.startDetector();
                        //Light Listener
                        mylightListener.onResumeLightListener();
                        if (mLegMovementDetector.LEG_ACTIVITY >0 && mylightListener.lightvalue<2 && callState!=2)
                        {
                            AudioManager amexit = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            int streamMaxVolumeExit = amexit.getStreamMaxVolume(AudioManager.STREAM_RING);
                            amexit.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolumeExit, AudioManager.FLAG_ALLOW_RINGER_MODES);
                            amexit.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            mylightListener.lightvalue=0;
                            mylightListener.onPauseLightListener();
                        }
                    }
                }, 3000);

            }
        }

        /**
         * Called when you take the phone out of pocket
         */
        public void phoneOutOfPocket()
        {
            Log.d("Phone out of Pocket", "Checked");
        }
    };

    /********************* Private methods *****************************/


    /**
     * Show a notification while this service is running.
     */
    @SuppressWarnings("deprecation")
    private void showNotification(int id, String string) {
        CharSequence text = string;
        Notification notification = new Notification(R.drawable.ic_stat_walking_icon, text, System.currentTimeMillis());
        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        notification.setLatestEventInfo(this, "Service is Running", text, contentIntent);

        mNotificationManager.notify(id, notification);
    }



    /**
     * Get the light value from Light Sensor.
     */
    public class LightListener implements SensorEventListener {
        Context context;
        public float lightvalue=0;
        SensorManager sensorManager;
        Sensor sensor;
        public  LightListener(Context context)
        {
            this.context=context;
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        }
        public void onResumeLightListener()
        {
            Log.d("Light", "Entered");
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        public void onPauseLightListener() {

            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            lightvalue = event.values[0];
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}