package com.davidnardya.upractice.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.activities.MainActivity;
import com.davidnardya.upractice.activities.ViewExerciseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class NotificationService extends Service {

    public static final String EXTRA_PLAN_ID = "com.davidnardya.upractice.activities.EXTRA_PLAN_ID";
    public static final String EXTRA_EXERCISE_ID = "com.davidnardya.upractice.activities.EXTRA_EXERCISE_ID";
    public static final String EXTRA_NOTIFICATION_TITLE = "com.davidnardya.upractice.activities.EXTRA_NOTIFICATION_TITLE";
    public static final String EXTRA_NOTIFICATION_TEXT = "com.davidnardya.upractice.activities.EXTRA_NOTIFICATION_TEXT";
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String planID, exerciseID;
    int pendingIntentRequestCode;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String notificationTitle = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
        String notificationText = intent.getStringExtra(EXTRA_NOTIFICATION_TEXT);
        planID = intent.getStringExtra(EXTRA_PLAN_ID);
        exerciseID = intent.getStringExtra(EXTRA_EXERCISE_ID);

        pendingIntentRequestCode = Integer.parseInt(exerciseID.replaceAll("[^0-9]", ""));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_PLAN_ID, planID);
        intent.putExtra(EXTRA_EXERCISE_ID, exerciseID);

        PendingIntent contentIntent = PendingIntent.getActivity(this, pendingIntentRequestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        String plan = "0";
//        String exercise = "0";
//
//        if(planID != null){
//            plan = "1";
//        }
//
//        if(exerciseID != null){
//            exercise = "2";
//        }

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this, App.CUSTOM_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(notificationTitle)
//                .setContentText(notificationText + " & " + plan + " & " + exercise )
                .setContentText(notificationText)
                .setContentIntent(contentIntent)
                .setSound(uri)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(pendingIntentRequestCode, notification);

        startForeground(pendingIntentRequestCode, notification);

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
