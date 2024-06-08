package com.example.pfemini.UI.common;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pfemini.Models.StatusRequest;
import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Network.RetrofitClient;
import com.example.pfemini.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity {

    private static final int AVATAR_SELECTION_REQUEST_CODE = 1;
    public static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SELECTED_AVATAR = "selectedAvatar";

    private Button modifyButton;
    private ImageView profileIcon;
    private int selectedAvatarResourceId;
    public static final String SESSION_USERNAME = "sessionUsername";

    private String username;

    private Apiservices apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        apiService = RetrofitClient.getClient().create(Apiservices.class);

        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        profileIcon = findViewById(R.id.profileIcon);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedAvatarResourceId = prefs.getInt(KEY_SELECTED_AVATAR, R.drawable.man);
        profileIcon.setImageResource(selectedAvatarResourceId);

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        username = prefs.getString("username", "");
        usernameTextView.setText(username);

        TextView modifyPasswordText = findViewById(R.id.modifyPasswordText);
        modifyPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, Passwordmodif.class);
                intent.putExtra(SESSION_USERNAME, username);
                startActivity(intent);
            }
        });

        TextView logoutText = findViewById(R.id.logoutText);
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username != null && !username.isEmpty()) {
                    updateStatus(username, "inactive");
                }

                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.remove("username");
                editor.apply();

                Intent intent = new Intent(profile.this, login.class);
                startActivity(intent);

                finish();
            }
        });

        modifyButton = findViewById(R.id.modifyProfileButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, Avatar_selection.class);
                startActivityForResult(intent, AVATAR_SELECTION_REQUEST_CODE);
            }
        });

        // Initialize apiservices here, e.g.:
        // apiservices = RetrofitClientInstance.getRetrofitInstance().create(Apiservices.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AVATAR_SELECTION_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("selectedAvatar")) {
                selectedAvatarResourceId = data.getIntExtra("selectedAvatar", R.drawable.gamer);
                profileIcon.setImageResource(selectedAvatarResourceId);

                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt(KEY_SELECTED_AVATAR, selectedAvatarResourceId);
                editor.apply();
            }
        }
    }

    private void updateStatus(String username, String status) {
        StatusRequest statusRequest = new StatusRequest(username, status);

        Call<Void> call = apiService.updateStatus(statusRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User status set to " + status);
                } else {
                    Log.e(TAG, "Failed to set user status to " + status);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error setting user status to " + status + ": " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
