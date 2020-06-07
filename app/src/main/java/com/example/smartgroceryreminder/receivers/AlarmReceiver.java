package com.example.smartgroceryreminder.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.activities.Expiry;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String text = "Your product is going to expire soon.";
        String cId = "1";
        int id = 1;
        if (intent != null) {
            String name = intent.getStringExtra("name");
            int pId = intent.getIntExtra("id", 0);
            text = "Your product " + name + " is going to expire.";
            cId = cId + pId;
            id = pId;
        } else {
            Log.e("Receiver", "Intent is not null");
        }
        Intent notificationIntent = new Intent(context, Expiry.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        builder.setTicker(text);
        builder.setAutoCancel(true);
        builder.setChannelId(cId);
        builder.setContentInfo(text);
        builder.setContentTitle(text);
        builder.setContentText(text);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setContentIntent(pendingIntent);
        builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(id, builder.build());
        }
    }
}
