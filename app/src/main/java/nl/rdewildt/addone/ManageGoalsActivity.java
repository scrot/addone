package nl.rdewildt.addone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.File;

public class ManageGoalsActivity extends AppCompatActivity {
    private File statspath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_goals);

        //Get used stats path
        this.statspath = new File(getIntent().getStringExtra("STATS_PATH"));

        // Enable exit in actionbar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
