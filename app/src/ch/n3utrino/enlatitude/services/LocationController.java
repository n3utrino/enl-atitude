package ch.n3utrino.enlatitude.services;


import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LocationController {

    public static final int GPS_FIX_WAIT_TIME = 1*60*1000; //wait one minute to find gps
    private LocationManager mLocationManager;
    private Context mContext;
    private LocationControllerCallback mCallback;

    private Location mLastLocation;

    private boolean gpsFixOK = false;

    private String TAG = "LocationController";


    public interface LocationControllerCallback{
        public void locationUpdate(Location update, boolean initial);
    }

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            if (isBetterLocation(location, mLastLocation)) {
                mLastLocation = location;
                mCallback.locationUpdate(mLastLocation,false);
            }

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {
        }
    };
    private final GpsStatus.Listener mGpsStatusListener;

    public LocationController(Context mContext, LocationControllerCallback callback) {
        this.mContext = mContext;
        mCallback = callback;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 20, locationListener);

        mGpsStatusListener = new GpsStatus.Listener() {

            private int gpsTries = 0;
            private boolean foundSatelite = false;


            @Override
            public void onGpsStatusChanged(int i) {
                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();

                switch (i) {
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        Log.d(TAG, "Gps First Fix");
                        gpsFixOK = true;
                        break;

                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        gpsTries++;
                        for(GpsSatellite satellite:satellites){
                            Log.d(TAG,satellite.toString());
                            foundSatelite = true;
                        }


                        if(gpsTries > 20 && !foundSatelite){
                            LocationController.this.stop();
                        }

                       Log.d(TAG, "Gps SatelliteStatus");
                        break;


                    case GpsStatus.GPS_EVENT_STARTED:
                        Log.d(TAG, "GpsStarted");
                        break;

                    case GpsStatus.GPS_EVENT_STOPPED:
                        Log.d(TAG, "Gps Stopped");
                        break;
                }
            }
        };
        mLocationManager.addGpsStatusListener(mGpsStatusListener);

        Handler gpsStatusHandler = new Handler();
        gpsStatusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gpsFixOK) {
                    return;
                }
                mLocationManager.removeUpdates(locationListener);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 500, locationListener);

            }
        }, GPS_FIX_WAIT_TIME);

        mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        mLastLocation = determineBetterLocation(mLastLocation, mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        mCallback.locationUpdate(mLastLocation,true);

    }

    public void stop() {

        mLocationManager.removeUpdates(locationListener);
        mLocationManager.removeGpsStatusListener(mGpsStatusListener);


    }

    private Location determineBetterLocation(Location location1, Location location2) {

        Location result;

        if (isBetterLocation(location1, location2)) {
            result = location1;
        } else {
            result = location2;
        }

        Log.d(TAG, "Better Location is: " + result);

        return result;
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
