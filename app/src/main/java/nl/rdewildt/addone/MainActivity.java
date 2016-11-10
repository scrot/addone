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
    private CounterMaintainer counterMaintainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init counter maintainer
        File statsFile = new File(getApplicationContext().getFilesDir(), "counter.json");
        this.counterMaintainer = new CounterMaintainer(statsFile, new WeeklyCounterUpdater());

        // Init counter
        TextView counter = (TextView) findViewById(R.id.value);
        counter.setText(String.valueOf(counterMaintainer.getCounter().getValue()));

        // Init goals
        RecyclerView goalsView = (RecyclerView) findViewById(R.id.goals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goalsView.setLayoutManager(linearLayoutManager);
        GoalsAdapter adapter = new GoalsAdapter(counterMaintainer.getGoals());
        goalsView.setAdapter(adapter);

        // On counter attributes changed
        counterMaintainer.setCounterChangedListeners(new CounterMaintainer.CounterListener() {
            @Override
            public void onValueChanged(Integer value) {
                counter.setText(String.valueOf(value));
            }

            @Override
            public void onGoalsChanged(List<Goal> goals) {

            }
        });

        // Open Reminder dialog
        if(counterMaintainer.noUpdateLastCycle()){
            new ReminderDialogFragment().show(getSupportFragmentManager(), "reminder_dialog");
        }

        // implement the App Indexing API
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            counterMaintainer.resetCounter();
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
        counterMaintainer.triggerValueChangedListeners();
    }

    public CounterMaintainer getCounterMaintainer() {
        return counterMaintainer;
    }

    public static class ReminderDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            CounterMaintainer counterMaintainer = ((MainActivity) getActivity()).getCounterMaintainer();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.reminder_newcycle)
                    .setCancelable(false)
                    .setPositiveButton(R.string.reminder_yes, (dialog, which) -> {
                        counterMaintainer.initNewCycle(true);
                    })
                    .setNegativeButton(R.string.reminder_no, (dialog, which) -> {
                        counterMaintainer.initNewCycle(false);
                    });
            return dialogBuilder.create();
        }
    }
}
