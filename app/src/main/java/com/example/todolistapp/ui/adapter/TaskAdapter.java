package com.example.todolistapp.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.R;
import com.example.todolistapp.data.entity.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private final OnTaskActionListener listener;

    // Interface to communicate with Activity
    public interface OnTaskActionListener {
        void onDelete(Task task);
        void onStatusChanged(Task task); // checkbox update
        void onUpdate(Task task); // edit task
    }

    public TaskAdapter(OnTaskActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        // prevent checkbox listener reuse bug
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.dueDate.setText("Due: " + formatDate(task.getDueDate()));
        holder.checkBox.setChecked(task.isCompleted());

        // Strike-through logic
        if (task.isCompleted()) {
            holder.checkBox.setPaintFlags(
                    holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.checkBox.setPaintFlags(
                    holder.checkBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
            );
        }

        // Checkbox update
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            listener.onStatusChanged(task);
        });

        // Delete
        holder.deleteIcon.setOnClickListener(
                v -> listener.onDelete(task));

        // Edit (open update dialog in Activity)
        holder.editIcon.setOnClickListener(
                v -> listener.onUpdate(task));
    }

    private String formatDate(long millis) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView description, dueDate;
        ImageView deleteIcon, editIcon;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkCompleted);
            description = itemView.findViewById(R.id.txtDescription);
            dueDate = itemView.findViewById(R.id.txtDueDate);
            deleteIcon = itemView.findViewById(R.id.imgDelete);
            editIcon = itemView.findViewById(R.id.imgEdit);
        }
    }
}
