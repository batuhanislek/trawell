package com.trawell.batu.trawell.Fragments;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.trawell.batu.trawell.Activity.NewTrip;
import com.trawell.batu.trawell.Adapters.DestinationAdapter;
import com.trawell.batu.trawell.Adapters.TripAdapter;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.Model.Trip;
import com.trawell.batu.trawell.R;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


public class HomeFragment extends Fragment {

    private RelativeLayout actionBar;
    private ImageButton addTripButton;
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private DatabaseReference tripsRef;
    private ArrayList<Destination> destList;
    private ArrayList<Trip> tripList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Query lastQuery;
    private String tripId;

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
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });


        tripList = new ArrayList<Trip>();

        tripsRef = FirebaseDatabase.getInstance().getReference("Trips");
        tripsRef.keepSynced(true);

        recyclerView = view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        tripAdapter = new TripAdapter(getContext(),tripList);

        lastQuery = tripsRef.orderByKey();
        lastQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Trip trip = new Trip();
                String ownerId = ((String) dataSnapshot.child("ownerId").getValue());
                String tripId = ((String) dataSnapshot.child("tripId").getValue());
                String tripName = ((String) dataSnapshot.child("tripName").getValue());

                for (DataSnapshot childSnapshot: dataSnapshot.child("destinations").getChildren()) {
                    childSnapshot.getValue(Destination.class);
                    trip.addDestination(childSnapshot.getValue(Destination.class));
                }
                trip.setOwnerId(ownerId);
                trip.setTripId(tripId);
                trip.setTripName(tripName);
                tripList.add(trip);
                recyclerView.setAdapter(tripAdapter);
                tripAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });



        return view;
    }

    void refreshItems() {
        tripList.clear();
        lastQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Trip trip = new Trip();
                String ownerId = ((String) dataSnapshot.child("ownerId").getValue());

                for (DataSnapshot childSnapshot: dataSnapshot.child("destinations").getChildren()) {
                    childSnapshot.getValue(Destination.class);
                    trip.addDestination(childSnapshot.getValue(Destination.class));
                }
                trip.setOwnerId(ownerId);
                tripList.add(trip);
                recyclerView.setAdapter(tripAdapter);
                tripAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        tripAdapter.notifyDataSetChanged();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


















}




