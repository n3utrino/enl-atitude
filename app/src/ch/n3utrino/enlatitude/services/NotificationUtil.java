package ch.n3utrino.enlatitude.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import ch.n3utrino.enlatitude.ui.EnLatitude;
import ch.n3utrino.enlatitude.R;

public class NotificationUtil {

    public static final int NOTIFICATION_ID = 100;


    public NotificationCompat.Builder prepareBuilder(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_ingress)
                        .setContentTitle(context.getString(R.string.sharing_location))
                        .setContentText(context.getString(R.string.sending_updates)).setOngoing(true);

        Intent resultIntent = new Intent(context, EnLatitude.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;
    }

    public NotificationCompat.Builder prepareProxyNotification(Context context){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_ingress)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setOngoing(false);

        Intent resultIntent = new Intent(context, EnLatitude.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;

    }
}