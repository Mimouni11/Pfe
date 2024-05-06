package com.example.pfemini;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Chef.chefActivity;
import com.example.pfemini.mecano.mecano_main;

public class loading extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000; // Duration of splash screen in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView image = findViewById(R.id.imageView);
        TextView logo = findViewById(R.id.textView);
        TextView slogan = findViewById(R.id.textView2);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Set animation to elements
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is already logged in
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String role = sharedPreferences.getString("role", "");

                Intent intent = new Intent(loading.this, login.class); // Initialize with login activity

                if (!username.isEmpty()) {
                    // User is already logged in
                    if ("driver".equals(role)) {
                        // Redirect admin users to Admin_activity
                        intent = new Intent(loading.this, MainActivity.class);
                    } else if ("mecano".equals(role)) {
                        // Redirect other users to MainActivity
                        intent = new Intent(loading.this, mecano_main.class);
                    }else if ("chef".equals(role)){
                        intent=new Intent(loading.this, chefActivity.class);
                    }
                }

// Attach all the elements those you want to animate in design
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<>(image, "logo_image");
                pairs[1] = new Pair<>(logo, "logo_text");
                pairs[2] = new Pair<>(slogan, "logo_slogan");

// Wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(loading.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }

                finish();
            }

        }, SPLASH_DURATION);
    }
}
