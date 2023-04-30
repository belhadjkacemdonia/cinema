package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class singupActivity extends AppCompatActivity {
    private EditText nom,prenom,email,pasword;
    private View log;
    private Button erg;
    private String name, subname, mail, password;
    FirebaseAuth fAuth;
    DatabaseReference reference;
    FirebaseDatabase rootNude;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        email = findViewById(R.id.idEmail);
        pasword = findViewById(R.id.iDpassword);
        log = findViewById(R.id.idlog);
        erg = findViewById(R.id.enreg);

        fAuth = FirebaseAuth.getInstance();

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(singupActivity.this, loginActivity.class));
            }
        });

        erg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNude = FirebaseDatabase.getInstance();
                reference = rootNude.getReference("User");
                name = nom.getText().toString().trim();
                subname = prenom.getText().toString().trim();
                mail = email.getText().toString().trim();
                password = pasword.getText().toString().trim();


                UserEnreg userEnreg = new UserEnreg(name, subname, mail);



                if (TextUtils.isEmpty(name)){
                    nom.setError("Veuillez entrer votre nom");
                    return;
                }

                if (TextUtils.isEmpty(subname)){
                    prenom.setError("Veuillez entrer votre prenom");
                    return;
                }

                if (TextUtils.isEmpty(mail)){
                    email.setError("Veuillez entrer votre mail");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    pasword.setError("Veuillez entrer votre mot de passe");
                    return;
                }

                //ATH FIRE BASE
                fAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String currentUserId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            reference.child(currentUserId).setValue(userEnreg);
                            Toast.makeText(singupActivity.this, "User Created. ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(singupActivity.this, loginActivity.class));


                        }else {Toast.makeText(singupActivity.this, "User Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }



                    }
                });
            }
        });
    }
}