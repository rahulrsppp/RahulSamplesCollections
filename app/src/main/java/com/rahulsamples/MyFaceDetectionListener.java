package com.rahulsamples;

import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/24/2017.
 */

public class MyFaceDetectionListener implements Camera.FaceDetectionListener  {
    private static final String TAG = MyFaceDetectionListener.class.getSimpleName();

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length == 0) {
            Log.i(TAG, "No faces detected");
        } else if (faces.length > 0) {
            Log.i(TAG, "Faces Detected = " +
                    String.valueOf(faces.length));

            List<Rect> faceRects;
            faceRects = new ArrayList<Rect>();

            for (int i=0; i<faces.length; i++) {
                int left = faces[i].rect.left;
                int right = faces[i].rect.right;
                int top = faces[i].rect.top;
                int bottom = faces[i].rect.bottom;
                Rect uRect = new Rect(left, top, right, bottom);
                faceRects.add(uRect);
            }

            // add function to draw rects on view/surface/canvas
        }

    }
}
