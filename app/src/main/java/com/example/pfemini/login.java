package com.example.pfemini;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Chef.chefActivity;
import com.example.pfemini.Models.LoginRequest;
import com.example.pfemini.Models.LoginResponse;
import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Network.RetrofitClient;
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
        initViews();
        apiService = RetrofitClient.getClient().create(Apiservices.class);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void initViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
    }

    private void handleLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (validateInput(username, password)) {
            loginUser(username, password);
        }
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loginUser(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<LoginResponse> call = apiService.authenticateUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleLoginResponse(response.body(), username);
                } else {
                    Toast.makeText(login.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(login.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginResponse(LoginResponse loginResponse, String username) {
        String status = loginResponse.getStatus();
        String message = loginResponse.getMessage();
        String role = loginResponse.getRole();

        if ("success".equals(status)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            saveUserData(username, role);
            navigateToRoleSpecificActivity(role);
            finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(String username, String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("role", role);
        editor.apply();
    }

    private void navigateToRoleSpecificActivity(String role) {
        switch (role) {
            case "mecano":
                startActivity(new Intent(this, mecano_main.class));
                saveDeviceToken("mecano");
                break;
            case "driver":
                startActivity(new Intent(this, MainActivity.class));
                saveDeviceToken("driver");
                break;
            case "chef":
                startActivity(new Intent(this, chefActivity.class));
                saveDeviceToken("chef");
                break;
            default:
                Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveDeviceToken(String role) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            String deviceToken = task.getResult();
            String username = editTextUsername.getText().toString().trim();
            Call<Void> call = null;

            switch (role) {
                case "mecano":
                    call = apiService.saveMecanoDeviceToken(username, deviceToken);
                    break;
                case "driver":
                    call = apiService.saveDriverDeviceToken(username, deviceToken);
                    break;
                case "chef":
                    call = apiService.saveChefDeviceToken(username, deviceToken);
                    break;
            }

            if (call != null) {
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(login.this, "Device token saved for " + role, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, "Failed to save device token for " + role, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(login.this, "Failed to save device token for " + role + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
