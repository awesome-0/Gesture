package com.example.samuel.gestures.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.samuel.gestures.Activities.ViewProductActivity;
import com.example.samuel.gestures.Models.Product;
import com.example.samuel.gestures.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.ViewHolder> {

    private ArrayList<Product> products = new ArrayList<>();
    Context ctx;


    public MainActivityRecyclerAdapter(Context ctx , ArrayList<Product> products) {
        this.products.addAll(products);
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.mainactivity_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.with(ctx).load(products.get(position).getImage()).resize(300,300).centerCrop()
                .into(holder.productImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,ViewProductActivity.class);
                intent.putExtra(ctx.getString(R.string.product),products.get(position));
                ctx.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
