package com.example.pfemini.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pfemini.Models.Task;
import com.example.pfemini.R;
import java.util.List;

public class PendingTaskAdapter extends RecyclerView.Adapter<PendingTaskAdapter.ViewHolder> {
    private List<Task> taskList;
    private OnTaskConfirmListener confirmListener;

    public PendingTaskAdapter(List<Task> taskList, OnTaskConfirmListener confirmListener) {
        this.taskList = taskList;
        this.confirmListener = confirmListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        Log.d("PendingTaskAdapter", "Task Name: " + task.getName());
        Log.d("PendingTaskAdapter", "Matricule: " + task.getMatricule());
        Log.d("PendingTaskAdapter", "Task Type: " + task.getTaskType());
        Log.d("PendingTaskAdapter", "Mechanic Name: " + task.getMechanicName());

        holder.taskName.setText(task.getName());
        holder.matricule.setText(task.getMatricule());
        holder.taskType.setText(task.getTaskType());
        holder.mechanicName.setText(task.getMechanicName());

        holder.itemView.findViewById(R.id.confirmButton).setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onConfirm(task);
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, matricule, taskType, mechanicName;
        Button confirmButton; // Initialize confirmButton

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.TaskName);
            matricule = itemView.findViewById(R.id.Matricule);
            taskType = itemView.findViewById(R.id.TaskType);
            mechanicName = itemView.findViewById(R.id.MechanicName);
            confirmButton = itemView.findViewById(R.id.confirmButton); // Reference confirmButton
        }
    }

    public interface OnTaskConfirmListener {
        void onConfirm(Task task);
    }
}
