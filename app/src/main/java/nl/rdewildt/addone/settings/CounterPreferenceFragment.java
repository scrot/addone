package nl.rdewildt.addone.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.io.File;
import java.io.IOException;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.Counter;

/**
 * Created by roydewildt on 03/10/2016.
 */

public class CounterPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_counter);
        setHasOptionsMenu(true);

        findPreference("counter").setOnPreferenceChangeListener((pref, o) -> {
            File counterFile = new File(getContext().getFilesDir(), "counter.json");
            Counter counter = null;
            try {
                counter = Counter.readCounter(counterFile);
                counter.setCounter(Integer.parseInt(o.toString()));
                Counter.writeCounter(counter, counterFile);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        });
    }
}
