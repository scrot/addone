package nl.rdewildt.addone.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import java.io.File;
import java.util.List;

import nl.rdewildt.addone.StatsController;
import nl.rdewildt.addone.R;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return CounterPreferenceFragment.class.getName().endsWith(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();
        if(id.equals(android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class CounterPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_counter);
            setHasOptionsMenu(true);

            findPreference("value").setOnPreferenceChangeListener((pref, o) -> {
                StatsController statsController = new StatsController(new File(getContext().getFilesDir(), "counter.json"), new WeeklyCounterUpdater());
                statsController.getCounter().setValue(Integer.parseInt(o.toString()));
                return true;
            });
        }
    }
}
