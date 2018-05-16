package com.trawell.batu.trawell.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.trawell.batu.trawell.R;


public class CurrentFragment extends Fragment {

    private RelativeLayout actionBar;

    public CurrentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_current, container, false);
        actionBar = view.findViewById(R.id.toolbar_layout);
        return view;



    }
}
