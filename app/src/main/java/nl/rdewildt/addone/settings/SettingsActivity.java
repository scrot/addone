package nl.rdewildt.addone.settings;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.List;

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
}
