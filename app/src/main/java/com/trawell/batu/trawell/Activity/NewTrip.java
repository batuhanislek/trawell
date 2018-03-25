package com.trawell.batu.trawell.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.trawell.batu.trawell.R;

public class NewTrip extends AppCompatActivity {

    ImageButton closeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        closeActivity = findViewById(R.id.close_newtrip_button);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent backActivityIntent = new Intent(NewTrip.this, HomeActivity.class);
                startActivity(backActivityIntent);
            }
        });



    }
}
