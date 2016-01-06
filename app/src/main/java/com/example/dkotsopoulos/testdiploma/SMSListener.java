package com.example.dkotsopoulos.testdiploma;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
class SMSListener extends ContentObserver
{
    DBHelper dbHelper;
    Context context;
    Handler smsobserver;
    Uri uri = Uri.parse("content://sms");

    public SMSListener(Handler smsobserver, Context context) {
        super(smsobserver);
        this.context = context;
        this.smsobserver=smsobserver;
    }

    public void onChange(boolean selfChange)
    {
        dbHelper= new DBHelper(context);
        Cursor smsmcursor = context.getContentResolver().query(uri, null, null, null, null);
        smsmcursor.moveToFirst();

        while(smsmcursor.isAfterLast()==false)
        {
            String sbody=smsmcursor.getString(smsmcursor.getColumnIndexOrThrow("body"));
            String SmsSender =smsmcursor.getString(smsmcursor.getColumnIndex("person"));
            if (SmsSender==null)
            {
                SmsSender="Unknown";
            }
            String saddress =smsmcursor.getString(smsmcursor.getColumnIndex("address"));
            long sdate =smsmcursor.getLong(smsmcursor.getColumnIndexOrThrow("date"));
            String Person= getContactName(context, smsmcursor.getString(smsmcursor.getColumnIndexOrThrow("address")));
            if (Person==null)
            {
                Person= "Unknown";
            }
            String type = smsmcursor.getString(smsmcursor.getColumnIndexOrThrow("type"));
            String typeOfSMS = null;
            switch (Integer.parseInt(type))
            {
                case 1:
                    typeOfSMS = "INBOX";
                    break;

                case 2:
                    typeOfSMS = "SENT";
                    break;

                case 3:
                    typeOfSMS = "DRAFT";
                    break;
            }

            dbHelper.InsertSmsLogs(sbody,saddress,Person,SmsSender,typeOfSMS,sdate);
            smsmcursor.moveToNext();
        }
        smsmcursor.close();

    }


    public SMSListener(Handler h)
    {
        super(h);
    }
    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }


    //// Retrieving the names from the numbers
    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

}