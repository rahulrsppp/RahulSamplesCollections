package com.rahulsamples;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.rahulsamples.disconnectCall.CallHandlingActivity;
import com.rahulsamples.googleMap.MapsActivity;
import com.rahulsamples.model.AppPreferenceManager;
import com.rahulsamples.service_sample.BoundServiceActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.rahulsamples.BrightnessActivity.RequestPermissionCode;

public class HomeActivity extends AppCompatActivity {

    private static final String LOGTAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.rv_options)
    RecyclerView rv_options;
    private List<String> sampleList;
    private SampleAdapter sampleAdapter;
    private AppPreferenceManager appPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appPreferenceManager=new AppPreferenceManager(this);
        checkAndroidPermission();
        setAdapter();
        setList();

    }

    private void checkAndroidPermission() {
        if(checkPermission()){
            requestPermission();
        }
    }

    private void setList() {
        sampleList.add("Social Login");
        sampleList.add("Brightness Manager");
        sampleList.add("Face Detection");
        sampleList.add("Profile Animation");
        sampleList.add("Bound Service");
        sampleList.add("Call Handling");
        sampleList.add("Google Map");
        sampleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        appPreferenceManager.removeValues(this);
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this,
                RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(this,
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        android.support.v4.app.ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission && CameraPermission) {
                        Toast.makeText(this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void setAdapter() {
        sampleList=new ArrayList<>();
        sampleAdapter=new SampleAdapter(this,sampleList);
        rv_options.setAdapter(sampleAdapter);
        rv_options.setHasFixedSize(true);
        rv_options.setLayoutManager(new StaggeredGridLayoutManager(3,1));
        sampleAdapter.setCallBacksSampleList(new SampleAdapter.CallBacksSampleList() {
            @Override
            public void onRowClick(String sample) {
                Intent intent=null;
                if(sample.equalsIgnoreCase("Social Login")) {
                     /*intent = new Intent(HomeActivity.this, SocialLoginActivity.class);
                     startActivity(intent);*/
                }else if(sample.equalsIgnoreCase("Brightness Manager")){
                    intent=new Intent(HomeActivity.this,BrightnessActivity.class);
                    startActivity(intent);
                } else if(sample.equalsIgnoreCase("Face Detection")){
                    intent=new Intent(HomeActivity.this,FaceDetectionActivity.class);
                    startActivity(intent);
                }else if(sample.equalsIgnoreCase("Bound Service")){
                    intent=new Intent(HomeActivity.this,BoundServiceActivity.class);
                    startActivity(intent);
                }
                else if(sample.equalsIgnoreCase("Call Handling")){
                    intent=new Intent(HomeActivity.this,CallHandlingActivity.class);
                    startActivity(intent);
                } else if(sample.equalsIgnoreCase("Google Map")){
                    intent=new Intent(HomeActivity.this, MapsActivity.class);
                    startActivity(intent);
                }

            }
        });
    }


}
