package com.example.pfemini.Chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pfemini.R;
import com.example.pfemini.profile;
import com.ramotion.circlemenu.CircleMenuView;

public class chefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);



        final CircleMenuView menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                switch (index) {
                    case 0:
                        // Button 0 clicked, navigate to ActivityA
                        startActivity(new Intent(chefActivity.this, chefActivity.class));
                        break;
                    case 1:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(chefActivity.this, LocationActivity.class));
                        break;
                    // Add more cases for additional buttons if needed
                    case 2:
                        startActivity(new Intent(chefActivity.this, Pending.class));

                        break;
                    case 3:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(chefActivity.this, profile.class));
                        break;
                    case 4:
                        // Button 1 clicked, navigate to ActivityB
                       startActivity(new Intent(chefActivity.this, NotificationActivity.class));
                        break;

                    default:
                        break;
                }
            }
        });

    }
}