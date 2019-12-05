package edu.fsu.cen4020.android.procrastinaint;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MyReceiver extends BroadcastReceiver {
    public static final String TAG = "channel";
    public static final String CHANNEL_ID = "channel_1";
    private NotificationManagerCompat builder;

    List<String> Notification_Motivation;



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "sasd");
        Toast.makeText(context, "FUUUCK", Toast.LENGTH_SHORT).show();


        Notification_Motivation = Arrays.asList(context.getResources().getStringArray(R.array.Notification_Motivation));
        Random rand = new Random();
        String randomElement = Notification_Motivation.get(rand.nextInt(Notification_Motivation.size()));


        createNotificationChannel(context);
        builder = NotificationManagerCompat.from(context);

        Intent result = new Intent(context, MainActivity.class);
        PendingIntent resultPending = PendingIntent.getActivity(context, 1, result, PendingIntent.FLAG_UPDATE_CURRENT);



                Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle("Good morning!")
                .setContentText(randomElement)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPending)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        builder.notify(1, notification);

    }


    private void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel1);
        }
    }

}
