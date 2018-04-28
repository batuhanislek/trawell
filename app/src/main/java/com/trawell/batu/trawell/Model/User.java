package com.trawell.batu.trawell.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Batuhan Islek on 18.03.2018.
 */

@IgnoreExtraProperties
public class User {

    private String username;
    private String email;
    private ArrayList<String> tripIdList;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getTrips() {
        return tripIdList;
    }

    public void setTrips(ArrayList<String> trips) {
        this.tripIdList = trips;
    }

    public User(String username, String email, ArrayList<String> tripIdList) {
        this.username = username;
        this.email = email;
        this.tripIdList = tripIdList;
    }


}
