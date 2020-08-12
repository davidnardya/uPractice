package com.davidnardya.upractice.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.davidnardya.upractice.pojo.Exercise;

@Database(entities = {Exercise.class}, version = 3)
@TypeConverters({DateConverter.class})
public abstract class AppDB extends RoomDatabase {

    private static AppDB instance;

    public abstract EntitiesDao entitiesDao();

    public static AppDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, AppDB.class, "AppDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
