package com.trawell.batu.trawell.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trawell.batu.trawell.R;


public class MainActivity extends AppCompatActivity {

    private Button signUpWithMailButton,
            loginButton,
            skip_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpWithMailButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        skip_button = findViewById(R.id.login_skip_button);

        signUpWithMailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent signupActivityIntent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(signupActivityIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });

        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivityIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeActivityIntent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent homeActivityIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeActivityIntent);
            Toast.makeText(getApplicationContext(), "Already logged in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
        }
    }
}
