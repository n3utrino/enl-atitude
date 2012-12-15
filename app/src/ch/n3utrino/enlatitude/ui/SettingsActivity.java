package ch.n3utrino.enlatitude.ui;

import android.app.Activity;
import android.os.Bundle;
import ch.n3utrino.enlatitude.ui.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
