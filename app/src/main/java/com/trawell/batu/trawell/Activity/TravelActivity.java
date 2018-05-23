package com.trawell.batu.trawell.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.trawell.batu.trawell.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.squareup.picasso.Picasso;



public class TravelActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private String username, lastDestName="";
    private String tripId, ownerId;
    private LinearLayout contentLayout;
    private ProgressBar contenProgressBar;
    Picasso picasso;



    DatabaseReference userRef;
    DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference("Contents");
    StorageReference imageStorageRef = FirebaseStorage.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        contentLayout = findViewById(R.id.travel_content_layout);
        contenProgressBar = findViewById(R.id.content_progress_bar);

        tripId = getIntent().getStringExtra("tripId");
        ownerId = getIntent().getStringExtra("ownerId");
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(ownerId);

        usernameTextView = findViewById(R.id.travel_username_textView);
        loadTripData();
        getFirebaseContent();





    }

    public void loadTripData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernameTextView.setText(String.valueOf(dataSnapshot.child("username").getValue()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    public void getFirebaseContent() {
        if(tripId != null) {
            contentRef.child(tripId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    displayContent(dataSnapshot.getValue().toString());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        contenProgressBar.setVisibility(View.INVISIBLE);
    }

    public void displayContent(String data) {
        String type="", comment="", price="", imageUrl="";

        Document doc = Jsoup.parse(data, "", Parser.xmlParser());
        for (Element e : doc.getAllElements()) {
            String tag = e.tagName();

            switch (tag) {
                case "destName":
                    if (!lastDestName.equals(e.ownText()) || lastDestName == "") {
                        lastDestName = e.ownText();
                        TextView destTextView = new TextView(this);
                        contentLayout.addView(destTextView);
                        LinearLayout.LayoutParams destParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        destParam.setMargins(15, 15, 0, 30);
                        destTextView.setTypeface(destTextView.getTypeface(), Typeface.BOLD);
                        destTextView.setTextSize(25);
                        destTextView.setText(e.ownText());
                        break;
                    } else {
                        break;
                    }

                case "date":
                    TextView dateTextView = new TextView(this);
                    contentLayout.addView(dateTextView);
                    LinearLayout.LayoutParams dateParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    dateParam.setMargins(15,15,0,30);
                    dateTextView.setLayoutParams(dateParam);
                    dateTextView.setTextSize(20);
                    dateTextView.setText(e.ownText());
                    break;

                case "text":
                    TextView newTextView = new TextView(this);
                    contentLayout.addView(newTextView);
                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParam.setMargins(15,15,15,15);
                    newTextView.setLayoutParams(textParam);
                    newTextView.setText(e.ownText());
                    newTextView.setTextSize(18);
                    newTextView.setTextColor(Color.BLACK);
                    break;

                case "image":
                    imageUrl = e.ownText();
                    final ImageView newImage = new ImageView(this);
                    LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    imageParam.setMargins(0,20,0,20);
                    imageParam.gravity = Gravity.CENTER;
                    newImage.setLayoutParams(imageParam);

                    Picasso.get()
                            .load(imageUrl)
                            .resize(500,500)
                            .centerCrop()
                            .into(newImage);
                    contentLayout.addView(newImage);
                    break;

                case "type":
                    type = e.ownText();
                    break;

                case "comment":
                    comment = e.ownText();
                    break;

                case "price":
                    price = e.ownText();
                    final View expenseCard = getLayoutInflater().inflate(R.layout.expense_cardview,null);
                    TextView expenseType = (TextView) expenseCard.findViewById(R.id.expense_type_textView);
                    TextView expenseInfo = (TextView) expenseCard.findViewById(R.id.expense_exp);
                    TextView expensePrice = (TextView) expenseCard.findViewById(R.id.expense_price_textView);
                    ImageButton closeExpenseCard = (ImageButton) expenseCard.findViewById(R.id.close_expense_card_button);

                    closeExpenseCard.setVisibility(View.INVISIBLE);
                    expenseType.setText(type);
                    expenseInfo.setText(comment);
                    expensePrice.setText(price);

                    contentLayout.addView(expenseCard);
                    LinearLayout.LayoutParams expenseParam= (LinearLayout.LayoutParams) expenseCard.getLayoutParams();
                    expenseParam.setMargins(15,15,15,15);
                    break;

                default:
                    break;
            }
        }
    }
}
