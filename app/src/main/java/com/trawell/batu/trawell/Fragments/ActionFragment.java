package com.trawell.batu.trawell.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.trawell.batu.trawell.Activity.SelectionActivity;
import com.trawell.batu.trawell.Model.Destination;
import com.trawell.batu.trawell.Model.Expense;
import com.trawell.batu.trawell.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


public class ActionFragment extends Fragment {

    private EditText ett, ettt, tripSelection, destSelection;
    private LinearLayout contentLayout;
    private RelativeLayout actionFragmentLayout;
    private ProgressBar imageUploadProgressBar;
    private Button publishButton;
    private ImageButton loadImageButton, addExpenseButton;
    private NestedScrollView scrollView;
    private View view;
    private String firebaseContent="";
    private StorageReference fireStorage;
    private StringBuilder content;
    private DatabaseReference tripRef, userRef;
    private FirebaseAuth mAuth;
    private HashMap<Integer,Expense> expList;
    private HashMap<Integer,String> imageList;
    private ArrayList<String> tripNames, destinationNames;
    private ArrayList<Expense> expenseList;
    private String selectedTripId, selectedDestName, selectedItem, itemType="", destNo;
    private TextView destHeadline, headline;
    private String currentDate, currentDay;
    private static final int REQUEST_CODE = 1;

    public ActionFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_action, null);

        expList = new HashMap<Integer,Expense>();
        imageList = new HashMap<Integer,String>();
        expenseList = new ArrayList<Expense>();

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.US);
        DateFormat dff = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
        currentDate = df.format(Calendar.getInstance().getTime());
        currentDay = dff.format(Calendar.getInstance().getTime());


        mAuth = FirebaseAuth.getInstance();
        tripRef = FirebaseDatabase.getInstance().getReference("Trips");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference("Contents");
        fireStorage = FirebaseStorage.getInstance().getReference();


        publishButton = view.findViewById(R.id.publish_button);
        imageUploadProgressBar = view.findViewById(R.id.image_upload_progressBar);
        loadImageButton = view.findViewById(R.id.load_image_button);
        addExpenseButton = view.findViewById(R.id.add_expense_button);
        scrollView = view.findViewById(R.id.action_scrollview);
        scrollView.setNestedScrollingEnabled(false);

        contentLayout = new LinearLayout(getContext());
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(contentLayout);

        actionFragmentLayout = view.findViewById(R.id.action_fragment_layout);
        tripSelection = view.findViewById(R.id.select_trip_editText);
        destSelection = view.findViewById(R.id.select_dest_editText);
        destHeadline = view.findViewById(R.id.dest_name_headline);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        tripNames = loadTripNames();
        loadSpinnerData();


        headline = new TextView(getContext());
        contentLayout.addView(headline);
        LinearLayout.LayoutParams headlineParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headlineParam.setMargins(15,10,0,15);
        headline.setLayoutParams(headlineParam);
        headline.setTextSize(20);
        headline.setTypeface(headline.getTypeface(), Typeface.BOLD);


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
                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose expense type..")) {

                            final View expenseCard = inflater.inflate(R.layout.expense_cardview,null,false);
                            TextView expenseType = (TextView) expenseCard.findViewById(R.id.expense_type_textView);
                            TextView expenseInfo = (TextView) expenseCard.findViewById(R.id.expense_exp);
                            TextView expensePrice = (TextView) expenseCard.findViewById(R.id.expense_price_textView);
                            ImageButton closeExpenseCard = (ImageButton) expenseCard.findViewById(R.id.close_expense_card_button);

                            String type = mSpinner.getSelectedItem().toString();
                            String comment = infoEditText.getText().toString();
                            Double price = Double.parseDouble(priceEditText.getText().toString());
                            expenseType.setText(type);
                            expenseInfo.setText(comment);
                            expensePrice.setText(String.valueOf(price));

                            contentLayout.addView(expenseCard);
                            LinearLayout.LayoutParams expenseParam= (LinearLayout.LayoutParams) expenseCard.getLayoutParams();
                            expenseParam.setMargins(15,15,15,15);

                            final int expenseIndex = contentLayout.indexOfChild(expenseCard);
                            Expense expense = new Expense(type, comment, price, selectedDestName, currentDate);

                            expenseList.add(expense);
                            expList.put(expenseIndex,expense);

                            ettt = new EditText(getContext());
                            ettt.setBackgroundResource(0);
                            ettt.setTextSize(20);
                            contentLayout.addView(ettt);

                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ettt.getLayoutParams();
                            lp.setMargins(15,15,15,0);

                            ettt.requestFocus();

                            ettt.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    if(keyCode == KeyEvent.KEYCODE_DEL && ettt.getSelectionStart() == 0) {
                                        int index = contentLayout.indexOfChild(ettt);
                                        if (contentLayout.getChildAt(index-1) instanceof EditText) {
                                            String ettText = ettt.getText().toString();
                                            contentLayout.removeView(ettt);
                                            ((EditText) contentLayout.getChildAt(index-1)).append(ettText);
                                        }
                                    }
                                    return false;
                                }
                            });
                            closeExpenseCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    expList.remove(expenseIndex);
                                    contentLayout.removeView(expenseCard);
                                }
                            });
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



        EditText et = new EditText(getContext());
        et.setBackgroundResource(0);
        et.setTextSize(20);
        contentLayout.addView(et);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) et.getLayoutParams();
        lp.setMargins(15,15,15,0);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //content.delete(0, content.length());
                firebaseContent="";
                publishButton.setVisibility(View.INVISIBLE);
                imageUploadProgressBar.setVisibility(View.VISIBLE);

                contentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(selectedTripId != null) {
                            firebaseContent = dataSnapshot.child(selectedTripId).child(destNo).exists() ?
                                    dataSnapshot.child(selectedTripId).child(destNo).getValue().toString() : "";
                            content.append(firebaseContent);

                            View tempView;

                            content.append("<destName>" + selectedDestName + "</destName>");
                            content.append("<date>" + currentDate + "</date>");

                            for(int i=0; i < contentLayout.getChildCount(); i++) {
                                tempView = contentLayout.getChildAt(i);
                                if(tempView instanceof EditText) {
                                    if(!((EditText) tempView).getText().toString().equals("")) {
                                        content.append("<text>");
                                        content.append(((EditText) tempView).getText().toString());
                                        content.append("</text>");
                                    }
                                }
                                else if(tempView instanceof ImageView) {
                                    content.append("<image>");
                                    content.append(imageList.get(i));
                                    content.append("</image>");
                                }
                                else if(tempView instanceof CardView) {
                                    content.append("<card>");
                                    String type = expList.get(i).getType();
                                    String comment = expList.get(i).getComment();
                                    String price = String.valueOf(expList.get(i).getPrice());
                                    content.append("<type>"+type+"</type>");
                                    content.append("<comment>"+comment+"</comment>");
                                    content.append("<price>"+price+"</price>");
                                    content.append("</card>");
                                }

                            }
                            contentRef.child(selectedTripId).child(destNo).setValue(content.toString());
                            tripRef.child(selectedTripId).child("expenseList").setValue(expenseList);
                            Toast.makeText(getContext(),"Content published", Toast.LENGTH_SHORT).show();
                            content.delete(0,content.length());
                            firebaseContent="";

                            publishButton.setVisibility(View.VISIBLE);
                            imageUploadProgressBar.setVisibility(View.INVISIBLE);
                            contentLayout.removeAllViews();

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

            final Uri uri = data.getData();
            final ImageView image = new ImageView(getContext());

            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                final String path = UUID.randomUUID().toString() + ".png";
                final String imagePath = "images/" + path;

                final StorageReference ref = fireStorage.child(imagePath);
                publishButton.setVisibility(View.INVISIBLE);
                imageUploadProgressBar.setVisibility(View.VISIBLE);

                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        publishButton.setVisibility(View.VISIBLE);
                        imageUploadProgressBar.setVisibility(View.INVISIBLE);

                        Picasso.get()
                                .load(uri)
                                .resize(500,500)
                                .centerCrop()
                                .into(image);

                        contentLayout.addView(image);

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                int index = contentLayout.indexOfChild(image);
                                imageList.put(index,downloadUrl);

                            }
                        });

                        ett = new EditText(getContext());
                        ett.setBackgroundResource(0);
                        ett.setTextSize(20);
                        contentLayout.addView(ett);

                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ett.getLayoutParams();
                        lp.setMargins(15,15,15,0);

                        ett.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                if(keyCode == KeyEvent.KEYCODE_DEL && ett.getSelectionStart() == 0) {
                                    int index = contentLayout.indexOfChild(ett);
                                    if (contentLayout.getChildAt(index-1) instanceof EditText) {
                                        String ettText = ett.getText().toString();
                                        contentLayout.removeView(ett);
                                        ((EditText) contentLayout.getChildAt(index-1)).append(ettText);
                                    }
                                }
                                return false;
                            }
                        });

                        ett.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                }
                            }
                        });

                        image.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getContext());
                                }
                                builder.setTitle("Delete this image ?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                final int deleteIndex = contentLayout.indexOfChild(image);
                                                if (contentLayout.getChildAt(deleteIndex+1) instanceof EditText &&
                                                        ((EditText) contentLayout.getChildAt(deleteIndex+1)).getText().toString().isEmpty()) {
                                                    fireStorage.child(imageList.get(deleteIndex)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            imageList.remove(deleteIndex);
                                                            contentLayout.removeView(contentLayout.getChildAt(deleteIndex+1));
                                                            Toast.makeText(getContext(),"Image deleted !", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getContext(),"Image delete unsuccessfull !", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                contentLayout.removeView(image);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {}
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                return  true;
                            }
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                loadExpenseData();

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
                headline.setText(selectedDestName + "  -  " + currentDay);
            }
        }
    }

    private void loadExpenseData() {
        tripRef.child(selectedTripId).child("expenseList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    expenseList.add(childSnapshot.getValue(Expense.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }










}


