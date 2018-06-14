package com.example.samuel.gestures;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class ScalableImageView extends AppCompatImageView implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{
    private static final String TAG = "ScalableImageView";
    Context mContext;
    ScaleGestureDetector mScaleDetector;
    GestureDetector mGestureDetector ;
    float[] matrixPoints;
    Matrix mMatrix;


    // Image States
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Scales
    float mSaveScale = 1f;
    float mMinScale = 1f;
    float mMaxScale = 4f;

    // view dimensions
    float origWidth, origHeight;
    int viewWidth, viewHeight;

    PointF mLast = new PointF();
    PointF mStart = new PointF();



    public ScalableImageView(Context context) {
        super(context);
        setUp(context);
    }

    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    private void setUp(Context ctx){
        //make sure the image is clickable
        super.setClickable(true);
        //set up other widgets
        mScaleDetector = new ScaleGestureDetector(ctx,new Scalelistener());
        mMatrix = new Matrix();
        matrixPoints = new float[9];
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);
        mGestureDetector = new GestureDetector(ctx,this);
        setOnTouchListener(this);
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //pass touch events to gesture and scaling detector
        mGestureDetector.onTouchEvent(motionEvent);
        mScaleDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }



    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        if(mSaveScale == 1){
            fitToScreen();
        }
    }
    public void fitToScreen(){
        // first set the scale to 1;
        mSaveScale = 1;
        Drawable drawable = getDrawable();
        //now let us get the width and height of the drawable we are viewing
        if(drawable == null || drawable.getIntrinsicHeight() == 0  ||drawable.getIntrinsicWidth() == 0){
            return;
        }
        float dWidth = drawable.getIntrinsicWidth();
        float dHeight = drawable.getIntrinsicHeight();
        //now let us get the maximum scale along both x and y axis

        float maxScaleX = (float)viewWidth / (float) dWidth;
        float maxScaleY = (float)viewHeight / (float) dHeight;

        //now we need to pick one to be our delimiting scale, so when that scale is met
        //along that direction, the other direction cannot increase any further..
        //giving that properly fitted look
        float limitingScale = Math.min(maxScaleX,maxScaleY);
        mMatrix.setScale(limitingScale,limitingScale);

        // now let us try to center the image
        // 1. get redundant y space
        float redundantY = (float)viewHeight - ((float)(dHeight * limitingScale));
        float redundantX = (float)viewWidth - ((float)(dWidth * limitingScale));

        // now we divide the redundant values by 2 so it can be properly centered

        redundantX /= 2;
        redundantY /= 2;

        mMatrix.postTranslate(redundantX,redundantY);

        origHeight = viewHeight - 2* redundantY;
        origWidth = viewWidth - 2* redundantX;
        setImageMatrix(mMatrix);



    }

    private class Scalelistener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //this is triggered upon any form of enlargement or shrinking action
             super.onScale(detector);
            //get the scale factor .. its >1 for enlargement and <1 for shrinking
             float scaleFactor = detector.getScaleFactor();
             //at each instance, put the scale as original scale
            Log.e(TAG, "onScale: current scale factor is " + scaleFactor );
             float originalScale = mSaveScale;
             // input our new value for the save scale
            Log.e(TAG, "onScale: previous save scale is " + mSaveScale );
             mSaveScale *= scaleFactor;
              Log.e(TAG, "onScale: new  save scale is " + mSaveScale );

             // now check if the scale factor is more than max scale or less than min scale
            //scale factor tells the view how much we want to scale

            if(mSaveScale > mMaxScale){
                mSaveScale = mMaxScale;
                scaleFactor = mMaxScale/originalScale;
            }else if(mSaveScale < mMinScale){
                mSaveScale = mMinScale;
                scaleFactor = mMinScale/originalScale;
            }
            /*
            this is checks to know if the view is occupying the whole view
            if its not occupying the whole view, we should be zooming from the middle of the view
             */

            if(origWidth * scaleFactor <= viewWidth
                    || origHeight * scaleFactor <= viewHeight){
                //the post scale method takes four parameters,
                //the first two is for the increment in both x and y directions,
                //while the last two is for the point of initiating the scaling
                mMatrix.postScale(scaleFactor,scaleFactor,viewWidth/2,viewHeight/2);
                Log.e(TAG, "onScale: zooming from the middle"   );
            }else{
                mMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());
                Log.e(TAG, "onScale: scaling from elsewhere" );
            }

            return true;
        }


        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
             super.onScaleBegin(detector);
            mode = ZOOM;
            return true;
        }
    }


}
