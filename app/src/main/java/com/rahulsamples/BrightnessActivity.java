package com.rahulsamples;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rahulsamples.model.BrightnessResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class BrightnessActivity extends AppCompatActivity {

    private static final String LOGTAG = BrightnessActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static String ACCESS_TOKEN;
    @ BindView(R.id.sv_camera)
    SurfaceView sv_camera;
    @ BindView(R.id.ll_other_options)
    LinearLayout ll_other_options;
    @ BindView(R.id.btn_sub)
    Button btn_sub;
    @ BindView(R.id.btn_add)
    Button btn_add;
    @ BindView(R.id.tv_count)
    TextView tv_count;
    @ BindView(R.id.btn_timer)
    Button btn_timer;
    @ BindView(R.id.btn_camera)
    Button btn_camera;
    @BindView(R.id.btn_other_options)
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


    private Camera mCamera;
    private Size mVideoSize;
    private Integer mSensorOrientation;
    private MediaRecorder mediaRecorder;
    private boolean mIsRecordingVideo;

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;

    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
    public static final int RequestPermissionCode = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);
        ButterKnife.bind(this);
        setListener();
        ll_other_options.setVisibility(View.GONE);


        if(!Settings.System.canWrite(this)) {
            checkSystemWritePermission();
        }

        mediaRecorder = new MediaRecorder();
        surfaceHolder=sv_camera.getHolder();


    }

   public void checkSystemWritePermission(){

       Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
       intent.setData(Uri.parse("package:" + getPackageName()));
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(intent);
    }

    private void setListener() {

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionOnButtonClick()) {
                    if (!isCameraOpen) {
                        isCameraOpen = true;
                        btn_camera.setText("Stop Camera");
                        openCamera(surfaceHolder);
                    } else {
                        isCameraOpen = false;
                        stopCamera();
                        btn_camera.setText("Start Camera");
                    }
                }else{
                    checkPermission();
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

                    btn_timer.setText("Stop Timer");
                    isTimerStarted=true;
                   /* btn_timer.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.button_disable,null));
                    btn_timer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.black,null));
                    startTimer();*/

                }else{
                    isTimerStarted=false;
                    btn_timer.setText("Start Timer");
                   /* btn_timer.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.discussion_form_toolbar,null));
                    btn_timer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.white,null));
                    stopTimer();
                    fetchAndSendValueToServer();*/

                }

                triggerRecording();

            }
        });


        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Settings.System.canWrite(BrightnessActivity.this)) {
                    checkSystemWritePermission();
                }else {

                    int level = 0;

                    if (Integer.parseInt(tv_count.getText().toString()) - 5 > 0) {
                        level = Integer.parseInt(tv_count.getText().toString()) - 5;
                    } else {
                        level = 0;
                    }
                    setBrightnessLevel(level);
                    tv_count.setText(String.valueOf(level));
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Settings.System.canWrite(BrightnessActivity.this)) {
                    checkSystemWritePermission();
                }else {

                    int level = 0;
                    if (Integer.parseInt(tv_count.getText().toString()) + 5 <= 255) {
                        level = Integer.parseInt(tv_count.getText().toString()) + 5;
                    } else {
                        level = 255;
                    }
                    setBrightnessLevel(level);
                    tv_count.setText(String.valueOf(level));
                }
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
            camera.setDisplayOrientation(0);
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

    public  boolean checkPermissionOnButtonClick() {
        int hasWritePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int hasRecordPermission = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
        int hasCameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED)
           return false;
        else if (hasWritePermission != PackageManager.PERMISSION_GRANTED)
            return false;
         else if (hasRecordPermission != PackageManager.PERMISSION_GRANTED)
            return false;

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void checkPermission() {
        int hasWritePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int hasRecordPermission = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
        int hasCameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);

        if (hasWritePermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasRecordPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestPermissionCode);
        }
    }



   /* @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission && CameraPermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
*/
   /* private void checkCameraAndStoragePermission() {
        int hasWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS);
        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int hasStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int hasRecordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.CAMERA);

        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWritePermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_SETTINGS);

        if (hasReadPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);

        if (hasRecordPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return;
        }

    }*/

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

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {

            try {
                mIsRecordingVideo = false;
                mediaRecorder.stop();
                mediaRecorder.reset();


            } catch (RuntimeException stopException) {
            }
            releaseMediaRecorder();
        }
    }

    private void startRecording() {
        if (mediaRecorder != null) {
            mIsRecordingVideo = true;
            try{
              //  setUpMediaRecorder();
                if (prepareVideoRecorder()) {
                    // Camera is available and unlocked, MediaRecorder is prepared,
                    // now you can start recording
                    mediaRecorder.start();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public void triggerRecording() {
        if (mIsRecordingVideo) {
            Log.d("", "Recording stopped");
            stopRecording();
        } else {
            Log.d("", "Recording starting");
            startRecording();
        }
    }

    private Camera getCameraInstance() {
        Camera c = null;
        for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
            Camera.CameraInfo camInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(camNo, camInfo);

            if (camInfo.facing == (Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                try {
                    c = Camera.open(camNo);
                } catch (Exception exception) {
                }
            }
        }
        return c;
    }

    private boolean prepareVideoRecorder(){

        mCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mediaRecorder.setOutputFile(getVideoFilePath(this));

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpMediaRecorder() throws IOException, CameraAccessException {
        CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = manager.getCameraIdList()[1];

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
            throw new RuntimeException("Cannot get available preview/video sizes");
        }

        mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));

        mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        /*mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);*/
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(getVideoFilePath(this));
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

        //  mediaRecorder.setOutputFile(getVideoFilePath(getContext()));
        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(5);
        mediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();

        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }

        mediaRecorder.prepare();
        mediaRecorder.start();


     /*   mediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setOutputFile(getVideoFilePath(getContext()));
        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(480, 640);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
        //int orientation = ORIENTATIONS.get(rotation);
        mediaRecorder.setOrientationHint(ORIENTATIONS.get(0));
        mediaRecorder.prepare();

        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                Log.e("ERROR", mr.toString() + " : what[" + what + "]" + " Extras[" + extra + "]");
            }
        });

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            //Log.d(LOGTAG, "mediaRecorder : " + e.getLocalizedMessage());

        } catch (IOException e) {
            //Log.d(LOGTAG, "mediaRecorder : " + e.getLocalizedMessage());
        }*/

    }

    private static android.util.Size chooseVideoSize(android.util.Size[] choices) {
        for (android.util.Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e("", "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    private String getVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/")) +"MyVideos1111.mp4";
    }
}
