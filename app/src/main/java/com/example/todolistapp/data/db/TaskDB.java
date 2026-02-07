package com.example.todolistapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.todolistapp.data.converter.DateConverter;
import com.example.todolistapp.data.dao.TaskDao;
import com.example.todolistapp.data.entity.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters(DateConverter.class)

public abstract class TaskDB extends RoomDatabase {
    private static TaskDB instance;
    public abstract TaskDao taskDao();
    public static synchronized TaskDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TaskDB.class,
                    "task_db"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
