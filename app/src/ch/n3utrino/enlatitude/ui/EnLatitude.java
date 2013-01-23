package ch.n3utrino.enlatitude.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import ch.n3utrino.enlatitude.EnlAtitudePreferences;
import ch.n3utrino.enlatitude.R;
import ch.n3utrino.enlatitude.common.User;
import ch.n3utrino.enlatitude.services.UpdateService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class EnLatitude extends Activity implements UpdateService.LocationUpdateListener {

    private static final String CAMERA_POSITION = "cameraPosition";
    private GoogleMap map;
    private boolean ready = true;


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mUpdateService = ((UpdateService.LocalBinder) service).getService();
            mUpdateService.addListener(EnLatitude.this);

        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mUpdateService.removeListener();
            mUpdateService = null;
        }
    };

    private EnlAtitudePreferences mPreferences;

    private UpdateService mUpdateService;
    private boolean mIsBound;

    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GoogleMapOptions options = new GoogleMapOptions();
        options.scrollGesturesEnabled(true).zoomControlsEnabled(false).mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(true);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.setMyLocationEnabled(true);

        if(savedInstanceState != null){
            mCameraPosition = (CameraPosition) savedInstanceState.get(CAMERA_POSITION);
        }

        mPreferences = new EnlAtitudePreferences(this);

        if (mPreferences.getUserName().equals("")) {
            showUsernameSelectDialog();
            ready = false;
        }

        Handler mapZoomHandler = new Handler();

        mapZoomHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Location loc = map.getMyLocation();

                if (loc != null) {
                    LatLng myLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(15).build();

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }

                , 2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings(){
        mUpdateService.stopUpdates();
        this.startActivity(new Intent(this,SettingsActivity.class));
    }

    private void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).$

        Intent serviceIntent = new Intent(EnLatitude.this,
                UpdateService.class);

        startService(serviceIntent);
        bindService(new Intent(EnLatitude.this,
                UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mUpdateService.removeListener();
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public void onLocationUpdate(Map<String, User> reply) {

        map.clear();

        for (User user : reply.values()) {

            EnLDrawable marker = new EnLDrawable(user,getResources());

            map.addMarker(new MarkerOptions().draggable(false)
                    .position(new LatLng(user.getLocation().getLat(), user.getLocation().getLon()))
                    .title(user.getName())
                    .icon(BitmapDescriptorFactory.fromBitmap(marker.getBitmap())));

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ready && !mIsBound) {
            doBindService();
        }

        if(mCameraPosition != null){
            map.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mUpdateService != null){
            mUpdateService.stopUpdates();
        }
    }

    private void showUsernameSelectDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Ingress Username");
        alert.setMessage("Enter your Ingress username");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EnlAtitudePreferences prefs = new EnlAtitudePreferences(EnLatitude.this);
                prefs.setUsername(input.getText().toString());
                doBindService();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showUsernameSelectDialog();
            }
        });

        alert.show();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(CAMERA_POSITION, map.getCameraPosition());


    }
}
