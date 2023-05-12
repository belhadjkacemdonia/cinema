package com.example.cinema;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class film extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private Button Btn;
    FirebaseAuth fAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        imageView = findViewById(R.id.imageView);
        Btn = findViewById(R.id.button);
        fAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            // Récupérer les détails du film depuis les données supplémentaires de l'intent
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String imageUrl = intent.getStringExtra("imageUrl");

            titleTextView.setText("TITRE : " + title);
            descriptionTextView.setText("Description : " + description);

            // Charger l'image du film à l'aide de la bibliothèque Glide
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

        String currentUserId = fAuth.getInstance().getCurrentUser().getUid();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        resFilm resfilm = new resFilm(title, description);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enregistrer la réservation du film dans la base de données Firebase Realtime Database
                FirebaseDatabase.getInstance().getReference("reservation").child(currentUserId).push().setValue(resfilm);
                startActivity(new Intent(film.this, MainActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Effectuer une recherche web pour les critiques du film
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, title + " Review ");
                startActivity(intent);
            }
        });
    }
}
