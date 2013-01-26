package ch.n3utrino.enlatitude.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import ch.n3utrino.enlatitude.EnlAtitudePreferences;
import ch.n3utrino.enlatitude.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Preference prefUsername = null;
    private Preference prefUpdateSpeedForeground = null;
    private Preference prefUpdateSpeedBackground = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        EnlAtitudePreferences prefs = new EnlAtitudePreferences(this.getActivity());

        prefUsername = findPreference(EnlAtitudePreferences.KEY_PREF_USERNAME);
        prefUsername.setOnPreferenceChangeListener(this);

        prefUpdateSpeedForeground = findPreference(EnlAtitudePreferences.KEY_PREF_UPDATE_SPEED_FOREGROUND);
        prefUpdateSpeedForeground.setOnPreferenceChangeListener(this);

        prefUpdateSpeedBackground = findPreference(EnlAtitudePreferences.KEY_PREF_UPDATE_SPEED_BACKGROUND);
        prefUpdateSpeedBackground.setOnPreferenceChangeListener(this);

        String sUserName = prefs.getUserName();
        prefUsername.setDefaultValue(sUserName);
        prefUsername.setSummary(sUserName);

        int iUpdateSpeedForeground = prefs.getUpdateSpeedForeground();
        prefUpdateSpeedForeground.setDefaultValue(iUpdateSpeedForeground);
        prefUpdateSpeedForeground.setSummary(Integer.toString(iUpdateSpeedForeground));

        int iUpdateSpeedBackground = prefs.getUpdateSpeedBackground();
        prefUpdateSpeedBackground.setDefaultValue(iUpdateSpeedBackground);
        prefUpdateSpeedBackground.setSummary(Integer.toString(iUpdateSpeedBackground));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        return true;
    }

}
