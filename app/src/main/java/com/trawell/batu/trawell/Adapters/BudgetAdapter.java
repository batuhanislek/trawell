package com.trawell.batu.trawell.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.R;
import com.trawell.batu.trawell.TaskManager.DateCalculation;
//import com.trawell.batu.trawell.TaskManager.DaysBetweenDates;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Batuhan Islek on 20.04.2018.
 */
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Destination> mList;
    private double totalBudget = 0;
    private double budget,
                   transportExpense=0,
                   accomodationExpense=0;

    public BudgetAdapter(Context context, ArrayList<Destination> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.budget_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Destination destItem = mList.get(position);

        TextView destinationName = holder.destinationName;
        destinationName.setText(destItem.getCityName());

        TextView daysSpent = holder.daysSpent;

        long days = new DateCalculation().DaysBetweenDates(destItem.getCheckInDate(),
                                                           destItem.getCheckOutDate());
        daysSpent.setText(String.valueOf(days));
        destItem.setDaysSpent(days);
        saveToSharedPref(mList);

        TextView currentExpense = holder.currentExpense;
        EditText destinationBudget = holder.destinationBudget;
        EditText transportExpense = holder.transportExpense_textView;
        EditText accomodationExpense = holder.accomodationExpense_textView;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView destinationName;
        TextView currentExpense;
        TextView daysSpent;
        EditText transportExpense_textView, accomodationExpense_textView, destinationBudget;

        public ViewHolder(View itemView) {
            super(itemView);

            destinationName = itemView.findViewById(R.id.headline);
            daysSpent = itemView.findViewById(R.id.days_spent);
            destinationBudget = itemView.findViewById(R.id.dest_budget_editText);
            transportExpense_textView = itemView.findViewById(R.id.transportation_editText);
            accomodationExpense_textView = itemView.findViewById(R.id.accomodation_editText);


            destinationBudget.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().matches("")) {
                        budget = 0;
                    } else {
                        budget = Double.parseDouble(s.toString());
                    }
                    mList.get(getAdapterPosition()).setBudget(budget);
                    saveToSharedPref(mList);
                }
            });

            transportExpense_textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().matches("")) {
                        transportExpense=0;
                    } else {
                        transportExpense = Double.parseDouble(s.toString());
                    }
                    mList.get(getAdapterPosition()).setTransportExpense(transportExpense);
                    saveToSharedPref(mList);
                }
            });

            accomodationExpense_textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().matches("")) {
                        accomodationExpense=0;
                    } else {
                        accomodationExpense = Double.parseDouble(s.toString());
                    }
                    mList.get(getAdapterPosition()).setAccomodationExpense(accomodationExpense);
                    saveToSharedPref(mList);
                }
            });

        }
    }

    public void saveToSharedPref( ArrayList<Destination> list ) {
        SharedPreferences settings = mContext.getSharedPreferences("sharedPref", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("destination list", json);
        editor.apply();
    }










}
