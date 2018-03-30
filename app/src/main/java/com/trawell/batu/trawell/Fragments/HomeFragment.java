package com.trawell.batu.trawell.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import com.trawell.batu.trawell.Activity.NewTrip;
import com.trawell.batu.trawell.R;


public class HomeFragment extends Fragment {

    private RelativeLayout actionBar;
    private ImageButton addTripButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        actionBar = view.findViewById(R.id.toolbar_layout);
        addTripButton = view.findViewById(R.id.new_trip_button);

        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTripActivityIntent = new Intent(view.getContext(), NewTrip.class);
                startActivity(newTripActivityIntent);
            }
        });

        return view;
    }

}




