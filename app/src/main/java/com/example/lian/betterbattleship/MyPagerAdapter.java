package com.example.lian.betterbattleship;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final int NUM_PAGES = 3;

    public MyPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return new FragmentTabEasy();
            case 1: return new FragmentTabNormal();
            case 2: return new FragmentTabHard();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                String str = mContext.getResources().getString(R.string.easy_level);
                return str;
            case 1:
                return mContext.getResources().getString(R.string.normal_level);
            case 2:
                return mContext.getResources().getString(R.string.hard_level);
            default: return null;
        }
    }
}
