package com.example.pfemini.UI.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.pfemini.R;
import com.example.pfemini.VPAdapter;
import com.example.pfemini.UI.common.profile;
import com.example.pfemini.viewpageritem;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ArrayList<viewpageritem> viewPagerItemArrayList;
    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gifImageView = findViewById(R.id.gifimageview);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String type = sharedPreferences.getString("type", "pickup"); // Default type is pickup

        if ("truck".equals(type)) {
            gifImageView.setImageResource(R.drawable.giphy); // Update this with your truck GIF
        } else {
            gifImageView.setImageResource(R.drawable.giphyp); // Update this with your pickup GIF
        }

        final CircleMenuView menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                switch (index) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, scan.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, Plannification.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, stops.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, profile.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
