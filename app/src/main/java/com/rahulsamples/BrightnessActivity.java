package com.rahulsamples;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rahulsamples.model.AppPreferenceManager;
import com.rahulsamples.model.BrightnessResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrightnessActivity extends AppCompatActivity {

    private static final String LOGTAG = BrightnessActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static String ACCESS_TOKEN;
    @Bind(R.id.sv_camera)
    SurfaceView sv_camera;
    @Bind(R.id.ll_other_options)
    LinearLayout ll_other_options;
    @Bind(R.id.btn_sub)
    Button btn_sub;
    @Bind(R.id.btn_add)
    Button btn_add;
    @Bind(R.id.tv_count)
    TextView tv_count;
    @Bind(R.id.btn_timer)
    Button btn_timer;
    @Bind(R.id.btn_camera)
    Button btn_camera;
    @Bind(R.id.btn_other_options)
    Button btn_other_options;

    private boolean isCameraOpen,isOptionsVisible,isTimerStarted;
    private int curBrightnessValue;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private Timer timer;
    private String currentBrightnessLevel;
    private String minBrightnessLevel;
    private String device_id;
    private String model_no;
    private String resolution;
    private int level;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);
        ButterKnife.bind(this);
        setListener();
        ll_other_options.setVisibility(View.GONE);

        checkCameraAndStoragePermission();

        if(!Settings.System.canWrite(this)) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        surfaceHolder=sv_camera.getHolder();


    }

    private void setListener() {

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isCameraOpen){
                    isCameraOpen=true;
                    btn_camera.setText("Stop Camera");
                    openCamera(surfaceHolder);
                }else{
                    isCameraOpen=false;
                    stopCamera();
                    btn_camera.setText("Start Camera");
                }
            }
        });

        btn_other_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isOptionsVisible){
                    isOptionsVisible=true;
                    btn_other_options.setText("Hide");
                    ll_other_options.setVisibility(View.VISIBLE);

                }else{
                    isOptionsVisible=false;
                    btn_other_options.setText("Show");
                    ll_other_options.setVisibility(View.GONE);

                }
            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isTimerStarted){
                    isTimerStarted=true;
                    btn_timer.setText("Stop Timer");
                    btn_timer.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.button_disable,null));
                    btn_timer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.black,null));
                    startTimer();

                }else{
                    isTimerStarted=false;
                    btn_timer.setText("Start Timer");
                    btn_timer.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.discussion_form_toolbar,null));
                    btn_timer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.white,null));
                    stopTimer();
                    fetchAndSendValueToServer();

                }
            }
        });


        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level=0;

                if(Integer.parseInt(tv_count.getText().toString())-5>0){
                    level=Integer.parseInt(tv_count.getText().toString())-5;
                }else{
                    level=0;
                }
                setBrightnessLevel(level);
                tv_count.setText(String.valueOf(level));
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level=0;
                if(Integer.parseInt(tv_count.getText().toString())+5<=255){
                    level=Integer.parseInt(tv_count.getText().toString())+5;
                }else{
                    level=255;
                }
                setBrightnessLevel(level);
                tv_count.setText(String.valueOf(level));
            }
        });
    }

    @SuppressLint("HardwareIds")
    private void fetchAndSendValueToServer() {
        currentBrightnessLevel="";
        minBrightnessLevel="";
        device_id="";
        model_no="";
        resolution="";

        currentBrightnessLevel=tv_count.getText().toString();

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        device_id=telephonyManager.getDeviceId();

        model_no = Build.MODEL;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        resolution=width + " * "+height;

        System.out.println(" Current Brightness: "+currentBrightnessLevel+" model: "+model_no + " resolution: "+resolution+" Device id: "+device_id);


        new ServerUpdate(this).execute();

    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            level = 0;
                            if (Integer.parseInt(tv_count.getText().toString()) + 5 <= 255) {
                                level = Integer.parseInt(tv_count.getText().toString()) + 5;
                            } else {
                                level = 255;
                            }
                            setBrightnessLevel(level);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_count.setText(String.valueOf(level));
                                }
                            });
                        }
                    },
                    1000, 5000);
        }else{
            Toast.makeText(this,"Timer is already running.",Toast.LENGTH_SHORT).show();
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer=null;
        }
    }

    private void setBrightnessLevel(int level){
        Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, level);
    }

    public void openCamera(SurfaceHolder holder) {
        Camera.Parameters param;
        camera = Camera.open(1);
        try {
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e) {
            Log.e("", "init_camera: " + e);
            return;
        }
    }

    public void stopCamera() {
        if(camera !=null){
            camera.release();
            camera=null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        isCameraOpen=false;
        btn_camera.setText("Start Camera");
        stopCamera();

        isTimerStarted=false;
        btn_timer.setText("Start Timer");
        stopTimer();


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            curBrightnessValue=android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tv_count.setText(String.valueOf(curBrightnessValue));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraAndStoragePermission() {
        int hasRecordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS);
        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int hasStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.CAMERA);

        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasRecordPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_SETTINGS);

        if (hasReadPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return;
        }

    }

    private class ServerUpdate extends AsyncTask<String,String,String> {

        private Context context;
        private ServerUpdate(Context context) {
            this.context=context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject object=new JSONObject();
                object.put("token","ecbcd7eaee29848978134beeecdfbc7c");
                object.put("methodname","insertAppBrightness");
                object.put("max_brightness_level",curBrightnessValue);
                object.put("min_brightness_level",0);
                object.put("model_name",model_no);
                object.put("resolution",resolution);
                object.put("device_id",device_id);

                System.out.print("}}} Request: "+object.toString());

                URL uri=new URL("http://35.154.53.72/admin/Fjapi/");

                HttpURLConnection conn= (HttpURLConnection) uri.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("content-type", "application/json");
                conn.setDoOutput(true);
                DataOutputStream outputStream=new DataOutputStream(conn.getOutputStream());
                outputStream.writeBytes(object.toString());
                outputStream.flush();
                outputStream.close();

                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder builder=new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                reader.close();
                String s= builder.toString();
                System.out.println("Output: "+s);

                return s;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s !=null) {
                BrightnessResponse response = new Gson().fromJson(s, BrightnessResponse.class);

                if(response.code.equalsIgnoreCase("200")) {
                    Toast.makeText(BrightnessActivity.this, "Successfully Updated to Server", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(BrightnessActivity.this, "An error occured: "+response.message, Toast.LENGTH_SHORT).show();
                }
            }
            System.out.println("SUCCESSSS DONE:"+s.toString());

        }
    }
}
