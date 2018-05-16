package com.trawell.batu.trawell.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trawell.batu.trawell.R;

import java.util.ArrayList;

/**
 * Created by Batuhan Islek on 5.05.2018.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> names;

    public SpinnerAdapter(Context context, ArrayList<String> names) {
        super(context, 0, names);
        this.context = context;
        this.names = names;
    }

    public SpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_spinner_item, parent,false);
        }

        //extView tripName = convertView.findViewById(R.id.trip_spinner_name_textView);
        String item = names.get(position);
        //tripName.setText(item);

        return convertView;
    }

}
