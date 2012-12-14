package ch.n3utrino.enlatitude.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import ch.n3utrino.enlatitude.EnLatitude;
import ch.n3utrino.enlatitude.R;

public class NotificationUtil {

    public static final int NOTIFICATION_ID = 100;


    public NotificationCompat.Builder prepareBuilder(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_ingress)
                        .setContentTitle("Sharing Location")
                        .setContentText("Sending updates").setOngoing(true);

        Intent resultIntent = new Intent(context, EnLatitude.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;
    }
}