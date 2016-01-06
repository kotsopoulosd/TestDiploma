package com.example.dkotsopoulos.testdiploma;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by DKotsopoulos on 18/09/2015.
 */
public class LightListener implements SensorEventListener {
    Context context;
    float lightvalue;
    SensorManager sensorManager;
    Sensor sensor;
    DBHelper dbHelper;
    private long lastUpdate = 0;

    public  LightListener(Context context)
    {
        this.context=context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }
    public void onResumeLightListener()
    {
        Log.d("Light", "Entered");
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPauseLightListener() {

        sensorManager.unregisterListener(this);
    }

    @Override
        public void onSensorChanged(SensorEvent event)
        {
            dbHelper=new DBHelper(context);
            lightvalue = event.values[0];
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 300000)//5min
            {

                lastUpdate = curTime;
                dbHelper.InsertLightLogs(Float.toString(lightvalue), lastUpdate);
            }
            dbHelper.close();
        }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
