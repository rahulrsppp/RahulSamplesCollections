package com.rahulsamples;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                System.out.println("{{{  onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                System.out.println("{{{  onActivityStarted");

            }

            @Override
            public void onActivityResumed(Activity activity) {
                System.out.println("{{{  onActivityResumed");

            }

            @Override
            public void onActivityPaused(Activity activity) {
                System.out.println("{{{  onActivityPaused");

            }

            @Override
            public void onActivityStopped(Activity activity) {
                System.out.println("{{{  onActivityStopped");

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                System.out.println("{{{  onActivitySaveInstanceState");

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                System.out.println("{{{  onActivityDestroyed");

            }
        });
    }
}
