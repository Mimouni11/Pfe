package com.example.pfemini.mecano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.pfemini.MainActivity;
import com.example.pfemini.Plannification;
import com.example.pfemini.R;
import com.example.pfemini.profile;
import com.example.pfemini.scan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mecano_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecano_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_item1) {
                    Intent intent = new Intent(mecano_main.this, scan.class);
                    startActivity(intent);
                } else if (itemId == R.id.navigation_item2) {
                    Intent intent = new Intent(mecano_main.this, Rapport_activity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navigation_item3) {
                    Intent intent = new Intent(mecano_main.this, Tasks_activity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navigation_item4) {
                    // Handle profile item click
                    // Navigate to the profile page
                    Intent intent = new Intent(mecano_main.this, profile.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}