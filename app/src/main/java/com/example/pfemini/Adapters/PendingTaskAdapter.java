package com.example.pfemini.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfemini.Models.Task;
import com.example.pfemini.R;

import java.util.List;

public class PendingTaskAdapter extends RecyclerView.Adapter<PendingTaskAdapter.PendingTaskViewHolder> {

    private List<Task> taskList;
    private OnConfirmClickListener onConfirmClickListener;

    public PendingTaskAdapter(List<Task> taskList, OnConfirmClickListener onConfirmClickListener) {
        this.taskList = taskList;
        this.onConfirmClickListener = onConfirmClickListener;
    }

    @NonNull
    @Override
    public PendingTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_task, parent, false);
        return new PendingTaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingTaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTaskName.setText(task.getName());
        holder.textViewMechanicName.setText(task.getMechanicName());

        holder.buttonConfirm.setOnClickListener(v -> onConfirmClickListener.onConfirmClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class PendingTaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskName;
        TextView textViewMechanicName;
        Button buttonConfirm;

        public PendingTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            textViewMechanicName = itemView.findViewById(R.id.textViewMechanicName);
            buttonConfirm = itemView.findViewById(R.id.buttonConfirm);
        }
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(Task task);
    }
}
