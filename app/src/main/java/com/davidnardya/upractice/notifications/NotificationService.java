package com.davidnardya.upractice.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.activities.AddNewExerciseActivity;
import com.davidnardya.upractice.activities.MainActivity;
import com.davidnardya.upractice.activities.ViewExerciseActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class NotificationService extends IntentService {

    //Strings
    public static final String NotificationServiceEXTRA_PLAN_ID = "com.davidnardya.upractice.NotificationService.EXTRA_PLAN_ID";
    public static final String NotificationServiceEXTRA_EXERCISE_ID = "com.davidnardya.upractice.NotificationService.EXTRA_EXERCISE_ID";
    String planID, exerciseID, notificationTitle, notificationText;

    //Integers
    int pendingIntentRequestCode, pendingContentRequestCode, pendingActionRequestCode;

    //Constructors
    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationTitle = intent.getStringExtra(AlarmReceiver.AlarmReceiverEXTRA_NOTIFICATION_TITLE);
        notificationText = intent.getStringExtra(AlarmReceiver.AlarmReceiverEXTRA_NOTIFICATION_TEXT);
        planID = intent.getStringExtra(AlarmReceiver.AlarmReceiverEXTRA_PLAN_ID);
        exerciseID = intent.getStringExtra(AlarmReceiver.AlarmReceiverEXTRA_EXERCISE_ID);

        generateRandomCodes();
        configureAlertAction();

        return START_NOT_STICKY;
    }

    private void configureAlertAction() {
        //When user clicks on the notification
        Intent notificationIntent = new Intent(this, ViewExerciseActivity.class);
        notificationIntent.putExtra(NotificationService.NotificationServiceEXTRA_PLAN_ID, planID);
        notificationIntent.putExtra(NotificationService.NotificationServiceEXTRA_EXERCISE_ID, exerciseID);

        PendingIntent contentIntent = PendingIntent.getActivity(this, pendingIntentRequestCode,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //The user clicks on the "Set new time" action
        Intent editNotificationIntent = new Intent(this, AddNewExerciseActivity.class);
        editNotificationIntent.putExtra(NotificationService.NotificationServiceEXTRA_PLAN_ID, planID);
        editNotificationIntent.putExtra(NotificationService.NotificationServiceEXTRA_EXERCISE_ID, exerciseID);

        PendingIntent actionIntent = PendingIntent.getActivity(this, pendingActionRequestCode, editNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this, App.CUSTOM_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(contentIntent)
                .addAction(R.mipmap.ic_launcher, "Set new time", actionIntent)
                .setAutoCancel(true)
                .setSound(uri)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(pendingIntentRequestCode, notification);

        startForeground(pendingIntentRequestCode, notification);

        stopSelf();
    }

    private void generateRandomCodes() {
        Random random = new Random();
        pendingIntentRequestCode = random.nextInt(1000) + 1;
        pendingContentRequestCode = random.nextInt(1000) + 1;
        if (pendingContentRequestCode == pendingIntentRequestCode) {
            pendingContentRequestCode--;
        }
        pendingActionRequestCode = random.nextInt(1000) + 1;
        if (pendingActionRequestCode == pendingIntentRequestCode || pendingActionRequestCode == pendingContentRequestCode) {
            pendingActionRequestCode--;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
