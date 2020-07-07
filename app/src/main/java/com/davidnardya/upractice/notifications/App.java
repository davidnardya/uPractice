package com.davidnardya.upractice.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CUSTOM_NOTIFICATION_CHANNEL_ID = "customNotificationChannelId";
    public static final String DAILY_NOTIFICATION_CHANNEL_ID = "dailyNotificationChannelId";

    @Override
    public void onCreate() {
        super.onCreate();

         createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Custom notifications
            NotificationChannel customNotificationChannel = new NotificationChannel(
                    CUSTOM_NOTIFICATION_CHANNEL_ID,
                    "Custom notifications for specific dates",
                    NotificationManager.IMPORTANCE_HIGH
            );
            customNotificationChannel.enableVibration(true);
            customNotificationChannel.setDescription("Enables customized notifications for your exercises");

            //Daily notifications
            NotificationChannel dailyNotificationChannel = new NotificationChannel(
                    DAILY_NOTIFICATION_CHANNEL_ID,
                    "Daily notifications for to repeat every day",
                    NotificationManager.IMPORTANCE_HIGH
            );
            customNotificationChannel.enableVibration(true);
            customNotificationChannel.setDescription("Enables daily notifications for your exercises");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(customNotificationChannel);
            manager.createNotificationChannel(dailyNotificationChannel);
        }
    }
}
