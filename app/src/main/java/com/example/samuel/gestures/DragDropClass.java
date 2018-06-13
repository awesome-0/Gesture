package com.example.samuel.gestures;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DragDropClass extends View.DragShadowBuilder {
    private Drawable drawable;

    public DragDropClass(View view,int imageResource) {
        super(view);
        drawable = getView().getContext().getDrawable(imageResource);
    }

    @Override
    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {


        int imageRatio = drawable.getIntrinsicWidth()/drawable.getIntrinsicHeight();
        int width = getView().getWidth() /2;
        int height = width * imageRatio;

        drawable.setBounds(0,0,width,height);
        outShadowSize.set(width,height);
        outShadowTouchPoint.set(width/2,height/2);


    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        drawable.draw(canvas);

    }
}
