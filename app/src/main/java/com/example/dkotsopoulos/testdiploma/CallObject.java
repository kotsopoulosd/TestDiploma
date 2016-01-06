package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 09/09/2015.
 */
public class CallObject
{
    private String phnumber ;
    private String callduration ;
    private String callname ;
    private String calltype ;
    private String callGeo;
    private long calldate;

    public String getphnumber()
    {
        return this.phnumber;
    }
    public String getcallduration()
    {
        return this.callduration;
    }
    public String getcallname()
    {
        return this.callname;
    }
    public String getcalltype()
    {
        return this.calltype;
    }
    public String getTempcallGeo() {
        return this.callGeo;
    }
    public long getcalldate()
    {
        return this.calldate;
    }

    public boolean setphnumber(String phnumber)
    {
        this.phnumber = phnumber;
        return true;
    }
    public boolean setcallduration(String callduration)
    {
        this.callduration = callduration;
        return true;
    }
    public boolean setcallname(String callname)
    {
        this.callname = callname;
        return true;
    }
    public boolean setTimestamp(long calldate)
    {
        this.calldate =calldate;
        return true;
    }

    public boolean setcalltype(String calltype)
    {
        this.calltype = calltype;
        return true;
    }
    public boolean setcallGeo(String callGeo)
    {
        this.callGeo = callGeo;
        return true;
    }

    public CallObject() {

    }
    public CallObject(String phnumber, String callduration, String callname, String calltype, String callGeo, long calldate) {
        this.phnumber = phnumber;
        this.callduration = callduration;
        this.callname = callname;
        this.calltype = calltype;
        this.callGeo = callGeo;
        this.calldate = calldate;
    }



    public String toString() {
        return  "Phone Number: " + phnumber+
                "Call Duration: " + callduration +
                "Caller/Callee Name:  " + callname+
                "Call Type: " + calltype+
                "Gall GeoLocation: " + callGeo;
    }


}