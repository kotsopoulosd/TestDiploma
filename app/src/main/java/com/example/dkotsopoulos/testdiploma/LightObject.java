package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 18/09/2015.
 */
public class LightObject
{
    private String LighhtVolume ;
    private long LightTimestamp;

    public String getLighhtVolume()
    {
    return this.LighhtVolume;
    }
    public long getLightTimestamp()
    {
    return this.LightTimestamp;
    }

    public boolean setLightTimestamp(long LightTimestamp)
    {
        this.LightTimestamp =LightTimestamp;
        return true;
    }
    public boolean setLighhtVolume(String LighhtVolume)
    {
        this.LighhtVolume =LighhtVolume;
        return true;
    }
    public LightObject(){}
    public LightObject(String LighhtVolume, long LightTimestamp) {
        this.LighhtVolume = LighhtVolume;
        this.LightTimestamp = LightTimestamp;
    }
    public String toString()
    {
        return  "Lighht Volume: " + LighhtVolume+
        "Light Timestamp: " + LightTimestamp;
    }

}
