package ch.n3utrino.enlatitude.services;

import android.location.Location;
import android.os.AsyncTask;
import ch.n3utrino.enlatitude.services.Client;
import ch.n3utrino.enlatitude.Constants;
import ch.n3utrino.enlatitude.common.UpdateReply;
import ch.n3utrino.enlatitude.common.UpdateRequest;
import ch.n3utrino.enlatitude.common.User;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class UpdateController {
    private final UpdateService.LocationUpdateListener updateService;
    public static final String MAP_META_DATA = "MAP_META_DATA";
    private Map<String, User> EMPTY_MAP = new HashMap<String, User>();
    private Client<UpdateReply> mClient;
    private User mUserData;

    public UpdateController(UpdateService.LocationUpdateListener updateService, User userData) {
        this.updateService = updateService;
        mUserData = userData;
        try {
           mClient = new Client<UpdateReply>(Constants.URL + "/update/", UpdateReply.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }

    }

    public void update(Location location) {
        AsyncTask<UpdateRequest, Void, Map<String, User>> task = new AsyncTask<UpdateRequest, Void, Map<String, User>>() {
            @Override
            protected Map<String, User> doInBackground(UpdateRequest... requests) {

                UpdateRequest request = requests[0];

                UpdateReply reply = mClient.connect(request);

                if (reply != null) {
                    if (reply.getStatus().equals("OK")) {
                        if (reply.getUsers() != null) {

                            return reply.getUsers();
                        }
                    } else {
                        //TODO: Netzwerk oder Serverfehler
                    }

                }

                return EMPTY_MAP;
            }

            @Override
            protected void onPostExecute(Map<String, User> reply) {
                updateService.onLocationUpdate(reply);
            }
        };
        if(location != null){
            task.execute(createRequest(location));
        }
    }

    private UpdateRequest createRequest(Location location) {

        UpdateRequest request = new UpdateRequest();

        mUserData.setLocation(new ch.n3utrino.enlatitude.common.Location(location.getLatitude(),location.getLongitude()));
        request.setUser(mUserData);


        return request;

    }
}