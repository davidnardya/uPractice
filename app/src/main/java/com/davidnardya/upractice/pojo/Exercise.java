package com.davidnardya.upractice.pojo;

import java.util.Calendar;


public class Exercise {

    private String exerciseName, exerciseDescription, ExerciseID;
    private ExerciseStatus exerciseStatus;


    public Exercise() {
    }

    public Exercise(String exerciseName, String exerciseDescription, ExerciseStatus exerciseStatus) {
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseStatus = exerciseStatus;

    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public ExerciseStatus getExerciseStatus() {
        return exerciseStatus;
    }

    public void setExerciseStatus(ExerciseStatus exerciseStatus) {
        this.exerciseStatus = exerciseStatus;
    }

    public String getExerciseID() {
        return ExerciseID;
    }

    public void setExerciseID(String exerciseID) {
        ExerciseID = exerciseID;
    }
}
