package com.example.pfemini;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pfemini.Adapters.DriverTaskAdapter;
import com.example.pfemini.Models.DriverTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scan extends AppCompatActivity {
    Button scaner;
    RecyclerView recyclerViewTasks;
    DriverTaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scaner = findViewById(R.id.scanner);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // Set up RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DriverTaskAdapter(new ArrayList<>(), getSharedPreferences(profile.PREFS_NAME, MODE_PRIVATE));
        recyclerViewTasks.setAdapter(adapter);

        scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(scan.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("scanner");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {
                // Send the scanned content to the server
                sendScannedContentToServer(contents);
            }
        }
    }

    private void sendScannedContentToServer(String content) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<List<DriverTask>> call = apiService.getTasksForScannedContent(content);
        call.enqueue(new Callback<List<DriverTask>>() {
            @Override
            public void onResponse(Call<List<DriverTask>> call, Response<List<DriverTask>> response) {
                if (response.isSuccessful()) {
                    List<DriverTask> tasks = response.body();
                    if (tasks != null && !tasks.isEmpty()) {
                        // Display the tasks
                        displayTasks(tasks);
                    } else {
                        // No tasks found for the scanned content
                        Toast.makeText(scan.this, "No tasks found for the scanned content", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(scan.this, "Failed to get tasks from the server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DriverTask>> call, Throwable t) {
                // Handle failure
                Toast.makeText(scan.this, "Failed to communicate with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTasks(List<DriverTask> tasks) {
        // Add tasks to the RecyclerView adapter
        adapter.setTaskList(tasks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
