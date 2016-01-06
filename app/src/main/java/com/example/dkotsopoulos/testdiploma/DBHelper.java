package com.example.dkotsopoulos.testdiploma;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by Stuart-pc on 30/08/2015.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String Tag="DBHelper";
    public static final String DB_NAME="FirstExample.db";
    public static final int DB_VERSION=2;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_GPS);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_GPS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_ACCELERATION);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_ACCELERATION);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_CALLS);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_CALLS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_SMS);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_SMS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_CALENDAR);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_CALENDAR);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_APPLICATION);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_APPLICATION);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_APPLICATION_INSTALLED);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_APPLICATION_INSTALLED);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_LIGHTLOG);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_LIGHTLOG);
        sqLiteDatabase.execSQL(DATABASE_CREATE_POINTS_TABLE_GYROSCOPE);
        Log.d(Tag, "on create SQL :" + DATABASE_CREATE_POINTS_TABLE_GYROSCOPE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion )
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_GPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_ACCELERATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_GYROSCOPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_CALLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_SMSLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_CALENDARLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_APPLICATIONLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_INSTALLED_APPLICATIONLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMPLE_LIGHTLOGS);
        onCreate(db);

    }

    /*------------------------------------Start of GPS queries!--------------------------------*/

    //* Table creation GPS
    public static final String TABLE_EXAMPLE_GPS = "GPSLogs";
    public static final String COLUMN_COORDINATION_X = "Coordination_x";
    public static final String COLUMN_LogGPSID = "GPSId";
    public static final String COLUMN_COORDINATION_Y = "Coordination_y";
    public static final String COLUMN_ACC = "ACC";
    public static final String COLUMN_TIMESTAMP_GPS = "Timestamp";

    // * table command GPS
    private static final String DATABASE_CREATE_POINTS_TABLE_GPS = "create table "
            +TABLE_EXAMPLE_GPS     +"("
            +COLUMN_COORDINATION_X   +" double not null,"
            +COLUMN_LogGPSID     +" integer primary key autoincrement, "
            +COLUMN_COORDINATION_Y   +" double not null,"
            +COLUMN_ACC  +" double not null,"
            +COLUMN_TIMESTAMP_GPS   +" integer not null);";

    // Insert a gps record
    public long InsertGPS(double coordx,double coordy,double acc,long Timestamp)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Coordination_x", coordx);
        contentValues.put("Coordination_y", coordy);
        contentValues.put("ACC", acc);
        contentValues.put("Timestamp", Timestamp);
        long l = db.insert(TABLE_EXAMPLE_GPS, null, contentValues);

        return l;
    }

    //// Get all the records from gps table
    public List<GPSObject> GetAllGPS()
    {

        List<GPSObject> tempGPS =new ArrayList<GPSObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_GPS+" order by "+COLUMN_TIMESTAMP_GPS+ " DESC", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            GPSObject temporarylocation= new GPSObject();
            temporarylocation.setTemp_CoordinationX(res.getDouble(res.getColumnIndex(COLUMN_COORDINATION_X)));
            temporarylocation.setTemp_CoordinationY(res.getDouble(res.getColumnIndex(COLUMN_COORDINATION_Y)));
            temporarylocation.setTemp_ACC(res.getDouble(res.getColumnIndex(COLUMN_ACC)));
            temporarylocation.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_TIMESTAMP_GPS)));

            tempGPS.add(temporarylocation);
            res.moveToNext();
        }
        res.close();

        return tempGPS;
    }
    //// Get all the records from gps table
    public List<GroupPointObject> GetGroupedGPS()
    {

        List<GroupPointObject> tempGPS =new ArrayList<GroupPointObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_GPS+" order by "+COLUMN_TIMESTAMP_GPS+ " DESC"+" LIMIT 1000", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            GroupPointObject temporarylocation= new GroupPointObject();
            temporarylocation.setX(res.getDouble(res.getColumnIndex(COLUMN_COORDINATION_X)));
            temporarylocation.setY(res.getDouble(res.getColumnIndex(COLUMN_COORDINATION_Y)));
            temporarylocation.setdatetime(res.getLong(res.getColumnIndex(COLUMN_TIMESTAMP_GPS)));
            tempGPS.add(temporarylocation);
            res.moveToNext();
        }
        res.close();

        return tempGPS;
    }
/*-------------------------------------------------End of GPS queries!------------------------------*/



/*------------------------------------Start of Acceleration queries!--------------------------------*/

    //* Table creation Acceleration
    public static final String TABLE_EXAMPLE_ACCELERATION = "AccelerationLogs";
    public static final String COLUMN_ACCELERATION_X = "Acceleration_x";
    public static final String COLUMN_LogACCID = "AccelerationId";
    public static final String COLUMN_ACCELERATION_Y = "Acceleration_y";
    public static final String COLUMN_ACCELERATION_Z = "Acceleration_Z";
    public static final String COLUMN_TIMESTAMP_XYZ = "Timestamp";

    // * table command Acceleration
    private static final String DATABASE_CREATE_POINTS_TABLE_ACCELERATION = "create table "
            +TABLE_EXAMPLE_ACCELERATION     +"("
            +COLUMN_ACCELERATION_X   +" double not null,"
            +COLUMN_LogACCID     +" integer primary key autoincrement, "
            +COLUMN_ACCELERATION_Y   +" double not null,"
            +COLUMN_ACCELERATION_Z  +" double not null,"
            +COLUMN_TIMESTAMP_XYZ   +" integer not null);";

    // Insert an Acceleration record
    public long InsertAcceleration(double accx,double accy,double accz,long Timestamp)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Acceleration_x", accx);
        contentValues.put("Acceleration_y", accy);
        contentValues.put("Acceleration_z", accz);
        contentValues.put("Timestamp", Timestamp);
        long l = db.insert(TABLE_EXAMPLE_ACCELERATION, null, contentValues);

        return l;
    }
    //// Get all the records from acceleration table
    public List<AccelerometerObject> GetAllAcceleration()
    {
        List<AccelerometerObject> tempXYZ =new ArrayList<AccelerometerObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_ACCELERATION+" order by "+COLUMN_TIMESTAMP_XYZ+ " DESC", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            AccelerometerObject temporarylocation= new AccelerometerObject();
            temporarylocation.setTemp_AccelerationX(res.getDouble(res.getColumnIndex(COLUMN_ACCELERATION_X)));
            temporarylocation.setTemp_AccelerationY(res.getDouble(res.getColumnIndex(COLUMN_ACCELERATION_Y)));
            temporarylocation.setTemp_AccelerationZ(res.getDouble(res.getColumnIndex(COLUMN_ACCELERATION_Z)));
            temporarylocation.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_TIMESTAMP_XYZ)));

            tempXYZ.add(temporarylocation);
            res.moveToNext();
        }
        res.close();

        return tempXYZ;
    }

/*-------------------------------------------------End of Acceleration queries!--------------------*/


/*------------------------------------Start of Gyroscope queries!--------------------------------*/

    //* Table creation Gyroscope
    public static final String TABLE_EXAMPLE_GYROSCOPE= "GyroscopeLogs";
    public static final String COLUMN_GYROSCOPE_X = "Gyroscope_x";
    public static final String COLUMN_LogGYROSCOPEID = "GyroscopeId";
    public static final String COLUMN_GYROSCOPE_Y = "Gyroscope_y";
    public static final String COLUMN_GYROSCOPE_Z = "Gyroscope_Z";
    public static final String COLUMN_TIMESTAMP_GYROSCOPE = "Timestamp";

    // * table command Gyroscope
    private static final String DATABASE_CREATE_POINTS_TABLE_GYROSCOPE = "create table "
            +TABLE_EXAMPLE_GYROSCOPE     +"("
            +COLUMN_GYROSCOPE_X   +" double not null,"
            +COLUMN_LogGYROSCOPEID     +" integer primary key autoincrement not null, "
            +COLUMN_GYROSCOPE_Y   +" double not null,"
            +COLUMN_GYROSCOPE_Z  +" double not null,"
            +COLUMN_TIMESTAMP_GYROSCOPE   +" integer not null);";

    // Insert an Gyroscope record
    public long InsertGyroscope(double Gyroscope_x,double Gyroscope_y,double Gyroscope_Z,long Timestamp)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Gyroscope_x", Gyroscope_x);
        contentValues.put("Gyroscope_y", Gyroscope_y);
        contentValues.put("Gyroscope_Z", Gyroscope_Z);
        contentValues.put("Timestamp", Timestamp);
        long l = db.insert(TABLE_EXAMPLE_GYROSCOPE, null, contentValues);

        return l;
    }
    //// Get all the records from Gyroscope table
    public List<GyroscopeObject> GetAllGyroscope()
    {
        List<GyroscopeObject> tempGyroscope =new ArrayList<GyroscopeObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_GYROSCOPE+" order by "+COLUMN_TIMESTAMP_GYROSCOPE+ " DESC", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            GyroscopeObject temporaryGyroscope= new GyroscopeObject();
            temporaryGyroscope.setTempGyroscopeX(res.getDouble(res.getColumnIndex(COLUMN_GYROSCOPE_X)));
            temporaryGyroscope.setTempGyroscopeY(res.getDouble(res.getColumnIndex(COLUMN_GYROSCOPE_Y)));
            temporaryGyroscope.setTempGyroscopeZ(res.getDouble(res.getColumnIndex(COLUMN_GYROSCOPE_Z)));
            temporaryGyroscope.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_TIMESTAMP_GYROSCOPE)));

            tempGyroscope.add(temporaryGyroscope);
            res.moveToNext();
        }
        res.close();

        return tempGyroscope;
    }

/*-------------------------------------------------End of Gyroscope queries!--------------------*/





/*------------------------------------Start of CallLog queries!-----------------------------------*/

//* Table creation Calllogs
public static final String TABLE_EXAMPLE_CALLOGS = "Call_Logs";
    public static final String COLUMN_CALLID = "CallId";
    public static final String COLUMN_CALLNUMBER = "CallNumber";
    public static final String COLUMN_CALLDURATION = "CallDuration";
    public static final String COLUMN_CALLNAME = "CallName";
    public static final String COLUMN_CALLTYPE = "CallType";
    public static final String COLUMN_CALLGEO = "CallGeo";
    public static final String COLUMN_CALL_TIMESTAMP = "Timestamp";

    // * table command Calls
    private static final String DATABASE_CREATE_POINTS_TABLE_CALLS = "create table "
            +TABLE_EXAMPLE_CALLOGS     +"("
            +COLUMN_CALLNUMBER   +" string not null,"
            +COLUMN_CALLID     +" integer primary key autoincrement, "
            +COLUMN_CALLDURATION     +" string not null, "
            +COLUMN_CALLNAME   +" string not null,"
            +COLUMN_CALLTYPE  +" string not null,"
            +COLUMN_CALLGEO  +" string not null,"
            +COLUMN_CALL_TIMESTAMP   +" integer unique not null);";
    // Insert an CallLog record
    public long InsertCallLogs(String callnumber,String callduration,String callname,String calltype,String callgeo, long Timestamp)
    {

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("CallNumber", callnumber);
            contentValues.put("CallDuration", callduration);
            contentValues.put("CallName", callname);
            contentValues.put("CallType", calltype);
            contentValues.put("CallGeo", callgeo);
            contentValues.put("Timestamp", Timestamp);
            long l = db.insertWithOnConflict(TABLE_EXAMPLE_CALLOGS, null, contentValues,SQLiteDatabase.CONFLICT_ABORT);
            return l;
        }
        catch (Throwable throwable)
        {
            return 0;
        }

    }
    //// Get all the records from acceleration table
    public List<CallObject> GetAllCalls()
    {

        List<CallObject> tempXYZ = new ArrayList<CallObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_CALLOGS+ " order by "+COLUMN_CALL_TIMESTAMP+ " DESC",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            CallObject temporaryCalls= new CallObject();
            temporaryCalls.setphnumber(res.getString(res.getColumnIndex(COLUMN_CALLNUMBER)));
            temporaryCalls.setcallduration(res.getString(res.getColumnIndex(COLUMN_CALLDURATION)));
            temporaryCalls.setcallname(res.getString(res.getColumnIndex(COLUMN_CALLNAME)));
            temporaryCalls.setcallGeo(res.getString(res.getColumnIndex(COLUMN_CALLGEO)));
            temporaryCalls.setcalltype(res.getString(res.getColumnIndex(COLUMN_CALLTYPE)));
            temporaryCalls.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_CALL_TIMESTAMP)));

            tempXYZ.add(temporaryCalls);
            res.moveToNext();
        }
        res.close();

        return tempXYZ;
    }
    //// Get top three callers
    public List<CallObject> GetTopThreeCallers()
    {

        List<CallObject> tempXYZ = new ArrayList<CallObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select *"  + ", sum(" + COLUMN_CALLDURATION + ") AS 'Duration'" + " from " + TABLE_EXAMPLE_CALLOGS  + " Group by " + COLUMN_CALLNUMBER+ " Order by Duration DESC"+" LIMIT 3",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            CallObject temporaryCalls= new CallObject();
            temporaryCalls.setphnumber(res.getString(res.getColumnIndex(COLUMN_CALLNUMBER)));
            temporaryCalls.setcallduration(res.getString(res.getColumnIndex("Duration")));
            temporaryCalls.setcallname(res.getString(res.getColumnIndex(COLUMN_CALLNAME)));
            temporaryCalls.setcallGeo(res.getString(res.getColumnIndex(COLUMN_CALLGEO)));
            temporaryCalls.setcalltype(res.getString(res.getColumnIndex(COLUMN_CALLTYPE)));
            temporaryCalls.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_CALL_TIMESTAMP)));

            tempXYZ.add(temporaryCalls);
            res.moveToNext();
        }
        res.close();

        return tempXYZ;
    }
    //// Get top three callers
    public List<CallObject> GetTopThreeCallersAfterWork()
    {

        List<CallObject> tempXYZ = new ArrayList<CallObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * ,datetime("+COLUMN_CALL_TIMESTAMP+"/1000, 'unixepoch'),sum("+COLUMN_CALLDURATION+")  AS 'Duration' FROM " +TABLE_EXAMPLE_CALLOGS+ " WHERE strftime('%H',datetime("+COLUMN_CALL_TIMESTAMP+"/1000, 'unixepoch'))> '18:00' AND strftime('%H',datetime("+COLUMN_CALL_TIMESTAMP+"/1000, 'unixepoch'))< '23:00' Group by "+ COLUMN_CALLNAME+ " Order by Duration DESC LIMIT 3"
                ,null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            CallObject temporaryCalls= new CallObject();
            temporaryCalls.setphnumber(res.getString(res.getColumnIndex(COLUMN_CALLNUMBER)));
            temporaryCalls.setcallduration(res.getString(res.getColumnIndex("Duration")));
            temporaryCalls.setcallname(res.getString(res.getColumnIndex(COLUMN_CALLNAME)));
            temporaryCalls.setcallGeo(res.getString(res.getColumnIndex(COLUMN_CALLGEO)));
            temporaryCalls.setcalltype(res.getString(res.getColumnIndex(COLUMN_CALLTYPE)));
            temporaryCalls.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_CALL_TIMESTAMP)));

            tempXYZ.add(temporaryCalls);
            res.moveToNext();
        }
        res.close();

        return tempXYZ;
    }

/*-------------------------------------------------End of CallLog queries!--------------------*/


/*------------------------------------Start of SmsLog queries!-----------------------------------*/


    //* Table creation SMS
    public static final String TABLE_EXAMPLE_SMSLOGS = "SMS_Logs";
    public static final String COLUMN_SMSID = "SMSId";
    public static final String COLUMN_SMSBODY = "SMSBody";
    public static final String COLUMN_SMSNUMBER = "SMSNumber";
    public static final String COLUMN_SMSPERSON = "PersonName";
    public static final String COLUMN_SMSPERSONID = "SMSPersonID";
    public static final String COLUMN_SMSTYPE = "SMSType";
    public static final String COLUMN_SMS_TIMESTAMP = "Timestamp";

    // * table command SMS
    private static final String DATABASE_CREATE_POINTS_TABLE_SMS = "create table "
            +TABLE_EXAMPLE_SMSLOGS     +"("
            +COLUMN_SMSID     +" integer primary key autoincrement not null, "
            +COLUMN_SMSBODY   +" string not null,"
            +COLUMN_SMSNUMBER     +" string not null, "
            +COLUMN_SMSPERSON   +" string not null,"
            +COLUMN_SMSPERSONID     +" string not null, "
            +COLUMN_SMSTYPE  +" string not null,"
            +COLUMN_SMS_TIMESTAMP   +" integer unique not null);";



    // Insert an smslog record
    public long InsertSmsLogs(String SMSBody,String SMSNumber,String PersonName,String SMSPersonID,String SMSType, long Timestamp)
    {

        try
        {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("SMSBody", SMSBody);
            contentValues.put("SMSNumber", SMSNumber);
            contentValues.put("PersonName", PersonName);
            contentValues.put("SMSPersonID", SMSPersonID);
            contentValues.put("SMSType", SMSType);
            contentValues.put("Timestamp", Timestamp);
            long l = db.insertWithOnConflict(TABLE_EXAMPLE_SMSLOGS, null, contentValues,SQLiteDatabase.CONFLICT_ABORT);
            return l;
        }
        catch (Throwable throwable)
        {
            return 0;
        }

    }

    //// Get all the records from sms table
    public List<SmsObject> GetAllSms()
    {

        List<SmsObject> tempXYZ = new ArrayList<SmsObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_SMSLOGS+ " order by "+COLUMN_SMS_TIMESTAMP+ " DESC LIMIT 100",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            SmsObject temporarysms= new SmsObject();
            temporarysms.setbody(res.getString(res.getColumnIndex(COLUMN_SMSBODY)));
            temporarysms.setaddress(res.getString(res.getColumnIndex(COLUMN_SMSNUMBER)));
            temporarysms.setPerson(res.getString(res.getColumnIndex(COLUMN_SMSPERSON)));
            temporarysms.settype(res.getString(res.getColumnIndex(COLUMN_SMSTYPE)));
            temporarysms.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_SMS_TIMESTAMP)));

            tempXYZ.add(temporarysms);
            res.moveToNext();
        }
        res.close();

        return tempXYZ;
    }

    //// Get top counts records from sms table
    public List<SmsObject> GetMostThreeSMS()
    {

        List<SmsObject> tempXYZ = new ArrayList<SmsObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+ COLUMN_SMSPERSON+","+COLUMN_SMSNUMBER+",count("+COLUMN_SMSPERSON+") as Count from "+TABLE_EXAMPLE_SMSLOGS+"  Where "+COLUMN_SMSTYPE+"='INBOX' AND "+COLUMN_SMSPERSON+"  NOT Like 'Κωτσοπουλος%'  "+"Group by "+ COLUMN_SMSPERSON +" order by Count DESC LIMIT 3",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            SmsObject temporarysms= new SmsObject();
            //Content now is the count of the sms
            temporarysms.setPerson(res.getString(res.getColumnIndex(COLUMN_SMSPERSON)));
            temporarysms.setaddress(res.getString(res.getColumnIndex(COLUMN_SMSNUMBER)));
            temporarysms.setbody(res.getString(res.getColumnIndex("Count")));


            tempXYZ.add(temporarysms);
            res.moveToNext();
        }
        res.close();

        return tempXYZ;
    }



/*-------------------------------------------------End of SMSLog queries!--------------------*/



/*------------------------------------Start of Calendar queries!-----------------------------------*/


    //* Table creation calendar
    public static final String TABLE_EXAMPLE_CALENDARLOGS = "CALENDAR_Logs";

    public static final String COLUMN_CALENDARID = "CalendarId";
    public static final String COLUMN_CALENDARNAME = "CalendarEventName";
    public static final String COLUMN_CALENDAREVENTDESCRIPTION = "CalendarEventDescription";
    public static final String COLUMN_CALENDAREVENTLOCATION = "CalendarEventLocation";
    public static final String COLUMN_CALENDAREVENTSTARTIME = "CalendarEventStartime";
    public static final String COLUMN_CALENDAREVENTENDTIME = "CalendarEventNameEndtime";

    // * table command calendar
    private static final String DATABASE_CREATE_POINTS_TABLE_CALENDAR = "create table "
            +TABLE_EXAMPLE_CALENDARLOGS     +"("
            +COLUMN_CALENDARID     +" integer primary key not null, "
            +COLUMN_CALENDARNAME   +" string not null,"
            +COLUMN_CALENDAREVENTDESCRIPTION     +" string not null, "
            +COLUMN_CALENDAREVENTLOCATION   +" string not null,"
            +COLUMN_CALENDAREVENTSTARTIME     +" integer not null, "
            +COLUMN_CALENDAREVENTENDTIME  +" integer not null );";

    // Insert an CallLog record
    public long InsertCalendarLogs(String CalendarId,String CalendarEventName,String CalendarEventDescription,String CalendarEventLocation,long CalendarEventStartime, long CalendarEventNameEndtime)
    {

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("CalendarId", CalendarId);
            contentValues.put("CalendarEventName", CalendarEventName);
            contentValues.put("CalendarEventDescription", CalendarEventDescription);
            contentValues.put("CalendarEventLocation", CalendarEventLocation);
            contentValues.put("CalendarEventStartime", CalendarEventStartime);
            contentValues.put("CalendarEventNameEndtime", CalendarEventNameEndtime);
            long l = db.insertWithOnConflict(TABLE_EXAMPLE_CALENDARLOGS, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            return l;
        }
        catch (Throwable throwable)
        {
            return 0;
        }

    }

    //// Get all the records from acceleration table
    public List<CalendarObject> GetAllCalendar()
    {
        long curTime = System.currentTimeMillis();
        List<CalendarObject> tempcalendar = new ArrayList<CalendarObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_CALENDARLOGS+" order by "+COLUMN_CALENDARID+ " DESC",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            CalendarObject temporarycalendar= new CalendarObject();
            temporarycalendar.seteventid(res.getString(res.getColumnIndex(COLUMN_CALENDARID)));
            temporarycalendar.setnameOfEvent(res.getString(res.getColumnIndex(COLUMN_CALENDARNAME)));
            temporarycalendar.setdescriptions(res.getString(res.getColumnIndex(COLUMN_CALENDAREVENTDESCRIPTION)));
            temporarycalendar.seteventLocation(res.getString(res.getColumnIndex(COLUMN_CALENDAREVENTLOCATION)));
            temporarycalendar.setstartDates(res.getLong(res.getColumnIndex(COLUMN_CALENDAREVENTSTARTIME)));
            temporarycalendar.setendDates(res.getLong(res.getColumnIndex(COLUMN_CALENDAREVENTENDTIME)));

            tempcalendar.add(temporarycalendar);
            res.moveToNext();
        }
        res.close();
        return tempcalendar;
    }
    public void DeletePreviousEvents()
    {
        long curTime = System.currentTimeMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_EXAMPLE_CALENDARLOGS+" WHERE "+COLUMN_CALENDAREVENTENDTIME+"<"+curTime);
    }

    public List<CalendarObject> GetMeetings()
    {

        List<CalendarObject> tempcalendar = new ArrayList<CalendarObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_CALENDARLOGS+" WHERE "+COLUMN_CALENDARNAME+" LIKE '%"+"Meet"+"%'",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            CalendarObject temporarycalendar= new CalendarObject();
            temporarycalendar.seteventid(res.getString(res.getColumnIndex(COLUMN_CALENDARID)));
            temporarycalendar.setnameOfEvent(res.getString(res.getColumnIndex(COLUMN_CALENDARNAME)));
            temporarycalendar.setdescriptions(res.getString(res.getColumnIndex(COLUMN_CALENDAREVENTDESCRIPTION)));
            temporarycalendar.seteventLocation(res.getString(res.getColumnIndex(COLUMN_CALENDAREVENTLOCATION)));
            temporarycalendar.setstartDates(res.getLong(res.getColumnIndex(COLUMN_CALENDAREVENTSTARTIME)));
            temporarycalendar.setendDates(res.getLong(res.getColumnIndex(COLUMN_CALENDAREVENTENDTIME)));

            tempcalendar.add(temporarycalendar);
            res.moveToNext();
        }
        res.close();

        return tempcalendar;
    }



/*-------------------------------------------------End of Calendar queries!------------------------*/




/*------------------------------------Start of Application queries!-----------------------------------*/


    //* Table creation application
    public static final String TABLE_EXAMPLE_APPLICATIONLOGS = "APPLICATION_Logs";


    public static final String COLUMN_APPLICATIONRID = "AppId";
    public static final String COLUMN_APPLICATIONNAME = "ApplicationName";
    public static final String COLUMN_APPLICATIONPACKAGENAME = "ApplicationpPackageName";
    public static final String COLUMN_FOREGROUNDDURATION = "ForegroundDuration";
    public static final String COLUMN_TIMESTAMP = "AppTimestamp";


    //Installed application table
    public static final String TABLE_EXAMPLE_INSTALLED_APPLICATIONLOGS = "Installed_APP_Logs";
    public static final String COLUMN_INSTALLED_APPID = "InstalledAppId";
    public static final String COLUMN_INSTALLED_APPNAME = "InstalledApplicationName";

    // * table command application
    private static final String DATABASE_CREATE_POINTS_TABLE_APPLICATION = "create table "
            +TABLE_EXAMPLE_APPLICATIONLOGS     +"("
            +COLUMN_APPLICATIONRID     +" integer primary key autoincrement not null, "
            +COLUMN_APPLICATIONNAME   +" string unique not null,"
            +COLUMN_APPLICATIONPACKAGENAME   +" string not null,"
            +COLUMN_FOREGROUNDDURATION     +" integer not null, "
            +COLUMN_TIMESTAMP  +" integer not null );";

    // * table command installed application
    private static final String DATABASE_CREATE_POINTS_TABLE_APPLICATION_INSTALLED = "create table "
            +TABLE_EXAMPLE_INSTALLED_APPLICATIONLOGS     +"("
            +COLUMN_INSTALLED_APPID     +" integer primary  key autoincrement not null, "
            +COLUMN_INSTALLED_APPNAME   +" string unique not  null);";

    // Delete all Applications
    public void DeleteInstalledApplications()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_EXAMPLE_INSTALLED_APPLICATIONLOGS);
    }

    //Insert Installed Applications
    public long InsertInstalledApplications(String ApplicationName)
    {

        try
        {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("InstalledApplicationName", ApplicationName);

            long l = db.insertWithOnConflict(TABLE_EXAMPLE_INSTALLED_APPLICATIONLOGS, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            return l;
        }
        catch (Throwable throwable)
        {
            return 0;
        }

    }

    //// Get all installed application table
    public List<String> GetInstalledApps()
    {

        List<String> tempapp = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_INSTALLED_APPLICATIONLOGS,null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            ApplicationObject temporaryapp= new ApplicationObject();
            tempapp.add(res.getString(res.getColumnIndex(COLUMN_INSTALLED_APPNAME)));
            res.moveToNext();
        }
        res.close();

        return tempapp;
    }
    // Delete all ApplicationRecord
    public void DeleteApplicationLogs()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_EXAMPLE_APPLICATIONLOGS);
    }

    // Insert an Application record
    public long InsertApplicationLogs(String ApplicationName,String ApplicationpPackageName,long ForegroundDuration,long AppTimestamp)
    {

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ApplicationName", ApplicationName);
            contentValues.put("ApplicationpPackageName", ApplicationpPackageName);
            contentValues.put("ForegroundDuration", ForegroundDuration);
            contentValues.put("AppTimestamp", AppTimestamp);
            long l = db.insertWithOnConflict(TABLE_EXAMPLE_APPLICATIONLOGS, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            return l;
        }
        catch (Throwable throwable)
        {
            return 0;
        }

    }

    //// Get all the records from application table
    public List<ApplicationObject> GetAllApplications()
    {

        List<ApplicationObject> tempapp = new ArrayList<ApplicationObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_APPLICATIONLOGS+ " order by "+COLUMN_FOREGROUNDDURATION+ " DESC",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            ApplicationObject temporaryapp= new ApplicationObject();
            temporaryapp.setApplicationName(res.getString(res.getColumnIndex(COLUMN_APPLICATIONNAME)));
            temporaryapp.setApplicationPackageName(res.getString(res.getColumnIndex(COLUMN_APPLICATIONPACKAGENAME)));
            temporaryapp.setApplicationForeground(res.getLong(res.getColumnIndex(COLUMN_FOREGROUNDDURATION)));
            temporaryapp.setTimestamp(res.getLong(res.getColumnIndex(COLUMN_TIMESTAMP)));

            tempapp.add(temporaryapp);
            res.moveToNext();
        }
        res.close();

        return tempapp;
    }



/*-------------------------------------------------End of application queries!------------------------*/








/*------------------------------------Start of Light queries!-----------------------------------*/


    //* Table creation Light
    public static final String TABLE_EXAMPLE_LIGHTLOGS = "Light_Logs";

    public static final String COLUMN_LIGHTID = "LightId";
    public static final String COLUMN_LIGHTVOLUME = "LightVolume";
    public static final String COLUMN_LIGHTDATE = "LightDate";

    // * table command Light
    private static final String DATABASE_CREATE_POINTS_TABLE_LIGHTLOG = "create table "
            +TABLE_EXAMPLE_LIGHTLOGS     +"("
            +COLUMN_LIGHTID     +" integer primary key autoincrement not null, "
            +COLUMN_LIGHTVOLUME   +" string not null,"
            +COLUMN_LIGHTDATE  +" integer not null );";

    // Insert an Light record
    public long InsertLightLogs(String LightVolume,long LightDate)
    {

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("LightVolume", LightVolume);
            contentValues.put("LightDate", LightDate);
            long l = db.insertWithOnConflict(TABLE_EXAMPLE_LIGHTLOGS, null, contentValues,SQLiteDatabase.CONFLICT_ABORT);
            return l;
        }
        catch (Throwable throwable)
        {
            return 0;
        }

    }

    //// Get all the records from Light table
    public List<LightObject> GetAllLightLogs()
    {

        List<LightObject> templight = new ArrayList<LightObject>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_EXAMPLE_LIGHTLOGS+" order by "+COLUMN_LIGHTDATE+ " DESC",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            LightObject temporarylight= new LightObject();
            temporarylight.setLighhtVolume(res.getString(res.getColumnIndex(COLUMN_LIGHTVOLUME)));
            temporarylight.setLightTimestamp(res.getLong(res.getColumnIndex(COLUMN_LIGHTDATE)));
            templight.add(temporarylight);
            res.moveToNext();
        }
        res.close();

        return templight;
    }


/*-------------------------------------------------End of Light queries!------------------------*/






}
