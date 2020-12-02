package com.davidnardya.upractice.pojo;

public enum ExerciseStatus {
    NOT_STARTED, IN_PROGRESS, COMPLETED, UNKNOWN;

    public static ExerciseStatus getExerciseStatus(int input) {

        ExerciseStatus exerciseStatus;

        switch (input) {
            case 0:
                exerciseStatus = ExerciseStatus.NOT_STARTED;
                break;
            case 1:
                exerciseStatus = ExerciseStatus.IN_PROGRESS;
                break;
            case 2:
                exerciseStatus = ExerciseStatus.COMPLETED;
                break;
            default:
                exerciseStatus = ExerciseStatus.UNKNOWN;
                break;
        }
        return exerciseStatus;
    }
}
