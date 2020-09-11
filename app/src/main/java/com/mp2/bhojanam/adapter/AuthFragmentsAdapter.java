package com.mp2.bhojanam.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class AuthFragmentsAdapter extends FragmentPagerAdapter {

    public AuthFragmentsAdapter(FragmentManager fragmentManager, ArrayList<Fragment> frag){
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.frag = frag;
    }

    FragmentManager fragmentManager;
    ArrayList<Fragment> frag;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return frag.get(position);
    }

    @Override
    public int getCount() {
        return frag.size();
    }
}
