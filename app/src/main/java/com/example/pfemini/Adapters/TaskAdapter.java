package com.example.pfemini.Adapters;

import android.content.SharedPreferences;
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
import com.example.pfemini.R;

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
        holder.textViewModel.setText(task.getModel()); // Set model text
        holder.textViewMatricule.setText(task.getMatricule()); // Set matricule text

        // Get the saved state of the checkbox from SharedPreferences
        boolean isChecked = sharedPreferences.getBoolean(task.getName(), false);
        holder.checkboxTask.setChecked(isChecked);

        holder.checkboxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setCompleted(isChecked);

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

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskName;
        TextView textViewModel;
        TextView textViewMatricule;
        CheckBox checkboxTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            textViewModel = itemView.findViewById(R.id.textViewModel);
            textViewMatricule = itemView.findViewById(R.id.textViewMatricule);
            checkboxTask = itemView.findViewById(R.id.checkboxTask);
        }
    }
}
