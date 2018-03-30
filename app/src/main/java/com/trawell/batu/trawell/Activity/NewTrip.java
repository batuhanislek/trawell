package com.trawell.batu.trawell.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trawell.batu.trawell.R;





public class NewTrip extends AppCompatActivity {

    ImageButton closeActivity;
    EditText search_destination_EditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        String placeName = getIntent().getStringExtra("placeName");

        closeActivity = findViewById(R.id.close_newtrip_button);
        search_destination_EditText = findViewById(R.id.search_editText);

        search_destination_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(NewTrip.this,SearchDestination.class);
                startActivity(searchIntent);
            }
        });

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
