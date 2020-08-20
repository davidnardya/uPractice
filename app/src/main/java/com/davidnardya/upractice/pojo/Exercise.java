package com.davidnardya.upractice.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    private String exerciseName, exerciseDescription, ExerciseID;
    @Ignore
    private ExerciseStatus exerciseStatus = ExerciseStatus.NOT_STARTED;
    private Date exerciseAlertDate;

    @Ignore
    public Exercise() {
    }

    @Ignore
    public Exercise(String exerciseName, String exerciseDescription, ExerciseStatus exerciseStatus, Date exerciseAlertDate) {
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseStatus = exerciseStatus;
        this.exerciseAlertDate = exerciseAlertDate;
    }

    public Exercise(String exerciseName, String exerciseDescription, Date exerciseAlertDate) {
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseAlertDate = exerciseAlertDate;
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
