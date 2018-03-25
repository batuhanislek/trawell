package com.trawell.batu.trawell.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.trawell.batu.trawell.R;


public class DiscoverFragment extends Fragment {

    private RelativeLayout actionBar;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        actionBar = view.findViewById(R.id.toolbar_layout);
        return view;
    }
}
