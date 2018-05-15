package com.rahulsamples.disconnectCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Admin on 3/28/2018.
 */

public class CallBarring extends BroadcastReceiver {
    private String number;

    @Override
    public void onReceive(Context context, Intent intent) {
// If, the received action is not a type of "Phone_State", ignore it
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

            // Else, try to do some action
        else
        {
            endCall(context, intent);
        }
    }

    public void endCall(Context context, Intent intent) {


        if(CallHandlingActivity.isActivate) {
            try {

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
                c = Class.forName(telephonyService.getClass().getName()); // Get its class
                m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
                m.setAccessible(true); // Make it accessible
                m.invoke(telephonyService); // invoke endCall()

                m = c.getDeclaredMethod("silenceRinger"); // Get the "endCall()" method
                m.setAccessible(true); // Make it accessible
                m.invoke(telephonyService); // invoke endCall()

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


       /* TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            Bundle bundle = intent.getExtras();
            String phoneNumber = bundle.getString("incoming_number");
            Log.e("INCOMING", phoneNumber);

            telephonyService.silenceRinger();
            telephonyService.endCall();
            Log.e("HANG UP", phoneNumber);


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
