package com.davidnardya.upractice.pojo;

import java.util.Calendar;

public class Exercise {
    private String exerciseName;
    private String exerciseDescription;
    private ExerciseStatus exerciseStatus;
    private Calendar alertDate;

    public Exercise(String exerciseName, String exerciseDescription, ExerciseStatus exerciseStatus, Calendar alertDate) {
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseStatus = exerciseStatus;
        this.alertDate = alertDate;
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

    public Calendar getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Calendar alertDate) {
        this.alertDate = alertDate;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseName='" + exerciseName + '\'' +
                ", exerciseDescription='" + exerciseDescription + '\'' +
                ", exerciseStatus=" + exerciseStatus +
                ", alertDate=" + alertDate +
                '}';
    }
}
