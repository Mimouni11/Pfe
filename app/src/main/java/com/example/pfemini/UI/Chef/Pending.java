package com.example.pfemini.UI.Chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pfemini.Adapters.PendingTaskAdapter;
import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Models.Task;
import com.example.pfemini.R;
import com.example.pfemini.Network.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pending extends AppCompatActivity {
    private RecyclerView recyclerViewPendingTasks;
    private PendingTaskAdapter adapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        // Set up RecyclerView
        recyclerViewPendingTasks = findViewById(R.id.recyclerViewPendingTasks);
        recyclerViewPendingTasks.setLayoutManager(new LinearLayoutManager(this));

        // Initialize task list and adapter
        taskList = new ArrayList<>();
        adapter = new PendingTaskAdapter(taskList, this::confirmTask);
        recyclerViewPendingTasks.setAdapter(adapter);

        // Fetch all pending tasks
        fetchPendingTasks();
    }

    private void fetchPendingTasks() {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<JsonObject> call = apiService.getPendingTasks();
        Log.d("PendingTasksActivity", "Fetching all pending tasks");

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
                            JsonObject taskObj = taskElement.getAsJsonObject();
                            String mechanicName = taskObj.get("mechanic").getAsString();
                            String taskName = taskObj.get("task").getAsString();
                            Task task = new Task(taskName,"","");
                            task.setMechanicName(mechanicName);
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

    private void confirmTask(Task task) {
        // Implement the logic to confirm the task
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<Void> call = apiService.confirmTask(task.getName());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Task confirmation successful
                    Toast.makeText(Pending.this, "Task confirmed: " + task.getName(), Toast.LENGTH_SHORT).show();
                    // Optionally, you can remove the confirmed task from the list
                    taskList.remove(task);
                    adapter.notifyDataSetChanged(); // Update RecyclerView
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(Pending.this, "Failed to confirm task: " + task.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Toast.makeText(Pending.this, "Error confirming task: " + task.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}