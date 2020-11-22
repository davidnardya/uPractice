package com.davidnardya.upractice.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.davidnardya.upractice.activities.AddNewExerciseActivity;
import com.davidnardya.upractice.activities.AddNewPlanActivity;
import com.davidnardya.upractice.activities.ViewExerciseActivity;
import com.davidnardya.upractice.activities.ViewPlanActivity;

public class AlarmReceiver extends BroadcastReceiver {

    String planID, exerciseID;
    public static final String AlarmReceiverEXTRA_PLAN_ID = "com.davidnardya.upractice.AlarmReceiver.EXTRA_PLAN_ID";
    public static final String AlarmReceiverEXTRA_EXERCISE_ID = "com.davidnardya.upractice.AlarmReceiver.EXTRA_EXERCISE_ID";
    public static final String AlarmReceiverEXTRA_NOTIFICATION_TITLE = "com.davidnardya.upractice.AlarmReceiver.EXTRA_NOTIFICATION_TITLE";
    public static final String AlarmReceiverEXTRA_NOTIFICATION_TEXT = "com.davidnardya.upractice.AlarmReceiver.EXTRA_NOTIFICATION_TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationTitle = intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_NOTIFICATION_TITLE);
        String notificationText = intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_NOTIFICATION_TEXT);
        planID = intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_PLAN_ID);
        exerciseID = intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_EXERCISE_ID);

        Intent notificationService = new Intent(context, NotificationService.class);

        notificationService.putExtra(AlarmReceiver.AlarmReceiverEXTRA_NOTIFICATION_TITLE, notificationTitle);
        notificationService.putExtra(AlarmReceiver.AlarmReceiverEXTRA_NOTIFICATION_TEXT, notificationText);
        notificationService.putExtra(AlarmReceiver.AlarmReceiverEXTRA_PLAN_ID, planID);
        notificationService.putExtra(AlarmReceiver.AlarmReceiverEXTRA_EXERCISE_ID, exerciseID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(notificationService);
        } else {
            context.startService(notificationService);
        }
    }
}
