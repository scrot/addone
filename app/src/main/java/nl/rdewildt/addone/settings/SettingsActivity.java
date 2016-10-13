package nl.rdewildt.addone.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.rdewildt.addone.Counter;
import nl.rdewildt.addone.R;

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
                Counter counter = null;
                try {
                    counter = Counter.readCounter(getContext().getFilesDir());
                    counter.setValue(Integer.parseInt(o.toString()));
                    Counter.writeCounter(counter, getContext().getFilesDir());
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            });
        }
    }
}
