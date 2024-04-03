package com.example.pfemini.mecano;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Apiservices;
import com.example.pfemini.R;
import com.example.pfemini.RetrofitClient;
import com.example.pfemini.profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tasks_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        SharedPreferences prefs = getSharedPreferences(profile.PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("username", "");
        fetchTasksForCurrentUser(username);
    }

    private void fetchTasksForCurrentUser(String username) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<List<Task>> call = apiService.getTasks(username);
        Log.d("TasksActivity", "Fetching tasks for username: " + username);

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    List<Task> tasks = response.body();
                    StringBuilder tasksText = new StringBuilder();
                    Log.d("aaa", "onResponse: ");
                    if (tasks != null) {
                        for (Task task : tasks) {
                            tasksText.append(task.getName()).append("\n");
                        }
                    }
                    Log.d("tasks", tasksText.toString());
                    // Set the tasks in the TextView
                    runOnUiThread(() -> {
                        TextView tasksTextView = findViewById(R.id.tasksTextView);
                        tasksTextView.setText(tasksText.toString());
                    });
                } else {
                    // Handle unsuccessful response
                }
                Log.d("TasksActivity", "Retrofit call enqueued");
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
