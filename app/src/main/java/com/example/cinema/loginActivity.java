package com.example.cinema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button log;
    private String mail, psword;
    private View signUp;
    private CheckBox rememberMe;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login_shared_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "remember_me";

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
        rememberMe = findViewById(R.id.rememberMe);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        boolean isRememberMeChecked = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        rememberMe.setChecked(isRememberMeChecked);

        if (isRememberMeChecked) {
            String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            email.setText(savedEmail);
            password.setText(savedPassword);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, singupActivity.class));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = email.getText().toString();
                psword = password.getText().toString();

                if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    if (!psword.isEmpty()) {
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

                        // Save login details if remember me is checked
                        if (rememberMe.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_EMAIL, mail);
                            editor.putString(KEY_PASSWORD, psword);
                            editor.putBoolean(KEY_REMEMBER_ME, true);
                            editor.apply();
                        } else {
                            // Clear login details from SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                        }

                    } else {
                        password.setError("password cannot be empty");
                    }
                } else if (mail.isEmpty()) {
                    email.setError("mail cannot be empty");
                } else {
                    email.setError("please enter valid email");
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}
