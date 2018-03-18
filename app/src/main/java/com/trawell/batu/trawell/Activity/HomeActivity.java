package com.trawell.batu.trawell.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trawell.batu.trawell.R;

public class HomeActivity extends AppCompatActivity {

    public Button signoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signoutButton = findViewById(R.id.signout_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            signoutButton.setVisibility(View.INVISIBLE);
        }

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Signed Out!", Toast.LENGTH_SHORT).show();
                Intent mainActivityIntent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });

    }
}
