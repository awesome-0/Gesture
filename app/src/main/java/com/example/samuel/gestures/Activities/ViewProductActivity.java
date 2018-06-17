package com.example.samuel.gestures.Activities;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.samuel.gestures.Utils.Cart;
import com.example.samuel.gestures.customViews.DragDropClass;
import com.example.samuel.gestures.Models.Product;
import com.example.samuel.gestures.ProductFragment;
import com.example.samuel.gestures.Models.Products;
import com.example.samuel.gestures.R;
import com.example.samuel.gestures.Adapters.fragmentAdapter;
import com.example.samuel.gestures.customViews.fragmentViewFullScreen;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity
        implements View.OnTouchListener,GestureDetector.OnGestureListener,
GestureDetector.OnDoubleTapListener,View.OnClickListener,View.OnDragListener{
    Product mProduct;
    ViewPager pager;
    TabLayout tab;
    private static final String TAG = "ViewProductActivity";
    GestureDetector mGestureDetector;
    RelativeLayout addToCart,cart;
    ImageView mPlusIcon,mCart;
    FrameLayout container;

    Product selectedProduct;
    Rect mRectangleCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        pager = findViewById(R.id.image_container);
        addToCart = findViewById(R.id.add_to_cart);
        mPlusIcon = findViewById(R.id.plus_image);
        mCart = findViewById(R.id.cart_image);
        cart = findViewById(R.id.cart);
        cart.setOnClickListener(this);
        container = findViewById(R.id.full_screen_container);
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
        //this just fetches all products from the simulated database that match this type
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
        //after instantiating the gesture detector, we pass the user event to the gesture detector
        mGestureDetector.onTouchEvent(motionEvent);
        //here, we try to get the coordinate of the cart icon so we can know where to drop the drag shadow created
        getAddIconCoord();
        return false;
    }

    private void getAddIconCoord() {
        // all of this code is to get the width of the screen, getting the height is really easy cos its at position 0
        mRectangleCoordinate = new Rect();
        // this stores the coordinate of the view on the Rect Object
        cart.getGlobalVisibleRect(mRectangleCoordinate);
        // this also stores the width of the screen on the point object that we then access to get the
        //width of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;

        mRectangleCoordinate.left= mRectangleCoordinate.left - Math.round(((int)(width * 0.18)));
        mRectangleCoordinate.top = 0;
        mRectangleCoordinate.bottom = mRectangleCoordinate.bottom - Math.round(((int)(width * 0.03)));
        mRectangleCoordinate.right = width;

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.d(TAG, "onTouch: onShowPress called");
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
        //once long press is activated we now create our drag shadow
        Product product = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).product;

        ImageView view = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).productImage;
        //Create a drag shadow after long press is activated
        //look into the drag shadow class to understand how its built
        View.DragShadowBuilder dragDropClass = new DragDropClass(view,product.getImage());
        // we pass null cos we are not passing any data along with the view
        view.startDragAndDrop(null,dragDropClass,null,0);
        dragDropClass.getView().setOnDragListener(this);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        // when the user double taps, we get the selected product
        selectedProduct = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).product;
        Bundle bundle = new Bundle();
        //send the selected product to the inflated fragment
        bundle.putParcelable(getString(R.string.product),selectedProduct);
        fragmentViewFullScreen fragment = new fragmentViewFullScreen();
        fragment.setArguments(bundle);

        //just some animation
        Fade enterFade = new Fade();
        enterFade.setStartDelay(1);
        enterFade.setDuration(300);
        fragment.setEnterTransition(enterFade);


        //now create your full blown fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.full_screen_container,fragment,"tag");
                transaction.addToBackStack("fragment").commit();

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
                addProductToCart();
                break;

            case R.id.cart:
                Intent intent = new Intent(this,ViewCartActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addProductToCart() {
        Cart cart = new Cart(this);
        selectedProduct = ((ProductFragment)((fragmentAdapter)pager.getAdapter()).getItem(tab.getSelectedTabPosition())).product;
        cart.addProductToCart(selectedProduct);
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {

        switch(event.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:


                setDragMode(true);

                return true;

            case DragEvent.ACTION_DRAG_ENTERED:

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // this action here gives us the points being covered by the image during the drag
                Point point = new Point(Math.round(event.getX()),Math.round(event.getY()));


                if(mRectangleCoordinate.contains(point.x,point.y)){
                    //our rectangle coordinates was calculated during on touch
                    //user is inside the top right corner
                    cart.setBackground(this.getResources().getDrawable(R.color.blue2));

                }else{
                    cart.setBackground(this.getResources().getDrawable(R.color.blue1));
                }

                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                return true;

            case DragEvent.ACTION_DROP:



                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                // now we will check with the colour of the plusIcon if the user dropped it there or elsewhere

                Drawable drawable = cart.getBackground();
                if(drawable instanceof ColorDrawable){
                    if(((ColorDrawable)drawable).getColor() == getResources().getColor(R.color.blue2)){
                        //at the point the drag ended, the plus icon was blue (user ended drag with the shadow still
                        //on the plusIconadd
                        addProductToCart();
                    }
                }
                cart.setBackground(this.getResources().getDrawable(R.drawable.blue_onclick_dark));

                setDragMode(false);
                return true;

            // An unknown action type was received.
            default:
                Log.e(TAG,"Unknown action type received by OnStartDragListener.");
                break;
        }
        return false;
    }

    private void setDragMode(boolean isDragging) {
        //helper method to ensure that the plus icon is showing during drag only
        if(isDragging){
            mCart.setVisibility(View.INVISIBLE);
            mPlusIcon.setVisibility(View.VISIBLE);
        }else{
            mCart.setVisibility(View.VISIBLE);
            mPlusIcon.setVisibility(View.INVISIBLE);
        }
    }
}
