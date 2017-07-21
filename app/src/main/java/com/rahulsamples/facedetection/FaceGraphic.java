/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rahulsamples.facedetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.google.android.gms.vision.face.Face;
import com.rahulsamples.CommonInterface;
import com.rahulsamples.FaceDetectionActivity;
import com.rahulsamples.model.AppPreferenceManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.*/


public class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 3;
    private static final int BOTTOM = 4;
    private static int mCurrentColorIndex = 0;
    private  AppPreferenceManager appPreferenceManager;
    private CommonInterface commonInterface;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;
    private Context context;
    private Timer timerLeft;
    private boolean isFaceWrongPositioned_Left;
    private boolean isFaceWrongPositioned_Right;
    private boolean isFaceWrongPositioned_Top;
    private boolean isFaceWrongPositioned_Bottom;
    private int rightDifference;
    private int topDifference;
    private int bottomDifference;
    private int leftDifference;

    public FaceGraphic(GraphicOverlay overlay,Context context) {
        super(overlay);
        commonInterface=(CommonInterface) context;

        appPreferenceManager=new AppPreferenceManager(context);
        this.context=context;
        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor =  Color.YELLOW;

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(Color.BLUE);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    public int getFaceId() {
        return mFaceId;
    }

    public void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.*/


    public void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.*/


    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
      //  canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
      //  canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);


        /*canvas.drawText("Face Width: " + face.getWidth(), x+ ID_X_OFFSET , y - ID_Y_OFFSET, mIdPaint);
        canvas.drawText("Face Height: " +face.getHeight() , x+ ID_X_OFFSET  , y -ID_Y_OFFSET * 2, mIdPaint);
        canvas.drawText("Face Position: " + face.getPosition(), x+ ID_X_OFFSET , y - ID_Y_OFFSET*3, mIdPaint);
        canvas.drawText("x: " + x, x + ID_X_OFFSET, y - ID_Y_OFFSET*4, mIdPaint);
        canvas.drawText("y: " + y, x + ID_X_OFFSET, y - ID_Y_OFFSET*5, mIdPaint);*/

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;



        canvas.drawText("Left: " + left, x+ ID_X_OFFSET , y - ID_Y_OFFSET, mIdPaint);
        canvas.drawText("Right: " +right, x+ ID_X_OFFSET  , y -ID_Y_OFFSET * 2, mIdPaint);
        canvas.drawText("Top: " + top, x+ ID_X_OFFSET , y - ID_Y_OFFSET*3, mIdPaint);
        canvas.drawText("Bottom: " + bottom, x + ID_X_OFFSET, y - ID_Y_OFFSET*4, mIdPaint);


        if(left<0 || top<0){
            commonInterface.isOutsideOfThresholdRectangle("");
        }

        checkAndCallInterface(left, top, right, bottom);
        checkUserPositionFromCamera();

        /*canvas.drawText("Left_Diff: " + leftDifference, x+ ID_X_OFFSET , y - ID_Y_OFFSET, mIdPaint);
        canvas.drawText("Right_Diff: " +rightDifference, x+ ID_X_OFFSET  , y -ID_Y_OFFSET * 2, mIdPaint);
        canvas.drawText("Top_Diff: " + topDifference, x+ ID_X_OFFSET , y - ID_Y_OFFSET*3, mIdPaint);
        canvas.drawText("Bottom_Diff: " + bottomDifference, x + ID_X_OFFSET, y - ID_Y_OFFSET*4, mIdPaint);*/
        System.out.println("!!!! FaceId ListSize: " + ((FaceDetectionActivity)context).getFaceIdListSize());

     /*   if(((FaceDetectionActivity)context).getFaceIdListSize()>1){
            commonInterface.isOutsideOfThresholdRectangle("Multiple Faces");
        }*/
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }

    private void checkUserPositionFromCamera() {

       int screenWidth= ((FaceDetectionActivity)context).getScreenWidth();
       int farThreshold= (int) (screenWidth*0.4861);


        if(leftDifference+rightDifference>farThreshold){
            System.out.println("++++++ Far Threshold: " + farThreshold);
            commonInterface.isOutsideOfThresholdRectangle("Far");
        }

    }

    private void checkAndCallInterface(float left, float top, float right, float bottom) {
        isFaceWrongPositioned_Left= getLeftDifferenceStatus(left); //could be positive for correct video recording. Should Be FALSE
        isFaceWrongPositioned_Right= getRightDifferenceStatus(right); //could be positive for correct video recording. Should Be FALSE
        isFaceWrongPositioned_Top= getTopDifferenceStatus(top); //could be positive for correct video recording. Should Be FALSE
        isFaceWrongPositioned_Bottom= getBottomDifferenceStatus(bottom); //could be positive for correct video recording. Should Be FALSE


        if(isFaceWrongPositioned_Left){
            commonInterface.isOutsideOfThresholdRectangle("Left");
        }

        if(isFaceWrongPositioned_Right){
            commonInterface.isOutsideOfThresholdRectangle("Right");
        }

        if(isFaceWrongPositioned_Top){
            commonInterface.isOutsideOfThresholdRectangle("Top");
        }

        if(isFaceWrongPositioned_Bottom){
            commonInterface.isOutsideOfThresholdRectangle("Bottom");
        }
    }


    private boolean getLeftDifferenceStatus(float left) {
        int leftMargin= ((FaceDetectionActivity)context).getLeftMargin();
        int leftInnerRect= (int) left/2;

        int differenceLeft=leftInnerRect-leftMargin;
         leftDifference=leftInnerRect/2-leftMargin;

        if(differenceLeft < 10) {
            System.out.println("::: Left Difference: " + differenceLeft);
        }

        return differenceLeft < 10;
    }

    private boolean getRightDifferenceStatus(float right) {
        int rightMargin= ((FaceDetectionActivity)context).getRightMargin();
        int screenWidth= ((FaceDetectionActivity)context).getScreenWidth();

        int outerRect_RightSidePosition_fromLeft=screenWidth-rightMargin;
        int rightInnerRect= (int) right;

          int differenceRight=outerRect_RightSidePosition_fromLeft-rightInnerRect;
         rightDifference=outerRect_RightSidePosition_fromLeft-rightInnerRect;


        if(differenceRight < -80) {
            System.out.println("::: Right Difference: " + differenceRight);
        }

        return differenceRight < -80;
    }

    private boolean getTopDifferenceStatus(float top) {
        int topMargin= ((FaceDetectionActivity)context).getTopMargin();
        int topInnerRect= (int) top;

        int differenceTop=topInnerRect-topMargin;
         topDifference=topInnerRect-topMargin;

        if(differenceTop >0) {
            System.out.println("::: Top Difference: " + differenceTop);
        }

        return differenceTop < 0;
    }

    private boolean getBottomDifferenceStatus(float bottom) {

        int bottomMargin= ((FaceDetectionActivity)context).getBottomMargin();
        int screenHeight= ((FaceDetectionActivity)context).getScreenHeight();

        int outerRect_BottomSidePosition_fromTop=screenHeight-bottomMargin;
        int bottomInnerRect= (int) bottom;

        int differenceBottom=outerRect_BottomSidePosition_fromTop-bottomInnerRect;
        bottomDifference=outerRect_BottomSidePosition_fromTop-bottomInnerRect;

        if(differenceBottom <55) {
            System.out.println("::: Bottom Difference: " + differenceBottom);
        }

        return differenceBottom <55;

    }
}
