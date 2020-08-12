package com.davidnardya.upractice.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.davidnardya.upractice.pojo.Exercise;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EntitiesDao {
    @Insert(onConflict = REPLACE)
    public void insertExercises(List<Exercise> exercises);

    @Insert(onConflict = REPLACE)
    public void insertExercise(Exercise exercise);

    @Query("Delete from Exercise where ExerciseID=:id")
    public void deleteExercise(String id);
}
