package com.example.pfemini.mecano;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfemini.Adapters.TaskAdapter;
import com.example.pfemini.Apiservices;
import com.example.pfemini.Models.Task;
import com.example.pfemini.R;
import com.example.pfemini.RetrofitClient;
import com.example.pfemini.profile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tasks_activity extends AppCompatActivity {
    private RecyclerView recyclerViewTasks;
    private TaskAdapter adapter;
    private List<Task> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Retrieve username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(profile.PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("username", "");

        // Set up title with username
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(getString(R.string.title_tasks, username));

        // Fetch tasks for the current user
        fetchTasksForCurrentUser(username);

        // Retrieve SharedPreferences

// Set up RecyclerView
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

// Initialize task list and adapter
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, prefs); // Pass SharedPreferences to the constructor
        recyclerViewTasks.setAdapter(adapter);

    }
    private void fetchTasksForCurrentUser(String username) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<JsonObject> call = apiService.getTasks(username);
        Log.d("TasksActivity", "Fetching tasks for username: " + username);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Tasks fetched successfully
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null && jsonObject.has("tasks")) {
                        JsonArray tasksArray = jsonObject.getAsJsonArray("tasks");

                        // Clear existing task list
                        taskList.clear();

                        // Iterate through tasksArray and add them to taskList
                        for (JsonElement taskElement : tasksArray) {
                            String taskName = taskElement.getAsString();
                            Task task = new Task(taskName);
                            taskList.add(task);
                        }

                        // Notify adapter that data has changed
                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        // Handle empty or missing tasks
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle failure
            }
        });
    }


}