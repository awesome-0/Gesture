package com.example.samuel.gestures.Activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
//import android.R;

import com.example.samuel.gestures.Adapters.MainActivityRecyclerAdapter;
import com.example.samuel.gestures.Models.Product;
import com.example.samuel.gestures.Models.Products;
import com.example.samuel.gestures.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

 RecyclerView recyclerView;
 ArrayList<Product> mProducts = new ArrayList<>();
 Toolbar toolbar;
 SwipeRefreshLayout refresh;
 ImageView cart;
 MainActivityRecyclerAdapter adapter;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        refresh = findViewById(R.id.swipeRefresh);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gestures");
        cart = findViewById(R.id.cart_image);
        setSupportActionBar(toolbar);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh: " + mProducts.get(0).toString() );

                //just to simulate the idea of a network call that returns different results
                Collections.shuffle(mProducts, new Random(45));
                OnItemsLoadComplete();

            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewCartActivity.class);
                startActivity(intent);
            }
        });
        mProducts.addAll(Arrays.asList(Products.FEATURED_PRODUCTS));
        initRecyclerView();


    }

    private void OnItemsLoadComplete() {

        initRecyclerView();

        refresh.setRefreshing(false);
    }

    private void initRecyclerView() {
         adapter = new MainActivityRecyclerAdapter(this,mProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}
