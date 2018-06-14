package com.example.samuel.gestures;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentViewFullScreen extends Fragment {
    Product selectedProduct;
    ScalableImageView imageView;


    public fragmentViewFullScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            selectedProduct = bundle.getParcelable(getActivity().getResources().getString(R.string.product));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_full_screen, container, false);
        imageView = view.findViewById(R.id.scalable_image);

        setProduct();
        return  view;
    }

    private void setProduct() {
        Picasso.with(getActivity()).load(selectedProduct.getImage()).fit().centerInside().into(imageView);
    }

}
