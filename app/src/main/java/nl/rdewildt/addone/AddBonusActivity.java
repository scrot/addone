package nl.rdewildt.addone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import nl.rdewildt.addone.fam.FloatingActionMenu;
import nl.rdewildt.addone.model.Bonus;
import nl.rdewildt.addone.model.Goal;

public class AddBonusActivity extends AppCompatActivity {
    private File statspath;

    private EditText bonusName;
    private EditText bonusPoints;
    private EditText bonusReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bonus);

        //Set fam listeners
        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.fam);
        fam.setOnClickListener(this::addBonus);

        //Get used stats path
        this.statspath = new File(getIntent().getStringExtra("STATS_PATH"));

        //Collect input elements
        this.bonusName = (EditText) findViewById(R.id.bonus_name_input);
        this.bonusPoints = (EditText) findViewById(R.id.bonus_points_needed_input);
        this.bonusReward = (EditText) findViewById(R.id.bonus_reward_input);

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

    //TODO Check for empty fields
    //TODO set max bonusPoints to 20
    public void addBonus(View view){
        StatsController statsController = new StatsController(statspath);
        statsController.addBonus(new Bonus(bonusName.getText().toString(),
                Integer.parseInt(bonusPoints.getText().toString()),
                Integer.parseInt(bonusReward.getText().toString())));
        finish();
    }
}
