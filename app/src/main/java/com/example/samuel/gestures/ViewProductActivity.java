package com.example.samuel.gestures;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity
        implements View.OnTouchListener,GestureDetector.OnGestureListener{
    Product mProduct;
    ViewPager pager;
    TabLayout tab;
    private static final String TAG = "ViewProductActivity";
    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        pager = findViewById(R.id.image_container);
        mGestureDetector = new GestureDetector(this,this);
        pager.setOnTouchListener(this);

        tab = findViewById(R.id.tab);
        if(getIntent() != null && getIntent().hasExtra(getString(R.string.product))){
            mProduct = getIntent().getParcelableExtra(getString(R.string.product));
        }


        setUpViewPager();
    }

    private void setUpViewPager() {
        ArrayList<Fragment>fragments = new ArrayList<>();
        Products product = new Products();



        Product[] products = product.PRODUCT_MAP.get(mProduct.getType());
        for(Product prod : products){
            ProductFragment fragment = new ProductFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.product),prod);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        fragmentAdapter adapter = new fragmentAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);

        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.d(TAG, "onTouch: ACTION_DOWN called");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.d(TAG, "onTouch: onShowPress called");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.d(TAG, "onTouch: onSingleTapUp called");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onTouch: ACTIOonScroll called");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.d(TAG, "onTouch: onLongPress called");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onTouch: ACTION_DOWN called");
        return false;
    }
}
