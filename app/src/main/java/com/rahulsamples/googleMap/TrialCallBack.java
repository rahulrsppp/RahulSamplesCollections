package com.rahulsamples.googleMap;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public abstract class TrialCallBack extends LocationCallback {


    @Override
    public abstract void onLocationResult(LocationResult locationResult);
}
