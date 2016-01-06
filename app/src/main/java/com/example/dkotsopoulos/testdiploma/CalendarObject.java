package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class CalendarObject{

    private String eventid ;
    private String nameOfEvent ;
    private String descriptions ;
    private String eventLocation;
    private long startDates;
    private long endDates ;

    public String getnameOfEvent()
    {
        return this.nameOfEvent;
    }
    public String getdescriptions()
    {
        return this.descriptions;
    }
    public String geteventLocation()
    {
        return this.eventLocation;
    }
    public long getstartDates() {
        return this.startDates;
    }
    public long getendDates()
    {
        return this.endDates;
    }

    public boolean seteventid(String eventid)
    {
        this.eventid = eventid;
        return true;
    }
    public boolean setnameOfEvent(String nameOfEvent)
    {
        this.nameOfEvent = nameOfEvent;
        return true;
    }
    public boolean setdescriptions(String descriptions)
    {
        this.descriptions = descriptions;
        return true;
    }
    public boolean seteventLocation(String eventLocation)
    {
        this.eventLocation =eventLocation;
        return true;
    }

    public boolean setstartDates(long startDates)
    {
        this.startDates = startDates;
        return true;
    }
    public boolean setendDates(long endDates)
    {
        this.endDates = endDates;
        return true;
    }

    public CalendarObject() {
    }

    public CalendarObject(String eventid, String nameOfEvent, String descriptions, String eventLocation,long startDates, long endDates) {
        this.eventid = eventid;
        this.nameOfEvent = nameOfEvent;
        this.descriptions = descriptions;
        this.eventLocation = eventLocation;
        this.startDates = startDates;
        this.endDates = endDates;
    }

    public String toString() {
        return  "Event ID: " + eventid+
                "Name of Event: " + nameOfEvent +
                "Event Description:  " + descriptions+
                "Event Location: " + eventLocation+
                "Start time: " + startDates+
                "End Time: " + endDates;
    }
}

