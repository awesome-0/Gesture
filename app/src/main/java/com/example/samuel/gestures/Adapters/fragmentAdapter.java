package com.example.samuel.gestures.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class fragmentAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;// = new ArrayList<>();

    public fragmentAdapter(FragmentManager fm,ArrayList<Fragment> frags) {
        super(fm);
        fragments = frags;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }
}
