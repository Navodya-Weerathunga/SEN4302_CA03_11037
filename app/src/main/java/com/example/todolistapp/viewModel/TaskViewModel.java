package com.example.todolistapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolistapp.data.entity.Task;
import com.example.todolistapp.data.repo.TaskRepo;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepo taskRepo;
    private final LiveData<List<Task>> allTasks;
    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepo = new TaskRepo(application);
        allTasks = taskRepo.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        // Secure coding practice:
        // Validate input before inserting into database
        taskRepo.insert(task);
    }

    public void update(Task task) {
        taskRepo.update(task);
    }

    public void delete(Task task) {
        taskRepo.delete(task);
    }
}
