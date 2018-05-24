package com.trawell.batu.trawell.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Batuhan Islek on 24.04.2018.
 */
public class Trip {
    private String ownerId;
    private String tripId;
    private String tripName;
    private int listSize;
    private String timestamp;
    private long likeCount=0;
    private ArrayList<Destination> destinationArrayList = new ArrayList<Destination>();
    private ArrayList<Expense> expenseArrayList = new ArrayList<Expense>();

    public ArrayList<Expense> getExpenseArrayList() {
        return expenseArrayList;
    }

    public void setExpenseArrayList(ArrayList<Expense> expenseArrayList) {
        this.expenseArrayList = expenseArrayList;
    }

    public Trip() {}

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public int getListSize() {
        return destinationArrayList.size();
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public Destination getLastItem() {
        return destinationArrayList.get(getListSize()-1);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Destination getFirstItem() {
        return destinationArrayList.get(0);
    }

    public Trip(String ownerId, String tripName) {
        this.ownerId = ownerId;
        this.tripName = tripName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public ArrayList<Destination> getDestinationArrayList() {
        return destinationArrayList;
    }

    public void setDestinationArrayList(ArrayList<Destination> destinationArrayList) {
        this.destinationArrayList = destinationArrayList;
    }

    public void addDestination(Destination d) {
        getDestinationArrayList().add(d);
    }
}


