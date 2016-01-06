package com.example.dkotsopoulos.testdiploma;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AccelerometerListener implements SensorEventListener
    {
        Context context;
        DBHelper dbHelper;
        private float alpha = (float) 0.8;
        private long lastUpdate = 0;
        private SensorManager mSensorManager;
        private Sensor mAccelerometer;

        public  AccelerometerListener(Context context)
        {
            mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            this.context=context;
        }
        public void onResumeAccelerometer()
        {
            Log.d("Accelerometer", "Entered");
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        public void onPauseAccelerometer() {

            mSensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            dbHelper=new DBHelper(context);
            Sensor mySensor = event.sensor;
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float [] gravity = new float[3];
                List<Float> test=new ArrayList<Float>();

                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];


                test.add(new Float(event.values[0] - gravity[0]));
                test.add(new Float(event.values[1] - gravity[1]));
                test.add(new Float(event.values[2] - gravity[2]));

                long curTime = System.currentTimeMillis();


                if ((curTime - lastUpdate) > 300000)//5min
                {
                    lastUpdate = curTime;
                    dbHelper.InsertAcceleration(test.get(0),test.get(1),test.get(2), lastUpdate);
                }
            }
            dbHelper.close();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }