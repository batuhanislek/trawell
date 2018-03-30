package com.trawell.batu.trawell.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Batuhan Islek on 18.03.2018.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
