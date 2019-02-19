package com.suspedeal.makeitbig.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.suspedeal.makeitbig.main.MainActivity;
import com.suspedeal.makeitbig.R;

//This class receives the data from FCM
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "channel_id";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String REGARDS = "regards";
    public static final String NEW_THEME = "new theme";
    public static final String THEME_ID = "theme id";
    public static final String REVIEW_REQUEST = "review request";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sendNotification(remoteMessage.getData().get(TITLE), remoteMessage.getData().get(BODY),
                remoteMessage.getData().get(REGARDS), remoteMessage.getData().get(THEME_ID));
    }

    private void sendNotification(String title, String body, String regards, String themeId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(BODY, body);
        intent.putExtra(REGARDS, regards);

        if(themeId != null){
            intent.putExtra(THEME_ID, themeId);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_mib_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel("my_channel_01",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId("my_channel_01");
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
