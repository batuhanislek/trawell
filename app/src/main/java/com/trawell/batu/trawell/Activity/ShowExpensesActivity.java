package com.trawell.batu.trawell.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.Model.Expense;
import com.trawell.batu.trawell.R;

import java.util.ArrayList;

public class ShowExpensesActivity extends AppCompatActivity {

    ScrollView scrollView;
    String tripId;
    ArrayList<Expense> expenses;
    DatabaseReference tripsRef;
    LinearLayout expensesLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);
        expenses = new ArrayList<>();

        tripsRef = FirebaseDatabase.getInstance().getReference("Trips");


        scrollView = findViewById(R.id.expenses_scroll_view);
        expensesLayout = new LinearLayout(this);
        expensesLayout.setOrientation(LinearLayout.VERTICAL);

        scrollView.addView(expensesLayout);
        getIncomingIntent();









    }

    private void getIncomingIntent() {
        tripId = getIntent().getStringExtra("tripId");
        if(tripId != null && !tripId.isEmpty()) {
            loadTripExpenseBudgetData(tripId);
        }
        Log.i("showId",tripId);
    }

    public void loadTripExpenseBudgetData(String id) {
        Log.i("postID",id);
        tripsRef.child(id).child("expenseList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //expenses.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    expenses.add(childSnapshot.getValue(Expense.class));
                    //createExpenseRows(childSnapshot.getValue(Expense.class));
                }
                createExpenseRows();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    public void createExpenseRows() {
        //Log.i("showList", expenses.toString());
        Double totalExpense=0.0;
        for(int i=0; i < expenses.size(); i++) {
            View row = getLayoutInflater().inflate(R.layout.expense_row_view,null);
            TextView type = (TextView) row.findViewById(R.id.row_type);
            TextView price = (TextView) row.findViewById(R.id.row_price);
            TextView date = (TextView) row.findViewById(R.id.row_date);
            TextView location = (TextView) row.findViewById(R.id.row_location);
            type.setText(expenses.get(i).getType());
            price.setText(String.valueOf(expenses.get(i).getPrice()));
            date.setText(expenses.get(i).getDate());
            location.setText(expenses.get(i).getLocation());
            expensesLayout.addView(row);

            totalExpense += expenses.get(i).getPrice();
        }

        View row = getLayoutInflater().inflate(R.layout.expense_row_view,null);
        TextView type = (TextView) row.findViewById(R.id.row_type);
        type.setTextColor(Color.BLACK);
        TextView price = (TextView) row.findViewById(R.id.row_price);
        price.setTextColor(Color.BLACK);
        type.setText("TOTAL");
        price.setText(String.valueOf(totalExpense));
        expensesLayout.addView(row);



    }


}
