package com.trawell.batu.trawell.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trawell.batu.trawell.Fragments.ActionFragment;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.R;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    private ImageButton selectionBackButton;
    private ArrayList<String> placeNameList, destNameList;
    private Boolean tripListViewShown=false;
    private ListView selectionListView;
    private String itemType="";
    private TextView selectionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        selectionBackButton = findViewById(R.id.selection_back_button);
        selectionListView = findViewById(R.id.selection_listView);
        selectionTextView = findViewById(R.id.selection_textView);

        getIncomingListView();

        selectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                saveData(item, itemType);
                finish();
            }
        });
    }

    private void saveData(String item, String itemType) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedItem", item);
        editor.putString("itemType", itemType);
        editor.apply();
        Log.i("id", item + " " + itemType);
    }

    private void getIncomingListView() {
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("trip_names")) {
            placeNameList = getIntent().getStringArrayListExtra("trip_names");
            if(placeNameList != null && !placeNameList.isEmpty()) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        placeNameList );
                selectionListView.setAdapter(arrayAdapter);

            }
            itemType = "trip";

        } else if(extras.containsKey("dest_names")) {
            selectionTextView.setText("Select the Destination");
            destNameList = getIntent().getStringArrayListExtra("dest_names");
            if(destNameList != null && !destNameList.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        destNameList );
                selectionListView.setAdapter(adapter);
            }
            itemType="destination";
        }
    }




}
