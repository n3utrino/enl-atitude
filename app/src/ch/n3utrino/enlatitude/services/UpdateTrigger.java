package ch.n3utrino.enlatitude.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ch.n3utrino.enlatitude.EnlAtitudePreferences;
import ch.n3utrino.enlatitude.common.User;

import java.util.Map;

public class UpdateTrigger extends BroadcastReceiver implements LocationController.LocationControllerCallback, UpdateService.LocationUpdateListener {

    public UpdateTrigger() {
    }

    private Location mLastLocation;
    private LocationController passiveLocationController;
    private UpdateController mUpdateController;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;


        mUpdateController = new UpdateController(this,new EnlAtitudePreferences(mContext).buildUserData());
        passiveLocationController = new LocationController(context,this);

        //Wait for 60 seconds stop after that.

        //TODO: if the alarmmanager gets stopped while this is running, the event gets rescheduled. need to handle this
        Handler stopHandler = new Handler();
        stopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUpdateController.update(mLastLocation);
                passiveLocationController.stop();
            }
        },60000);

    }

    @Override
    public void locationUpdate(Location update, boolean initial) {
        if(LocationController.isBetterLocation(update,mLastLocation)){
            mLastLocation = update;
        }

        Log.d("Location SERVICE", "Location Update:"+ update.toString());
        if(!initial){
            mUpdateController.update(update);
            passiveLocationController.stop();
        }

    }

    @Override
    public void onLocationUpdate(Map<String, User> reply) {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder myBuilder = new NotificationUtil().prepareBuilder(mContext);
        myBuilder.setNumber(reply.size());
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NotificationUtil.NOTIFICATION_ID, myBuilder.build());

        for(User user:reply.values()){
            passiveLocationController.addProximityAlert(user);
        }

    }
}
