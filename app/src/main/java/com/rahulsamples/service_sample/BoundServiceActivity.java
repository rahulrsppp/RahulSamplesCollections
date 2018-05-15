package com.rahulsamples.service_sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rahulsamples.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoundServiceActivity extends AppCompatActivity  {

    private static final String LOGTAG = BoundServiceActivity.class.getSimpleName();
    @BindView(R.id.tv_task_1)
    TextView tv_task_1;

    @BindView(R.id.tv_task_2)
    TextView tv_task_2;

    @BindView(R.id.tv_bind)
    TextView tv_bind;

    @BindView(R.id.tv_unbind)
    TextView tv_unbind;


    private Messenger messenger=null;
    private boolean isBind;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boundservice);
        ButterKnife.bind(this);
    }


    public void runTask_1(View view){

        if(isBind){
            Message message=Message.obtain(null,BoundServiceWithMessenger.JOB_1,0,0);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"Please Start Service",Toast.LENGTH_SHORT ).show();
        }
    }

    public void runTask_2(View view){
        if(isBind){
            Message message=Message.obtain(null,BoundServiceWithMessenger.JOB_2,0,0);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"Please Start Service",Toast.LENGTH_SHORT ).show();
        }
    }

    public void stopServiceBinding(View view){

        if(isBind) {
            Toast.makeText(this,"Service stopped",Toast.LENGTH_SHORT ).show();

            isBind=false;
            unbindService(serviceConnection);
            serviceConnection=null;
        }else{
            Toast.makeText(this,"Service not started yet",Toast.LENGTH_SHORT ).show();
        }
    }

    public void startServiceBinding(View view){
        Intent intent=new Intent(this, BoundServiceWithMessenger.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
             messenger=new Messenger(iBinder);
             isBind=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind=false;
            messenger=null;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        isBind=false;
        messenger=null;
        if(serviceConnection!=null) {
            unbindService(serviceConnection);
        }
    }
}



