package com.trawell.batu.trawell.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trawell.batu.trawell.R;

/**
 * Created by Batuhan Islek on 22.05.2018.
 */
public class FollowedTripsFragment extends Fragment {

    public FollowedTripsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followed_trips, container, false);
        return view;
    }
}
