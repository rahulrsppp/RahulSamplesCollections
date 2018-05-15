package com.rahulsamples.disconnectCall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rahulsamples.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallHandlingActivity extends AppCompatActivity {

    private static final String LOGTAG = CallHandlingActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int RC_HANDLE_CAMERA_PERM = 111;
    private static String ACCESS_TOKEN;
    @BindView(R.id.btn_activate)
    Button btn_activate;
    @BindView(R.id.btn_deactivate)
    Button btn_deactivate;
    private Intent intent;
    public static boolean isActivate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_handling);
        ButterKnife.bind(this);
        requestPermission();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        Log.w("", "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.MODIFY_AUDIO_SETTINGS};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        int hasReadPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int hasModifySettingPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (hasReadPhonePermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);

        if (hasModifySettingPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);


        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
            return;
        }


    }


    public void deactivate(View view) {
        isActivate = false;
    }

    public void activate(View view) {
        isActivate = true;
    }

}
