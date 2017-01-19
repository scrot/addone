package nl.rdewildt.addone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import nl.rdewildt.addone.fam.FloatingActionMenu;

public class ManageBonusesActivity extends AppCompatActivity {
    private File statspath;
    private StatsController statsController;
    private RecyclerView bonusesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bonuses);

        //Get used stats path
        this.statspath = new File(getIntent().getStringExtra("STATS_PATH"));
        this.statsController = new StatsController(statspath);

        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.fam);
        fam.setOnClickListener(this::addBonus);

        //View bonuses
        bonusesView = (RecyclerView) findViewById(R.id.bonuses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bonusesView.setLayoutManager(linearLayoutManager);
        BonusesAdapter bonusesAdapter = new BonusesAdapter(statsController.getBonuses());
        bonusesView.setAdapter(bonusesAdapter);
        bonusesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                statsController.writeStats();
            }
        });

        //Hook listeners
        statsController.addBonusesChangedListener(new StatsController.BonusesListener() {
            @Override
            public void onBonusAdded(Integer position) {
                bonusesAdapter.notifyItemInserted(position);
            }

            @Override
            public void onBonusRemoved(Integer position) {
                bonusesAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onBonusChanged() {
                bonusesAdapter.notifyDataSetChanged();
            }
        });

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

    public void addBonus(View view) {
        Intent intent = new Intent(this, AddBonusActivity.class);
        intent.putExtra("STATS_PATH", this.statspath.toString());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            statsController.reloadStats();
        }
    }
}
