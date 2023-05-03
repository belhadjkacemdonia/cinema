package com.example.cinema;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinema.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth fAuth;
    FirebaseUser currentUser ;
    String nom,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        if (fAuth.getCurrentUser() !=null) {
            View headerView = navigationView.getHeaderView(0);
            TextView name = (TextView) headerView.findViewById(R.id.nameid);
            TextView mail = (TextView) headerView.findViewById(R.id.mailid);
            mail.setText(currentUser.getEmail());
            name.setText(currentUser.getDisplayName());
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem id = menu.findItem(R.id.idLogin);
        MenuItem id3 = menu.findItem(R.id.Profil);
        MenuItem id1 = menu.findItem(R.id.Liste);
        MenuItem id2 = menu.findItem(R.id.logout);
        if (fAuth.getCurrentUser() !=null) {
            id.setVisible(false);
            id1.setVisible(true);
            id2.setVisible(true);
            id3.setVisible(true);
            }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int id1 = item.getItemId();
        int id2 = item.getItemId();
        int id3 = item.getItemId();
        if (id == R.id.idLogin) {
            Intent intent= new Intent(MainActivity.this, loginActivity.class);
            startActivity(intent);
            return true;
        }
        if (fAuth.getCurrentUser() !=null) {
            if (id1 == R.id.logout) {
                fAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Login out successful.", Toast.LENGTH_SHORT).show();
                return true;
            }}
        if (id2 == R.id.Liste) {
            startActivity(new Intent(MainActivity.this, ListeActivity.class));
            Toast.makeText(getApplicationContext(), "Liste !!!.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id3 == R.id.Profil) {
            startActivity(new Intent(MainActivity.this, MyProfil.class));
            Toast.makeText(getApplicationContext(), "My Profil !!!.", Toast.LENGTH_SHORT).show();
            return true;
        }



        return false;
    }

}