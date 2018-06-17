package com.example.samuel.gestures.customViews;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DragDropClass extends View.DragShadowBuilder {
    private Drawable drawable;

    public DragDropClass(View view,int imageResource) {
        super(view);
        //this basically supplies the drawable to be dragged to the drag shadow class
        drawable = getView().getContext().getDrawable(imageResource);
    }

    @Override
    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {

        /*
         here we get the image ratio to ensure we get a square image
         that is delimited by the the smallest of either the height or the width
        in portrait mode ... the width is most times the smallest
         in getting the image ratio... we've decided to make our drag shadow half the normal size
         ... and then make the height a ratio of the width by multiplying by the image ratio
         this gives us that fine square image
         */

        int imageRatio = drawable.getIntrinsicWidth()/drawable.getIntrinsicHeight();
        int width = getView().getWidth() /2;
        int height = width * imageRatio;

        drawable.setBounds(0,0,width,height);
        outShadowSize.set(width,height);
        // this tells the class to start to center the image at the middle of the image
        outShadowTouchPoint.set(width/2,height/2);


    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        drawable.draw(canvas);

    }
}
