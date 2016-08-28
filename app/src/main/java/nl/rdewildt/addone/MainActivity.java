package nl.rdewildt.addone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Context context;
    Stats stats;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        this.context = this.getApplicationContext();

        // Try read stats file or create one
        File statsFile = new File(context.getFilesDir(), "stats.json");
        if (statsFile.exists()) {
            try {
                this.stats = readStats(statsFile);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        } else {
            this.stats = new Stats();
        }

        // Init counter
        TextView counter = (TextView) findViewById(R.id.counter);
        counter.setText(String.valueOf(stats.getCounter()));


        // On stats changed
        this.stats.setStatsListener(new StatsListener() {
            @Override
            public void OnCounterIncreased(Stats stats) {
                try {
                    writeStats(stats);
                    TextView counter = (TextView) findViewById(R.id.counter);
                    counter.setText(String.valueOf(stats.getCounter()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // On + button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    stats.incrementCounter(1);
                    writeStats(stats);

                    TextView counter = (TextView) findViewById(R.id.counter);
                    counter.setText(String.valueOf(stats.getCounter()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        this.stats.resetStats();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Stats readStats(File statsFile) throws JSONException, IOException {
        return new Gson().fromJson(new FileReader(statsFile), Stats.class);
    }

    private void writeStats(Stats stats) throws IOException {
        String statsJson = new Gson().toJson(stats);
        try (FileOutputStream fileOutputStream = openFileOutput("stats.json", MODE_PRIVATE)) {
            fileOutputStream.write(statsJson.getBytes());
        }
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
}
