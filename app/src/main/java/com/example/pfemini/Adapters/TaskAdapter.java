package com.example.pfemini.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.pfemini.Models.Task;
import com.example.pfemini.Apiservices;
import com.example.pfemini.R;
import com.example.pfemini.RetrofitClient;
import com.example.pfemini.profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private SharedPreferences sharedPreferences;

    public TaskAdapter(List<Task> taskList, SharedPreferences sharedPreferences) {
        this.taskList = taskList;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTaskName.setText(task.getName());

        // Get the saved state of the checkbox from SharedPreferences
        boolean isChecked = sharedPreferences.getBoolean(task.getName(), false);
        holder.checkboxTask.setChecked(isChecked);

        holder.checkboxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setCompleted(isChecked);
                updateTaskStatus(task.getName(), isChecked ? "yes" : "no", holder.itemView);

                // Save the state of the checkbox in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(task.getName(), isChecked);
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void updateTaskStatus(String taskName, String status, View itemView) {
        // Get Retrofit instance
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);

        // Get username from SharedPreferences
        SharedPreferences prefs = itemView.getContext().getSharedPreferences(profile.PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("username", "");

        // Make network request to update task status
        Call<Void> call = apiService.updateTaskStatus(username, taskName, status);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Task status updated successfully
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

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskName;
        CheckBox checkboxTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            checkboxTask = itemView.findViewById(R.id.checkboxTask);
        }
    }
}
