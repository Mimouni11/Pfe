package com.example.pfemini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class Avatar_selection extends AppCompatActivity {

    private GridView avatarGridView;
    private Button confirmButton;
    private int[] avatarList; // Your array of avatar resource IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_selection);

        avatarGridView = findViewById(R.id.avatarGridView);
        confirmButton = findViewById(R.id.confirmButton);

        // Initialize avatarList with your avatar resource IDs
        avatarList = new int[]{
                R.drawable.astronaut,
                R.drawable.avatar,
                R.drawable.chicken,
                R.drawable.man,
                R.drawable.gamer,
                R.drawable.woman,
                R.drawable.cat,
                R.drawable.office,
                R.drawable.user,
                R.drawable.daisy2
        };

        AvatarAdapter adapter = new AvatarAdapter(this, avatarList);
        avatarGridView.setAdapter(adapter);

        avatarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle avatar selection
                int selectedAvatar = avatarList[position];
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedAvatar", selectedAvatar);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirmation button click
                // If needed, add selected avatar data to the intent before setting the result
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish(); // Finish the activity and return to the previous activity
            }
        });
    }
}