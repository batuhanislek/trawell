package com.trawell.batu.trawell.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trawell.batu.trawell.Adapters.TripAdapter;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.Model.Trip;
import com.trawell.batu.trawell.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Batuhan Islek on 22.05.2018.
 */
public class MyTripsFragment extends Fragment {

    private DatabaseReference tripsRef, userRef;
    private FirebaseAuth mAuth;
    private ArrayList<Trip> tripList;
    private ArrayList<String> tripIdList;
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private Query lastQuery;
    private String userId;

    public MyTripsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);

        tripList = new ArrayList<Trip>();
        tripIdList = new ArrayList<>();

        tripsRef = FirebaseDatabase.getInstance().getReference("Trips");
        mAuth = FirebaseAuth.getInstance();
        tripsRef.keepSynced(true);

        recyclerView = view.findViewById(R.id.mytrips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        getUserId();
        tripAdapter = new TripAdapter(getContext(), tripList);


        return view;
    }

    public void getUserId() {
        userId = mAuth.getCurrentUser().getUid();
        getMyTripIds();
    }

    public void getMyTripIds() {
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("tripIdList");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //Log.i("snapshot", childSnapshot.getKey().toString());
                    tripIdList.add(childSnapshot.getKey().toString());
                }
                getMyTripsContent();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void getMyTripsContent() {
        for(int i = 0; i < tripIdList.size(); i++) {
            String tripId = tripIdList.get(i);
            Log.i("selectedTripID",tripId);
            tripsRef.child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("data", dataSnapshot.toString());
                    Trip trip = new Trip();
                    String ownerId = ((String) dataSnapshot.child("ownerId").getValue());
                    String tripId = ((String) dataSnapshot.child("tripId").getValue());
                    String tripName = ((String) dataSnapshot.child("tripName").getValue());

                    for (DataSnapshot childSnapshot : dataSnapshot.child("destinations").getChildren()) {
                        childSnapshot.getValue(Destination.class);
                        trip.addDestination(childSnapshot.getValue(Destination.class));
                    }
                    trip.setOwnerId(ownerId);
                    trip.setTripId(tripId);
                    trip.setTripName(tripName);
                    tripList.add(trip);
                    Log.i("triplist", tripList.toString());
                    recyclerView.setAdapter(tripAdapter);
                    tripAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

    }
}
