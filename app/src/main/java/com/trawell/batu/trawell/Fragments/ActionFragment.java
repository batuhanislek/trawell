package com.trawell.batu.trawell.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.trawell.batu.trawell.Activity.SelectionActivity;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ActionFragment extends Fragment {

    private EditText ett, tripSelection, destSelection;
    private LinearLayout ll;
    private RelativeLayout actionFragmentLayout;
    private Button publishButton;
    private ImageButton loadImageButton, addExpenseButton;
    private ScrollView scrollView;
    private View view;
    private String firebaseContent="";
    private StorageReference fireStorage;
    private StringBuilder content;
    private DatabaseReference tripRef, userRef;
    private FirebaseAuth mAuth;
    private ArrayList<String> tripNames, destinationNames;
    private String selectedTripId, selectedDestName, selectedItem, itemType="", destNo;
    private TextView destHeadline;
    private static final int REQUEST_CODE = 1;

    public ActionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_action, null);

        mAuth = FirebaseAuth.getInstance();
        tripRef = FirebaseDatabase.getInstance().getReference("Trips");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference("Contents");
        fireStorage = FirebaseStorage.getInstance().getReference();


        publishButton = view.findViewById(R.id.publish_button);
        loadImageButton = view.findViewById(R.id.load_image_button);
        addExpenseButton = view.findViewById(R.id.add_expense_button);
        scrollView = view.findViewById(R.id.action_scrollview);

        actionFragmentLayout = view.findViewById(R.id.action_fragment_layout);
        tripSelection = view.findViewById(R.id.select_trip_editText);
        destSelection = view.findViewById(R.id.select_dest_editText);
        destHeadline = view.findViewById(R.id.dest_name_headline);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        loadSpinnerData();

        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"), REQUEST_CODE);
            }
        });


        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.expense_dialog, null);
                mBuilder.setTitle("Fill the Expense Details");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.expenseTypeList));
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(typeAdapter);

                final EditText infoEditText = (EditText) mView.findViewById(R.id.expense_info_editText);
                final EditText priceEditText = (EditText) mView.findViewById(R.id.expense_price_editText);

                mBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose expense type..")) {
                            Toast.makeText(getActivity(), infoEditText.getText(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), priceEditText.getText(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),mSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.setIcon(R.drawable.money_24dp);
                dialog.show();
            }
        });


        tripSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tripSelectionIntent = new Intent(getContext(), SelectionActivity.class);
                tripSelectionIntent.putExtra("trip_names", tripNames);
                startActivity(tripSelectionIntent);
                destSelection.setVisibility(View.INVISIBLE);
                destHeadline.setVisibility(View.INVISIBLE);
            }
        });



        destSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent destSelectionIntent = new Intent(getContext(), SelectionActivity.class);
                destSelectionIntent.putExtra("dest_names", destinationNames);
                startActivity(destSelectionIntent);
            }
        });

        tripNames = loadTripNames();

        ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(ll);

        EditText et = new EditText(getContext());
        et.setBackgroundResource(0);
        et.setTextSize(20);
        ll.addView(et);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) et.getLayoutParams();
        lp.setMargins(15,15,15,0);



        /*

        contentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(selectedTripId != null) {
                    content.delete(0, content.length());
                    firebaseContent="";
                    firebaseContent = dataSnapshot.child(selectedTripId).child(destNo).getValue().toString();
                    Log.i("The firebase", firebaseContent);
                    content.append(firebaseContent);
                    Log.i("AfterFirebaseContent",content.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        */



        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //content.delete(0, content.length());
                firebaseContent="";

                contentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(selectedTripId != null) {
                            firebaseContent = dataSnapshot.child(selectedTripId).child(destNo).exists() ?
                                    dataSnapshot.child(selectedTripId).child(destNo).getValue().toString() : "";
                            Log.i("The firebase", firebaseContent);
                            content.append(firebaseContent);
                            Log.i("AfterFirebaseContent",content.toString());
                            View tempView;
                            for(int i =0; i < ll.getChildCount(); i++) {
                                tempView = ll.getChildAt(i);
                                if(tempView instanceof EditText) {
                                    content.append("<text>");
                                    content.append(((EditText) tempView).getText().toString());
                                    content.append("</text>");
                                }
                                else if(tempView instanceof ImageView) {
                                    content.append("<image>");
                                    //content.append();
                                    content.append("</image>");
                                }
                            }
                            Log.i("publishButtonContent",content.toString());
                            contentRef.child(selectedTripId).child(destNo).setValue(content.toString());
                            Toast.makeText(getContext(),"Content published", Toast.LENGTH_SHORT).show();
                            content.delete(0,content.length());
                            firebaseContent="";
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });





            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSpinnerData();
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();
            ImageView image = new ImageView(getContext());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                image.setImageBitmap(bitmap);
                ll.addView(image);
                LinearLayout.LayoutParams ip = (LinearLayout.LayoutParams) image.getLayoutParams();
                ip.setMargins(5,10,5,0);

            } catch (Exception e) {
                e.printStackTrace();
            }

            ett = new EditText(getContext());
            ett.setBackgroundResource(0);
            ett.setTextSize(20);
            ll.addView(ett);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ett.getLayoutParams();
            lp.setMargins(15,0,15,0);
            ett.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            });
        }
    }

    public ArrayList<String> loadTripNames() {
        tripNames = new ArrayList<String>();
        String currentUserId = mAuth.getCurrentUser().getUid();
        userRef.child(currentUserId).child("tripIdList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    tripNames.add(childSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return tripNames;

    }


    public void loadDestinationNames(String selectedTripId) {
        destinationNames = new ArrayList<String>();
        tripRef.child(selectedTripId).child("destinations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Destination selectedDestination = childSnapshot.getValue(Destination.class);
                    selectedDestination.setDestination_Id(Integer.parseInt(childSnapshot.getKey()));
                    destinationNames.add(selectedDestination.getCityName());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    private void loadSpinnerData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", MODE_PRIVATE);
        selectedItem = sharedPreferences.getString("selectedItem",null);
        itemType = sharedPreferences.getString("itemType",null);

        if(itemType != null) {
            if(itemType.contains("trip")) {
                selectedTripId = selectedItem;
                tripSelection.setText(selectedTripId);
                destHeadline.setVisibility(View.VISIBLE);
                destSelection.setVisibility(View.VISIBLE);
                destSelection.setText("");
                loadDestinationNames(selectedTripId);

            } else if(itemType.contains("destination")) {
                selectedDestName = selectedItem;
                destSelection.setText(selectedDestName);

                DatabaseReference destNoRef = tripRef.child(selectedTripId).child("destinations");
                destNoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            Destination selectedDestination = childSnapshot.getValue(Destination.class);
                            if(selectedDestination.getCityName().equals(selectedDestName)) {
                                content = new StringBuilder();
                                destNo = String.valueOf(childSnapshot.getKey());
                                firebaseContent="";
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        }
    }








}


