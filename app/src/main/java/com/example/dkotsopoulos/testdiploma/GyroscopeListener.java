package com.example.dkotsopoulos.testdiploma;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import static android.util.FloatMath.cos;
import static android.util.FloatMath.sin;
import static android.util.FloatMath.sqrt;

public class GyroscopeListener implements SensorEventListener {

    Context context;
    DBHelper dbHelper;
    private long lastUpdate = 0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final double EPSILON = 0.000000001f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private SensorManager mSensorManager;
    private Sensor mGyroSensor;
    private float axisX=0;
    private float axisY=0;
    private float axisZ=0;

    public GyroscopeListener(Context context) {

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.context=context;
    }

    protected void onResumeGyroscopeListener() {
        Log.d("GyroscopeListener", "Entered");
        // Register a listener for the sensor.
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    protected void onPauseGyroscopeListener() {
        // important to unregister the sensor when the activity pauses.
        mSensorManager.unregisterListener(this);
    }

public void onSensorChanged(SensorEvent event) {
        dbHelper=new DBHelper(context);
        // This timestep's delta rotation to be multiplied by the current rotation
        //after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            axisX = event.values[0];
            axisY = event.values[1];
            axisZ = event.values[2];
            // Calculate the angular speed of the sample
            float omegaMagnitude = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }
            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = sin(thetaOverTwo);
            float cosThetaOverTwo = cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }

        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 300000)//5min
        {
            lastUpdate = curTime;
            dbHelper.InsertGyroscope(event.values[0],event.values[1],event.values[2],lastUpdate);
        }
        dbHelper.close();


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}