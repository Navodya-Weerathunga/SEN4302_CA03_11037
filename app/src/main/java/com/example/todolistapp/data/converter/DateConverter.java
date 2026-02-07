package com.example.todolistapp.data.converter;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    // Convert Date to Long
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    // Convert Long to Date
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}
