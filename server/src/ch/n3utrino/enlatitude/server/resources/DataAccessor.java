package ch.n3utrino.enlatitude.server.resources;


import ch.n3utrino.enlatitude.common.User;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataAccessor {

    public static final String USERMAP = "usermap";
    private Cache cache;
    private static DataAccessor mInstance = new DataAccessor();

    private HashMap<String,User> userMap = new HashMap<String, User>();

    public static DataAccessor getInstance() {
        return mInstance;
    }

    private DataAccessor() {

        CacheFactory cacheFactory = null;
        try {
            cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
            e.printStackTrace();
        }

    }

    public void updateUser(User theUser) {

        userMap.put(theUser.getUuid(),theUser);
        cache.put(USERMAP,userMap);
    }


    public Map<String,User> getUsers() {
        userMap = (HashMap<String, User>) cache.get(USERMAP);
        return userMap;
    }

    public void removeUsers(Set<User> removeMap) {
        for(User user:removeMap){
            userMap.remove(user.getUuid());
        }

        cache.put(USERMAP,userMap);
    }
}
