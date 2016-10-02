package nl.rdewildt.addone.settings;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import java.io.File;
import java.util.List;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.Stats;
import nl.rdewildt.addone.StatsIO;

public class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onIsMultiPane() {
        return (this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || CounterPreferenceFragment.class.getName().equals(fragmentName);
    }

    public static class CounterPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_counter);
            setHasOptionsMenu(true);

            // Update stats counter
            findPreference("counter").setOnPreferenceChangeListener((preference, o) -> {
                File statsFile = new File(preference.getContext().getFilesDir(), "stats.json");
                Stats stats = StatsIO.readStats(statsFile);
                stats.setCounter(Integer.parseInt(o.toString()));
                StatsIO.writeStats(stats, statsFile);
                return true;
            });
        }
    }
}
