/**
 * AppPreferenceManager.java - a class that stores the data locally in shared Preferences
 *
 * @author Ashish Kumar
 * @version 1.0
 * @createddate Feb 12, 2016
 * @change on March 15, 2017
 * @modificationlog not used longer in the code
 * @see android.content.SharedPreferences
 */

package com.rahulsamples.model;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferenceManager {
    private  SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private Context context;
    private static final String SHARED_PREF = "FIRST JOB PREFERENCES";
    private static final String WRONG_POSITION = "WRONG_POSITION";


    public AppPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void removeValues(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }



    public  int getPreferenceInt() {
        return sharedPreferences.getInt("NOTICOUNT", 0);
    }
    public  void savePreferenceLong(int gcmToken) {
        editor.putInt("NOTICOUNT", gcmToken);
        editor.commit();
    }

    public  boolean getPositionCheckStatus() {
        return sharedPreferences.getBoolean(WRONG_POSITION, true);
    }
    public  void savePositionCheckStatus(boolean positionStatus) {
        editor.putBoolean(WRONG_POSITION, positionStatus);
        editor.commit();
    }


}

