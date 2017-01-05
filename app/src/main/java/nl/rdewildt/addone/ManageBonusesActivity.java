package nl.rdewildt.addone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;

public class ManageBonusesActivity extends AppCompatActivity {
    private File statspath;
    private StatsController statsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bonuses);

        //Get used stats path
        this.statspath = new File(getIntent().getStringExtra("STATS_PATH"));
        this.statsController = new StatsController(statspath);

        //View bonuses
        RecyclerView bonusesView = (RecyclerView) findViewById(R.id.bonuses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bonusesView.setLayoutManager(linearLayoutManager);
        BonusesAdapter bonusesAdapter = new BonusesAdapter(new ArrayList<>(statsController.getBonuses()));
        bonusesView.setAdapter(bonusesAdapter);

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
