package com.example.samuel.gestures;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ViewCartActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Product> mProducts = new ArrayList<>();
    private FloatingActionButton mFab;
    CartRecyclerAdapter adapter;

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

    }

    private void getProductsFromCart() {
        mProducts.addAll(new Cart(this).getProducts());

    }
}
