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

import com.example.pfemini.Models.DriverTask;
import com.example.pfemini.R;

import java.util.List;

public class DriverTaskAdapter extends RecyclerView.Adapter<DriverTaskAdapter.TaskViewHolder> {

    private List<DriverTask> taskList;
    private SharedPreferences prefs;

    public DriverTaskAdapter(List<DriverTask> taskList, SharedPreferences preferences) {
        this.taskList = taskList;
        this.prefs = preferences;
    }

    public void setTaskList(List<DriverTask> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        DriverTask task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkboxTask;
        private TextView taskTextView;
        private TextView matriculeTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxTask = itemView.findViewById(R.id.checkboxTask);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            matriculeTextView = itemView.findViewById(R.id.matriculeTextView);

            checkboxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DriverTask task = taskList.get(position);
                        task.setSelected(isChecked);
                    }
                }
            });
        }

        public void bind(DriverTask task) {
            taskTextView.setText(task.getTask());
            matriculeTextView.setText(task.getMatricule());
            checkboxTask.setChecked(task.isSelected()); // Set checkbox state
        }
    }
}
