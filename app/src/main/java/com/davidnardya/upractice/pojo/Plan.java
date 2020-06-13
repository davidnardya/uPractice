package com.davidnardya.upractice.pojo;

import java.util.ArrayList;

public class Plan {

    private String planName, planDescription;
    private ArrayList<Exercise> exerciseArrayList = new ArrayList<>();

    public Plan() {
    }

    public Plan(String planName, String planDescription, ArrayList<Exercise> exerciseArrayList) {
        this.planName = planName;
        this.planDescription = planDescription;
        this.exerciseArrayList = exerciseArrayList;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public ArrayList<Exercise> getExerciseArrayList() {
        return exerciseArrayList;
    }

    public void setExerciseArrayList(ArrayList<Exercise> exerciseArrayList) {
        this.exerciseArrayList = exerciseArrayList;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planName='" + planName + '\'' +
                ", planDescription='" + planDescription + '\'' +
                ", exerciseArrayList=" + exerciseArrayList +
                '}';
    }
}
