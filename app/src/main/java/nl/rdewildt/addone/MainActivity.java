package nl.rdewildt.addone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nl.rdewildt.addone.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;
    private StatsMaintainer statsMaintainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        File statsFile = new File(getApplicationContext().getFilesDir(), "stats.json");
        this.statsMaintainer = new StatsMaintainer(statsFile);

        // Init update counter
        try {
            statsMaintainer.decreaseCounter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Init counter
        TextView counter = (TextView) findViewById(R.id.counter);
        try {
            counter.setText(String.valueOf(statsMaintainer.getStats().getCounter()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // On stats changed
        statsMaintainer.setCounterListener((Integer c) -> {
            System.out.println(c);
            counter.setText(String.valueOf(c));
        });

        // On + button click
        fab.setOnClickListener(view -> {
            try {
                statsMaintainer.increaseCounter(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
            try {
                statsMaintainer.resetStats();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        try {
            statsMaintainer.triggerCounterListener();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
