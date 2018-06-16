package com.example.android.blooddonationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AfterLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Button logoutButton = (Button)findViewById(R.id.log_out);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Logout feature-----------------------------------------------------------------------------------------
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(AfterLogin.this,MainActivity.class));
                }
            }
        };

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                mAuth.signOut();
                Intent intent = new Intent(AfterLogin.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //Logout feature ends-------------------------------------------------------------------------------------
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
