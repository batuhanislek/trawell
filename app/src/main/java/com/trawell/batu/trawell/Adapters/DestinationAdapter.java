package com.trawell.batu.trawell.Adapters;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.trawell.batu.trawell.Activity.NewTrip;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.R;
import com.trawell.batu.trawell.TaskManager.MyEditTextDatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Batuhan Islek on 31.03.2018.
 */
public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Destination> mList;

    public DestinationAdapter(Context context, ArrayList<Destination> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Destination destItem = mList.get(position);

        EditText destinationName = holder.destinationName;
        EditText checkInDate = holder.checkInDate;
        EditText checkOutDate = holder.checkOutDate;

        destinationName.setText(destItem.getCityName());
        checkInDate.setText(destItem.getCheckInDate());
        checkOutDate.setText(destItem.getCheckOutDate());

        holder.closeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
                saveToSharedPref(mList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        EditText destinationName, checkInDate, checkOutDate;
        public ImageButton closeCard;


        public ViewHolder(View itemView) {
            super(itemView);

            final int id = R.id.first_date;
            destinationName = itemView.findViewById(R.id.destination_editText);
            checkInDate = itemView.findViewById(id);
            checkOutDate = itemView.findViewById(R.id.last_date);
            closeCard = itemView.findViewById(R.id.close_card_button);


            checkInDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyEditTextDatePicker picker = new MyEditTextDatePicker(mContext, id);
                }
            });

            checkInDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String indate = s.toString();
                    mList.get(getAdapterPosition()).setCheckInDate(indate);
                    saveToSharedPref(mList);
                }
            });

            checkOutDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyEditTextDatePicker picker = new MyEditTextDatePicker(mContext, R.id.last_date);
                }
            });

            checkOutDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String outdate = s.toString();
                    mList.get(getAdapterPosition()).setCheckOutDate(outdate);
                    saveToSharedPref(mList);
                }
            });
        }
    }

    public void removeAt(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
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


