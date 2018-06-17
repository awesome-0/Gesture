package com.example.samuel.gestures.Activities;

import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.samuel.gestures.Utils.Cart;
import com.example.samuel.gestures.Adapters.CartRecyclerAdapter;
import com.example.samuel.gestures.ItemTouchHelperCallback;
import com.example.samuel.gestures.Models.Product;
import com.example.samuel.gestures.R;
import com.example.samuel.gestures.interfaces.deleteProductInterface;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ViewCartActivity extends AppCompatActivity  implements deleteProductInterface {
    private RecyclerView mRecyclerView;
    private ArrayList<Product> mProducts = new ArrayList<>();
    private FloatingActionButton mFab;
    CartRecyclerAdapter adapter;
    CoordinatorLayout layout;
    private static final String TAG = "ViewCartActivity";
    public static final String[] HEADER_TITLES = {"Must Have", "Maybe", "Probably Not"};
    private boolean isScrollling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        mRecyclerView =findViewById(R.id.recycler_view);
        mFab = findViewById(R.id.fab);
        layout = findViewById(R.id.coord);

        getProductsFromCart();
        initRecyclerView();

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                    mRecyclerView.setOnScrollListener(new ScrollListener());
                }else{
                    mRecyclerView.addOnScrollListener(new ScrollListener());
                }
            }
        });




        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });


    }
    void initRecyclerView(){
        adapter = new CartRecyclerAdapter(this,mProducts,this);
        // we create a touch helper callback class that takes the adapter as a parameter
        //so it can directly control the adapter in terms of removing and adding objects
        ItemTouchHelperCallback touchAdapter = new ItemTouchHelperCallback(adapter);
        // we create an item touch helper so we can pass it to the adapter for the dragging effect
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchAdapter);

        adapter.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    private void getProductsFromCart() {
        //these contain the text that is used to inflate the different views before the main products
        mProducts.add(new Product(HEADER_TITLES[0], 0, "", new BigDecimal(0), 0));
        mProducts.add(new Product(HEADER_TITLES[1], 0, "", new BigDecimal(0), 0));
        mProducts.add(new Product(HEADER_TITLES[2], 0, "", new BigDecimal(0), 0));
        //these contain the major content
        mProducts.addAll(new Cart(this).getProducts());

    }
    public void setScrolling(boolean isScrollling){
        this.isScrollling = isScrollling;
    }
    public boolean isScrolling(){
        return isScrollling;
    }

    private boolean isScrollable(){
        // this checks whether the recycler view is full enough to be scrollable
        return mRecyclerView.computeVerticalScrollRange() > mRecyclerView.getHeight();
    }

    private void showFab(boolean isAtBottom){
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        if(isAtBottom){
            mFab.setAnimation(fadeIn);
            mFab.setVisibility(View.VISIBLE);
        }else{
            mFab.setAnimation(fadeOut);
            mFab.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void showSnackBar(final Product product, final int position) {
        Snackbar.make(layout,"Undo deletion?",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((CartRecyclerAdapter)adapter).mProducts.add(position,product);
                adapter.notifyItemInserted(position);
            }
        }).show();
    }

    private class ScrollListener extends RecyclerView.OnScrollListener{
        // this class is to know when the user is scrolling and has reached the bottom
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            setScrolling(true);

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(isScrollable()){
                if(!mRecyclerView.canScrollVertically(1)){
                    showFab(true);
                }else{
                    showFab(false);
                }

            }
            setScrolling(true);
        }
    }
}
