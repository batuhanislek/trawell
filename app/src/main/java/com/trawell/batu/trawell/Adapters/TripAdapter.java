package com.trawell.batu.trawell.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trawell.batu.trawell.Model.Trip;
import com.trawell.batu.trawell.TaskManager.DateCalculation;
import com.trawell.batu.trawell.R;
import com.trawell.batu.trawell.Activity.TravelActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Batuhan Islek on 22.04.2018.
 */
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Trip> mList;
    private DatabaseReference userRef, tripRef, likeRef, followRef;
    private FirebaseAuth mAuth;
    private int counter=0;
    private String username, ownerId;
    private Boolean isLiked = false, isFollowed=false;

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

        mAuth = FirebaseAuth.getInstance();
        tripRef = FirebaseDatabase.getInstance().getReference("Trips");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        likeRef = FirebaseDatabase.getInstance().getReference("Likes");
        followRef = FirebaseDatabase.getInstance().getReference("Follows");
        likeRef.keepSynced(true);
        followRef.keepSynced(true);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.ViewHolder holder, final int position) {

        final Trip destItem = getItem(position);

        final TextView usernameTextView = holder.usernameTextView;
        final TextView routeTextView = holder.routeTextView;
        final TextView timeSpentTextView = holder.timeSpentTextView;
        final ImageView travelCardImage = holder.travelCardImage;
        final ImageButton likeButton = holder.likeButton;
        final ImageButton followButton = holder.followButton;


        //DatabaseReference likeRef = tripRef.child("likeCount");

        final String tripId = destItem.getTripId() == null ? "": destItem.getTripId();
        final String userId = mAuth.getUid() == null ? "": mAuth.getUid();
        holder.setLikeButton(tripId, userId);
        holder.setFollowButton(tripId,userId);

        ownerId = destItem.getOwnerId() == null ? "": destItem.getOwnerId();
        DatabaseReference ownerRef = userRef.child(ownerId);
        backgroundColorPicker(travelCardImage);

        ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    username = String.valueOf(dataSnapshot.child("username").getValue());
                    usernameTextView.setText(username);
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
                routeTextView.setText(firstLocation+" - " +lastLocation);
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
        return mList.get(arg0);
    }

    public Trip getItemNormal(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView;
        TextView routeTextView;
        TextView timeSpentTextView;
        ImageView travelCardImage;
        ImageButton likeButton, followButton;

        public void setLikeButton(final String tripId, final String userId) {
            likeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(tripId).hasChild(userId)) {
                        likeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                    } else {
                        likeButton.setImageResource(R.drawable.like_24dp);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Liked-tripId", tripId);
                    Log.i("liked-who",mAuth.getUid());
                    isLiked = true;
                    likeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(isLiked) {
                                if (dataSnapshot.child(tripId).hasChild(userId)) {
                                    likeRef.child(tripId).child(userId).removeValue();
                                    likeButton.setImageResource(R.drawable.like_24dp);
                                    isLiked = false;
                                } else {
                                    likeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                                    likeRef.child(tripId).child(userId).setValue("liked");
                                    isLiked = false;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

                }
            });
        }

        public void setFollowButton(final String tripId, final String userId) {

            followRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(tripId).hasChild(userId)) {
                        followButton.setImageResource(R.drawable.ic_bookmark_green_24dp);
                    } else {
                        followButton.setImageResource(R.drawable.bookmark_24dp);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("followed-tripId", tripId);
                    Log.i("follow-who",mAuth.getUid());
                    isFollowed = true;
                    followRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(isFollowed) {
                                if (dataSnapshot.child(tripId).hasChild(userId)) {
                                    followRef.child(tripId).child(userId).removeValue();
                                    followButton.setImageResource(R.drawable.bookmark_24dp);
                                    isFollowed = false;
                                } else {
                                    followButton.setImageResource(R.drawable.ic_bookmark_green_24dp);
                                    followRef.child(tripId).child(userId).setValue("followed");
                                    isFollowed = false;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

                }
            });


        }

        public ViewHolder(final View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.username_textView);
            routeTextView = itemView.findViewById(R.id.route);
            timeSpentTextView = itemView.findViewById(R.id.time_spent);
            travelCardImage = itemView.findViewById(R.id.travelcard_image);
            likeButton = itemView.findViewById(R.id.like_button);
            followButton = itemView.findViewById(R.id.follow_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent travelIntent = new Intent(v.getContext(), TravelActivity.class);
                    Trip t = getItem(getAdapterPosition());
                    travelIntent.putExtra("tripId", t.getTripId());

                    userRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
                    travelIntent.putExtra("ownerId", t.getOwnerId());
                    itemView.getContext().startActivity(travelIntent);
                }
            });


        }
    }


    public void backgroundColorPicker(ImageView iv) {

        String[] colorArray = {"#BBDEFB","#90CAF9","#42A5F5","#1565C0","#82B1FF","#03A9F4",
                "#039BE5","#00BCD4","#26C6DA","#00838F","#00B8D4","#009688","#26A69A","#00796B",
                "#004D40","#66BB6A","#388E3C","#8BC34A"};

        Random color = new Random();
        int n = color.nextInt(18);
        iv.setBackgroundColor(Color.parseColor(colorArray[n]));
    }






}
