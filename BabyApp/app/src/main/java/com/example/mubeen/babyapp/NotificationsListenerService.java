package com.example.mubeen.babyapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Mubeen on 6/5/2017.
 */

public class NotificationsListenerService extends GcmListenerService {
    @Override

    public void onMessageReceived(String from, Bundle data) {

        sendNotification();

    }


    private void sendNotification() {

        Intent intent = new Intent(getApplicationContext(),Video_Streaming.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,

                PendingIntent.FLAG_ONE_SHOT);

        //Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.crying)

                .setContentTitle ("Warning ")

                .setContentText("Baby Woke Up..!!")

                .setAutoCancel(true)

                .setSound(Uri.parse("android.resource://" + getPackageName() + "/"  + R.raw.warningtone))

                .setContentIntent (pendingIntent);

        NotificationManager notificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}
