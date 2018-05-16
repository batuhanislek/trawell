package com.trawell.batu.trawell.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trawell.batu.trawell.Adapters.BudgetAdapter;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.R;
import com.trawell.batu.trawell.TaskManager.DateCalculation;
;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.format;

public class PlanBudget extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    BudgetAdapter adapter;
    EditText total_budget_editText;
    TextView trip_total_days;
    TextView budget_per_day;
    Button save_button;
    ImageButton back_button;
    RecyclerView recyclerView;
    ArrayList<Destination> destList;
    SharedPreferences sharedPref;

    String tripName;
    double total_budget;
    double tripBudget=0;
    long totalDays=0;
    int listSize=0;

    private DatabaseReference tripRef;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_budget);

        final TextView trip_name_textView = (TextView) findViewById(R.id.trip_name);
        trip_total_days = findViewById(R.id.total_days);
        total_budget_editText = findViewById(R.id.total_budget_editText);
        budget_per_day = findViewById(R.id.budget_per_day_editText);
        save_button = findViewById(R.id.save_button);
        back_button = findViewById(R.id.back_button);

        sharedPref = this.getSharedPreferences("sharedPref",0);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        recyclerView = findViewById(R.id.budget_recyclerView);

        mAuth = FirebaseAuth.getInstance();
        tripRef = FirebaseDatabase.getInstance().getReference("Trips");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        loadData();
        trip_name_textView.setText(tripName);

        adapter = new BudgetAdapter(this, destList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        updateTotalDays();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dataID = tripRef.push().getKey();
                String userId = mAuth.getCurrentUser().getUid();

                tripRef.child(dataID).child("destinations").setValue(destList);

                tripRef.child(dataID).child("ownerId").setValue(mAuth.getCurrentUser().getUid());
                tripRef.child(dataID).child("tripName").setValue(tripName);
                tripRef.child(dataID).child("tripId").setValue(dataID);
                userRef.child(userId).child("tripIdList").child(dataID).setValue(true);

                Intent planBudgetIntent = new Intent(PlanBudget.this, HomeActivity.class);
                startActivity(planBudgetIntent);
                deleteSharedPref();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent planBudgetIntent = new Intent(PlanBudget.this, NewTrip.class);
                startActivity(planBudgetIntent);
            }
        });




    }

    private void deleteSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("destination list",null);
        tripName = sharedPreferences.getString("trip name", null);
        Type type = new TypeToken<ArrayList<Destination>>() {}.getType();
        destList = gson.fromJson(json, type);
        listSize = destList.size();
    }

    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
        double budget;
        total_budget=0;
        double budgetPerDay =0;
        for(int i=0; i<listSize; i++) {
            budget = destList.get(i).getBudget();
            total_budget += budget;
        }
        tripBudget = total_budget;
        total_budget_editText.setText(Double.toString(total_budget));
        budgetPerDay = (tripBudget /(double)totalDays);
        budget_per_day.setText(Double.toString(budgetPerDay));

    }

    public void updateTotalDays() {
        for(int i=0; i<listSize; ++i) {
            totalDays += new DateCalculation().DaysBetweenDates(destList.get(i).getCheckInDate().toString(),
                                                                destList.get(i).getCheckOutDate().toString());
        }
        trip_total_days.setText(Long.toString(totalDays));
    }







}
