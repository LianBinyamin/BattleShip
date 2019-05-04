package com.example.lian.betterbattleship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;


public class FragmentTabEasy extends Fragment {


    public FragmentTabEasy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_easy, container, false);

        GridView gv = (GridView)view.findViewById(R.id.gvEasy);
        TextView tv = (TextView)view.findViewById(R.id.textNoDataEasy);

        if (MainActivity.myDb.getAllDataFromEasyTable().isEmpty()) {
            tv.setVisibility(View.VISIBLE);
        }
        else {
            tv.setVisibility(View.INVISIBLE);
        }

        gv.setAdapter(new CellTableAdapter(getContext(), MainActivity.myDb.getAllDataFromEasyTable()));

        return view;
    }

}
