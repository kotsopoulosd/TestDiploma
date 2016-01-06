package com.example.dkotsopoulos.testdiploma;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DKotsopoulos on 12/10/2015.
 */
public class EventFullObject implements Parcelable {
    private String title;
    private String description;
    private String EventURL;
    private String start_time;
    private String stop_time;
    private String VenueURL;
    private String venue_name;
    private String venue_address;
    private String City;
//    private LatLng coordinates;


    public String getEventTitle()
    {
        return this.title;
    }
    public String getdescription()
    {
        return this.description;
    }
    public String getEventURL()
    {
        return this.EventURL;
    }
    public String getstart_time()
    {
        return this.start_time;
    }
    public String getstop_time()
    {
        return this.stop_time;
    }
    public String getVenueURL()
    {
        return this.VenueURL;
    }
    public String getvenue_name()
    {
        return this.venue_name;
    }
    public String getvenue_address()
    {
        return this.venue_address;
    }
    public String getCity()
    {
        return this.City;
    }
//    public LatLng getcoordinates()
//    {
//        return this.coordinates;
//    }

    public boolean setEventTitle(String title)
    {
        this.title = title;
        return true;
    }
    public boolean setdescription(String description)
    {
        this.description = description;
        return true;
    }
    public boolean setEventURL(String EventURL)
    {
        this.EventURL = EventURL;
        return true;
    }
    public boolean setStart_time(String start_time)
    {
        this.start_time = start_time;
        return true;
    }
    public boolean setStop_time(String stop_time)
    {
        this.stop_time = stop_time;
        return true;
    }
    public boolean setVenueURL(String VenueURL)
    {
        this.VenueURL = VenueURL;
        return true;
    }
    public boolean setVenue_name(String venue_name)
    {
        this.venue_name = venue_name;
        return true;
    }
    public boolean setvenue_address(String venue_address)
    {
        this.venue_address= venue_address;
        return true;
    }
    public boolean setCity(String City)
    {
        this.City = City;
        return true;
    }
//    public boolean setCoordinates(LatLng coordinates)
//    {
//        this.coordinates =coordinates;
//        return true;
//    }

    public EventFullObject() {
        title="";
        description="";
        EventURL="";
        start_time="";
        stop_time="";
        VenueURL="";
        venue_name="";
        venue_address="";
        City="";

    }
    public EventFullObject(Parcel in) {
        String title=in.readString();
        String description=in.readString();
        String EventURL=in.readString();
        String start_time=in.readString();
        String stop_time=in.readString();
        String VenueURL=in.readString();
        String venue_name=in.readString();
        String venue_address=in.readString();
        String City=in.readString();
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(EventURL);
        dest.writeString(start_time);
        dest.writeString(stop_time);
        dest.writeString(VenueURL);
        dest.writeString(venue_name);
        dest.writeString(venue_address);
        dest.writeString(City);

    }

    public static final Parcelable.Creator<EventFullObject> CREATOR = new Parcelable.Creator<EventFullObject>()
    {
        public EventFullObject createFromParcel(Parcel in)
        {
            return new EventFullObject(in);
        }
        public EventFullObject[] newArray(int size)
        {
            return new EventFullObject[size];
        }
    };


    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

//    public EventFullObject(String title,String description,String EventURL, String start_time,String stop_time,String VenueURL,String venue_name,String venue_address
//                           ,String City) {
//        this.title = title;
//        this.description=description;
//        this.EventURL=EventURL;
//        this.start_time = start_time;
//        this.stop_time = stop_time;
//        this.VenueURL = VenueURL;
//        this.venue_name = venue_name;
//        this.venue_address = venue_address;
//        this.City = City;
//        //this.coordinates = coordinates;
//
//    }

}
