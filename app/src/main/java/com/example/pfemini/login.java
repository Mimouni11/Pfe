package com.example.pfemini;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pfemini.Chef.chefActivity;
import com.example.pfemini.driver.MainActivity;
import com.example.pfemini.mecano.mecano_main;
import com.google.firebase.messaging.FirebaseMessaging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private Apiservices apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);


        apiService = RetrofitClient.getClient().create(Apiservices.class);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password from EditText fields
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Call the API service method to authenticate user
                Call<LoginResponse> call = apiService.authenticateUser(username, password);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            // Handle successful response
                            LoginResponse loginResponse = response.body();
                            String status = loginResponse.getStatus();
                            String message = loginResponse.getMessage();
                            String role = loginResponse.getRole(); // Assuming you get the role from the server

                            if (status.equals("success")) {
                                // Authentication successful
                                Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                                // Save the username in shared preferences
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("role", role);
                                editor.apply();

                                if ("mecano".equals(role)) {
                                    Intent intent = new Intent(login.this, mecano_main.class);
                                    startActivity(intent);
                                    savemecanoToken();
                                } else if ("driver".equals(role)) {
                                    saveDriverToken();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                } else if ("chef".equals(role)) {
                                    saveChefDeviceToken();
                                    Intent intent = new Intent(login.this, chefActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Handle unknown role
                                    Toast.makeText(login.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            } else {
                                // Authentication failed
                                Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }}
                            @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Handle network errors
                        Toast.makeText(login.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    private void saveChefDeviceToken() {
        // Retrieve the device token from Firebase Cloud Messaging
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String deviceToken = task.getResult();
                    String username = editTextUsername.getText().toString().trim();
                    Log.d("token",deviceToken);                   // Get new FCM registration token

                    // Now you can save the device token to your backend server or database
                    // Example: Call an API to save the device token for the logged-in chef
                    Call<Void> call = apiService.saveChefDeviceToken(username, deviceToken);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Device token saved successfully
                                Toast.makeText(login.this, "Device token saved for chef", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to save device token
                                Toast.makeText(login.this, "Failed to save device token for chef", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Handle failure to make API call
                            Toast.makeText(login.this, "Failed to save device token for chef: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
    }

    private void saveDriverToken() {
        // Retrieve the device token from Firebase Cloud Messaging
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String deviceToken = task.getResult();
                    String username = editTextUsername.getText().toString().trim();
                    Log.d("token",deviceToken);                   // Get new FCM registration token

                    // Now you can save the device token to your backend server or database
                    // Example: Call an API to save the device token for the logged-in chef
                    Call<Void> call = apiService.saveChefDeviceTokenDriver(username, deviceToken);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Device token saved successfully
                                Toast.makeText(login.this, "Device token saved for chef", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to save device token
                                Toast.makeText(login.this, "Failed to save device token for chef", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Handle failure to make API call
                            Toast.makeText(login.this, "Failed to save device token for chef: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
    }




    private void savemecanoToken() {
        // Retrieve the device token from Firebase Cloud Messaging
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String deviceToken = task.getResult();
                    String username = editTextUsername.getText().toString().trim();
                    Log.d("token",deviceToken);                   // Get new FCM registration token

                    // Now you can save the device token to your backend server or database
                    // Example: Call an API to save the device token for the logged-in chef
                    Call<Void> call = apiService.saveChefDeviceTokenmecano(username, deviceToken);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Device token saved successfully
                                Toast.makeText(login.this, "Device token saved for chef", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to save device token
                                Toast.makeText(login.this, "Failed to save device token for chef", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Handle failure to make API call
                            Toast.makeText(login.this, "Failed to save device token for chef: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
    }

}





