package com.example.samuel.gestures.customViews;

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
    /*
    to understand the whole idea of the scaling process... start from the scale listener
     */
    private static final String TAG = "ScalableImageView";
    Context mContext;
    //these are the detectors we need.
    ScaleGestureDetector mScaleDetector;
    GestureDetector mGestureDetector ;
    // this is to assist we scaling and translating the image
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
        // the scale gesture listener tells the app what to do whenever there is any scaling process
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
        mScaleDetector.onTouchEvent(motionEvent);
        mGestureDetector.onTouchEvent(motionEvent);

        PointF currentPoint = new PointF(motionEvent.getX(),motionEvent.getY());

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLast.set(currentPoint);
                mStart.set(mLast);
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mode == DRAG) {
                    float changeInX = currentPoint.x - mLast.x;
                    float changeInY = currentPoint.y - mLast.y;
                    //when the image is moved, we try to get the maximum permissible distance that the image can move ...
                    //both horizontally and vertically...
                    //once the the user scales and the image now exceeds its original size in either direction
                    // only then should we permit any sort of movement

                    float permissibleDy = getDragDistance(changeInY,viewHeight,origHeight * mSaveScale);
                    float permissibleDx = getDragDistance(changeInX,viewWidth,origWidth * mSaveScale);


                    //this will only work if the image has been zoomed in either distance
                    mMatrix.postTranslate(permissibleDx, permissibleDy);
                    mLast.set(currentPoint.x,currentPoint.y);
                }
                break;

                case MotionEvent.ACTION_UP:
                    mode = NONE;
                    break;

        }
        setImageMatrix(mMatrix);
        fixTranslation();
        return false;
    }

    private void fixTranslation() {
        // without this method call, once the user zooms in ...
        //and the whole screen is covered by the image, the user can now drag and the black background showing
        //what this method does is that once the image is fully zoomed and the user drags to a point where it reveals the black screen
        // do some sort of reverse translation just enough in that direction to remove out the black background


        // this call here stores the value of the most recent operations in done on the matrix,
        // so we can test if the user has gone beyond bounds
        mMatrix.getValues(matrixPoints);
        float mostRecentX =  matrixPoints[Matrix.MTRANS_X];
        float mostRecentY = matrixPoints[Matrix.MTRANS_Y];

        float correctionX =  getReturnTranslation(mostRecentX,viewWidth,origWidth * mSaveScale);
        float correctionY = getReturnTranslation(mostRecentY,viewHeight,origHeight * mSaveScale);

        if(correctionX != 0 || correctionY != 0) {
            mMatrix.postTranslate(correctionX, correctionY);
        }

    }
    float getReturnTranslation(float actualTranslation,float viewSize,float contentSize){
        float min,max;

        if(contentSize <= viewSize){//not yet zoomed
            //mostly applies to 
            max = viewSize - contentSize;
            min = 0;

        }else{
            max = 0;
            min = viewSize - contentSize;// always negative
        }




       if(actualTranslation < min){
           return -actualTranslation + min;
       }
       if(actualTranslation >max){
           return -actualTranslation + max;
       }
        return 0;

    }


    private float getDragDistance(float change,float viewSize,float contentSize){
        // this method ensures that unless zoomed(view size becomes less than contentSize) ...
        //there should be no translation in that direction
        if(contentSize <= viewSize){
            return 0;
        }
        return change;
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
        //we fit the image to screen
        fitToScreen();
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
        // make sure you put the null check, because sometimes, it may take a while for the drawable to come up
        //this should be done to avoid potential null pointer errors
        if(drawable == null || drawable.getIntrinsicHeight() == 0  ||drawable.getIntrinsicWidth() == 0){
            return;
        }

        //now get the drawable width and height of our drawable
        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();

        //now let us get the maximum scale along both x and y axis
        // this tells us how much we can scale in either direction
        float maxScaleX = (float)viewWidth / (float) dWidth;
        float maxScaleY = (float)viewHeight / (float) dHeight;

        //now we need to pick one to be our delimiting scale, so when that limiting scale is met...
        //along that direction, the other direction cannot be scaled any further...
        //giving that properly fitted look
        float limitingScale = Math.min(maxScaleX,maxScaleY);
        mMatrix.setScale(limitingScale,limitingScale);

        // now let us try to center the image
        // 1. get redundant y space
        float redundantY = (float) viewHeight - (limitingScale * (float) dHeight);
        float redundantX = (float) viewWidth - (limitingScale * (float) dWidth);


        // now we divide the redundant values by 2 so it can be properly centered

        redundantX /= (float)2;
        redundantY /= (float)2;

        // the post translate method tells the view where to put the image from the starting point
        //we want it evenly spaced out both vertically and horizontally

        mMatrix.postTranslate(redundantX,redundantY);

        //this is where we calculate the original height and width of our drawable
        origHeight = viewHeight - (2 * redundantY);
        origWidth = viewWidth - (2* redundantX);

        setImageMatrix(mMatrix);



    }

    private class Scalelistener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //this is triggered upon any form of enlargement or shrinking action

            //get the scale factor .. its >1 for enlargement and <1 for shrinking
             float scaleFactor = detector.getScaleFactor();
             //at each instance, put the scale as original scale

            //at initial value, our savedImage scale is 1 ... so the image is the exact size
             float previousScale = mSaveScale;


             //this tells us how much the user either enlarges or shrinks the image
             mSaveScale *= scaleFactor;


             // now check if the scale factor is more than max scale or less than min scale

            if(mSaveScale > mMaxScale){
                // so the image can only go 4 times its size
                mSaveScale = mMaxScale;
                scaleFactor = mMaxScale/previousScale;
            }else if(mSaveScale < mMinScale){
                //the image can never go less than its original size
                mSaveScale = mMinScale;
                scaleFactor = mMinScale/previousScale;
            }
            // at this point , out scale is within specified limits


            /*
            this is checks to know if the view is occupying the whole view
            if its not occupying the whole view, we should be zooming from the middle of the view
             */

            //our original width and height is calculated when our image properly fits the screen
            if(origWidth * mSaveScale <= viewWidth
                    || origHeight * mSaveScale <= viewHeight){

                //the post scale method takes four parameters,
                //the first two is for the increment(in terms of scalefactor) in both x and y directions,
                //while the last two is for the point of initiating the scaling
                // so we want to start enlarging from the middle when the black background is still showing
                mMatrix.postScale(scaleFactor,scaleFactor,viewWidth/2,viewHeight/2);

            }else{
                // when we have passed the view width and view height, start zooming in from the point where the user is touching it
                mMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());

            }
            fixTranslation();

            return true;
        }


        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            mode = ZOOM;
            return true;
        }
    }


}
