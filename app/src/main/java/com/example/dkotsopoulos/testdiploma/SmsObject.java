package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 10/09/2015.
 */
public class SmsObject {

    private String Person;
    private String body ;
    private String address ;
    private String type;
    private long calldate;

    public String getPerson()
    {
        return this.Person;
    }
    public String getbody()
    {
        return this.body;
    }
    public String getaddress()
    {
        return this.address;
    }
    public String gettype()
    {
        return this.type;
    }
    public long getcalldate()
    {
        return this.calldate;
    }

    public boolean setbody(String body)
    {
        this.body = body;
        return true;
    }
    public boolean setPerson(String Person)
    {
        this.Person = Person;
        return true;
    }
    public boolean setaddress(String address)
    {
        this.address = address;
        return true;
    }
    public boolean settype(String type)
    {
        this.type = type;
        return true;
    }
    public boolean setTimestamp(long calldate)
    {
        this.calldate =calldate;
        return true;
    }

    public SmsObject() {

    }
    public SmsObject(String body, String address, String Person, String type, long calldate) {
        this.body = body;
        this.Person=Person;
        this.address = address;
        this.type = type;
        this.calldate = calldate;

    }

    public String toString() {
        return  "Context: " + body+
                "Sms Person: " + address +
                "Sms Type: " + type+
                "Sms date: " + calldate;
    }


}