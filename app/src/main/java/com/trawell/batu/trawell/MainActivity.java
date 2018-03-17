package com.trawell.batu.trawell;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button signUpWithMailButton,
                   loginButton;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpWithMailButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);

        signUpWithMailButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent signupActivityIntent = new Intent(view.getContext(),SignUpActivity.class);
                startActivity(signupActivityIntent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(view.getContext(),LoginActivity.class);
                startActivity(loginActivityIntent);
                finish();
            }
        });

    }
}
