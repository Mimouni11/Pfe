package com.example.pfemini.UI.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.R;
import com.example.pfemini.UI.driver.MainActivity;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Display the splash screen for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the splash screen
                Intent intent = new Intent(loading.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
