package com.davidnardya.upractice.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    String planID, exerciseID;
    public static final String EXTRA_PLAN_ID = "com.davidnardya.upractice.activities.EXTRA_PLAN_ID";
    public static final String EXTRA_EXERCISE_ID = "com.davidnardya.upractice.activities.EXTRA_EXERCISE_ID";
    public static final String EXTRA_NOTIFICATION_TITLE = "com.davidnardya.upractice.activities.EXTRA_NOTIFICATION_TITLE";
    public static final String EXTRA_NOTIFICATION_TEXT = "com.davidnardya.upractice.activities.EXTRA_NOTIFICATION_TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationTitle = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
        String notificationText = intent.getStringExtra(EXTRA_NOTIFICATION_TEXT);
        planID = intent.getStringExtra(EXTRA_PLAN_ID);
        exerciseID = intent.getStringExtra(EXTRA_EXERCISE_ID);

        Intent notificationService = new Intent(context, NotificationService.class);

        notificationService.putExtra(EXTRA_NOTIFICATION_TITLE, notificationTitle);
        notificationService.putExtra(EXTRA_NOTIFICATION_TEXT, notificationText);
        notificationService.putExtra(EXTRA_PLAN_ID, planID);
        notificationService.putExtra(EXTRA_EXERCISE_ID, exerciseID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(notificationService);
        } else {
            context.startService(notificationService);
        }
    }
}
