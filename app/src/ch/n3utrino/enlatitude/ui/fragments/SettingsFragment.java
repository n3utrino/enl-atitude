package ch.n3utrino.enlatitude.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import ch.n3utrino.enlatitude.EnlAtitudePreferences;
import ch.n3utrino.enlatitude.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        EnlAtitudePreferences prefs = new EnlAtitudePreferences(this.getActivity());

        Preference username = findPreference("username");
        username.setSummary(prefs.getUserName());

    }

}
