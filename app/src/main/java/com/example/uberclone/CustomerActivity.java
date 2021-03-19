package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerActivity extends AppCompatActivity {

    private Button logInButton, signInButton;
    private EditText passwordTextView, emailTextView;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(getApplicationContext(),MapActivity.class));
                    finish();
                }
            }
        };

        logInButton = findViewById(R.id.logInButtonId);
        signInButton = findViewById(R.id.signInButtonId);
        passwordTextView = findViewById(R.id.passwordTextViewId);
        emailTextView = findViewById(R.id.emailTextViewId);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String email = emailTextView.getText().toString().trim();
                final String password = passwordTextView.getText().toString().trim();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(CustomerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MapActivity.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Log In Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String email = emailTextView.getText().toString().trim();
                final String password = passwordTextView.getText().toString().trim();
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CustomerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = auth.getCurrentUser().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child("Customers").child(userId);
                            databaseReference.setValue(true);
                        }else{
                            Toast.makeText(getApplicationContext(),"Sign Up Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

}