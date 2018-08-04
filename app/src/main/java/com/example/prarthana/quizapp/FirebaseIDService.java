package com.example.prarthana.quizapp;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//add service to manifest
public class FirebaseIDService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);

    }

    private void sendNotification(RemoteMessage remoteMessage) {
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_stat_name)
//                .setContentTitle(remoteMessage.getNotification().getBody())
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0,notification.build());

       Bitmap image= getBitmap(remoteMessage);

         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_stat_name)
                 .setLargeIcon(image)
                .setContentTitle(remoteMessage.getNotification().getBody())
                //.setContentText(notificationArticle.getDesc())
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle())

                       // .bigText(notificationArticle.getDesc()))
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.download)))
              //  .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
              //  .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);



        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());
    }

    private Bitmap getBitmap(RemoteMessage remoteMessage) {
        URL url = null;
        try {
            url = new URL(remoteMessage.getData().get("image"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return  bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

}
