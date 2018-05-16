package com.trawell.batu.trawell.Activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trawell.batu.trawell.R;

public class TravelActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        usernameTextView = findViewById(R.id.travel_username_textView);

        loadTripData();


    }

    public void loadTripData() {

        String tripId = getIntent().getStringExtra("tripId");
        final String ownerId = getIntent().getStringExtra("ownerId");

        Log.i("ownerId", ownerId);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                     username = String.valueOf(dataSnapshot.child(ownerId).child("username").getValue());
                     usernameTextView.setText(username);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });











    }

}
