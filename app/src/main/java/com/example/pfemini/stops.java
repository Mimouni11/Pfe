package com.example.pfemini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.circlemenu.CircleMenuView;

public class stops extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        // Retrieve data from Intent extras
        Intent intent = getIntent();
        if (intent != null) {
            int childCount = intent.getIntExtra("childCount", 0); // Retrieve the total number of EditText fields
            StringBuilder displayText = new StringBuilder("Data from previous activity:\n");
            for (int i = 0; i < childCount; i++) {
                String editTextValue = intent.getStringExtra("editTextValue" + i);
                displayText.append("EditText Value ").append(i + 1).append(": ").append(editTextValue).append("\n");
            }

            // Display the received data in a TextView
            TextView textView = findViewById(R.id.textView);
            textView.setText(displayText.toString());
        }

        // Display icons

        final CircleMenuView menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                switch (index) {
                    case 0:
                        // Button 0 clicked, navigate to ActivityA
                        startActivity(new Intent(stops.this, FuelingAct.class));
                        break;
                    case 1:
                        // Button 1 clicked, navigate to ActivityB
                        // Add your navigation code here
                        break;
                    default:
                        break;
                }
            }
        }); // Add this closing parenthesis
    }
}
