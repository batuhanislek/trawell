package com.trawell.batu.trawell.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trawell.batu.trawell.Adapters.DestinationAdapter;
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
    EditText search_destination_EditText, first_date;
    RecyclerView recyclerView;
    DestinationAdapter adapter;
    String placeName;
    ArrayList<Destination> destList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        loadData();

        closeActivity = findViewById(R.id.close_newtrip_button);
        search_destination_EditText = findViewById(R.id.search_editText);
        recyclerView = findViewById(R.id.recycler_view);
        addDestinationButton = findViewById(R.id.add_button);

        adapter = new DestinationAdapter(this,destList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        getIncomingIntent();










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
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                startActivity(backActivityIntent);
            }
        });

        addDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(NewTrip.this, SearchDestination.class);
                startActivity(searchIntent);
            }
        });
    }

    private void getIncomingIntent() {
            placeName = getIntent().getStringExtra("DEST");
            if(placeName != null && !placeName.isEmpty()) {
                destList.add(new Destination(placeName,"",""));
                Log.i("From Search", " " + placeName);
                saveData();
            }
    }


    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(destList);
        editor.putString("destination list",json);
        editor.apply();
        Log.i("list saved", " " + Integer.toString(destList.size()) );
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("destination list",null);
        Type type = new TypeToken<ArrayList<Destination>>() {}.getType();
        destList = gson.fromJson(json, type);
        if(destList == null) {
            destList = new ArrayList<>();
            Log.i("new list", " " + Integer.toString(destList.size()));
        } else {
            Log.i("the list", " " + Integer.toString(destList.size()));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSharedPreferences("sharedPref",0).edit().clear().commit();
    }






}
