package com.trawell.batu.trawell.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trawell.batu.trawell.Model.Trip;
import com.trawell.batu.trawell.TaskManager.DateCalculation;
import com.trawell.batu.trawell.R;
import com.trawell.batu.trawell.TravelActivity;

import java.util.ArrayList;

/**
 * Created by Batuhan Islek on 22.04.2018.
 */
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Trip> mList;
    private DatabaseReference userRef;
    private DatabaseReference tripRef;
    private  int counter=0;

    public TripAdapter(Context context, ArrayList<Trip> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.travel_card, parent, false);
        TripAdapter.ViewHolder viewHolder = new TripAdapter.ViewHolder(view);

        tripRef = FirebaseDatabase.getInstance().getReference("Trips");
        userRef = FirebaseDatabase.getInstance().getReference("Users");


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.ViewHolder holder, final int position) {

        final Trip destItem = getItem(position);
        String username;

        final TextView usernameTextView = holder.usernameTextView;
        final TextView routeTextView = holder.routeTextView;
        final String ownerId = destItem.getOwnerId();
        final TextView timeSpentTextView = holder.timeSpentTextView;


        DatabaseReference ownerRef = userRef.child(ownerId);

        ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    usernameTextView.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        String firstLocation = destItem.getFirstItem().getCityName();
        String lastLocation = destItem.getLastItem().getCityName();

        switch (destItem.getListSize()) {
            case 1:
                routeTextView.setText(firstLocation);
                break;
            case 2:
                routeTextView.setText(firstLocation +" - " +lastLocation);
                break;
                default:
                    routeTextView.setText(firstLocation + " - " +
                            String.valueOf(destItem.getListSize() -2) + " more places" + " - " +
                            lastLocation);
                    break;
        }

        Long days = new DateCalculation().DaysBetweenDates(destItem.getFirstItem().getCheckInDate(),
                destItem.getLastItem().getCheckOutDate());

        timeSpentTextView.setText(days + " days " + "( "+ destItem.getFirstItem().getCheckInDate() +
                " - " + destItem.getLastItem().getCheckOutDate() +")" );


    }
    public Trip getItem(int arg0) {
        return mList.get(getItemCount() - arg0 - 1);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView;
        TextView routeTextView;
        TextView timeSpentTextView;

        public ViewHolder(final View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.username_textView);
            routeTextView = itemView.findViewById(R.id.route);
            timeSpentTextView = itemView.findViewById(R.id.time_spent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent travelIntent = new Intent(v.getContext(), TravelActivity.class);
                    itemView.getContext().startActivity(travelIntent);
                }
            });


        }
    }







}
