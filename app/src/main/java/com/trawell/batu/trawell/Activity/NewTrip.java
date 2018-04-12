package com.trawell.batu.trawell.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trawell.batu.trawell.Adapters.DestinationAdapter;
import com.trawell.batu.trawell.Fragments.CurrentFragment;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.R;
import com.trawell.batu.trawell.TaskManager.MyEditTextDatePicker;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class NewTrip extends AppCompatActivity {

    ImageButton closeActivity;
    Button addDestinationButton;
    Button saveTripButton;
    Button showMapButton;
    EditText searchDestination_EditText, tripName_EditText;
    RecyclerView recyclerView;

    DestinationAdapter adapter;
    String placeName;
    String tripName;
    ArrayList<Destination> destList;

    private DatabaseReference tripRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        closeActivity               =    findViewById(R.id.close_newtrip_button);
        searchDestination_EditText  =    findViewById(R.id.search_editText);
        recyclerView                =    findViewById(R.id.recycler_view);
        saveTripButton              =    findViewById(R.id.save_button);
        addDestinationButton        =    findViewById(R.id.add_button);
        showMapButton               =    findViewById(R.id.show_map_button);
        tripName_EditText           =    findViewById(R.id.trip_name_edit);


        loadData();

        adapter = new DestinationAdapter(this, destList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        getIncomingIntent();


        mAuth = FirebaseAuth.getInstance();
        tripRef = FirebaseDatabase.getInstance().getReference("Trips");

        searchDestination_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(NewTrip.this,SearchDestination.class);
                startActivity(searchIntent);
            }
        });

        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogCreator();
            }
        });

        addDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(NewTrip.this, SearchDestination.class);
                startActivity(searchIntent);
            }
        });

        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();
                tripName = tripName_EditText.getText().toString();
                tripRef.child(userId).child(tripName).setValue(destList);
                Intent currentTravelIntent = new Intent(NewTrip.this, HomeActivity.class);
                startActivity(currentTravelIntent);
                deleteSharedPref();
                Toast.makeText(getApplicationContext(), "Trip Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIncomingIntent() {
            placeName = getIntent().getStringExtra("DEST");
            if(placeName != null && !placeName.isEmpty()) {
                destList.add(new Destination(placeName,"",""));
                saveData();
                Log.i("size5", " "+ destList);
            }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(destList);
        editor.putString("destination list", json);
        tripName = tripName_EditText.getText().toString();
        editor.putString("trip name", tripName);
        editor.apply();
        Log.i("size1", " "+ destList);
    }

    private void loadData() {
        Log.i("size2", " "+ destList);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("destination list",null);
        String tName = sharedPreferences.getString("trip name", null);
        tripName_EditText.setText(tName);
        Type type = new TypeToken<ArrayList<Destination>>() {}.getType();
        destList = gson.fromJson(json, type);
        if(destList == null) {
            destList = new ArrayList<>();
            Log.i("size4", " "+ destList);
        }
        Log.i("size3", " "+ destList);
    }

    @Override
    public void onBackPressed() {
        alertDialogCreator();
    }

    private void deleteSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    private void alertDialogCreator() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(NewTrip.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(NewTrip.this);
        }

        builder.setMessage("Discard Changes ?")
                .setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteSharedPref();
                        Intent currentTravelIntent = new Intent(NewTrip.this, HomeActivity.class);
                        startActivity(currentTravelIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_menu_save)
                .setCancelable(false)
                .show();
    }




}
