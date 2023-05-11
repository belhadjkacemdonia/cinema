package com.example.cinema;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListeActivity extends AppCompatActivity {
    FirebaseDatabase rootNude;
    FirebaseAuth fAuth;
    private TextView liste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        liste=findViewById(R.id.text);
        String currentUserId =fAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference referance= FirebaseDatabase.getInstance().getReference().child("reservation")
                .child(currentUserId);
        referance.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if any child exists under the "reservation" node for the user
                if (snapshot.exists() && snapshot.hasChildren()) {
                    StringBuilder stringBuilder = new StringBuilder();

                    // Iterate over the child snapshots
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String Title = childSnapshot.child("title").getValue().toString();
                        String Description = childSnapshot.child("description").getValue().toString();

                        stringBuilder.append("Title: ").append(Title).append("\n");
                        stringBuilder.append("Description: ").append(Description).append("\n\n");
                    }
//afficher StringBuilder dans textview
                    liste.setText(stringBuilder.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


    }
}