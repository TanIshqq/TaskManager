package com.example.lenovo.todoupdated;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Lenovo on 12-10-2017.
 */

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "Alarm Received!", Toast.LENGTH_LONG).show();
        NotificationCompat.Builder builder=new NotificationCompat.Builder(arg0);
        String Title= arg1.getStringExtra(Constants.key_task);
        builder.setContentTitle(Title);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.icon);
        builder.setVibrate(new long[]{1000, 1000});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

    }
}
