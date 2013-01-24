package ch.n3utrino.enlatitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ch.n3utrino.enlatitude.common.User;

import java.util.UUID;

public class EnlAtitudePreferences {

    public static final String PREF_ID = "PREF_ID";
    public static final String EMPTY = "EMPTY";


    private SharedPreferences mPreferences;

    public EnlAtitudePreferences(Context mContext) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public String getUuid(){
        String uuid = mPreferences.getString(PREF_ID, EMPTY);

        if(uuid.equals(EMPTY)){
            uuid = UUID.randomUUID().toString();
            mPreferences.edit().putString(PREF_ID,uuid).commit();

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
        mPreferences.edit().putString("username",newUsername).commit();
    }

    public String getUserName(){
        return mPreferences.getString("username","");
    }

    public boolean proxyAlertEnabled() {
        return mPreferences.getBoolean("proxyAlert",true);
    }

    public int getProxyDistance() {
        return mPreferences.getInt("proxy_distance",200);
    }
}
