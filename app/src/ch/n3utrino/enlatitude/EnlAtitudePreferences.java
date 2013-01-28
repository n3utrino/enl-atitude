package ch.n3utrino.enlatitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ch.n3utrino.enlatitude.common.User;

import java.util.UUID;

public class EnlAtitudePreferences {

    public static final String EMPTY = "EMPTY";

    public static final String KEY_PREF_ID = "PREF_ID";
    public static final String KEY_PREF_USERNAME = "username";
    public static final String KEY_PREF_UPDATE_SPEED_FOREGROUND = "updateSpeedForeground";
    public static final String KEY_PREF_UPDATE_SPEED_BACKGROUND = "updateSpeedBackground";
    public static final String KEY_PREF_PROXY_ALERT = "proxyAlert";
    public static final String KEY_PREF_PROXY_DISTANCE = "proxy_distance";
    public static final String KEY_PREF_LAST_ZOOM = "last_zoom";

    public static final int    DEFAULT_UPDATE_SPEED_FOREGROUND = 20;    //Also adjust in preferences.xml
    public static final int    DEFAULT_UPDATE_SPEED_BACKGROUND = 300;   //Also adjust in preferences.xml

    private SharedPreferences mPreferences;

    public EnlAtitudePreferences(Context mContext) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public String getUuid(){
        String uuid = mPreferences.getString(KEY_PREF_ID, EMPTY);

        if(uuid.equals(EMPTY)){
            uuid = UUID.randomUUID().toString();
            mPreferences.edit().putString(KEY_PREF_ID,uuid).commit();

        }

        return uuid;
    }

    public User buildUserData(){
        User data = new User();
        data.setName(getUserName());
        data.setUuid(getUuid());

        return data;
    }

    public void setUsername(String newUsername){
        mPreferences.edit().putString(KEY_PREF_USERNAME,newUsername).commit();
    }

    public String getUserName(){
        return mPreferences.getString(KEY_PREF_USERNAME,"");
    }

    public void setUpdateSpeedForeground(int seconds) {
        mPreferences.edit().putInt(KEY_PREF_UPDATE_SPEED_FOREGROUND,seconds).commit();
    }

    public int getUpdateSpeedForeground() {
        try {
            return Integer.parseInt(mPreferences.getString(KEY_PREF_UPDATE_SPEED_FOREGROUND,Integer.toString(DEFAULT_UPDATE_SPEED_FOREGROUND)));
        }
        catch (Exception ex) {
            return DEFAULT_UPDATE_SPEED_FOREGROUND;
        }
    }

    public void setUpdateSpeedBackground(int seconds) {
        mPreferences.edit().putInt(KEY_PREF_UPDATE_SPEED_BACKGROUND,seconds).commit();
    }

    public int getUpdateSpeedBackground() {
        try {
            return Integer.parseInt(mPreferences.getString(KEY_PREF_UPDATE_SPEED_BACKGROUND,Integer.toString(DEFAULT_UPDATE_SPEED_BACKGROUND)));
        }
        catch (Exception ex) {
            return DEFAULT_UPDATE_SPEED_BACKGROUND;
        }
    }

    public boolean proxyAlertEnabled() {
        return mPreferences.getBoolean(KEY_PREF_PROXY_ALERT,true);
    }

    public int getProxyDistance() {
        return mPreferences.getInt(KEY_PREF_PROXY_DISTANCE,200);
    }

    public void setLastZoom(float lastZoom) {
        mPreferences.edit().putFloat(KEY_PREF_LAST_ZOOM,lastZoom).commit();
    }

    public float getLastZoom() {
        return mPreferences.getFloat(KEY_PREF_LAST_ZOOM,0.0F);
    }
}
