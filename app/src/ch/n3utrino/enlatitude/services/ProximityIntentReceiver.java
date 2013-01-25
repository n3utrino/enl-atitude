package ch.n3utrino.enlatitude.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ch.n3utrino.enlatitude.R;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationUtil().prepareProxyNotification(context);

            builder.setContentTitle(context.getString(R.string.notification_near) + " " + intent.getStringExtra("user"));

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            Log.d(getClass().getSimpleName(), "exiting");
        }


    }

}
