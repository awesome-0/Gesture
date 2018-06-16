package com.example.samuel.gestures;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

public class ViewCartActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Product> mProducts = new ArrayList<>();
    private FloatingActionButton mFab;
    CartRecyclerAdapter adapter;
    private static final String TAG = "ViewCartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        mRecyclerView =findViewById(R.id.recycler_view);
        mFab = findViewById(R.id.fab);

        getProductsFromCart();
        adapter = new CartRecyclerAdapter(this,mProducts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
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
        Log.e(TAG, "onCreate:  number of products in cart is " + mProducts.size());

    }

    private void getProductsFromCart() {
        mProducts.addAll(new Cart(this).getProducts());

    }

    private boolean isScrollable(){
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
    private class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
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
        }
    }
}
