package nl.rdewildt.addone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import nl.rdewildt.addone.model.Goal;

public class AddGoalActivity extends AppCompatActivity {
    private File statspath;

    private EditText goalName;
    private EditText goalDescription;
    private EditText goalPointsNeeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        //Get used stats path
        this.statspath = new File(getIntent().getStringExtra("STATS_PATH"));

        //Collect input elements
        this.goalName = (EditText) findViewById(R.id.goal_name_input);
        this.goalDescription = (EditText) findViewById(R.id.goal_description_input);
        this.goalPointsNeeded = (EditText) findViewById(R.id.goal_points_needed_input);

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
    public void addGoal(View view){
        StatsController statsController = new StatsController(statspath);
        statsController.addGoal(new Goal(goalName.getText().toString(),
                goalDescription.getText().toString(),
                Integer.parseInt(goalPointsNeeded.getText().toString())));
        finish();
    }
}
