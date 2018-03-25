package com.trawell.batu.trawell.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trawell.batu.trawell.Activity.HomeActivity;
import com.trawell.batu.trawell.Activity.MainActivity;
import com.trawell.batu.trawell.R;


public class ProfileFragment extends Fragment {

    View view;
    private Button signOutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        signOutButton = view.findViewById(R.id.signout_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            signOutButton.setVisibility(View.INVISIBLE);
        }

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(),"Signed Out!", Toast.LENGTH_SHORT).show();
                Intent mainActivityIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });
        return view;
    }
}
