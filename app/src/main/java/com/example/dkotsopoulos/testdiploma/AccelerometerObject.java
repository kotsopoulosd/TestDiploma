package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 04/09/2015.
 */
public class AccelerometerObject {
    private double temp_AccelerationX;
    private double temp_AccelerationY;
    private double temp_AccelerationZ;
    private long timestamp;


    public double getTemp_AccelerationX()
    {
        return this.temp_AccelerationX;
    }
    public double getTemp_AccelerationY()
    {
        return this.temp_AccelerationY;
    }
    public double getTemp_AccelerationZ() {
        return this.temp_AccelerationZ;
    }
    public long getTimestamp()
    {
        return this.timestamp;
    }

    public boolean setTemp_AccelerationX(double temp_AccelerationX)
    {
        this.temp_AccelerationX = temp_AccelerationX;
        return true;
    }
    public boolean setTemp_AccelerationY(double temp_AccelerationY)
    {
        this.temp_AccelerationY = temp_AccelerationY;
        return true;
    }
    public boolean setTemp_AccelerationZ(double temp_AccelerationZ)
    {
        this.temp_AccelerationZ = temp_AccelerationZ;
        return true;
    }
    public boolean setTimestamp(long timestamp)
    {
        this.timestamp =timestamp;
        return true;
    }

    public AccelerometerObject() {

    }
    public AccelerometerObject(double temp_AccelerationX, double temp_AccelerationY, double temp_AccelerationZ,long timestamp) {
        this.temp_AccelerationX = temp_AccelerationX;
        this.temp_AccelerationY=temp_AccelerationY;
        this.temp_AccelerationZ=temp_AccelerationZ;
        this.timestamp = timestamp;

    }

    public String toString() {
        return "Acceleration is :{" +
                "Acceleration X='" + temp_AccelerationX + '\'' +
                ", Acceleration Y='" + temp_AccelerationY + '\'' +
                ", Acceleration Z='" + temp_AccelerationZ + '\'' +
                " and timestamp=" + timestamp+'}';
    }

}

