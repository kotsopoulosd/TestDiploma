package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 21/10/2015.
 */

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/// If the number does not match any phone number of my contants then reject it!

public class CallRejecterSmsSender extends BroadcastReceiver{

    public int CheckReceiver;
    DBHelper dbHelper;
    List<CallObject> topcallers= new ArrayList<CallObject>();
    String incomingNumber="";
    AudioManager audioManager;
    TelephonyManager telephonyManager;

    public void onReceive(Context context, Intent intent) {
        CheckReceiver= 0;
        // Get AudioManager
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // Get TelephonyManager
        telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE"))  {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))  {

                // Get incoming number
                incomingNumber =  intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            }
        }

        if(!incomingNumber.equals(""))
        {
                CheckReceiver=1;
                // Turn on the mute
                audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                // Reject the call
                rejectCall();
                // Send the rejected message ton app
                dbHelper= new DBHelper(context.getApplicationContext());
                topcallers= dbHelper.GetTopThreeCallers();
                for (int i=0; i<5;i++)
                {
                    if(topcallers.get(i).getphnumber().equals(incomingNumber))
                    {
                        sendSMS(incomingNumber);
                    }
                }
        }
    }

    private void sendSMS(String number){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, "I am at meeting-Eimai se sinantisi", null, null);
    }

    private void rejectCall(){

        try {

            // Get the getITelephony() method
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            // Disable access check
            method.setAccessible(true);
            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = method.invoke(telephonyManager);
            // Get the endCall method from ITelephony
            Class<?> telephonyInterfaceClass =Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
