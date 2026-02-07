package com.example.todolistapp.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolistapp.R;
import com.example.todolistapp.data.entity.Task;
import com.example.todolistapp.viewModel.TaskViewModel;

import java.util.Calendar;


public class AddTaskActivity extends AppCompatActivity {
    private EditText editTitle, editDescription, editDueDate;

    // Stores due date as milliseconds instead of a formatted string
    private long selectedDate = 0;

    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDueDate = findViewById(R.id.editDueDate);

        // Using a DatePickerDialog prevents invalid or malformed date input
        editDueDate.setOnClickListener(v -> showDatePicker());

        Button btnSave = findViewById(R.id.btnSave);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        // Disabling keyboard input avoids manual date entry
        editDueDate.setFocusable(false);

        editDueDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(year, month, dayOfMonth);

                        selectedDate = selected.getTimeInMillis();
                        editDueDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

    }


    private void saveTask() {
        // Input validation: trimming user input removes unnecessary whitespace
        // and helps prevent empty or malformed data being saved
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        // Prevents invalid records from being stored in the database
        if (title.isEmpty()){
            editTitle.setError("Title Required!!!");
            return;
        }

        Task task = new Task(title, description, selectedDate );
        taskViewModel.insert(task);
        finish();
    }


}
