package ch.n3utrino.enlatitude.server.resources;


import ch.n3utrino.enlatitude.common.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataAccessor {


    private static DataAccessor mInstance = new DataAccessor();

    private HashMap<String,User> userMap = new HashMap<String, User>();

    public static DataAccessor getInstance() {
        return mInstance;
    }

    private DataAccessor() {
    }

    public void updateUser(User theUser) {
        userMap.put(theUser.getUuid(),theUser);
    }


    public Map<String,User> getUsers() {
        return userMap;
    }

    public void removeUsers(Set<User> removeMap) {
        for(User user:removeMap){
            userMap.remove(user.getUuid());
        }
    }
}
