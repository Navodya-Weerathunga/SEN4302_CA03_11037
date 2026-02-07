package com.example.todolistapp.data.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todolistapp.data.dao.TaskDao;
import com.example.todolistapp.data.db.TaskDB;
import com.example.todolistapp.data.entity.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepo {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskRepo(Application application){
        TaskDB taskDB = TaskDB.getInstance(application);
        taskDao = taskDB.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task){
        executorService.execute(() ->
                taskDao.insert(task));
    }

    public void update(Task task){
        executorService.execute(() ->
                taskDao.update(task));
    }

    public void delete(Task task){
        executorService.execute(() ->
                taskDao.delete(task));
    }
}
