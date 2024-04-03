package com.example.pfemini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        // Set click listeners for the icons
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action for icon1 (rest icon)
                // Call the Maps API to suggest a resting location
                // Example: You can start a new activity where the user selects two locations
                // Then, you can calculate a midpoint between these locations and suggest it as a resting location
                Intent intent = new Intent(stops.this, FuelingAct.class);
                startActivity(intent);
            }
        });

        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action for icon2
                // For example, display a message or perform an action
                Toast.makeText(stops.this, "Icon 2 clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

