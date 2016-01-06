package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class ApplicationObject {

    private String ApplicationName ;
    private String ApplicationPackageName ;
    private long ApplicationForeground ;
    private long Timestamp;

    public String getApplicationName()
    {
        return this.ApplicationName;
    }
    public Long getApplicationForeground()
    {
        return this.ApplicationForeground;
    }
    public Long getTimestamp()
    {
        return this.Timestamp;
    }
    public String getApplicationPackageName()
    {
        return this.ApplicationPackageName;
    }
    public boolean setApplicationName(String ApplicationName)
    {
        this.ApplicationName = ApplicationName;
        return true;
    }
    public boolean setApplicationPackageName(String ApplicationPackageName)
    {
        this.ApplicationPackageName = ApplicationPackageName;
        return true;
    }
    public boolean setApplicationForeground(Long ApplicationForeground)
    {
        this.ApplicationForeground = ApplicationForeground;
        return true;
    }
    public boolean setTimestamp(long Timestamp)
    {
        this.Timestamp =Timestamp;
        return true;
    }

    public ApplicationObject() {

    }
    public ApplicationObject(String ApplicationName,String ApplicationPackageName, long ApplicationForeground, long Timestamp) {
        this.ApplicationName = ApplicationName;
        this.ApplicationForeground = ApplicationForeground;
        this.Timestamp = Timestamp;
    }
    public String toString() {
        return  "Application Name: " + ApplicationName+
                "Application Foreground: " + ApplicationForeground;
    }


}
