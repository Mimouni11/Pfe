package com.example.pfemini.UI.Mecano;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfemini.Adapters.TaskAdapter;
import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Models.Task;
import com.example.pfemini.R;
import com.example.pfemini.Network.RetrofitClient;
import com.example.pfemini.UI.common.profile;
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

        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            for (Task task : taskList) {
                if (task.isCompleted()) {
                    // Call the endpoint to update task status
                    updateTaskStatus(task.getName(), "yes");
                }
            }
        });
    }


    private void updateTaskStatus(String taskName, String status) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        SharedPreferences prefs = getSharedPreferences(profile.PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("username", "");

        Call<Void> call = apiService.updateTaskStatus(username, taskName, status);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    sendNotificationToChef(taskName);
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }


    private void sendNotificationToChef(String taskName) {
        // Get Retrofit instance
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        String username = "kaka";
        String title = "task done";
        // Get username from SharedPreferences

        SharedPreferences prefs = getSharedPreferences(profile.PREFS_NAME, MODE_PRIVATE);
        String mecano = prefs.getString("username", "");

        // Make network request to send notification to chef
        Call<Void> call = apiService.sendNotification(username, title, taskName, mecano);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Notification sent successfully
                    // You can handle any success logic here
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void fetchTasksForCurrentUser(String username) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<JsonObject> call = apiService.getTasks(username);
        Log.d("TasksActivity", "Fetching tasks for username: " + username);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null && jsonObject.has("tasks")) {
                        JsonArray tasksArray = jsonObject.getAsJsonArray("tasks");

                        taskList.clear();

                        for (JsonElement taskElement : tasksArray) {
                            JsonObject taskObject = taskElement.getAsJsonObject();
                            String taskName = taskObject.get("task").getAsString();
                            String model = taskObject.get("model").getAsString();
                            String matricule = taskObject.get("matricule").getAsString();
                            String taskType = taskObject.get("taskType").getAsString();

                            Task task = new Task(taskName, model, matricule, taskType);
                            taskList.add(task);
                        }

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }






}