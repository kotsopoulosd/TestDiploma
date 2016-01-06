package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 18/09/2015.
 */
public class GyroscopeObject  {
    private double temp_GyroscopeX;
    private double temp_GyroscopeY;
    private double temp_GyroscopeZ;
    private long timestamp;

    public double getTemp_GyroscopeX()
    {
        return this.temp_GyroscopeX;
    }
    public double getTemp_GyroscopeY()
    {
        return this.temp_GyroscopeY;
    }
    public long getTimestamp()
    {
        return this.timestamp;
    }
    public double getTemp_GyroscopeZ() {
        return this.temp_GyroscopeZ;
    }

    public boolean setTempGyroscopeX(double temp_GyroscopeX)
    {
        this.temp_GyroscopeX = temp_GyroscopeX;
        return true;
    }
    public boolean setTempGyroscopeY(double temp_GyroscopeY)
    {
        this.temp_GyroscopeY = temp_GyroscopeY;
        return true;
    }
    public boolean setTempGyroscopeZ(double temp_GyroscopeZ)
    {
        this.temp_GyroscopeZ = temp_GyroscopeZ;
        return true;
    }
    public boolean setTimestamp(long timestamp)
    {
        this.timestamp =timestamp;
        return true;
    }

    public GyroscopeObject() {}

    public GyroscopeObject(double temp_GyroscopeX, double temp_GyroscopeY, double temp_GyroscopeZ, long timestamp) {
        this.temp_GyroscopeX = temp_GyroscopeX;
        this.temp_GyroscopeY=temp_GyroscopeY;
        this.temp_GyroscopeZ=temp_GyroscopeZ;
        this.timestamp = timestamp;

    }

    public String toString() {
        return "Gyroscope is :{" +
                "Gyroscope X='" + temp_GyroscopeX + '\'' +
                ", Gyroscope Y='" + temp_GyroscopeY + '\'' +
                ", Gyroscope Z='" + temp_GyroscopeZ + '\'' +
                " and timestamp=" + timestamp+'}';
    }
}