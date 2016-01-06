package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 01/09/2015.
 */

public class GPSObject{
    private double temp_CoordinationX;
    private double temp_CoordinationY;
    private double temp_ACC;
    private long timestamp;
    private double temp_speed;

    public double getTemp_CoordinationX()
    {
        return this.temp_CoordinationX;
    }
    public double getTemp_CoordinationY()
    {
        return this.temp_CoordinationY;
    }
    public double getTemp_ACC()
    {
        return this.temp_ACC;
    }
    public long getTimestamp()
    {
        return this.timestamp;
    }
    public double getSpeed()
    {
        return this.temp_speed;
    }

    public boolean setTemp_CoordinationX(double temp_CoordinationX)
    {
        this.temp_CoordinationX = temp_CoordinationX;
        return true;
    }
    public boolean setTemp_CoordinationY(double temp_CoordinationY)
    {
        this.temp_CoordinationY = temp_CoordinationY;
        return true;
    }
    public boolean setTemp_ACC(double temp_ACC)
    {
        this.temp_ACC = temp_ACC;
        return true;
    }
    public boolean setTimestamp(long timestamp)
    {
        this.timestamp =timestamp;
        return true;
    }

    public boolean speed(double speed)
    {
        this.temp_speed = speed;
        return true;
    }

    public GPSObject() {}
    public GPSObject( double temp_CoordinationX, double temp_CoordinationY, double temp_ACC, long timestamp, double temp_speed) {
        this.temp_CoordinationX = temp_CoordinationX;
        this.temp_CoordinationY = temp_CoordinationY;
        this.temp_ACC = temp_ACC;
        this.timestamp = timestamp;
        this.temp_speed = temp_speed;
    }
    public String toString() {
        return "GPS{" +
                "Coordinate X='" + temp_CoordinationX + '\'' +
                ", Coordinate Y='" + temp_CoordinationY + '\'' +
                ", Accuracy='" + temp_ACC + '\'' +
                ", Accuracy='" + temp_speed + '\'' +
                " and timestamp=" + timestamp+'}';
    }
}

