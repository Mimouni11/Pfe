package com.example.pfemini.UI.driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Models.MatriculeResponse;
import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Network.RetrofitClient;
import com.example.pfemini.R;

import com.example.pfemini.UI.common.profile;
import com.ramotion.circlemenu.CircleMenuView;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private GifImageView gifImageView;
    private TextView matriculeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gifImageView = findViewById(R.id.gifimageview);
        matriculeTextView = findViewById(R.id.matricule_text_view);

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

        // Call the API to get matricule
        getMatricule();
    }

    private void getMatricule() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "default_username");

        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<MatriculeResponse> call = apiService.getMatricule(username);

        call.enqueue(new Callback<MatriculeResponse>() {
            @Override
            public void onResponse(Call<MatriculeResponse> call, Response<MatriculeResponse> response) {
                if (response.isSuccessful()) {
                    MatriculeResponse matriculeResponse = response.body();
                    if (matriculeResponse != null) {
                        String matricule = matriculeResponse.getMatricule();
                        if (matricule != null) {
                            matriculeTextView.setText("Matricule : "+ matricule);
                        } else {
                            matriculeTextView.setText("No tasks found for today");
                        }
                    }
                } else {
                    Log.e("MainActivity", "Error: " + response.errorBody());
                    matriculeTextView.setText("Error fetching matricule");
                }
            }

            @Override
            public void onFailure(Call<MatriculeResponse> call, Throwable t) {
                Log.e("MainActivity", "Failure: " + t.getMessage());
                matriculeTextView.setText("Error fetching matricule");
            }
        });
    }
}
