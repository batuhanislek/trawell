package com.trawell.batu.trawell.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trawell.batu.trawell.Fragments.ProfileFragment;
import com.trawell.batu.trawell.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new ProfileFragment()).commit();

    }
}
