package com.example.pfemini.UI.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pfemini.R;
import com.example.pfemini.VPAdapter;
import com.example.pfemini.UI.common.profile;
import com.example.pfemini.viewpageritem;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ArrayList<viewpageritem> viewPagerItemArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.pager);
        int[] images = {R.drawable.scanqr,R.drawable.plans,R.drawable.comm,R.drawable.prof};
        String[] heading = {"Scaner le qr code","Planifier votre voyage","Communiquer avec votre chef ","Consulter votre profile","Shakes"};


        viewPagerItemArrayList = new ArrayList<>();

        for (int i =0; i< images.length ; i++) {

            viewpageritem viewPager = new viewpageritem(images[i], heading[i]);
            viewPagerItemArrayList.add(viewPager);
        }


        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);

        viewPager2.setAdapter(vpAdapter);

        viewPager2.setClipToPadding(false);

        viewPager2.setClipChildren(false);

        viewPager2.setOffscreenPageLimit(2);

        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);





















        final CircleMenuView menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                switch (index) {
                    case 0:
                        // Button 0 clicked, navigate to ActivityA
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;

                    case 1:
                        // Button 0 clicked, navigate to ActivityA
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        break;
                        case 2:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(MainActivity.this, scan.class));
                        break;
                    // Add more cases for additional buttons if needed
                    case 3:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(MainActivity.this, Plannification.class));
                        break;
                    case 4:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(MainActivity.this, stops.class));
                        break;
                    case 5:
                        // Button 1 clicked, navigate to ActivityB
                        startActivity(new Intent(MainActivity.this, profile.class));
                        break;

                        default:
                        break;
                }
            }
        });
    }}