package com.example.samuel.gestures;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity  implements View.OnTouchListener{
    Product mProduct;
    ViewPager pager;
    TabLayout tab;
    private static final String TAG = "ViewProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        pager = findViewById(R.id.image_container);
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
        int action = motionEvent.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouch: ACTION_DOWN called");
                return false;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouch: ACTION_UP called");
                return false;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouch: ACTION_CANCEL called");
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouch: ACTION_MOVE called");
                return false;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "onTouch: ACTION_OUTSIDE called");
                return false;
        }
        return false;
    }
}
