package com.example.samuel.gestures.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.samuel.gestures.Models.Product;
import com.example.samuel.gestures.Models.Products;
import com.example.samuel.gestures.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Cart {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private Context ctx;
    private static final String TAG = "Cart";

    public Cart(Context ctx) {
        this.ctx = ctx;
        mSharedPreferences = ctx.getSharedPreferences(ctx.getString(R.string.cart_items),0);
        editor = mSharedPreferences.edit();
    }

    public void addProductToCart(Product product){
        Set<String> cart = mSharedPreferences.getStringSet(ctx.getString(R.string.cart_items), new HashSet<String>());
        cart.add(String.valueOf(product.getSerial_number()));
        editor.putStringSet(ctx.getString(R.string.cart_items),cart);
        Toast.makeText(ctx,"added " + product.getSerial_number() ,Toast.LENGTH_LONG).show();
        editor.commit();

    }
    public ArrayList<Product> getProducts(){
        Set<String> cart = mSharedPreferences.getStringSet(ctx.getString(R.string.cart_items), new HashSet<String>());
        Log.e(TAG, "getProducts: " + cart.size() );
        ArrayList<Product> prods = new ArrayList<>();
        HashMap<String, Product> products = Products.getProducts();//.get(nums);
        for(String nums: cart){
            prods.add(products.get(nums));
        }


        return prods;

    }

    public void deleteItem(Product product){
        Set<String> cart = mSharedPreferences.getStringSet(ctx.getString(R.string.cart_items), new HashSet<String>());
        cart.remove(product);
        editor.putStringSet(ctx.getString(R.string.cart_items),cart);
        editor.commit();

    }
}
