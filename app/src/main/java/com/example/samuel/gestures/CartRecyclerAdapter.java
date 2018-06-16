package com.example.samuel.gestures;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartRecyclerAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperInterface
        ,GestureDetector.OnGestureListener{
    ArrayList<Product> mProducts;
    Context mContext;
    private int HEADER = 1;
    private int PRODUCT = 2;
    ItemTouchHelper itemTouchHelper;
    GestureDetector mGestureDetector;
    ViewHolder viewholder;



    public CartRecyclerAdapter( Context mContext,ArrayList<Product> mProducts) {
        this.mProducts = mProducts;
        this.mContext = mContext;
        mGestureDetector = new GestureDetector(mContext,this);
    }

    void setItemTouchHelper(ItemTouchHelper helper){
        itemTouchHelper = helper;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == HEADER){
            view = LayoutInflater.from(mContext).inflate(R.layout.cart_headers,parent,false);


            return new HeaderViewHolder(view);

        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.cart_list,parent,false);

            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

       int type = getItemViewType(position);
        if(type == HEADER){
            ((HeaderViewHolder)holder).textView.setText(mProducts.get(position).getTitle());

        }else{
            Product currentProd = mProducts.get(position);
            viewholder =((ViewHolder)holder);
                    ((ViewHolder)holder).title.setText(currentProd.getTitle());
            ((ViewHolder)holder).Price.setText(currentProd.getPrice().toString().substring(0,5));
            ((ViewHolder)holder).itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        ((ViewCartActivity)mContext).setScrolling(false);
                        viewholder =((ViewHolder)holder);
                        mGestureDetector.onTouchEvent(motionEvent);
                    }

                    return false;
                }
            });
              Picasso.with(mContext).load(currentProd.getImage()).resize(200,200).into(((ViewHolder)holder).productImage);
        }

    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
       // return super.getItemViewType(position);
        if(TextUtils.isEmpty(mProducts.get(position).getType())){
        return HEADER;
        }else{
            return PRODUCT;
        }
    }

    @Override
    public void onItemMoved(int from, int to) {
        Product prod = mProducts.get(from);

        mProducts.remove(from);
        mProducts.add(to,prod);
        notifyItemMoved(from,to);


    }

    @Override
    public void onSwiped(int position,int productPostion) {
        Cart cart = new Cart(mContext);

        Product prod = mProducts.get(productPostion);
        cart.deleteItem(prod);
        mProducts.remove(productPostion);
        notifyItemRemoved(productPostion);

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

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
        if(!((ViewCartActivity)mContext).isScrolling()){
            itemTouchHelper.startDrag(viewholder);
        }


    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
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
    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cart_section_header);
        }
    }
}
