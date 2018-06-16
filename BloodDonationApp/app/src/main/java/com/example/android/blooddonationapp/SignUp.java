package com.example.android.blooddonationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        Button CreateAccountButton = (Button)findViewById(R.id.create_account);
        final EditText Musername = (EditText)findViewById(R.id.username);
        final EditText Mpassword = (EditText)findViewById(R.id.password);
        final EditText MconfirmPassword = (EditText)findViewById(R.id.confirm_password);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                final String email = Musername.getText().toString();
                final String password = Mpassword.getText().toString();
                final String confirmPassword = MconfirmPassword.getText().toString();

                Log.d("T",email);

                if( (!email.isEmpty()) && (!password.isEmpty()) && (!confirmPassword.isEmpty()) && password.equals(confirmPassword))
                {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                          FirebaseUser user = mAuth.getCurrentUser();

                                           user.sendEmailVerification()
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                      public void onComplete(@NonNull Task<Void> task) {
                                                         if (task.isSuccessful()) {
                                                             Log.d("R", "Email sent.");
                                                         }
                                                      }
                                                 });
                                           Log.d("TAG","##########");
                                           Intent intent = new Intent(SignUp.this,MainActivity.class);
                                           startActivity(intent);
                                           finish();
                                    } else {

                                        Toast.makeText(SignUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    MconfirmPassword.setError("Password Didn't Match");
                }


            }
        });


    }
}
