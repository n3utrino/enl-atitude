package ch.n3utrino.enlatitude.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import ch.n3utrino.enlatitude.EnlAtitudePreferences;
import ch.n3utrino.enlatitude.common.User;

import java.util.Map;


public class UpdateService extends Service implements LocationController.LocationControllerCallback {


    private NotificationManager mNotificationManager;
    private AlarmManager mAlarmManager;

    private PendingIntent alarmManagerPendingIntent;

    public static final String UPDATE_ACTION = "enlatitude.client.android.update";

    private int mUpdateSpeed = -1;

    private boolean running = false;
    private boolean keepRunning = true;

    private LocationController mLocationController;

    private Handler updateHandler;
    private Runnable updateRunnable;

    private UpdateTrigger trigger;
    private UpdateController updateController;
    private Location mCurrentLocation;
    private final NotificationUtil notificationUtil = new NotificationUtil();


    public enum Mode {
        ACTIVE, PASSIVE
    }


    public void addListener(LocationUpdateListener listener) {
        setMode(Mode.ACTIVE);
        updateController = new UpdateController(listener, new EnlAtitudePreferences(this).buildUserData());
        postUpdate();

    }

    public void removeListener() {
        if (keepRunning) {
            setMode(Mode.PASSIVE);
            updateController = null;
        }
    }

    public class LocalBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }

    public interface LocationUpdateListener {
        public void onLocationUpdate(Map<String, User> reply);
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        trigger = new UpdateTrigger();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && !running) {
            keepRunning=true;

            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            if (alarmManagerPendingIntent == null) {
                Intent i = new Intent(UPDATE_ACTION);
                alarmManagerPendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
            }

            running = true;

            mLocationController = new LocationController(this, this);

            updateHandler = new Handler();
            showNotification();
        }

        return START_STICKY;

    }

    @Override
    public void locationUpdate(Location update, boolean initial) {
        mCurrentLocation = update;
    }

    private void postUpdate() {
        updateHandler.removeCallbacks(updateRunnable);
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (updateController != null) {
                    updateController.update(mCurrentLocation);
                } else {
                    return;
                }
                if (UpdateService.this.isRunning()) {
                    updateHandler.postDelayed(this, mUpdateSpeed);
                }
            }
        };
        updateHandler.post(updateRunnable);
    }

    private boolean isRunning() {
        return running;
    }

    public void stopUpdates() {
        keepRunning=false;
        stopSelf();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        NotificationCompat.Builder mBuilder = notificationUtil.prepareBuilder(this);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NotificationUtil.NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        updateHandler.removeCallbacks(updateRunnable);
        mLocationController.stop();
        mNotificationManager.cancelAll();
        mAlarmManager.cancel(alarmManagerPendingIntent);
    }

    public void setUpdateSpeed(int updateSpeed) {
        mUpdateSpeed = updateSpeed;
    }

    public void setMode(Mode mode) {
        EnlAtitudePreferences prefs = new EnlAtitudePreferences(this);

        if (mode == Mode.ACTIVE) {
            running = true;
            mAlarmManager.cancel(alarmManagerPendingIntent);
            int iInterval = prefs.getUpdateSpeedForeground() * 1000;
            setUpdateSpeed(iInterval);

        } else if (mode == Mode.PASSIVE) {
            running = false;
            mLocationController.stop();

            int iInterval = prefs.getUpdateSpeedBackground() * 1000;
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime()+2*60*1000, //Trigger in 2 Minutes
                    iInterval, //Loop this in intervals defined by UpdateSpeedBackground
                    alarmManagerPendingIntent);

        }
    }

}
