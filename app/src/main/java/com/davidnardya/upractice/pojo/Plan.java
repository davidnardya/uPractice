package com.davidnardya.upractice.pojo;


import java.util.ArrayList;

public class Plan {

    private String planName, planDescription, planID;

    public Plan() {
    }

    public Plan(String planName, String planDescription) {
        this.planName = planName;
        this.planDescription = planDescription;
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

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planName='" + planName + '\'' +
                ", planDescription='" + planDescription + '\'' +
                '}';
    }
}
