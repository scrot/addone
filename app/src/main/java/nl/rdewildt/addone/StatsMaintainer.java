package nl.rdewildt.addone;

import android.content.Context;
import android.graphics.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsMaintainer {
    private Context context;
    private Stats stats;
    private File statsFile;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public StatsMaintainer(Context context){
        this.context = context;
        this.statsFile = new File(context.getFilesDir(), "stats.json");

        Stats stats = readStats();
        if(stats == null){
            stats = new Stats();
        }
        this.stats = stats;

        initialize();
    }

    private void initialize(){
        this.counterUpdater = new WeeklyCounterUpdater(this.stats);
    }

    public void increaseCounter(Integer i){
        this.counterUpdater.increaseCounter(i);
        if(counterListener != null) {
            counterListener.onChanged(stats.getCounter());
        }
    }

    public void decreaseCounter(){
        counterUpdater.decreaseCounter();
        if(counterListener != null) {
            counterListener.onChanged(stats.getCounter());
        }
    }

    public Stats getStats(){
        return this.stats;
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public Stats readStats() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());

        Stats rStats = null;
        try {
            return gsonBuilder.create().fromJson(new FileReader(statsFile), Stats.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return rStats;
    }

    public void writeStats() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        String statsJson = gsonBuilder.create().toJson(stats);
        try (FileOutputStream fileOutputStream = context.openFileOutput("stats.json", Context.MODE_PRIVATE)) {
            fileOutputStream.write(statsJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
