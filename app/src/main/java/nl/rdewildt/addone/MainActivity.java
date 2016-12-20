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
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.List;

import nl.rdewildt.addone.settings.SettingsActivity;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;
    private StatsController statsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init counter maintainer
        File statsFile = new File(getApplicationContext().getFilesDir(), "counter.json");
        this.statsController = new StatsController(statsFile, new WeeklyCounterUpdater());

        // Init counter
        TextView counter = (TextView) findViewById(R.id.value);
        counter.setText(String.valueOf(statsController.getCounter().getValue()));

        // Init goals
        RecyclerView goalsView = (RecyclerView) findViewById(R.id.goals);

        // Setup goals view layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goalsView.setLayoutManager(linearLayoutManager);

        // Bind goals to view
        GoalsAdapter adapter = new GoalsAdapter(statsController.getGoals());
        goalsView.setAdapter(adapter);
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

        // On counter value changed
        statsController.setCounterChangedListeners(value -> counter.setText(String.valueOf(value)));

        // Open Reminder dialog
        if(statsController.noUpdateLastCycle()){
            new ReminderDialogFragment().show(getSupportFragmentManager(), "reminder_dialog");
        }

        // implement the App Indexing API
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onAddOneButtonClick(View view){
        statsController.addGoal(new Goal("Goal", "Dit is een test goal", 200));
    }

    public void onAddOneButtonClick2(View view){
        List<Goal> temp = statsController.getGoals();
        if(temp.size() > 0) {
            statsController.removeGoal(temp.get(temp.size() - 1));
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
            statsController.resetCounter();
            statsController.resetGoals();
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
        statsController.notifyCounterValueChanged();
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
