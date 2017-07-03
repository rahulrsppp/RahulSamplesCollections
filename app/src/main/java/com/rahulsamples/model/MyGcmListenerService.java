/*
package com.rahulsamples.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.rahulsamples.BrightnessActivity;
import com.rahulsamples.R;

public class MyGcmListenerService extends GcmListenerService {


        private static final String TAG = "MyGcmListenerService";
        @Override
        public void onMessageReceived(String from, Bundle data) {

            String message = data.getString("Msg");
            String Type = data.getString("Type");
            Intent intent = new Intent(this, BrightnessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.BigTextStyle bigTextStyle= new NotificationCompat.BigTextStyle();

            bigTextStyle .setBigContentTitle(getString(R.string.app_name))
                    .bigText(message);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setStyle(bigTextStyle)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            int color = getResources().getColor(R.color.app_golden);
            notificationBuilder.setColor(color);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            int unOpenCount=AppPreferenceManager.getPreferenceInt();
            unOpenCount=unOpenCount+1;

            AppPreferenceManager.savePreferenceLong(unOpenCount);
            notificationManager.notify(unOpenCount */
/* ID of notification *//*
, notificationBuilder.build());

// This is for bladge on home icon
            BadgeUtils.setBadge(MyGcmListenerService.this,(int)unOpenCount);

        }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.drawable.ic_launcher;
    }
}
*/
