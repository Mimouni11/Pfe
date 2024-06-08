package com.example.pfemini.UI.common;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Network.RetrofitClient;
import com.example.pfemini.R;
import com.example.pfemini.Models.ResetPasswordRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private Button buttonResetPassword;
    private Apiservices apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        apiService = RetrofitClient.getClient().create(Apiservices.class);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePasswordReset();
            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
    }

    private void handlePasswordReset() {
        String email = editTextEmail.getText().toString().trim();

        if (validateInput(email)) {
            sendPasswordResetEmail(email);
        }
    }

    private boolean validateInput(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendPasswordResetEmail(String email) {
        Call<ResponseBody> call = apiService.resetPassword(new ResetPasswordRequest(email));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isFinishing() && !isDestroyed()) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Failed to send email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(ResetPasswordActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
