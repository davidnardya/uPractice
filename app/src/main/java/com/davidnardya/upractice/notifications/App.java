package com.davidnardya.upractice.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.stetho.Stetho;

public class App extends Application {

    public static final String CUSTOM_NOTIFICATION_CHANNEL_ID = "customNotificationChannelId";

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
         createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel customNotificationChannel = new NotificationChannel(
                    CUSTOM_NOTIFICATION_CHANNEL_ID,
                    "Custom notifications for specific dates",
                    NotificationManager.IMPORTANCE_HIGH
            );
            customNotificationChannel.enableVibration(true);
            customNotificationChannel.setDescription("Enables customized notifications for your exercises");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(customNotificationChannel);
        }
    }
}
