package com.example.lian.betterbattleship;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HighScoresFragment extends Fragment {

    public HighScoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getContext(), getFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}