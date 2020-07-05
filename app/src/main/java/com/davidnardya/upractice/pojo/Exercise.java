package com.davidnardya.upractice.pojo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


public class Exercise {

    private String exerciseName, exerciseDescription, ExerciseID;
    private ExerciseStatus exerciseStatus;
    private Date exerciseAlertDate;
//    private Timestamp timestamp;


    public Exercise() {
    }

    public Exercise(String exerciseName, String exerciseDescription, ExerciseStatus exerciseStatus, Date exerciseAlertDate) {
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseStatus = exerciseStatus;
        this.exerciseAlertDate = exerciseAlertDate;
//        this.timestamp = timestamp;
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

//    public Timestamp getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Timestamp timestamp) {
//        this.timestamp = timestamp;
//    }


    public Date getExerciseAlertDate() {
        return exerciseAlertDate;
    }

    public void setExerciseAlertDate(Date exerciseAlertDate) {
        this.exerciseAlertDate = exerciseAlertDate;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseName='" + exerciseName + '\'' +
                ", exerciseDescription='" + exerciseDescription + '\'' +
                ", ExerciseID='" + ExerciseID + '\'' +
                ", exerciseStatus=" + exerciseStatus +
                '}';
    }
}
