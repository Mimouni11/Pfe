package com.example.pfemini;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pfemini.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            if (isAppInForeground()) {
                handleForegroundNotification(remoteMessage);
            } else {
                displayNotification(remoteMessage);
            }
        }

        if (remoteMessage.getData().size() > 0) {
            // Handle data messages here
        }
    }

    private boolean isAppInForeground() {
        // Check if the app is in the foreground
        // This implementation may vary depending on how you track the app's foreground state in your app
        // Here's a simple implementation that checks if the app has any visible activities
        return MyApplication.isAppInForeground();
    }

    private void handleForegroundNotification(RemoteMessage remoteMessage) {
        displayNotification(remoteMessage);
    }

    private void displayNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_none_24)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
