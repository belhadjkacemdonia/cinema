package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button log;
    private String mail,psword;
    private View signUp;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.loading);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        log = findViewById(R.id.login);
        signUp = findViewById(R.id.IDsignup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, singupActivity.class));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail =email.getText().toString();
                psword =password.getText().toString();

                if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    if (!psword.isEmpty()){
                        fAuth.signInWithEmailAndPassword(mail, psword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(loginActivity.this, "Login successful. ", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(loginActivity.this, MainActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(loginActivity.this, "Login Failed. ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else{
                        password.setError("password cannot be empty");
                    }
                } else if(mail.isEmpty()){
                    email.setError("mail cannot be empty");
                }else{
                    email.setError("please enter valid email");
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}