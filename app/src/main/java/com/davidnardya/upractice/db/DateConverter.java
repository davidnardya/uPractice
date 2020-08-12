package com.davidnardya.upractice.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public String dateToJson(Date date){
        return new Gson().toJson(date);
    }

    @TypeConverter
    public Date jsonToDate(String json){
        return new Gson().fromJson(json, Date.class);
    }
}
