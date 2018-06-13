package com.example.samuel.gestures;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductFragment extends Fragment {
    ImageView productImage;
    TextView Price;
    TextView title;
    private static final String TAG = "ProductFragment";

    Product product;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            product = bundle.getParcelable(getString(R.string.product));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product,container,false);

        productImage = view.findViewById(R.id.image);
        Price = view.findViewById(R.id.price);
        title = view.findViewById(R.id.title);
        Log.d(TAG, "onCreateView: product is " + product);
                
        setProduct();


       return view;
    }

    private void setProduct() {
        Picasso.with(getActivity()).load(product.getImage()).fit().into(productImage);
        Price.setText(product.getPrice().toString().substring(0,5));
        title.setText(product.getTitle());

    }


}
