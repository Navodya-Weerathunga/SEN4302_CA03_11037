package com.example.todolistapp.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.R;
import com.example.todolistapp.data.entity.Task;
import com.example.todolistapp.ui.adapter.TaskAdapter;
import com.example.todolistapp.viewModel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fabAdd);

        taskAdapter = new TaskAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            Log.d("TASK_DEBUG", "Tasks count: " + tasks.size());
            taskAdapter.setTasks(tasks);
        });

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddTaskActivity.class))
        );
    }

    @Override
    public void onDelete(Task task) {
        // Confirm Deletion before delete the task
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the task
                    taskViewModel.delete(task);
                })
                .setNegativeButton("No", null) // just dismiss
                .show();
    }

    @Override
    public void onStatusChanged(Task task) {
        taskViewModel.update(task);
    }

    @Override
    public void onUpdate(Task task) {
        showUpdateDialog(task);
    }

    @SuppressLint("SetTextI18n")
    private void showUpdateDialog(Task task) {

        View view = getLayoutInflater().inflate(R.layout.add_task, null);

        EditText editTitle = view.findViewById(R.id.editTitle);
        EditText editDescription = view.findViewById(R.id.editDescription);
        EditText editDueDate = view.findViewById(R.id.editDueDate);

        // Hide Save button when Updating a task
        View saveButton = view.findViewById(R.id.btnSave);
        if(saveButton != null){
            saveButton.setVisibility(View.GONE);
        }

        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(task.getDueDate());

        editDueDate.setText(
                calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        (calendar.get(Calendar.MONTH) + 1) + "/" +
                        calendar.get(Calendar.YEAR)
        );

        final long[] selectedDateMillis = { task.getDueDate() };

        // Calendar picker
        editDueDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view1, year, month, dayOfMonth) -> {

                        Calendar selected = Calendar.getInstance();
                        selected.set(year, month, dayOfMonth, 0, 0, 0);

                        selectedDateMillis[0] = selected.getTimeInMillis();

                        editDueDate.setText(
                                dayOfMonth + "/" + (month + 1) + "/" + year
                        );
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Update Task")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {

                    task.setTitle(editTitle.getText().toString());
                    task.setDescription(editDescription.getText().toString());
                    task.setDueDate(selectedDateMillis[0]);

                    taskViewModel.update(task);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}