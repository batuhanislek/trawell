package com.trawell.batu.trawell.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trawell.batu.trawell.Model.ChartExpenseItem;
import com.trawell.batu.trawell.Model.Expense;
import com.trawell.batu.trawell.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CurrentFragment extends Fragment {


    View view;
    private FragmentActivity mContext;
    ArrayList<String> tripIdList;
    ArrayList<String> tripNames;
    ArrayList<Expense> expenseArrayList;
    DatabaseReference tripsRef;
    DatabaseReference userTripIdListRef, tripExpenseListRef;
    FirebaseAuth mAuth;
    Button showExpensesButton;
    Spinner spinner;
    RelativeLayout contentLayout;

    public CurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_current, container, false);

        showExpensesButton = view.findViewById(R.id.show_all_button);
        contentLayout = view.findViewById(R.id.current_content_layout);
        showExpensesButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        userTripIdListRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("tripIdList");


        tripsRef = FirebaseDatabase.getInstance().getReference("Trips");
        spinner = view.findViewById(R.id.current_spinner);
        tripNames = new ArrayList<>();
        tripNames.add("Select a trip");
        loadTripIds();
        expenseArrayList = new ArrayList<>();



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, tripNames);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTrip = spinner.getSelectedItem().toString();

                if(!selectedTrip.equals("Select a trip")) {
                    showExpensesButton.setVisibility(View.VISIBLE);
                    contentLayout.setVisibility(View.VISIBLE);

                    String tripId = tripIdList.get(tripNames.indexOf(selectedTrip)-1);
                    tripExpenseListRef = FirebaseDatabase.getInstance().getReference("Trips").child(tripId).child("expenseList");
                    loadTripExpenseBudgetData();
                    drawPieChart();


                } else {
                    showExpensesButton.setVisibility(View.INVISIBLE);
                    contentLayout.setVisibility(View.INVISIBLE);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });









        return view;
    }

    public void loadTripIds() {
        tripIdList = new ArrayList<String>();
        userTripIdListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    spinner.setVisibility(View.VISIBLE);
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        tripIdList.add(childSnapshot.getKey());
                    }
                    loadTrips();
                } else {
                    showExpensesButton.setVisibility(View.INVISIBLE);
                    TextView noTripText = new TextView(getContext());
                    contentLayout.addView(noTripText);
                    RelativeLayout.LayoutParams textPraram = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    textPraram.setMargins(180,400,0,0);
                    noTripText.setLayoutParams(textPraram);
                    noTripText.setText("You did not create any trip.");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void loadTrips() {
        for(int i=0; i<tripIdList.size(); i++) {
            tripsRef.child(tripIdList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String tripName = dataSnapshot.child("tripName").getValue().toString();
                    tripNames.add(tripName);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void loadTripExpenseBudgetData() {
        tripExpenseListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    expenseArrayList.add(childSnapshot.getValue(Expense.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    public void drawPieChart() {
        float foodBevExp=0, transportExp=0, accomExp=0, eventExp=0, entertExp=0;
        String expenseType [] = {"Food&Bev", "Transport", "Accomodation", "Event", "Entertainment"};
        List<PieEntry> itemList = new ArrayList<>();

        PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);

        for (int i=0; i < expenseArrayList.size(); i++) {
            String type = expenseArrayList.get(i).getType();
            Double price = expenseArrayList.get(i).getPrice();

            Log.i("type + price", type + " " + String.valueOf(price));

            switch (type){
                case "Accomodation":
                    accomExp += price;
                    break;
                case "Food & Beverages":
                    foodBevExp += price;
                    break;
                case "Transportation":
                    transportExp += price;
                    break;
                case "Event":
                    eventExp += price;
                    itemList.add(new PieEntry(eventExp,"Event"));
                    break;
                case "Entertainment":
                    entertExp += price;
                    break;
                default:
                    break;
            }
            
            itemList.add(new PieEntry(accomExp,"Acc."));
            itemList.add(new PieEntry(foodBevExp,"Food & Bev"));
            itemList.add(new PieEntry(transportExp,"Transport."));
            itemList.add(new PieEntry(entertExp, "Entert."));
        }

        Log.i("itemList", itemList.toString());

        PieDataSet dataSet = new PieDataSet(itemList,"Trip Expense");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();



    }

    public void drawLineChart() {

    }






}


