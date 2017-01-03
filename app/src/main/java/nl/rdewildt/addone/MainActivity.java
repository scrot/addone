package nl.rdewildt.addone;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

import nl.rdewildt.addone.fam.FloatingActionMenu;
import nl.rdewildt.addone.model.Bonus;
import nl.rdewildt.addone.model.Counter;
import nl.rdewildt.addone.settings.SettingsActivity;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;

    private File statspath;
    private StatsController statsController;

    private TextView counterView;
    private LinearLayout subCounterView;
    private RecyclerView goalsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupCounter();
        setupSubCounter();
        setupGoals();

        // Open Reminder dialog
        /*if(statsController.isNewCycle()){
            new ReminderDialogFragment().show(getSupportFragmentManager(), "reminder_dialog");
        }*/

        // implement the App Indexing API
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupCounter(){
        // Init counterView maintainer
        File statsFile = new File(getApplicationContext().getFilesDir(), "counterView.json");
        this.statspath = statsFile;
        this.statsController = new StatsController(statsFile, new WeeklyCounterUpdater());

        // Init counterView
        counterView = (TextView) findViewById(R.id.value);
        counterView.setText(String.valueOf(statsController.getCounter().getValue()));

        statsController.setCounterChangedListeners(new StatsController.CounterListener() {
            @Override
            public void onValueChanged(Integer value) {
                counterView.setText(String.valueOf(value));
            }

            @Override
            public void onSubValueChanged(Integer value) {
                setupSubCounter();
            }

            @Override
            public void onCounterChanged(Counter counter) {
                counterView.setText(String.valueOf(counter.getValue()));
                setupSubCounter();
            }
        });

        statsController.addBonusesChangedListener(this::setupSubCounter);
    }

    private void setupSubCounter(){
        // Init subcounterView
        subCounterView = (LinearLayout) findViewById(R.id.subcounter);
        subCounterView.removeAllViews();

        Bonus relevantBonus = statsController.getNextRelevantBonus(statsController.getCounter().getSubValue());
        for(int i = 1; i <= relevantBonus.getPoints(); i++){
            ImageView subCounterItem = (ImageView) getLayoutInflater().inflate(R.layout.sub_counter_item, null);
            if(i <= statsController.getCounter().getSubValue()){
                subCounterItem.setColorFilter(getResources().getColor(R.color.colorAccent, null));
            }
            subCounterView.addView(subCounterItem);
        }
    }

    private void setupGoals(){
        // Init goals
        goalsView = (RecyclerView) findViewById(R.id.goals);

        // Setup goals view layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goalsView.setLayoutManager(linearLayoutManager);

        // Bind goals to view
        GoalsAdapter adapter = new GoalsAdapter(statsController.getGoals());
        goalsView.setAdapter(adapter);

        // Set listeners
        statsController.setGoalsChangedListeners(new StatsController.GoalsListener() {
            @Override
            public void onGoalAdded(Integer position) {
                adapter.notifyItemInserted(position);
            }

            @Override
            public void onGoalRemoved(Integer position) {
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onGoalsChanged() {
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.fam);
        fam.setOnClickListener(this::addOne);
    }

    public void addOne(View view){
        statsController.increaseCounter();
    }

    public void addNewGoal(View view){
        Intent intent = new Intent(this, AddGoalActivity.class);
        intent.putExtra("STATS_PATH", this.statspath.toString());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            statsController.reloadStats();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();

        // Start settings activity
        if (id.equals(R.id.action_settings)) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id.equals(R.id.action_reset)){
            statsController.resetStats();
            statsController.addBonus(new Bonus("test", 3, 1)); //TODO testBonus
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://nl.rdewildt.addone/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://nl.rdewildt.addone/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public StatsController getStatsController() {
        return statsController;
    }

    public static class ReminderDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            StatsController statsController = ((MainActivity) getActivity()).getStatsController();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.reminder_newcycle)
                    .setCancelable(false)
                    .setPositiveButton(R.string.reminder_yes, (dialog, which) -> {
                        statsController.initNewCycle(true);
                    })
                    .setNegativeButton(R.string.reminder_no, (dialog, which) -> {
                        statsController.initNewCycle(false);
                    });
            return dialogBuilder.create();
        }
    }
}
