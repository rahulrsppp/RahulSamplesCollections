package com.rahulsamples.facedetection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.Face;
import com.rahulsamples.FaceDetectionActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class CameraSourcePreview extends ViewGroup {
    private static final String TAG = "CameraSourcePreview";

    private Context mContext;
    private SurfaceView mSurfaceView;
    private boolean mStartRequested;
    private boolean mSurfaceAvailable;
    private CameraSource mCameraSource;

    private GraphicOverlay mOverlay;
    private MediaRecorder mediaRecorder;
    private static Camera mCamera;
    public static boolean mIsRecordingVideo;

    public CameraSourcePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mStartRequested = false;
        mSurfaceAvailable = false;

        mSurfaceView = new SurfaceView(context);
        mSurfaceView.getHolder().addCallback(new SurfaceCallback());
        addView(mSurfaceView);
    }


    private static boolean cameraFocus(@NonNull CameraSource cameraSource, @NonNull String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    mCamera = (Camera) field.get(cameraSource);
                    if (mCamera != null) {
                        Camera.Parameters params = mCamera.getParameters();
                        params.setFocusMode(focusMode);
                        mCamera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return false;
    }


    private boolean prepareVideoRecorder(){

        /*mCamera = getCameraInstance();


        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(1, info);

        int rotation = ((FaceDetectionActivity) getContext()).getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();*/
       // mediaRecorder.setCamera();


        cameraFocus(mCameraSource, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setCamera(mCamera);
        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
       // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
      //  mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.VP8);
      //  mediaRecorder.setVideoEncodingBitRate(512 * 1000);
     //   mediaRecorder.setVideoFrameRate(30);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        } else {
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
          //  mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
          //  mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

         //   CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
         //   mediaRecorder.setProfile(camcorderProfile);

           /* if (!isFrontCamera) {
                CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                mediaRecorder.setProfile(camcorderProfile);

            } else {
                CamcorderProfile camcorderProfile = CamcorderProfile.get(mFrontCamId, CamcorderProfile.QUALITY_LOW);
                mediaRecorder.setProfile(camcorderProfile);
            }*/

           // mediaRecorder.setVideoSize(720, 480);
        }

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
      //  mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mediaRecorder.setOutputFile(getVideoFilePath(getContext()));

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());

        mediaRecorder.setVideoSize(480, 640);

       /* List<Camera.Size> videoSizesList = parameters.getSupportedVideoSizes();
        if (videoSizesList != null) {
            if (videoSizesList.size() > 3) {
                if ((parameters.getPictureSize().width * parameters.getPictureSize().height) / 102400 > 45) {
                    mediaRecorder.setVideoSize(videoSizesList.get(videoSizesList.size() - 1).width, videoSizesList.get(videoSizesList.size() - 1).height);
                } else if ((videoSizesList.get(0).width * videoSizesList.get(0).height) / 102400 > 30) {
                    mediaRecorder.setVideoSize(videoSizesList.get(videoSizesList.size() - 2).width, videoSizesList.get(videoSizesList.size() - 2).height);
                } else {
                    mediaRecorder.setVideoSize(videoSizesList.get(videoSizesList.size() - 3).width, videoSizesList.get(videoSizesList.size() - 3).height);
                }
            } else {
                mediaRecorder.setVideoSize(videoSizesList.get(videoSizesList.size() - 1).width, videoSizesList.get(videoSizesList.size() - 1).height);
            }
        }
*/
        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
           // mediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.d("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
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

    private String getVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + File.separator)) +"MyVideos1111.mp4";
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
        if (mediaRecorder == null) {
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





    @RequiresPermission(Manifest.permission.CAMERA)
    public void start(CameraSource cameraSource) throws IOException, SecurityException {
        if (cameraSource == null) {
            stop();
        }

        mCameraSource = cameraSource;

        if (mCameraSource != null) {
            mStartRequested = true;
            startIfReady();
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    public void start(CameraSource cameraSource, GraphicOverlay overlay) throws IOException, SecurityException {
        mOverlay = overlay;
        start(cameraSource);
    }

    public void stop() {
        if (mCameraSource != null) {
            mCameraSource.stop();
        }
    }

    public void release() {
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private void startIfReady() throws IOException, SecurityException {
        if (mStartRequested && mSurfaceAvailable) {
            mCameraSource.start(mSurfaceView.getHolder());
            if (mOverlay != null) {
                Size size = mCameraSource.getPreviewSize();
                int min = Math.min(size.getWidth(), size.getHeight());
                int max = Math.max(size.getWidth(), size.getHeight());
                if (isPortraitMode()) {
                    // Swap width and height sizes when in portrait, since it will be rotated by
                    // 90 degrees
                    mOverlay.setCameraInfo(min, max, mCameraSource.getCameraFacing());
                } else {
                    mOverlay.setCameraInfo(max, min, mCameraSource.getCameraFacing());
                }
                mOverlay.clear();
            }
            mStartRequested = false;
        }
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surface) {
            mSurfaceAvailable = true;
            try {
                startIfReady();
            } catch (SecurityException se) {
                Log.e(TAG,"Do not have permission to start the camera", se);
            } catch (IOException e) {
                Log.e(TAG, "Could not start camera source.", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surface) {
            mSurfaceAvailable = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int previewWidth = 320;
        int previewHeight = 240;
        if (mCameraSource != null) {
            Size size = mCameraSource.getPreviewSize();
            if (size != null) {
                previewWidth = size.getWidth();
                previewHeight = size.getHeight();
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
            int tmp = previewWidth;
            previewWidth = previewHeight;
            previewHeight = tmp;
        }

        final int viewWidth = right - left;
        final int viewHeight = bottom - top;

        int childWidth;
        int childHeight;
        int childXOffset = 0;
        int childYOffset = 0;
        float widthRatio = (float) viewWidth / (float) previewWidth;
        float heightRatio = (float) viewHeight / (float) previewHeight;

        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
        // it is usually necessary to slightly oversize the child and to crop off portions along one
        // of the dimensions.  We scale up based on the dimension requiring the most correction, and
        // compute a crop offset for the other dimension.
        if (widthRatio > heightRatio) {
            childWidth = viewWidth;
            childHeight = (int) ((float) previewHeight * widthRatio);
            childYOffset = (childHeight - viewHeight) / 2;
        } else {
            childWidth = (int) ((float) previewWidth * heightRatio);
            childHeight = viewHeight;
            childXOffset = (childWidth - viewWidth) / 2;
        }

        for (int i = 0; i < getChildCount(); ++i) {
            // One dimension will be cropped.  We shift child over or up by this offset and adjust
            // the size to maintain the proper aspect ratio.
            getChildAt(i).layout(
                    -1 * childXOffset, -1 * childYOffset,
                    childWidth - childXOffset, childHeight - childYOffset);
        }

        try {
            startIfReady();
        } catch (SecurityException se) {
            Log.e(TAG,"Do not have permission to start the camera", se);
        } catch (IOException e) {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    private boolean isPortraitMode() {
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }

        Log.d(TAG, "isPortraitMode returning false by default");
        return false;
    }
}