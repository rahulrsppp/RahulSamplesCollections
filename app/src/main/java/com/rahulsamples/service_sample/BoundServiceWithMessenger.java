package com.rahulsamples.service_sample;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Admin on 3/14/2018.
 */

public class BoundServiceWithMessenger extends Service {
    Messenger messenger=new Messenger(new MyHandler());

    public static final int JOB_1=1;
    public static final int JOB_2=2;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"Service Created",Toast.LENGTH_SHORT ).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(),"Service Binded",Toast.LENGTH_SHORT ).show();

        return messenger.getBinder();
    }
    

    // Use to perform different task on 'Messenger' commands from Client (BoundServiceActivity, in this case)
    class  MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case JOB_1:
                   // performAddition();
                    Toast.makeText( getApplicationContext(),"JOB_1",Toast.LENGTH_SHORT).show();
                    break;
                case JOB_2:
                  //  performMultiplication();
                    Toast.makeText( getApplicationContext(),"JOB_2",Toast.LENGTH_SHORT).show();

                    break;
                default:
                    super.handleMessage(msg);

            }
        }

        private void performMultiplication(int i) {

        }

        private void performAddition(int i) {

        }
    }
}
