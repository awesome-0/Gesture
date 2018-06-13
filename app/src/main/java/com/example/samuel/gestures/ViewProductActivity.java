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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity
        implements View.OnTouchListener,GestureDetector.OnGestureListener,
GestureDetector.OnDoubleTapListener,View.OnClickListener{
    Product mProduct;
    ViewPager pager;
    TabLayout tab;
    private static final String TAG = "ViewProductActivity";
    GestureDetector mGestureDetector;
    RelativeLayout addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        pager = findViewById(R.id.image_container);
        addToCart = findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(this);
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

        Product product = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).product;

        ImageView view = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).productImage;
        //view.
        View.DragShadowBuilder dragDropClass = new DragDropClass(view,product.getImage());
        view.startDragAndDrop(null,dragDropClass,null,0);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onTouch: ACTION_DOWN called");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_to_cart :
                // this is gets the adapter of the view pager and the current fragment and then returns the product in the current fragment
                Product selectedProduct = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).product;
                addProductToCart(selectedProduct);
                break;
        }
    }

    private void addProductToCart(Product selectedProduct) {
        Cart cart = new Cart(this);
        cart.addProductToCart(selectedProduct);
    }
}
