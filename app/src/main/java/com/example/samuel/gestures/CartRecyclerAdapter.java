package com.example.samuel.gestures;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartRecyclerAdapter  extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>{
    ArrayList<Product> mProducts;
    Context mContext;

    public CartRecyclerAdapter( Context mContext,ArrayList<Product> mProducts) {
        this.mProducts = mProducts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_list,parent,false);
                return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product currentProd = mProducts.get(position);
        holder.title.setText(currentProd.getTitle());
        holder.Price.setText(currentProd.getPrice().toString().substring(0,5));

        Picasso.with(mContext).load(currentProd.getImage()).resize(200,200).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView Price;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image);
            Price = itemView.findViewById(R.id.price);
            title = itemView.findViewById(R.id.title);
        }
    }
}
