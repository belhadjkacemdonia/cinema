package com.example.cinema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MyProfil extends AppCompatActivity {

    private String name, surname;
    private EditText nomM, prenomM;
    private Button edit;

    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNude;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);
        nomM = findViewById(R.id.modifnom);
        prenomM = findViewById(R.id.modifprenom);
        fAuth = FirebaseAuth.getInstance();
        edit = findViewById(R.id.modif);
        progressBar = findViewById(R.id.idprogressBar);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupération des données
                String nom = dataSnapshot.child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid().toString()).child("name").getValue(String.class);
                String prenom = dataSnapshot.child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid().toString()).child("surname").getValue(String.class);

                // Affichage des données récupérées dans les vues
                nomM.setText(nom);
                prenomM.setText(prenom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestion des erreurs
            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNude = FirebaseDatabase.getInstance();
                reference = rootNude.getReference("User");
                String currentUserId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                name = nomM.getText().toString().trim();
                surname = prenomM.getText().toString().trim();



                Modif modif = new Modif(name,surname);
//modifier les cordonnée de user
                reference.child(currentUserId).child("name").setValue(modif.getNom());
                reference.child(currentUserId).child("surname").setValue(modif.getPrenom());
                Toast.makeText(MyProfil.this, "modified with success ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyProfil.this, MainActivity.class));

            }
        });


    }
}