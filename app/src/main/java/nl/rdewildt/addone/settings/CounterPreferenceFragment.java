package nl.rdewildt.addone.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import java.io.File;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.Stats;

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
            File statsFile = new File(getContext().getFilesDir(), "stats.json");
            Stats stats = Stats.readStats(statsFile);
            stats.setCounter(Integer.parseInt(o.toString()));
            Stats.writeStats(stats, statsFile);
            return true;
        });
    }
}
