package com.rahulsamples;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.rahulsamples.facedetection.CameraSourcePreview;
import com.rahulsamples.facedetection.FaceGraphic;
import com.rahulsamples.facedetection.GraphicOverlay;
import com.rahulsamples.model.AppPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.rahulsamples.BrightnessActivity.RequestPermissionCode;

public class FaceDetectionActivity extends AppCompatActivity implements CommonInterface {

    private static final String LOGTAG = FaceDetectionActivity.class.getSimpleName();
    @BindView(R.id.preview)
    CameraSourcePreview mPreview;
    @BindView(R.id.faceOverlay)
    GraphicOverlay mGraphicOverlay;
    @BindView(R.id.ll_threshold_view)
    LinearLayout ll_threshold_view;

    @BindView(R.id.tv_record)
    TextView tv_record;
    private CameraSource mCameraSource;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;


    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private int screenWidth;
    private int screenHeight;
    private int layoutHeight;
    private int layoutWidth;
    private AppPreferenceManager appPreferenceManager;

    public int getFaceIdListSize() {
        return faceIdList.size();
    }

    public List<Integer> faceIdList;
    //  private Dialog dialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facedetection);
        ButterKnife.bind(this);
        faceIdList=new ArrayList<>();
        appPreferenceManager = new AppPreferenceManager(this);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        /*int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }*/

        if (!checkPermission()) {
            requestPermission();
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll_threshold_view.getLayoutParams();
        leftMargin = lp.leftMargin;
        rightMargin = lp.rightMargin;
        topMargin = lp.topMargin;
        bottomMargin = lp.bottomMargin;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        layoutHeight = screenHeight - (bottomMargin + topMargin);
        layoutWidth = screenWidth - (leftMargin + rightMargin);


        System.out.println("left: " + leftMargin +
                " right: " + rightMargin +
                " top: " + topMargin +
                " bottom: " + bottomMargin +
                " Screen width: " + screenWidth +
                " Screen height: " + screenHeight +
                " Threshold height: " + layoutHeight +
                " Threshold Width: " + layoutWidth
        );

        if (!checkPermission()) {
            createCameraSource();
        }


    }

    ///////////////////////////////////////////////////////////////

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
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, android.Manifest.permission.CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
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


    ///////////////////////////////////////////////////////////////




    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(LOGTAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, "No Access to camera",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", listener)
                .show();
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).build();

        detector.setProcessor(new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory()).build());
        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(LOGTAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .setFacing(1)
                .build();
    }

    @Override
    public void isOutsideOfThresholdRectangle(Object object) {
        String type = (String) object;
        if (appPreferenceManager.getPositionCheckStatus()) {
            if(type.equalsIgnoreCase("Far")){
                //showDialog("User is so far from camera");
            }else if (type.equalsIgnoreCase("Multiple Faces")){
              //  showDialog("Double faces detected");
            }else {
              //  showDialog(getString(R.string.wrong_position));
            }
        }
    }


    private void showDialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_pending_interview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                appPreferenceManager.savePositionCheckStatus(true);
            }
        });
        dialog.show();
        appPreferenceManager.savePositionCheckStatus(false);

    }


    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay, FaceDetectionActivity.this);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
            faceIdList.add(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
            if (appPreferenceManager.getPositionCheckStatus()) {
               // showDialog("USER ABSENT");
            }
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);

               /* int faceId= mFaceGraphic.getFaceId();
                System.out.println("FaceId: "+faceId);
                for(int fid:faceIdList) {
                    if (fid==faceId) {
                        faceIdList.remove(fid);
                        System.out.println("Removed : "+faceId);
                        break;
                    }
            }*/
        //    faceIdList.clear();
            /*int faceId= mFaceGraphic.getFaceId();
            System.out.println("FaceId: "+faceId);

            for(int fid:faceIdList) {
                if (fid==faceId) {
                    faceIdList.remove(fid);
                    System.out.println("Removed : "+faceId);
                }
            }*/

        }
    }


    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(LOGTAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }


    public int getLeftMargin() {
        return leftMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getLayoutHeight() {
        return layoutHeight;
    }

    public int getLayoutWidth() {
        return layoutWidth;
    }

    @OnClick(R.id.tv_record)
    void record() {

        if(CameraSourcePreview.mIsRecordingVideo){
            tv_record.setText("Start");
        }else{
            tv_record.setText("stop");

        }
        mPreview.triggerRecording();
    }



}



