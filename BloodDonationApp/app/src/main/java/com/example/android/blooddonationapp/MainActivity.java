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


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    SignInButton button;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        button = (SignInButton)findViewById(R.id.sign_in_button);
        Button performLoginButton = (Button)findViewById(R.id.perform_login);
        final EditText Memail = (EditText)findViewById(R.id.login_username);
        final EditText Mpassword = (EditText)findViewById(R.id.login_password);
        Button signupbutton = (Button)findViewById(R.id.sign_up_id);


        //-----------------------------------------------------------------------------------------------------------------------
        //email and password login starts here
        performLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                final String email = Memail.getText().toString();
                final String password = Mpassword.getText().toString();

                if((!email.isEmpty()) && (!password.isEmpty()))
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intend = new Intent(MainActivity.this,AfterLogin.class);
                                        startActivity(intend);

                                    } else {

                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                        Toast.makeText(MainActivity.this,"Enter Login Details.",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //email and password login ends here
        //-----------------------------------------------------------------------------------------------------------------------

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });


        //-----------------------------------------------------------------------------------------------------------------------
        //Google+ login starts here
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this,AfterLogin.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        }


        private void signIn(){
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent,RC_SIGN_IN);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                Toast.makeText(MainActivity.this,"Auth Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user =mAuth.getCurrentUser();
                                //updateUI(user);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Auth failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

        //Google+ sign in end here
        //-----------------------------------------------------------------------------------------------------------------------


    //part of Google+ login
    @Override
    protected void onStart() {

        super.onStart();
        Log.d("TAG","##############");
        mAuth.addAuthStateListener(mAuthListener);
    }
    //part of google+ login ends here
}
