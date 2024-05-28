package com.example.pfemini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.pfemini.UI.common.profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_item1) {
                    Intent intent = new Intent(Admin_activity.this, Gere_utlisateur.class);
                    startActivity(intent);
                } else if (itemId == R.id.navigation_item2) {
                    // Handle navigation item 2 click
                } else if (itemId == R.id.navigation_item3) {
                    // Handle navigation item 3 click
                } else if (itemId == R.id.navigation_item4) {
                    // Handle profile item click
                    // Navigate to the profile page
                    Intent intent = new Intent(Admin_activity.this, profile.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }
}