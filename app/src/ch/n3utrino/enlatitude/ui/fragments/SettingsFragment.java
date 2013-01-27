package ch.n3utrino.enlatitude.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import ch.n3utrino.enlatitude.EnlAtitudePreferences;
import ch.n3utrino.enlatitude.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Preference prefUsername = null;
    private ListPreference prefUpdateSpeedForeground = null;
    private ListPreference prefUpdateSpeedBackground = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        EnlAtitudePreferences prefs = new EnlAtitudePreferences(this.getActivity());

        prefUsername = findPreference(EnlAtitudePreferences.KEY_PREF_USERNAME);
        prefUsername.setOnPreferenceChangeListener(this);

        prefUpdateSpeedForeground = (ListPreference)findPreference(EnlAtitudePreferences.KEY_PREF_UPDATE_SPEED_FOREGROUND);
        prefUpdateSpeedForeground.setOnPreferenceChangeListener(this);

        prefUpdateSpeedBackground = (ListPreference)findPreference(EnlAtitudePreferences.KEY_PREF_UPDATE_SPEED_BACKGROUND);
        prefUpdateSpeedBackground.setOnPreferenceChangeListener(this);

        String sUserName = prefs.getUserName();
        prefUsername.setDefaultValue(sUserName);
        prefUsername.setSummary(sUserName);

        CharSequence[] entries;
        int iEntryIdx;

        int iUpdateSpeedForeground = prefs.getUpdateSpeedForeground();
        prefUpdateSpeedForeground.setDefaultValue(iUpdateSpeedForeground);
        entries = prefUpdateSpeedForeground.getEntries();
        iEntryIdx = prefUpdateSpeedForeground.findIndexOfValue(Integer.toString(iUpdateSpeedForeground));
        prefUpdateSpeedForeground.setSummary(entries[iEntryIdx]);

        int iUpdateSpeedBackground = prefs.getUpdateSpeedBackground();
        prefUpdateSpeedBackground.setDefaultValue(iUpdateSpeedBackground);
        entries = prefUpdateSpeedBackground.getEntries();
        iEntryIdx = prefUpdateSpeedBackground.findIndexOfValue(Integer.toString(iUpdateSpeedBackground));
        prefUpdateSpeedBackground.setSummary(entries[iEntryIdx]);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof ListPreference) {
            ListPreference lstPref = (ListPreference)preference;
            int iLstIdx = lstPref.findIndexOfValue(newValue.toString());
           lstPref.setSummary(lstPref.getEntries()[iLstIdx]);
        }
        return true;
    }

}
