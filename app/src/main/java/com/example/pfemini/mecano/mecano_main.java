package com.example.pfemini.mecano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pfemini.R;
import com.example.pfemini.profile;
import com.ramotion.circlemenu.CircleMenuView;

public class mecano_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecano_main);
        final CircleMenuView menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                switch (index) {
                    case 0:
                        // Button 0 clicked, navigate to ActivityA

                        break;
                    case 1:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(mecano_main.this, Tasks_activity.class));
                        break;
                    // Add more cases for additional buttons if needed
                    case 2:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(mecano_main.this, Rapport_activity.class));
                        break;
                    case 3:
                        // Button 1 clicked, navigate to ActivityB

                        break;
                    case 4:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(mecano_main.this, profile.class));

                        break;

                    default:
                        break;
                }
            }
        });
    }}