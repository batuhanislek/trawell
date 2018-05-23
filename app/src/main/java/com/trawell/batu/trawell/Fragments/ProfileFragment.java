package com.trawell.batu.trawell.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trawell.batu.trawell.Activity.EditProfileActivity;
import com.trawell.batu.trawell.Activity.HomeActivity;
import com.trawell.batu.trawell.Activity.LoginActivity;
import com.trawell.batu.trawell.Activity.MainActivity;
import com.trawell.batu.trawell.R;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    View view;
    private Button signOutButton, editProfileButton;
    private FragmentTabHost mTabHost;
    private TextView usernameTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        signOutButton = view.findViewById(R.id.signout_profile_button);
        usernameTextView = view.findViewById(R.id.profile_username);
        editProfileButton = view.findViewById(R.id.edit_button);

        mTabHost = view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        String mytrips = "My Trıps";
        String followTrips = "Followed Trıps";

        mTabHost.addTab(mTabHost.newTabSpec("myTrips").setIndicator(mytrips),
                MyTripsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("followedTrips").setIndicator(followTrips),
                FollowedTripsFragment.class, null);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            signOutButton.setVisibility(View.INVISIBLE);
        }

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(editProfileIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Signed Out!", Toast.LENGTH_SHORT).show();
                Intent mainActivityIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });

        loadUserData();

        return view;
    }

    public void loadUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedUserInfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        usernameTextView.setText(username);
    }
}
